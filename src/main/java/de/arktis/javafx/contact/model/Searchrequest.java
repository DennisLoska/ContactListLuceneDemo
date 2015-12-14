package de.arktis.javafx.contact.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Pati on 10.12.2015.
 */
public class Searchrequest {

    private final StringProperty searchField = new SimpleStringProperty();

    //Konstruktor
    public Searchrequest() {

    }

    public StringProperty searchFieldProperty() {

        return this.searchField;

    }

    public void setSearchField(String searchField) {

        this.searchField.set(searchField);

    }

    public String getSearchField() {

        return searchField.get();

    }
}
