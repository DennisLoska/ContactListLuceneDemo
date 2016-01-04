package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.ContactMain;
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

    private ContactMain contactMain;
    private int docID;
    private String searchRequest;
    private String[] fuzzyResults;
    private Document d = LuceneUtils.getInstance().getDoc();
    private IndexReader reader = LuceneUtils.getInstance().getReader();

    public LuceneIndexSearcher(String searchRequest, ContactMain contactMain) {
        this.searchRequest = searchRequest;
        this.contactMain = contactMain;
    }

    public void searchIndex() throws IOException {

        Query fuzzyQuery = new FuzzyQuery(new Term("title", this.searchRequest), 2);
        System.out.println(this.searchRequest);
        int hitsPerPage = 10;

        //nicht gebraucht, wenn index bereits offen bzw. nicht geschlossen
        this.reader = DirectoryReader.open(LuceneUtils.getInstance().getIndex());

        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        this.fuzzyResults = new String[hits.length];

        //3. output
        for (int i = 0; i < hits.length; ++i) {

            this.docID = hits[i].doc;
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
            //System.out.println("Es wurde kein Kontakt gefunden.");
            foundName = "Nicht Gefunden";
        }
        return foundName;
    }


}
