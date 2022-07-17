package ch.m1m.infra.api;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LuceneInstance {

    private final Directory directory;
    private final Analyzer analyzer;
    private final IndexWriter iWriter;

    public LuceneInstance(Directory directory, Analyzer analyzer) throws IOException {
        this.directory = directory;
        this.analyzer = analyzer;
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        this.iWriter = new IndexWriter(directory, iwc);
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void addDocument(Document doc) throws IOException {
        iWriter.addDocument(doc);
        iWriter.commit();
    }

    public void deleteDocument(Query query) throws IOException {
        iWriter.deleteDocuments(query);
        iWriter.commit();
    }

    public List<Document> search(Query query) throws IOException {
        return search(query, 100);
    }

    public List<Document> search(Query query, int limit) throws IOException {
        return search(query, limit, null);
    }

    public List<Document> search(Query query, int limit, Sort sort) throws IOException {

        IndexReader iReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(iReader);

        TopDocs topDocs;
        if (sort != null) {
            topDocs = searcher.search(query, limit, sort);
        } else {
            topDocs = searcher.search(query, limit);
        }

        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(searcher.doc(scoreDoc.doc));
        }
        return documents;
    }
}
