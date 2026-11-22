package io.github.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Inverted index (term → documents containing it) with TF-IDF ranking:
 * <pre>
 *   tf(t, d)  = occurrences of t in d / number of tokens in d
 *   idf(t)    = ln(1 + N / df(t))        N = number of documents, df = documents containing t
 *   score(d)  = sum over distinct query terms of tf * idf
 * </pre>
 * Only documents containing at least one query term are returned.
 * Results are ordered by score desc, then document id asc.
 */
public class InvertedIndex {

    private final Map<String, Map<String, Integer>> postings = new HashMap<>();  // term -> (doc -> count)
    private final Map<String, List<String>> documents = new HashMap<>();         // doc -> tokens

    public void addDocument(String id, String text) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean removeDocument(String id) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public List<SearchHit> search(String query, int k) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public int documentCount() {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }
}
