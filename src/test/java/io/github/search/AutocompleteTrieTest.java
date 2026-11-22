package io.github.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AutocompleteTrieTest {

    private AutocompleteTrie trie;

    @BeforeEach
    void setUp() {
        trie = new AutocompleteTrie();
        trie.insert("java", 100);
        trie.insert("javascript", 300);
        trie.insert("java streams", 50);
        trie.insert("jackson", 80);
        trie.insert("kotlin", 200);
    }

    @Test
    void returnsHeaviestTermsForPrefix() {
        assertThat(trie.topK("ja", 3)).extracting(Suggestion::term)
                .containsExactly("javascript", "java", "jackson");
    }

    @Test
    void prefixCanBeAWholeTerm() {
        assertThat(trie.topK("java", 10)).extracting(Suggestion::term)
                .containsExactly("javascript", "java", "java streams");
    }

    @Test
    void tiesAreBrokenAlphabetically() {
        trie.insert("jab", 80);
        assertThat(trie.topK("ja", 5)).extracting(Suggestion::term)
                .containsExactly("javascript", "java", "jab", "jackson", "java streams");
    }

    @Test
    void unknownPrefixReturnsEmpty() {
        assertThat(trie.topK("py", 5)).isEmpty();
    }

    @Test
    void emptyPrefixMatchesEverything() {
        assertThat(trie.topK("", 2)).extracting(Suggestion::term).containsExactly("javascript", "kotlin");
    }

    @Test
    void isCaseInsensitive() {
        trie.insert("Spring Boot", 10);
        assertThat(trie.topK("SPR", 1)).containsExactly(new Suggestion("spring boot", 10));
    }

    @Test
    void insertOverwritesWeight() {
        trie.insert("java", 1000);
        assertThat(trie.topK("ja", 1)).containsExactly(new Suggestion("java", 1000));
        assertThat(trie.size()).isEqualTo(5);
    }

    @Test
    void recordIncrementsWeight() {
        trie.record("kotlin");
        trie.record("rust");
        trie.record("rust");
        assertThat(trie.topK("kot", 1)).containsExactly(new Suggestion("kotlin", 201));
        assertThat(trie.topK("ru", 1)).containsExactly(new Suggestion("rust", 2));
    }

    @Test
    void deleteRemovesTermButKeepsLongerOnes() {
        assertThat(trie.delete("java")).isTrue();
        assertThat(trie.delete("java")).isFalse();
        assertThat(trie.topK("java", 10)).extracting(Suggestion::term)
                .containsExactly("javascript", "java streams");
        assertThat(trie.size()).isEqualTo(4);
    }

    @Test
    void deletePrunesUnusedNodes() {
        AutocompleteTrie t = new AutocompleteTrie();
        t.insert("ab", 1);
        int withAb = t.nodeCount();
        t.insert("abcdef", 1);
        t.delete("abcdef");
        assertThat(t.nodeCount()).isEqualTo(withAb);
    }
}
