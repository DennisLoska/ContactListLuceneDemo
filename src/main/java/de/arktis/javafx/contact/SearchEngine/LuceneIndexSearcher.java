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
    private Document d = new Document();


    public LuceneIndexSearcher(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    public void searchIndex() throws IOException {
        Query fuzzyQuery = new FuzzyQuery(new Term("title", this.searchRequest), 2);
        System.out.println(this.searchRequest);
        int hitsPerPage = 10;
        //nicht gebraucht, wenn index bereits offen bzw. nicht geschlossen  LuceneUtils.getInstance().getIndex()
        IndexReader reader = DirectoryReader.open(LuceneUtils.getInstance().getIndex());
        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        String[] fuzzyResults = new String[hits.length];
        //3. output
        for (int i = 0; i < hits.length; ++i) {
            int docID = hits[i].doc;
            LuceneUtils.getInstance().setDocID(docID);
            this.d = searcher.doc(docID);
            fuzzyResults[i] = this.d.get("title");
            System.out.println("Found " + hits.length + " hits.");
            System.out.println((i + 1) + ". " + d.get("title"));
        }
        reader.close();
    }

    public String getFuzzyResults() {
        String foundName = this.d.get("title");
        if (foundName == null) {
            foundName = "Nicht Gefunden";
        }
        return foundName;
    }
}
