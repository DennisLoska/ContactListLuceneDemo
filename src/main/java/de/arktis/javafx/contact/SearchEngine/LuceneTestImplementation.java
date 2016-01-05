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

    private int docId;
    private String searchField;
    private ContactMain contactMain;
    private String[] fuzzyResults;
    private Document d = new Document();
    private Document doc;
    private IndexWriter indWriter;
    private IndexReader reader;
    private IndexWriterConfig indexConfig;
    private IndexWriterConfig indexUpdateConfig;
    private PersonOverviewController persOvCtrl;

    public LuceneTestImplementation(){

    };

    public LuceneTestImplementation(String searchField, ContactMain contactMain) throws IOException, ParseException {
        this.searchField = searchField;
        this.contactMain = contactMain;
    };

    /*
     *TODO Die Update-Funktion implementieren, sodass gelöschte , bearbeitete und neu hinzugefügte Kontakte indexiert werden
     * Diese Methode funktioniert noch nicht.
     */

    /*
    public void updateDocument() throws IOException {

        System.out.println("updating...\n");
        this.doc = null;
        indWriter.addDocument(doc);

        persOvCtrl = new PersonOverviewController();
        Person found = persOvCtrl.getFoundPerson();
        contactMain.getPersonData().add(found);
        Document addedDoc = reader.document(0);
        addedDoc.add(new Field("title", found.getFirstName() + " "
                + found.getLastName(), Field.Store.YES,
                Field.Index.ANALYZED));
        indWriter.tryDeleteDocument(reader,this.docId);
        indWriter.updateDocument(new Term("title", "test"),doc);
        indWriter.close();

    }
*/



    /*
     * 1.Es wird im for-Loop die Kontaktliste durchlaufen und alle Namen der Personen
     *   in einem erstellten Inde gespeichert. Pro Person wurden zwei Felder "title" und
     *   "name" erstellt, wobei in diesem Falle auch eines gereicht hätte.
     *   "title" ist von Vorteil wenn z.b. zwei Personen den gleichen Namen haben. Man
     *   könnte z.B. mit einer person_ID die PErson einzigartig machen und über das Feld
     *   "name" den Namen bekommen.
     * 2.Es wird eine FuzzyQuery (ungenaue Abfrage) Erstellt. Die Parameter sind das
     *   abzufragende Feld "title" (alternativ "name") und der eingegebene Suchbegriff,
      *  welcher mit "this.searchfield" (Zeile 119) übergeben wird.
     * 3.Gibt die gefundenen Personen anhand der DocID aus. Beginnend bei 0 bzw 1 (Array)
     *   1. , 2. , 3. Person usw.
     */
    public void searchEngine() throws IOException, ParseException {
        // create some index
        // we could also create an index in our ram ..
        // Directory index = new RAMDirectory();
        // createDirectory
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

        //1. createIndex
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

        //2. parse query over multiple fields
        Query fuzzyQuery = new FuzzyQuery(new Term("title", this.searchField), 2);
        System.out.println(this.searchField);
        int hitsPerPage = 10;

        //nicht gebraucht, wenn index bereits offen bzw. nicht geschlossen
        this.reader = DirectoryReader.open(index);

        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] hits = searcher.search(fuzzyQuery, hitsPerPage).scoreDocs;
        this.fuzzyResults = new String[hits.length];

        //3. output
        for (int i = 0; i < hits.length; ++i) {
            this.docId = hits[i].doc;
            this.doc = searcher.doc(docId);

            this.fuzzyResults[i] = this.doc.get("title");
            System.out.println("Found " + hits.length + " hits.");
            System.out.println((i + 1) + ". " + doc.get("title"));
        }
        reader.close();
    }

    /*
     * Gibt den vollen Namen der gefunden Person zurück.
     */

    /*
    public String getFuzzyResults() {
        String foundName = this.d.get("title");
        if (foundName == null) {
            //System.out.println("Es wurde kein Kontakt gefunden.");
            foundName = "Nicht Gefunden";
        }
        return foundName;
    }
   */
}
