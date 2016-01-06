package de.arktis.javafx.contact.SearchEngine;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import de.arktis.javafx.contact.ContactMain;
import de.arktis.javafx.contact.model.Person;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuceneIndex {

    private ContactMain contactMain;
    private IndexWriter indWriter;
    private static Document doc = new Document();
    private int docID;
    private static Directory index;
    private StandardAnalyzer analyzer;
    private IndexWriterConfig indexConfig;
    private IndexWriterConfig indexUpConfig;

    public void createDirectory() {
        this.analyzer = new StandardAnalyzer();
        this.index = new RAMDirectory();
        this.indexConfig = new IndexWriterConfig(analyzer);
        try {
            indWriter = new IndexWriter(index, indexConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createIndex(ContactMain contactMain) {
        //TODO deprecatete Methode updaten
        this.contactMain = contactMain;
        for (Person person : contactMain.getPersonData()) {
            this.doc = new Document();
            System.out.println("indexing " + person.getFirstName() + " "
                    + person.getLastName());
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
    }

    public IndexWriter getIndWriter() {
        return indWriter;
    }
    public int getDocID() {
        return docID;
    }
    public static Document getDoc() {
        return doc;
    }
    public static Directory getIndex() {
        return index;
    }
    public void setDocID(int docID) {
        this.docID = docID;
    }
}

