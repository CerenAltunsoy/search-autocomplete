package io.github.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class InvertedIndexTest {

    private InvertedIndex index;

    @BeforeEach
    void setUp() {
        index = new InvertedIndex();
        index.addDocument("d1", "Kafka consumer groups and partitions");
        index.addDocument("d2", "Spring Boot with Kafka: Kafka producer configuration");
        index.addDocument("d3", "Spring Boot actuator and metrics");
    }

    @Test
    void scoreMatchesTfIdfFormula() {
        InvertedIndex idx = new InvertedIndex();
        idx.addDocument("a", "apple banana");
        idx.addDocument("b", "apple apple cherry");
        // cherry: tf(b) = 1/3, idf = ln(1 + 2/1)
        assertThat(idx.search("cherry", 10)).singleElement()
                .satisfies(h -> {
                    assertThat(h.documentId()).isEqualTo("b");
                    assertThat(h.score()).isCloseTo(Math.log(3) / 3, within(1e-9));
                });
    }

    @Test
    void higherTermFrequencyRanksHigher() {
        assertThat(index.search("kafka", 10)).extracting(SearchHit::documentId).containsExactly("d2", "d1");
    }

    @Test
    void rareTermsWeighMoreThanCommonOnes() {
        // "actuator" appears in 1 doc, "spring" in 2: d3 should beat d2
        assertThat(index.search("spring actuator", 10)).extracting(SearchHit::documentId)
                .startsWith("d3");
    }

    @Test
    void onlyMatchingDocumentsAreReturned() {
        assertThat(index.search("partitions", 10)).extracting(SearchHit::documentId).containsExactly("d1");
        assertThat(index.search("nothing here", 10)).isEmpty();
    }

    @Test
    void resultsAreLimitedToK() {
        assertThat(index.search("kafka spring", 1)).hasSize(1);
    }

    @Test
    void repeatedQueryTermsCountOnce() {
        assertThat(index.search("kafka kafka kafka", 10)).isEqualTo(index.search("kafka", 10));
    }

    @Test
    void removedDocumentIsNoLongerFound() {
        assertThat(index.removeDocument("d1")).isTrue();
        assertThat(index.search("partitions", 10)).isEmpty();
        assertThat(index.documentCount()).isEqualTo(2);
    }

    @Test
    void reAddingADocumentReplacesIt() {
        index.addDocument("d1", "completely different text");
        assertThat(index.search("kafka", 10)).extracting(SearchHit::documentId).containsExactly("d2");
        assertThat(index.documentCount()).isEqualTo(3);
    }
}
