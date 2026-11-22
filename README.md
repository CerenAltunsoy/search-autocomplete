# search-autocomplete

Search building blocks in Java: a weighted trie for top-k prefix suggestions and an
inverted index with TF-IDF ranking.

## Components

- `AutocompleteTrie`: insert/record/delete terms; `topK(prefix, k)` by weight, ties alphabetical.
- `InvertedIndex`: term → postings; TF-IDF scoring over multi-term queries.
- `Tokenizer`: Unicode-aware lower-casing tokenizer.

## Usage

```java
var trie = new AutocompleteTrie();
trie.record("spring boot");
trie.topK("spr", 5);

var index = new InvertedIndex();
index.addDocument("doc-1", "Kafka consumer groups and partitions");
index.search("kafka partitions", 10);
```

## Build

```bash
mvn verify
```

## Roadmap

- [ ] Trie with top-k, inverted index with TF-IDF
- [ ] Cache top-k per trie node for O(prefix length) queries
- [ ] BM25 ranking and a comparison with TF-IDF
- [ ] Spring Boot API with debounce-friendly endpoint and sharding by prefix
- [ ] Compare results with Elasticsearch

## Design decisions

_TODO_
