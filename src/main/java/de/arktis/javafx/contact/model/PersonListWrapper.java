package de.arktis.javafx.contact.model;

/**
 * Created by Pati on 10.12.2015.
 */
import de.arktis.javafx.contact.ContactMain;
import de.arktis.javafx.contact.SearchEngine.LuceneTestImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 *
 * @author Marco Jakob
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {

    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private List<Person> persons;
    private LuceneTestImplementation luceneEngine = new LuceneTestImplementation();
    private ContactMain contactMain = new ContactMain();
    @XmlElement(name = "person")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    }


