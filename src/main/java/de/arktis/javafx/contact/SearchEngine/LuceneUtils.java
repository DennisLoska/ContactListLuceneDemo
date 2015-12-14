/*

package de.arktis.javafx.contact.SearchEngine;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class LuceneUtils {

    private LuceneUtils lucy = new LuceneUtils();
    private String indexDir = "/de/arktis/javafx/contact/model/testIndex";

    //Konstruktor
    public LuceneUtils(){

    }

    public void createIndex() throws Exception {

        boolean create = true;
        File indexDirFile = new File(this.indexDir);
        if (indexDirFile.exists() && indexDirFile.isDirectory()) {
            create = false;
        }

        Directory dir = FSDirectory.open(indexDirFile);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);

        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }

        IndexWriter writer = new IndexWriter(dir, iwc);
        writer.commit();
        writer.close(true);
    }

}
*/
