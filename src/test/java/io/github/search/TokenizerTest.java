package io.github.search;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenizerTest {

    @Test
    void splitsOnPunctuationAndLowercases() {
        assertThat(Tokenizer.tokenize("Hello, World! Spring-Boot 3.2"))
                .containsExactly("hello", "world", "spring", "boot", "3", "2");
    }

    @Test
    void keepsNonAsciiLetters() {
        assertThat(Tokenizer.tokenize("Çalışma güzel")).containsExactly("çalışma", "güzel");
    }

    @Test
    void blankGivesNoTokens() {
        assertThat(Tokenizer.tokenize("  ...  ")).isEmpty();
        assertThat(Tokenizer.tokenize(null)).isEmpty();
    }
}
