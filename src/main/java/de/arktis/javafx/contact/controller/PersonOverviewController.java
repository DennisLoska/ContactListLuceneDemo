package de.arktis.javafx.contact.controller;

/**
 * Created by Pati on 09.12.2015.
 */

import de.arktis.javafx.contact.SearchEngine.LuceneEngine;
import de.arktis.javafx.contact.model.Searchrequest;
import de.arktis.javafx.contact.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import de.arktis.javafx.contact.model.Person;
import de.arktis.javafx.contact.ContactMain;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    @FXML
    private ListView listView;

    private Searchrequest request;
    //private LuceneSearch lucene;
    private LuceneEngine lucineEngine;

    // Reference to the main application.
    private ContactMain contactMain;
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


    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().lastNameProperty());

        // Clear person details.
           showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
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

    @FXML
    private void handleSearchEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            System.out.println("enter");
            System.out.println(event.getText());
            contactMain.closeSearchPopup();

        }
    }

    //TODO Nach Lucene übergeben
    @FXML
    private void handleSearch(){

        lucineEngine = new LuceneEngine();
        request = new Searchrequest(this.searchField.getText());

        String requesthelper = request.getSearchField();
        System.out.println(request.getSearchField());

        try {
            lucineEngine.engineMain(requesthelper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //öffnet Extra-Fenster
       // contactMain.showSearchPopup();



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
}