package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.ContactMain;
import de.arktis.javafx.contact.controller.PersonEditDialogController;
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
    private String[] fuzzyResults;
    private Document d = new Document();
    private PersonOverviewController personOvctrl = new PersonOverviewController();

    private Document doc;
    private IndexWriter indWriter;
    private IndexReader reader;
    private IndexWriterConfig indexConfig;
    private IndexWriterConfig indexUpdateConfig;


    public LuceneTestImplementation(String searchField, ContactMain contactMain) throws IOException, ParseException {
        this.searchField = searchField;
        this.contactMain = contactMain;
    }

    public void updateDocument(IndexWriter indWriter, Document doc, IndexReader reader) throws IOException {

        this.indWriter = indWriter;
        this.doc = doc;
        this.reader = reader;
       // Person tmp = personOvctrl.setPersonDetails(Person );

        indWriter.tryDeleteDocument(reader,this.ID);
        indWriter.updateDocument(new Term("title"),doc); // hier die Person rein //vden namen
        indWriter.close();

    }

    public void searchEngine() throws IOException, ParseException {
        // create some index
        // we could also create an index in our ram ..
        // Directory index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        this.indexConfig = new IndexWriterConfig(analyzer);
        this.indexUpdateConfig = new IndexWriterConfig(analyzer);
        Directory index = new RAMDirectory();

        //Path path = new Paths();
        //index = FSDirectory.open(path);

        try {
            this.indWriter = new IndexWriter(index, indexConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Person person : contactMain.getPersonData()) {
            System.out.println("indexing " + person.getFirstName() + " "
                    + person.getLastName());
            this.doc = new Document();
            doc.add(new Field("title", person.getFirstName() + " "
                    + person.getLastName(), Field.Store.YES,
                    Field.Index.ANALYZED));
            doc.add(new Field("name", person.getFirstName() + " "
                    + person.getLastName(), Field.Store.YES,
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
        System.out.println(this.searchField);
        int hitsPerPage = 10;
        //nicht gebraucht, wenn index bereits offen bzw. nicht geschlossen
        this.reader = DirectoryReader.open(index);

        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        this.fuzzyResults = new String[hits.length];

        //output
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            this.d = searcher.doc(docId);

            this.fuzzyResults[i] = this.d.get("title");
            System.out.println("Found " + hits.length + " hits.");
            System.out.println((i + 1) + ". " + d.get("title"));
        }
       // reader.close();

        try {
            this.indWriter = new IndexWriter(index, indexUpdateConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateDocument(indWriter, doc,reader);
        reader.close();
    }

    public String getFuzzyResults() {

        String foundName = this.d.get("title");
        if (foundName == null) {
            //System.out.println("Es wurde kein Kontakt gefunden.";
            foundName = "Nicht Gefunden";
        }
        return foundName;

    }

}
