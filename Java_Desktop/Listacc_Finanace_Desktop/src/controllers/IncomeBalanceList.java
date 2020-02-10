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
import model.display.DisplayIncome;
import model.display.DisplayUser;
import services.data.IncomeService;


/**
 *
 * @author Agozie
 */
public class IncomeBalanceList implements Initializable{
    private boolean confirmed;
    private final List<DisplayIncome> incomeList;
    private FilteredList<DisplayIncome> incomesFiltered;
    DisplayIncome income;
    @FXML
    TableView<DisplayIncome> incomeTablePop;
    @FXML
    TextField incomeTxtSearch;
    @FXML
    TableColumn incomeColSerial, incomeColClient, incomeColService,
            incomeColPayment, incomeColBalance, incomeColCounts, incomeColId;
    @FXML
    Button selectBtn;
    @FXML 
     private void searchIncome(KeyEvent evt){
            String search = incomeTxtSearch.getText().toLowerCase().trim();
            incomesFiltered.setPredicate(p -> 
                    (p.getAmountReceived()+"").toLowerCase().contains(search.trim()) ||
        p.getClientName().toLowerCase().contains(search.trim()) ||
        (p.getAmountReceivable()+"").contains(search.trim()) );
        incomeTablePop.setItems(incomesFiltered); 
        }
    public IncomeBalanceList() {
        
        this.incomeList = new IncomeService().getAllReceivableIncomes();
    }

    public DisplayIncome getIncome() {
        return income;
    }

    public void setIncome(DisplayIncome income) {
        this.income = income;
    }
    
        public void initialize(URL url, ResourceBundle rb) {
            ObservableList<DisplayIncome> clientData
            = FXCollections.observableArrayList(incomeList);
                       incomesFiltered =  new FilteredList(clientData,(p -> true ));
                  incomeTablePop.setItems(incomesFiltered);
            incomeColSerial.setCellFactory(indexCellFactory());
            incomeColId.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("id"));
            incomeColClient.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("clientName"));
            incomeColService.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("serviceName"));
            incomeColPayment.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("amountReceived"));
            incomeColBalance.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("amountReceivable"));
            incomeColCounts.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("paymentCounts"));
             try{
                incomeTablePop.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null ) {
                    selectBtn.setDisable(false);
                }
                else 
                    selectBtn.setDisable(true);
                });
                }catch(Exception ex){ex.printStackTrace();}
             
        }
        
    @FXML
    private void confirm() {
        confirmed = true ;
        income = incomeTablePop.getSelectionModel().getSelectedItem();
        incomeTablePop.getScene().getWindow().hide();
    }
    public boolean isConfirmed(){
        return confirmed;
    }
}
