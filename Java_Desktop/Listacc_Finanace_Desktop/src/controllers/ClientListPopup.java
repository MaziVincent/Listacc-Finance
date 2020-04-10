/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import static controllers.MaiinUI.indexCellFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.display.DisplayClient;
import model.display.DisplayUser;

/**
 *
 * @author Agozie
 */
public class ClientListPopup implements Initializable {
    private boolean confirmed;
    private final List<DisplayClient> clientList;
    private FilteredList<DisplayClient> clientsFiltered;
    DisplayClient client;
    boolean income;

    public DisplayClient getClient() {
        return client;
    }

    public void setClient(DisplayClient client) {
        this.client = client;
    }

    public List<DisplayClient> getClientList() {
        return clientList;
    }
    @FXML
    TextField clientTxtSearch;
    @FXML
    Button selectBtn;
   
    @FXML
    TableColumn clientColSerial, clientColName, clientColPhone, 
                            clientColEmail, clientColAddress, clientColId;
    @FXML 
    TableView <DisplayClient> clientListTable ;
    public ClientListPopup(List<DisplayClient> clientList, boolean income){
        this.clientList = clientList;
        this.income = income;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<DisplayClient> clientData
            = FXCollections.observableArrayList(clientList);
        if(income)
            clientsFiltered =  new FilteredList<>(clientData,(p -> (null != p.getUId() && !p.getUId().trim().isEmpty()) ));
        else 
            clientsFiltered =  new FilteredList<>(clientData,(p -> (null == p.getUId() || p.getUId().isEmpty()) ));
        
        clientListTable.setItems(clientsFiltered);
        clientColSerial.setCellFactory(indexCellFactory());
        clientColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        try{
            clientListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null ) {
                   selectBtn.setDisable(false);
                }
                else {
                   selectBtn.setDisable(true);
                }
            });
        }
        catch(Exception ex){ex.printStackTrace();}
    }
    
    @FXML 
    private void searchClient(KeyEvent evt){
        String search = clientTxtSearch.getText().toLowerCase().trim();
        clientsFiltered.setPredicate(p -> p.getName().toLowerCase().contains(search.trim()) ||
            p.getLastName().toLowerCase().contains(search.trim()) ||
            p.getEmail().toLowerCase().contains(search.trim()) ||
            p.getPhone().toLowerCase().contains(search.trim()) ||
            p.getAddress().toLowerCase().contains(search.trim()));
        clientListTable.setItems(clientsFiltered); 
    }
        
    @FXML
    private void cancelPop() {
        confirmed = false ;
        //clientTxtSearch.getScene().getWindow().;
    }
    
     @FXML
    private void confirm() {
        confirmed = true ;
        client = clientListTable.getSelectionModel().getSelectedItem();
        clientTxtSearch.getScene().getWindow().hide();
    }
    public boolean isConfirmed(){
        return confirmed;
    }
}
