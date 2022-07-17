package ch.m1m.infra.api;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LuceneTest {

    private static String LUCENE_TEST_INDEX_DIR = "/tmp/lucene/LuceneTest";

    /*
    TextField    gets tokenized i.e: title
    StringField  is not tokenized i.e: isbn
     */

    @BeforeAll
    public static void runOnce() throws IOException {
        deleteAllTestFiles();
    }

    @Test
    public void plainAddAndThenSearch() throws IOException, ParseException {

        // create writer
        //
        Directory memoryDir = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter iWriter = new IndexWriter(memoryDir, iwc);

        // add a new document
        //
        Document doc = new Document();
        doc.add(new TextField("body", "this is my body", Field.Store.YES));
        iWriter.addDocument(doc);
        iWriter.commit();

        // create reader (searcher)
        //
        IndexReader iReader = DirectoryReader.open(memoryDir);
        IndexSearcher searcher = new IndexSearcher(iReader);

        // search for the document
        //
        Query query = new QueryParser("body", analyzer)
                .parse("body:\"this is my body\"");

        // from: https://lucene.apache.org/core/8_9_0/memory/index.html
        //  index.search(parser.parse("+author:james +salmon~ +fish* manual~"));

        TopDocs topDocs = searcher.search(query, 100);

        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            documents.add(searcher.doc(scoreDoc.doc));
        }

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("this is my body", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_QueryParser() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "QueryParser");

        Document doc = new Document();
        doc.add(new TextField("body", "this is my body", Field.Store.YES));
        instance.addDocument(doc);

        Query query = new QueryParser("body", instance.getAnalyzer())
                .parse("this is my body");
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("this is my body", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_TermQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "TermQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "this is my body", Field.Store.YES));
        instance.addDocument(doc);

        Term term = new Term("body", "this");
        Query query = new TermQuery(term);
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("this is my body", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_PrefixQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "PrefixQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        instance.addDocument(doc);

        Term term = new Term("body", "swiss");
        Query query = new PrefixQuery(term);
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("exchange broker swissquote", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_WildcardQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "WildcardQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        instance.addDocument(doc);

        Term term = new Term("body", "swiss*");
        Query query = new WildcardQuery(term);
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("exchange broker swissquote", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_PhraseQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "PhraseQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        instance.addDocument(doc);

        Query query = new PhraseQuery(1, "body",
                new BytesRef("exchange"), new BytesRef("broker"));
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("exchange broker swissquote", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_FuzzyQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "FuzzyQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        instance.addDocument(doc);

        Term term = new Term("body", "swisquote");
        Query query = new FuzzyQuery(term);
        List<Document> documents = instance.search(query, 200);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("exchange broker swissquote", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_BooleanQuery() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "BooleanQuery");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        instance.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("body", "exchange broker lehman", Field.Store.YES));
        instance.addDocument(doc);

        Term term1 = new Term("body", "exchange");
        Term term2 = new Term("body", "broker");

        TermQuery termQuery1 = new TermQuery(term1);
        TermQuery termQuery2 = new TermQuery(term2);

        Query query = new BooleanQuery.Builder()
                .add(termQuery1, BooleanClause.Occur.MUST)
                .add(termQuery2, BooleanClause.Occur.MUST)
                .build();
        List<Document> documents = instance.search(query, 200);

        // verify
        //
        assertEquals(2, documents.size());
    }

    @Test
    public void helperClassAddAndSearch_FuzzyQuerySorted() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "FuzzyQuerySorted");

        Document doc = new Document();
        doc.add(new TextField("body", "exchange broker swissquote", Field.Store.YES));
        doc.add(new StringField("name", "broker", Field.Store.YES));
        doc.add(new SortedDocValuesField("name", new BytesRef("broker")));
        instance.addDocument(doc);

        Term term = new Term("body", "swisquote");
        Query query = new FuzzyQuery(term);

        SortField sortField = new SortField("name", SortField.Type.STRING_VAL, false);
        Sort sortByName = new Sort(sortField);
        List<Document> documents = instance.search(query, 200, sortByName);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("exchange broker swissquote", documents.get(0).get("body"));
    }

    @Test
    public void helperClassAddAndSearch_TermQueryDelete() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "TermQueryDelete");

        Document doc = new Document();
        doc.add(new TextField("body", "this is my body", Field.Store.YES));
        instance.addDocument(doc);

        Term term = new Term("body", "this");
        Query query = new TermQuery(term);
        List<Document> documents = instance.search(query);

        // verify
        //
        assertEquals(1, documents.size());
        assertEquals("this is my body", documents.get(0).get("body"));

        // delete
        //
        instance.deleteDocument(query);
        List<Document> documentsAfterDelete = instance.search(query);
        assertEquals(0, documentsAfterDelete.size());
    }

    @Test
    public void helperClassAddAndSerch_DifferentWays() throws IOException, ParseException {

        LuceneInstance instance = createLuceneInstance(new StandardAnalyzer(), "DifferentWays");

        addTestDataSet(instance);

        Query query = new QueryParser("body", instance.getAnalyzer())
                .parse("church");
        List<Document> documents = instance.search(query);
        assertEquals(3, documents.size());

        query = new QueryParser("body", instance.getAnalyzer())
                .parse("basel");
        documents = instance.search(query);
        assertEquals(2, documents.size());

        query = new QueryParser("body", instance.getAnalyzer())
                .parse("foot* AND bas*");
        documents = instance.search(query);
        assertEquals(1, documents.size());
    }

    public static void deleteAllTestFiles() throws IOException {
        Path dir = Paths.get(LUCENE_TEST_INDEX_DIR);
        // Traverse the file tree in depth-first fashion and delete each file/directory.
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        //System.out.println("Deleting: " + path);
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static LuceneInstance createLuceneInstance(Analyzer analyzer, String testName) throws IOException {
        String indexFSDirectory = String.format("%s/%s", LUCENE_TEST_INDEX_DIR, testName);
        Directory directory = new MMapDirectory(Paths.get(indexFSDirectory));
        return new LuceneInstance(directory, new StandardAnalyzer());
    }

    public static void addTestDataSet(LuceneInstance instance) throws IOException {

        Document doc = new Document();
        doc.add(new TextField("body", "church middletown", Field.Store.YES));
        instance.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("body", "church zuerich", Field.Store.YES));
        instance.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("body", "church Basel", Field.Store.YES));
        instance.addDocument(doc);

        doc = new Document();
        doc.add(new TextField("body", "football Basel", Field.Store.YES));
        instance.addDocument(doc);
    }
}
