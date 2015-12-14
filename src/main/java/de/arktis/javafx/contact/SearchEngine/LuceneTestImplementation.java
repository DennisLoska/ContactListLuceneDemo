package de.arktis.javafx.contact.SearchEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.arktis.javafx.contact.model.Person;
import de.arktis.javafx.contact.model.Searchrequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneTestImplementation {

    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private Searchrequest searchReQuest = new Searchrequest();
    private final String searchField;


    public LuceneTestImplementation(String searchField) throws IOException, ParseException {

        this.searchField = searchField;
    }

    public void searchEngine(String[] args) throws IOException, ParseException {
        // create some index
        // we could also create an index in our ram ...
        // Directory index = new RAMDirectory();

       // Path path = new Paths();
        Directory index;
           // index = FSDirectory.open(path);
            index = new RAMDirectory();

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iConfig = new IndexWriterConfig(analyzer);

        IndexWriter w = null;
        try {
            w = new IndexWriter(index, iConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // index some data
        for (Person person : personData) {
            System.out.println("indexing " + person);
            Document doc = new Document();

            doc.add(new Field("title", person.getFirstName(), Field.Store.YES,
                    Field.Index.ANALYZED));
            doc.add(new Field("name", person.getLastName(), Field.Store.YES,
                    Field.Index.ANALYZED));

            try {
                w.addDocument(doc);
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
                w.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("index generated");

        // parse query over multiple fields
        String querystr = args.length > 0 ? args[0] : this.searchField;
        System.out.println(querystr);
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // searching ...
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;


        // output results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("name") + ": "
                    + d.get("title"));


        }


    }

}
