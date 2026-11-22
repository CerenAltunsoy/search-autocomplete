package io.github.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Trie of search terms with weights (e.g. how often each was searched).
 * Terms are normalized to lower case. {@link #topK} returns the heaviest terms
 * with the given prefix; ties are broken alphabetically.
 */
public class AutocompleteTrie {

    static final Comparator<Suggestion> RANKING =
            Comparator.comparingLong(Suggestion::weight).reversed().thenComparing(Suggestion::term);

    private final Node root = new Node();
    private int size = 0;

    /** Sets the weight of a term, inserting it if needed. */
    public void insert(String term, long weight) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    /** Increments a term's weight by one (a user searched for it). */
    public void record(String term) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean delete(String term) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public List<Suggestion> topK(String prefix, int k) {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public int size() {
        return size;
    }

    /** Number of trie nodes, for tests that check pruning. */
    int nodeCount() {
        return count(root);
    }

    private int count(Node n) {
        int c = 1;
        for (Node child : n.children.values()) c += count(child);
        return c;
    }

    private static final class Node {
        final Map<Character, Node> children = new HashMap<>();
        boolean terminal;
        long weight;
    }
}
