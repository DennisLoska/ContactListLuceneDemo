package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.controller.PersonOverviewController;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuceneUtils {

    private static LuceneUtils utilsInstance = null;
    private int docID;
    private Document doc = new Document();
    private IndexWriterConfig indexConfig = new IndexWriterConfig(new StandardAnalyzer());
    private Directory index = new RAMDirectory();
    private IndexReader reader;
    private IndexSearcher searcher;
    private String[] fuzzyResults;
    private IndexWriter indWriter;

    protected LuceneUtils() {
        // Exists only to defeat instantiation.
    }

    public static LuceneUtils getInstance() {
        if(utilsInstance == null) {
            utilsInstance = new LuceneUtils();
        }
        return utilsInstance;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public Document getDoc() {
        return doc;
    }

    public IndexWriterConfig getIndexConfig() {
        return indexConfig;
    }

    public Directory getIndex() {
        return index;
    }

    public IndexReader getReader() {
        return reader;
    }

    public IndexWriter getIndWriter() {
        return indWriter;
    }

    public IndexSearcher getSearcher() {
        return searcher;
    }

    public String[] getFuzzyResults() {
        return fuzzyResults;
    }


}

