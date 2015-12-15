package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.ContactMain;
import de.arktis.javafx.contact.controller.PersonOverviewController;
import de.arktis.javafx.contact.model.Person;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;


public class LuceneTestImplementation {

    private final int ID = 7;
    private final String searchField;
    private ContactMain contactMain;

    private Directory index = new RAMDirectory();
    private StandardAnalyzer analyzer = new StandardAnalyzer();
    private IndexWriterConfig indexConfig = new IndexWriterConfig(analyzer);
    private IndexWriter indWriter = null;
    private IndexReader reader = DirectoryReader.open(index);
    private IndexSearcher searcher = new IndexSearcher(reader);
    private Document doc = new Document();

    public LuceneTestImplementation(String searchField,ContactMain contactMain) throws IOException, ParseException {
        this.searchField = searchField;
        this.contactMain = contactMain;
    }

    public void updateDocument() throws IOException {

        indWriter.tryDeleteDocument(reader,this.ID);
        indWriter.updateDocument(new Term("title"),doc);
        indWriter.close();

    }

    public String[] searchEngine() throws IOException, ParseException {
        // create some index
        // we could also create an index in our ram ...
        // Directory index = new RAMDirectory();

        //Path path = new Paths();
        //index = FSDirectory.open(path);
        try {
            indWriter = new IndexWriter(index, indexConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Mit jeder Query wird erneut ein Index angelegt, um Veränderungen abzufangen
        for (Person person : contactMain.getPersonData()) {

            System.out.println("indexing " + person.getFirstName() + " "
                    + person.getLastName());

            Document doc = new Document();

            doc.add(new Field("title", person.getFirstName() + " "
                    + person.getLastName(), Field.Store.YES,
                    Field.Index.ANALYZED));

            doc.add(new Field("name",person.getFirstName() + " "
                    + person.getLastName(), Field.Store.YES,
                    Field.Index.ANALYZED));

            try {
                indWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // loop and index some random data
        for (int i = 1; i < 40000; i++) {
            Document doc = new Document();
            doc.add(new Field("title", "xyz" + i, Field.Store.YES,
                    Field.Index.ANALYZED));
            doc.add(new Field("name", "" + i, Field.Store.YES,
                    Field.Index.ANALYZED));

            try {
                indWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            indWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nIndex erstellt:");
        System.out.println(this.contactMain.getPersonData().size() + " Personen insgesamt. \n");

        //parse query over multiple fields
        Query fuzzyQuery = new FuzzyQuery(new Term("title", this.searchField), 2);

        int hitsPerPage = 10;


        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        String[] fuzzyResults = new String[hits.length];

        //output
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            fuzzyResults[i] = d.get("title");
            System.out.println("Found " + hits.length + " hits.");
            System.out.println((i + 1) + ". " + d.get("title"));
        }

        reader.close();
        return fuzzyResults;


        /* Für die 0-Anzeige
        Query q = new QueryParser("title", analyzer).parse(this.searchField);

        // searching ...
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits2 = docs.scoreDocs;

        // output results
        System.out.println("Found " + hits2.length + " hits.");
        for (int i = 0; i < hits2.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("name") + ": "
                    + d.get("title"));


        }
        */


    }

}
