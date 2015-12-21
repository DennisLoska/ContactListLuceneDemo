package de.arktis.javafx.contact.controller;

/**
 * Created by Pati on 09.12.2015.
 */

import de.arktis.javafx.contact.SearchEngine.LuceneIndexSearcher;
import de.arktis.javafx.contact.SearchEngine.LuceneTestImplementation;
import de.arktis.javafx.contact.model.Searchrequest;
import de.arktis.javafx.contact.util.DateUtil;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import de.arktis.javafx.contact.model.Person;
import de.arktis.javafx.contact.ContactMain;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.event.ChangeListener;
import javax.swing.text.html.*;
import java.beans.EventHandler;
import java.io.IOException;

public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    @FXML
    private TextField searchField;

    private Searchrequest request;

    // Reference to the main application.
    private ContactMain contactMain;
    private Person foundPerson = new Person();
    private LuceneTestImplementation luceneQuery;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PersonOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws IOException {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().lastNameProperty());
        // Clear person details.
        showPersonDetails(null);
        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showPersonDetails(newValue);

                    /* updateDocument-Methode noch nicht fertig
                    try {
                        try {
                            handleSearch();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        luceneQuery.updateDocument();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   */
                });

    }


    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());

            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
            // birthdayLabel.setText(...);
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {

        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(contactMain.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }

    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = contactMain.showPersonEditDialog(tempPerson);
        if (okClicked) {
            contactMain.getPersonData().add(tempPerson);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = contactMain.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(contactMain.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    /*
     *
     * Startt Suche, sobald erster Buchstabe getippt wird.
     * Noch kein Popup daher auskommentiert
     */
    @FXML
    private void startSearch(KeyEvent event) throws IOException, ParseException {
            handleSearch();
            //contactMain.showSearchPopup(event);
        }

    /*
     *startet die Suche auch, wenn Enter gedrückt wird und schließt das Popup
     *
     */
    @FXML
    private void handleSearchEnter(KeyEvent event) throws IOException, ParseException {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearch();
            contactMain.closeSearchPopup();

        }
    }

    /*
     *Aktion, wenn der Such-Button angeklickt wird.
     *
     */
    @FXML
    private void handleSearch() throws IOException, ParseException {

        request = new Searchrequest();
        request.setSearchField(searchField.getText());
        String searchRequest = request.getSearchField();
        if (searchRequest == null){
            searchRequest = "Null Catcher";
        }
        this.luceneQuery = new LuceneTestImplementation(searchRequest, new ContactMain());
        luceneQuery.searchEngine();
        setPersonDetails(this.foundPerson);

    }

    /*
     *Gleicht die Kontaktliste anhand des vollen Namens ab.
     */
    public void setPersonDetails(Person foundPerson) {

        this.foundPerson = foundPerson;
        int i = 0;
        for (Person person : this.personTable.getItems()) {
            if (this.luceneQuery.getFuzzyResults().equals(personTable.getItems().get(i).getFullName())) {
                this.foundPerson = personTable.getItems().get(i);
                showPersonDetails(this.foundPerson);
                System.out.println();
                break;
            } else {
                person = new Person("Nicht", "Gefunden");
                showPersonDetails(person);
            }
            i++;
        }
    }

    public Person getFoundPerson() {
        return this.foundPerson;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param contactMain
     */
    public void setContactMain(ContactMain contactMain) {
        this.contactMain = contactMain;
        // Add observable list data to the table
        personTable.setItems(contactMain.getPersonData());
    }

    public ContactMain getContactMain() {
        return this.contactMain;
    }
}