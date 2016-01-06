package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.model.Searchrequest;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;

public class LuceneIndexSearcher {

    private String searchRequest;
    private Document doc = LuceneIndex.getDoc();
    private LuceneIndex indexInstance = new LuceneIndex();

    public IndexReader getReader() {
        return reader;
    }

    IndexReader reader;

    public LuceneIndexSearcher(String searchRequest) {
        this.searchRequest = searchRequest;
    }
    public LuceneIndexSearcher() {

    }

    public void searchIndex() throws IOException {
        Query fuzzyQuery = new FuzzyQuery(new Term("title", this.searchRequest), 2);
        System.out.println(this.searchRequest);
        int hitsPerPage = 10;
        this.reader = DirectoryReader.open(LuceneIndex.getIndex());
        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        String[] fuzzyResults = new String[hits.length];
        for (int i = 0; i < hits.length; ++i) {
            int docID = hits[i].doc;
            indexInstance.setDocID(docID);
            this.doc = searcher.doc(docID);
            fuzzyResults[i] = this.doc.get("title");
            System.out.println("Found " + hits.length + " hits.");
            System.out.println((i + 1) + ". " + doc.get("title"));
        }
        reader.close();
    }

    public String getFuzzyResults() {
        String foundName = this.doc.get("title");
        if (foundName == null) {
            foundName = "Nicht Gefunden";
        }
        return foundName;
    }
}
