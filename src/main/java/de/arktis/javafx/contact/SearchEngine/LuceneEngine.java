package de.arktis.javafx.contact.SearchEngine;

import de.arktis.javafx.contact.model.Contact;
import de.arktis.javafx.contact.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import java.util.List;
import java.util.Scanner;

/**
 * Created by Pati on 11.12.2015.
 */
public class LuceneEngine  {

    public LuceneEngine(){

    }

    private static void doIndex() throws InterruptedException {
        Session session = HibernateUtil.getSession();

        FullTextSession fullTextSession = Search.getFullTextSession(session);
        fullTextSession.createIndexer().startAndWait();

        fullTextSession.close();
    }

    private static List<Contact> search(String queryString) {
        Session session = HibernateUtil.getSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Contact.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("name").matching(queryString).createQuery();

        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Contact.class);

        List<Contact> contactList = fullTextQuery.list();

        fullTextSession.close();

        return contactList;
    }

    private static void displayContactTableData() {
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            // Fetching saved data
            List<Contact> contactList = session.createQuery("from Contact").list();

            for (Contact contact : contactList) {
                //System.out.println(contact);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            if(session != null) {
                session.close();
            }
        }
    }

    public void engineMain(String searchBarInput) throws InterruptedException {

        displayContactTableData();

        // Create an initial Lucene index for the data already present in the database
        doIndex();

        Scanner scanner = new Scanner(System.in);

        while(searchBarInput != null) {
            List<Contact> result = search(searchBarInput);

            System.out.println("\n\n>>>>>>Record found for '" + searchBarInput + "'");

            for (Contact contact : result) {
                System.out.println(contact);
            }

            searchBarInput = null;
        }
    }
}