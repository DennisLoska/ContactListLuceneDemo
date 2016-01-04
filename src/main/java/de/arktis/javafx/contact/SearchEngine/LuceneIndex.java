package de.arktis.javafx.contact.SearchEngine;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import de.arktis.javafx.contact.ContactMain;
import de.arktis.javafx.contact.model.Person;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuceneIndex {

    private ContactMain contactMain = new ContactMain();
    private IndexWriter indWriter;
    private IndexWriterConfig indexConfig;
    private Directory index;
    private Document doc;

    public void createDirectory() {

        this.indexConfig = LuceneUtils.getInstance().getIndexConfig();
        doc = LuceneUtils.getInstance().getDoc();
        this.indWriter = LuceneUtils.getInstance().getIndWriter();
        this.index = LuceneUtils.getInstance().getIndex();

        try {
            this.indWriter = new IndexWriter(index, indexConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createIndex(){

        //TODO deprecatete Methode updaten
        for (Person person : contactMain.getPersonData()) {
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


}

