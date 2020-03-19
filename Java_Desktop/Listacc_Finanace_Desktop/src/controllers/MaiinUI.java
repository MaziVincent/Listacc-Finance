/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

//import java.awt.event.ActionEvent;
import helpers.CostCatStringConverter;
import helpers.DeptStringConverter;
import helpers.DoubleValuChangeListener;
import helpers.FocusChangeListener;
import helpers.NumberChangeListener;
import helpers.PrjStringConverter;
import helpers.SrvStringConverter;
import helpers.UnitNumberChangeListener;
import helpers.UserStringConverter;
import javafx.event.ActionEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import model.AppModel;
import model.Clients;
import model.CostCategories;
import model.Departments;
import model.Persons;
import model.Projects;
import model.Services;
import model.Users;
import model.display.DisplayClient;
import model.display.DisplayExpenditure;
import model.display.DisplayIncome;
import model.display.DisplayProject;
import model.display.DisplayService;
import model.display.DisplayUser;
import services.data.ClientService;
import services.data.CostCategoryService;
import services.data.DepartmentService;
import services.data.ExpenditureService;
import services.data.IncomeService;
import services.data.ProjectService;
import services.data.ServiceService;
import services.data.UserService;
import services.net.ConnectionService;
import services.net.SynchronizationDownloadService;
import services.net.SynchronizationUploadService;

/**
 *
 * @author Agozie
 */
public class MaiinUI implements Initializable {
    
    public MaiinUI(AppModel model){
        this.connectionSp =  new SimpleStringProperty();
        
        this.connectionColorProperty = new SimpleObjectProperty<>();
        
        this.connectionDisplayPro = new SimpleBooleanProperty();
        this.syncDisplayPro = new SimpleBooleanProperty();
        this.model = model;
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginUser = this.model.getUser();
        adminDisplayProp.set(true);
        connectionDisplayPro.set(true);
        syncDisplayPro.set(false);
        adminPane.visibleProperty().bind(adminDisplayProp);
        incomePane.visibleProperty().bind(incomeDisplayProp);
        expenditurePane.visibleProperty().bind(expenditureDisplayProp);
        settingsPane.visibleProperty().bind(settingsDisplayProp);
        totalLabel.textProperty().bind(incomeTotalProp);
        totalLabel1.textProperty().bind(expendtureTotalProp);
        syncStatus.visibleProperty().bind(syncDisplayPro);
        connectionStatus.textProperty().bind(connectionSp);
        connectionStatus.textFillProperty().bind(connectionColorProperty);
        connectionStatus.visibleProperty().bind(connectionDisplayPro);
        initializeAdminComponents();
        initializeButtonEnableProp();
        populateTables();
        InitializePopup();
        initializeNumberFields();
        initializeNetworkActivities();  
        initializeTableCells();
    }
    
    private void initializeNetworkActivities(){
        // set initial connection values
        //connectionColorProperty.set(Color.TOMATO);
        //connectionSp.set("Not Connected");

        //check connection status
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                ConnectionService conn = new ConnectionService(connectionColorProperty);
                conn.execute(connectionSp);
            });
        }, 0, 10, TimeUnit.SECONDS);

        // run synchronization download operation
        ScheduledExecutorService synDownExec = Executors.newSingleThreadScheduledExecutor();
        synDownExec.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                SynchronizationDownloadService conn = 
                    new SynchronizationDownloadService(connectionDisplayPro, syncDisplayPro); //activitySp
                conn.execute();
            });
        }, 0, 3, TimeUnit.MINUTES);

        // run synchronization upload operation
        ScheduledExecutorService synUpExec = Executors.newSingleThreadScheduledExecutor();
        synUpExec.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                SynchronizationUploadService conn = 
                        new SynchronizationUploadService(connectionDisplayPro, syncDisplayPro); //activitySp
                conn.execute();
            });
        }, 0, 3, TimeUnit.MINUTES);
    }

    private void initializeButtonEnableProp()
    {
        dptBtnSave.disableProperty().bind(dptSaveBtnDisableProp);
        prjBtnSave.disableProperty().bind(prjSaveBtnDisableProp);
        srvBtnSave.disableProperty().bind(srvSaveBtnDisableProp);
        cctgBtnSave.disableProperty().bind(cctgSaveBtnDisableProp);
        incomeBtnSave.disableProperty().bind(incomeSaveBtnDisableProp);
        expBtnEnter.disableProperty().bind(expenditureSaveBtnDisableProp);
        clientSaveBtn.disableProperty().bind(clientSaveBtnDisableProp);
        prjComboDepartment.valueProperty().addListener((obs, oldval, newval) -> {validateProjectForm();});
        clientTxtDob.valueProperty().addListener((obs, oldval, newval) -> {validateClientForm();});
        clientRadioFemale.selectedProperty().addListener((obs, oldval, newval) -> {validateClientForm();});
        clientRadioMale.selectedProperty().addListener((obs, oldval, newval) -> {validateClientForm();});
        srvComboProject.valueProperty().addListener((obs, oldval, newval) -> {validateServiceForm();});
        cctgComboType.valueProperty().addListener((obs, oldval, newval) -> {validateCctgForm();});
        cctgComboType.valueProperty().addListener((obs, oldval, newval) -> {validateCctgForm();});
        incomeComboPType.valueProperty().addListener((obs, oldval, newval) -> {validateIncomeForm();});
        incomeComboPType.valueProperty().addListener((obs, oldval, newval) -> {validateIncomeForm();});
        incomeComboService.valueProperty().addListener((obs, oldval, newval) -> {validateIncomeForm();});
        incomeTxtDate.valueProperty().addListener((obs, oldval, newval) -> {validateIncomeForm();});
        incomeTxtDob.valueProperty().addListener((obs, oldval, newval) -> {validateIncomeForm();});
        expComboCost.valueProperty().addListener((obs, oldval, newval) -> {validateExpenditureForm();});
        expComboProject.valueProperty().addListener((obs, oldval, newval) -> {validateExpenditureForm();});
        expComboIssuer.valueProperty().addListener((obs, oldval, newval) -> {validateExpenditureForm();});
        expComboCost.valueProperty().addListener((obs, oldval, newval) -> {validateExpenditureForm();});

    }
        
    private void populateTables()
    {
        Platform.runLater(() -> {refreshDepartmentTable();});
        Platform.runLater(() -> {refreshProjectView();});
        Platform.runLater(() -> {refreshServiceView();});
        Platform.runLater(() -> {refreshCostCategoryView();});
        Platform.runLater(() -> {refreshIncomeView(false);});
        Platform.runLater(() -> {refreshExpenditureView(false);});
        Platform.runLater(() -> {refreshUserView();});
        Platform.runLater(() -> {refreshClientView();});
        Platform.runLater(() -> {refreshPrinterList();} );
    }
        
    private void refreshPrinterList(){
        try{
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            pservice = PrintServiceLookup.lookupPrintServices(flavor,null);
            ObservableList<PrintService> userDisplayData
                = FXCollections.observableArrayList(pservice);

            printerFiltered =  new FilteredList(userDisplayData,(p -> true ));

            printerList.setCellFactory(new Callback<ListView<PrintService>, ListCell<PrintService>>() {

                @Override
                public ListCell<PrintService> call(ListView<PrintService> param) {
                    ListCell<PrintService> cell = new ListCell<PrintService>() {

                        @Override
                        protected void updateItem(PrintService item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                PrintServiceAttributeSet psaSet =item.getAttributes();
                               setText(psaSet.get(PrinterName.class).toString());
                            }else {
                              setText("");   
                           }
                        }
                    };
                    return cell;
                }
            });
            printerList.setItems(printerFiltered);
            printerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PrintService>() {
                @Override
                public void changed(ObservableValue<? extends PrintService> observable, PrintService oldValue, PrintService newValue) {
                   if(SelectedPrinterIndex != printerList.getSelectionModel().getSelectedIndex() )
                        {
                             makeDefaultBtn.setDisable(false);
                         }

                }
            });
            int size = printerFiltered.getSource().size();

        }catch(Exception exc){
            exc.printStackTrace();
        }
    }
        
    @FXML
    private void makeDefault(ActionEvent evt){
       SelectedPrinterIndex = printerList.getSelectionModel().getSelectedIndex();
       makeDefaultBtn.setDisable(true);
    }
    
    @FXML
    private void validateClientForm(KeyEvent evt){
       validateClientForm();
    }
    
     @FXML
    private void validateClientForm(ActionEvent evt){
       validateClientForm();
    }
    
    private void validateClientForm(){
         Platform.runLater(() -> {
            try{
                String lName = clientTxtLastName.getText();
                String fName =  clientTxtFirstName.getText().trim();
                String phone = clientTxtPhone.getText().trim();
                String email = clientTxtEmail.getText().trim();
                email = null == email?"": email;
                String Uid = clientTxtUid.getText().trim();
                String add = clientTxtAddress.getText().trim();
                String dob = null == clientTxtDob.getValue()? "": clientTxtDob.getValue().toString();
                String gender = clientRadioMale.isSelected() ? "Male": "Female" ;
                        
                String editLName = clientListTable.getSelectionModel().getSelectedItem().getLastName();
                String editFName = clientListTable.getSelectionModel().getSelectedItem().getFirstName();
                String editPhone = clientListTable.getSelectionModel().getSelectedItem().getPhone();
                String editEmail = clientListTable.getSelectionModel().getSelectedItem().getEmail();
                editEmail = null == editEmail?"": editEmail;
                String editUid = clientListTable.getSelectionModel().getSelectedItem().getUId();
                String editAdd =  clientListTable.getSelectionModel().getSelectedItem().getAddress();
                editAdd = null == editAdd?"": editAdd;
                String editGender = clientListTable.getSelectionModel().getSelectedItem().getGender();
                String editDob = clientListTable.getSelectionModel().getSelectedItem().getDateOfBirth();
                editDob = null == editDob?"": editDob;
                if(null == editFName)
                    editFName = clientListTable.getSelectionModel().getSelectedItem().getBusinessName();

                    boolean edit =  lName.compareTo(editLName)==0
                        &&
                        dob.compareTo(editDob)==0
                        && 
                       fName.compareTo(editFName) == 0 &&
                       phone.compareTo(editPhone) == 0 &&
                       email.compareTo(editEmail) == 0 &&
                       Uid.compareTo(editUid ) == 0 &&
                       gender.compareTo(editGender) == 0 &&
                      add.compareTo(editAdd ) == 0;

//                    boolean disable  = (clientRadioPerson.isSelected() && (null == lName ||lName.length()<1) ) || fName.length() < 1 ||
//                                      phone.length() < 1 ||  Uid.length()  < 1
//                                         ; 
                    clientSaveBtnDisableProp.set(edit) ;

            }
            catch(Exception exc)
            {
                clientSaveBtnDisableProp.set(true);
            }
        });  
    }
    
    @FXML
    private void clientSwitchBusiness(ActionEvent evt){
        RadioButton person = (RadioButton)evt.getSource();
        if(!clientListTable.getSelectionModel().isEmpty()){
            if(person == clientRadioBusiness)
            {
                clientTxtLastName.setDisable(true);
                clientRadioMale.setDisable(true);
                clientRadioFemale.setDisable(true);
                clientTxtDob.setDisable(true);

            }else {
                clientTxtLastName.setDisable(false);
                clientRadioMale.setDisable(false);
                clientRadioFemale.setDisable(false);
                clientTxtDob.setDisable(false);
            }
        }
    }
        
    @FXML
    private void saveClient(ActionEvent evt){
        try{
            Persons incomePerson = new Persons();
            Clients client = new Clients();
            String fname = clientTxtFirstName.getText();
            LocalDate localDobDate = clientTxtDob.getValue();
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, localDobDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDobDate.getMonthValue()-1);
            calendar.set(Calendar.YEAR, localDobDate.getYear());
            
            String dob = null == localDobDate ? null :""+calendar.getTimeInMillis();
            if(clientRadioPerson.isSelected()){
                String lname = clientTxtLastName.getText().trim();
                incomePerson.setFirstName(fname);
                incomePerson.setLastName(lname);
                incomePerson.setDateOfBirth(dob);
                incomePerson.setGender(clientRadioMale.isSelected() ? "Male": "Female"  );
            }
            else{
                client.setBusinessName(fname);
                incomePerson = null;
            }

            String email = clientTxtEmail.getText().trim();
            String phone = clientTxtPhone.getText().trim();
            String uid = clientTxtUid.getText().trim();
            client.setPerson(incomePerson);
            String add = clientTxtAddress.getText();
            client.setEmail(email);
            client.setPhone(phone);
            client.setUId(uid);
            client.setAddress(add);
            if(null == email && !email.trim().isEmpty()&& !validateEmail(client.getEmail()))
            {
                error("Invalid Email address");
                return;
            }
            if(clientTxtPhone.getText().trim().length() != 11)
            {
                 error("Invalid  phone number");
                 return;
            }
            if(new ClientService().updateClient(client, clientListTable.getSelectionModel().getSelectedItem().getId()))
            {    info("Client updated successfully");
                    Platform.runLater(() -> {
                        refreshClientView();
                        clientListTable.refresh();
                        clientListTable.getSelectionModel().select(null);
                    });
            }
            else { 
                error("A problem occured while updating client information");
            }

        }catch(Exception e){
            error("A problem occured while updating client");
        }

    }
        
    private void refreshClientView(){
         clientList = new ClientService().getAllClients();
        ObservableList<DisplayClient> clientData
            = FXCollections.observableArrayList(clientList);
                       clientsFiltered =  new FilteredList(clientData,(p -> true ));

       clientListTable.setItems(clientsFiltered);
        
        clientListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
              if(null == newSelection)
                  newSelection = oldSelection;
                if (newSelection != null && !businessCreateProp.get() ) {
                    resetClientForm();
                clientTxtLastName.setText(newSelection.getLastName());
                clientTxtFirstName.setText(newSelection.getFirstName());
                if(null == newSelection.getFirstName() || newSelection.getFirstName().trim().isEmpty())
                {
                  clientTxtFirstName.setText(newSelection.getBusinessName());  
                  clientRadioBusiness.setSelected(true);
                }
                else
                    clientRadioPerson.setSelected(true);
                clientTxtPhone.setText(newSelection.getPhone());
                clientTxtEmail.setText(newSelection.getEmail());

                clientTxtUid.setText(newSelection.getUId());
                clientTxtAddress.setText(newSelection.getAddress());
                if(null == newSelection.getGender() || newSelection.getGender().compareTo("Male") == 0)
                    clientRadioMale.setSelected(true);
                else
                    clientRadioFemale.setSelected(true);
                String date =   newSelection.getDateOfBirth();
                if(null != date && date.length() > 1)
                {
                    long dateLong = Long.parseLong(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dateLong);
                    Instant instant = calendar.getTime().toInstant();
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    clientTxtDob.setValue(instant.atZone(defaultZoneId).toLocalDate());
                }

                     clientSaveBtnDisableProp.set(true);
                     disableClientForm(false);        
            }
        });
         clientSaveBtnDisableProp.set(true);
                disableClientForm(true);  
    }

    private void resetClientForm(){
        clientTxtLastName.setText("");
                clientTxtFirstName.setText("");
                clientTxtPhone.setText("");
                clientTxtEmail.setText("");
                clientTxtDob.setValue(null);
                clientTxtUid.setText("");
                clientTxtAddress.setText("");
                clientRadioMale.setSelected(true);

    }

    private void disableClientForm(boolean disable){
                clientTxtLastName.setDisable(disable);
                clientTxtFirstName.setDisable(disable);
                     clientTxtDob.setDisable(disable);
                     clientRadioMale.setDisable(disable);
                     clientRadioFemale.setDisable(disable);
                if(!disable && clientRadioBusiness.isSelected())
                {
                     clientTxtLastName.setDisable(true);
                     clientTxtDob.setDisable(true);
                     clientRadioMale.setDisable(true);
                     clientRadioFemale.setDisable(true);
                }

                clientTxtPhone.setDisable(disable);
                clientTxtEmail.setDisable(disable);

                clientTxtUid.setDisable(disable);
                clientTxtAddress.setDisable(disable);

    }

    private void refreshUserView(){
        try{
                           List<DisplayUser> usersDisplayList = new UserService().getAllDisplayUsers();
                       
            ObservableList<DisplayUser> userDisplayData
            = FXCollections.observableArrayList(usersDisplayList);
           
                       usersFiltered =  new FilteredList(userDisplayData,(p -> true ));
                       //userListTable.setItems(usersFiltered);
                       expComboIssuer.setItems(usersFiltered);
                       expComboIssuer.setConverter(new UserStringConverter(expComboIssuer));
        }catch(Exception ex){ex.printStackTrace();}
        }
        
    public void initializeTableCells(){
            //departments
            dptColName.setCellValueFactory(new PropertyValueFactory<Departments, String>("name"));
            dptColId.setCellValueFactory(new PropertyValueFactory<Departments, String>("id"));
            dptColSerial.setCellFactory(indexCellFactory());
            //projects
            prjColName.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("name"));
            prjColId.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("id"));
            prjColDepartment.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("departmentName"));
            prjColSerial.setCellFactory(indexCellFactory());
            //services
            srvColName.setCellValueFactory(new PropertyValueFactory<DisplayService, String>("name"));
            srvColId.setCellValueFactory(new PropertyValueFactory<DisplayService, String>("id"));
            srvColAmount.setCellValueFactory(new PropertyValueFactory<DisplayService, String>("amount"));
            srvColProject.setCellValueFactory(new PropertyValueFactory<DisplayService, String>("projectName"));
            srvColSerial.setCellFactory(indexCellFactory());
            //costCAtegory
            cctgColName.setCellValueFactory(new PropertyValueFactory<CostCategories, String>("name"));
            cctgColId.setCellValueFactory(new PropertyValueFactory<CostCategories, String>("id"));
            cctgColType.setCellValueFactory(new PropertyValueFactory<CostCategories, String>("type"));
            cctgColSerial.setCellFactory(indexCellFactory());
            //User
//            userColLastName.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("lastName"));
//            userColId.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("id"));
//            userColFirstName.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("firstName"));
//            userColEmail.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("email"));
//            userColPhone.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("phone"));
//            userColDepartment.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("departmentName"));
//            userColRole.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("role"));
//            userColSerial.setCellFactory(indexCellFactory());
            //Client
            clientColSerial.setCellFactory(indexCellFactory());
            clientColId.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("id"));
            clientColName.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("name"));
            clientColEmail.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("email"));
            clientColPhone.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("phone"));
            clientColAddress.setCellValueFactory(new PropertyValueFactory<DisplayUser, String>("address"));
            //Income
            incomeColSerial.setCellFactory(indexCellFactory());
            incomeColId.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("id"));
            incomeColService.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("serviceName"));
            incomeColClient.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("clientName"));
            incomeColAmount.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("amountReceived"));
            incomeColDate.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("dateString"));
            incomeColPaymentType.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("displayPaymentType"));
            //Expenditures
             expColSerial.setCellFactory(indexCellFactory());
            expIdCol.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("id"));
            expColRecipient.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("businessName"));
            expColProject.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("projectName"));
            expColCostCat.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("costCategoryName"));
            expColAmt.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("amount"));
            expColDate.setCellValueFactory(new PropertyValueFactory<DisplayIncome, String>("dateString"));
            
        }
       
    private void InitializePopup()
    {
        infoLabel = new Label("");  
         popupRegion.setStyle("-fx-background-color: linear-gradient(to bottom, derive(cadetblue, 20%), cadetblue);-fx-border-radius: 20 20 20 20;");
         popupRegion.setOpacity(.4);
         popupRegion.setMinSize(250, 60);  
        infoLabel.setStyle("-fx-effect: dropshadow(three-pass-box, derive(cadetblue, -20%), 10, 0, 4, 4);-fx-padding: 20;");    
        infoLabel.setTextFill(Color.WHITESMOKE);      
        popup.setAutoHide(true);
        popup.getContent().add(popupRegion);
        popup.getContent().add(infoLabel); 
    }
        
    private void info(String message){
        popupRegion.setStyle("-fx-background-color: linear-gradient(to bottom, derive(Green, 20%), cadetblue);-fx-border-radius: 20 20 20 20;");
        infoLabel.setText(message);
        popup.show(model.getStage());
    }

    private void error(String message){
        popupRegion.setStyle("-fx-background-color: linear-gradient(to bottom, derive(Red, 20%), cadetblue);-fx-border-radius: 20 20 20 20;");
        infoLabel.setText(message);
        popup.show(model.getStage());
    }
        
    private void initializeNumberFields(){
        incomeTxtAmount.textProperty().addListener(new DoubleValuChangeListener(incomeTxtAmount));
        incomeTxtDiscount.textProperty().addListener(new DoubleValuChangeListener(incomeTxtDiscount));
        srvTextAmount.textProperty().addListener(new DoubleValuChangeListener(srvTextAmount));
        incomeTxtPhone.textProperty().addListener(new NumberChangeListener(incomeTxtPhone));
        clientTxtPhone.textProperty().addListener(new NumberChangeListener(clientTxtPhone));

        expTxtPhone.textProperty().addListener(new NumberChangeListener(expTxtPhone));
        expTxtAmount.textProperty().addListener(new DoubleValuChangeListener(expTxtAmount));
        incomeTxtUnit.textProperty().addListener(new UnitNumberChangeListener(incomeTxtUnit));
        incomeTxtUnit.focusedProperty().addListener(new FocusChangeListener(incomeTxtUnit));
    }
    
    private void refreshExpenditureView(boolean filter){
        Platform.runLater(() -> {
                Date date = new Date();
                   SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate= formatter.format(date);
                    expTxtDate.setValue(LOCAL_DATE(strDate));
                   expTxtAmount.setText("");
                    expTxtDescription.setText("");
                    expComboCost.getSelectionModel().select(null);
                    expComboProject.getSelectionModel().select(null);
                    expComboIssuer.getSelectionModel().select(null);
                    expRadioNew.setSelected(true);
                    expRadioPerson.setSelected(true);
                    newExpenditureClient();
                    expenditureSaveBtnDisableProp.set(true);
            if(!filter)
                    {
                        expFromDate.setValue(LOCAL_DATE(strDate));
                        expToDate.setValue(LOCAL_DATE(strDate));
                    }
             List<DisplayExpenditure> expList = new ExpenditureService().getAllExpenditures();
        ObservableList<DisplayExpenditure> serviceData
        = FXCollections.observableArrayList(expList);
                   expenditureFiltered =  filterDateforExpenditureView(serviceData);
                   expenditureListTable.setItems(expenditureFiltered);
        });
    }
    
    private void refreshIncomeView(boolean filter)
    {
        Platform.runLater(() -> {
         Date date = new Date();
                   SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate= formatter.format(date);
        if(!filter)
                    {
                        incomeFromDate.setValue(LOCAL_DATE(strDate));
                        incomeToDate.setValue(LOCAL_DATE(strDate));
                    }


        incomeList = new IncomeService().getAllIncomes();
        ObservableList<DisplayIncome> serviceData
        = FXCollections.observableArrayList(incomeList);
                   incomesFiltered =  filterDateforIncomeView(serviceData);
                   incomeListTable.setItems(incomesFiltered);
                   incomeSaveBtnDisableProp.set(true);
                    incomeTxtDate.setValue(LOCAL_DATE(strDate));
                    IncomeTxtDueDate.setValue(LOCAL_DATE(strDate));
                    ObservableList<String> PaymentTypesData
        = FXCollections.observableArrayList(new String[]{"Cash", "Cheque", "Transfer"});
                    incomeComboPType.setItems(PaymentTypesData);
                    //incomeTxtAmount.setText("");
                    incomeTxtDiscount.setText("");
                    IncomeTxtLName.setText("");
                    incomeTxtFName.setText("");
                    incomeTxtPhone.setText("");
                    incomeTxtEmail.setText("");
                    incomeTxtDob.setValue(null);
                    incomeTxtAdd.setText("");
                    incomeTxtUid.setText("");
                    amountReceived =0;
                    try{
            incomeListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null ) {
                inocmePrintBtn.setDisable(false);
            }
            else 
                inocmePrintBtn.setDisable(true);
            });
            }catch(Exception ex){ex.printStackTrace();}

        });
    }
    
    private FilteredList<DisplayIncome> filterDateforIncomeView(ObservableList<DisplayIncome> serviceData){
         LocalDate localDate = incomeFromDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, localDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDate.getMonthValue()-1);
            calendar.set(Calendar.YEAR, localDate.getYear());
             calendar.set(Calendar.HOUR_OF_DAY, 0);
             calendar.set(Calendar.MINUTE, 0);
             calendar.set(Calendar.SECOND, 0);

            LocalDate localDate2 = incomeToDate.getValue();
            Instant instant2 = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.DATE, localDate2.getDayOfMonth());
            calendar2.set(Calendar.MONTH, localDate2.getMonthValue()-1);
            calendar2.set(Calendar.YEAR, localDate2.getYear());
            calendar2.set(Calendar.HOUR_OF_DAY, 23);
             calendar2.set(Calendar.MINUTE, 59);
             calendar2.set(Calendar.SECOND, 59);
               totalIncome = 0;
             FilteredList<DisplayIncome> filtered = new FilteredList(serviceData,(p -> true ));
             filtered.setPredicate(p -> {
                 try{
                 long time = Long.parseLong(p.getDate()+"");
                   boolean valid = time >= calendar.getTimeInMillis() && time <= calendar2.getTimeInMillis();
                   if(valid)
                        totalIncome += p.getAmountReceived();
                 return valid;
                }catch(Exception exc){}
                 return true;
             });
                NumberFormat formatter = new DecimalFormat("#,###.##"); 
                String amountStr = formatter.format(totalIncome);
                incomeTotalProp.set("Total NGN "+ amountStr);
             return filtered;
     }
    
    private FilteredList<DisplayExpenditure> filterDateforExpenditureView(ObservableList<DisplayExpenditure> serviceData){
         LocalDate localDate = expFromDate.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, localDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDate.getMonthValue()-1);
            calendar.set(Calendar.YEAR, localDate.getYear());
             calendar.set(Calendar.HOUR_OF_DAY, 0);
             calendar.set(Calendar.MINUTE, 0);
             calendar.set(Calendar.SECOND, 0);

            LocalDate localDate2 = expToDate.getValue();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.DATE, localDate2.getDayOfMonth());
            calendar2.set(Calendar.MONTH, localDate2.getMonthValue()-1);
            calendar2.set(Calendar.YEAR, localDate2.getYear());
            calendar2.set(Calendar.HOUR_OF_DAY, 23);
             calendar2.set(Calendar.MINUTE, 59);
             calendar2.set(Calendar.SECOND, 59);
                totalExpenditure = 0;
             FilteredList<DisplayExpenditure> filtered = new FilteredList(serviceData,(p -> true ));
             filtered.setPredicate(p -> {
                 try{
                 long time = p.getDate();
                   boolean valid = time >= calendar.getTimeInMillis() && time <= calendar2.getTimeInMillis();
                   if(valid)
                        totalExpenditure += p.getAmount();
                 return valid;
                }catch(Exception exc){}
                 return true;
             });
                NumberFormat formatter = new DecimalFormat("#,###.##"); 
                String amountStr = formatter.format(totalExpenditure);
                expendtureTotalProp.set("Total NGN "+ amountStr);
             return filtered;
     }
    
    private void refreshCostCategoryView(){
         List<CostCategories> costCategoryList = new CostCategoryService().getAllCostCategories();
    ObservableList<CostCategories> costCategoryData
        = FXCollections.observableArrayList(costCategoryList);
                   costCategoriesFiltered =  new FilteredList(costCategoryData,(p -> true ));
                    costCategoryListTable.setItems(costCategoriesFiltered);
        ObservableList<String> costCategoryTypesData
        = FXCollections.observableArrayList(new String[]{"Direct Cost", "Indirect Cost", "Capital Cost"});
                    cctgComboType.setItems(costCategoryTypesData);
                    expComboCost.setItems(costCategoryData);
                    expComboCost.setConverter(new CostCatStringConverter(expComboCost));
                    costCategoryListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
              if(null == newSelection)
                  newSelection = oldSelection;
            if (newSelection != null && !costCategoryCreateProp.get() ) {
                cctgTextName.setText(newSelection.getName());
                cctgTextDescription.setText(newSelection.getDescription());
                cctgComboType.getSelectionModel().select(cctgComboType.getConverter().fromString(newSelection.getType()));
                cctgSaveBtnDisableProp.set(true);
                disableCostCategoryForm(false);             
            }
                    });
      }
    
    private void disablecostCategoryForm(boolean disable){
                cctgTextName.setDisable(disable);
                cctgTextDescription.setDisable(disable);
                cctgComboType.setDisable(disable);
    }
    
    private void refreshServiceView()
    {
        List<DisplayService> serviceList = new ServiceService().getAllServices();
        ObservableList<DisplayService> serviceData
        = FXCollections.observableArrayList(serviceList);
                   servicesFiltered =  new FilteredList(serviceData,(p -> true ));
                   serviceListTable.setItems(servicesFiltered);
                   srvSaveBtnDisableProp.set(true);
                   incomeComboService.setItems(serviceData);
                   incomeComboService.setConverter(new SrvStringConverter(incomeComboService));

          serviceListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
              if(null == newSelection)
                  newSelection = oldSelection;
            if (newSelection != null && !serviceCreateProp.get() ) {
                srvTextName.setText(newSelection.getName());
                srvTextAmount.setText(newSelection.getAmount()+"");
                adminServCheckFixed.setSelected(newSelection.getFixedAmount() == 1);
                srvTextDescription.setText(newSelection.getDescription());
                srvComboProject.getSelectionModel().select(srvComboProject.getConverter().fromString(newSelection.getProjectName()));

                srvTextName.setDisable(false);
                srvTextAmount.setDisable(false);
                adminServCheckFixed.setDisable(false);
                srvComboProject.setDisable(false);
                srvTextDescription.setDisable(false);
            }

            });
    }
    
    private void refreshProjectView(){
        List<DisplayProject> projectList = new ProjectService().getAllProjects();
        ObservableList<DisplayProject> projectData
        = FXCollections.observableArrayList(projectList);
                   projectsFiltered =  new FilteredList(projectData,(p -> true ));
                   projectListTable.setItems(projectsFiltered);
                   prjSaveBtnDisableProp.set(true);
                   srvComboProject.setItems(projectData);
         srvComboProject.setConverter(new PrjStringConverter(srvComboProject));
         expComboProject.setConverter(new PrjStringConverter(expComboProject));
         expComboProject.setItems(projectData);
         projectListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
             if(null == newSelection)
                  newSelection = oldSelection;
            if (newSelection != null && !projectCreateProp.get() ) {
                prjTextName.setText(newSelection.getName());
                prjTextDescription.setText(newSelection.getDescription());
                prjComboDepartment.getSelectionModel().select(prjComboDepartment.getConverter().fromString(newSelection.getDepartmentName()));
                prjTextName.setDisable(false);
                prjTextDescription.setDisable(false);
                prjComboDepartment.setDisable(false);
            }

            });

    }
    
    private void refreshDepartmentTable(){
         List<Departments> departmentList = new DepartmentService().getAllDepartments();
         ObservableList<Departments> departmentData
        = FXCollections.observableArrayList(departmentList);
                   departmentsFiltered =  new FilteredList(departmentData,(p -> true ));
                   departmentListTable.setItems(departmentsFiltered);
                   //disable save button
                   dptSaveBtnDisableProp.set(true);
         //populate all department list combo box
         prjComboDepartment.setItems(departmentData);
         prjComboDepartment.setConverter(new DeptStringConverter(prjComboDepartment));

          departmentListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
              if(null == newSelection)
                  newSelection = oldSelection;
            if (newSelection != null && !departmentCreateProp.get() ) {
                dptTextName.setText(newSelection.getName());
                dptTextName.setDisable(false);
            }

            });

    }

    public static final LocalDate LOCAL_DATE (String dateString){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return localDate;
        }


    private void initializeAdminComponents()
    {
         //Department
        departmentCreateLine.visibleProperty().bind(departmentCreateProp);
        departmentEditLine.visibleProperty().bind(departmentCreateProp.not());
        departmentInfoLabel.visibleProperty().bind(departmentCreateProp.not());
        departmentInfoBullet.visibleProperty().bind(departmentCreateProp.not());
        //Project
        projectCreateLine.visibleProperty().bind(projectCreateProp);
        projectEditLine.visibleProperty().bind(projectCreateProp.not());
        projectInfoLabel.visibleProperty().bind(projectCreateProp.not());
        projectInfoBullet.visibleProperty().bind(projectCreateProp.not());
        //Service
        serviceCreateLine.visibleProperty().bind(serviceCreateProp);
        serviceEditLine.visibleProperty().bind(serviceCreateProp.not());
        serviceInfoLabel.visibleProperty().bind(serviceCreateProp.not());
        serviceInfoBullet.visibleProperty().bind(serviceCreateProp.not());
         //costCategory
        costCategoryCreateLine.visibleProperty().bind(costCategoryCreateProp);
        costCategoryEditLine.visibleProperty().bind(costCategoryCreateProp.not());
        costCategoryInfoLabel.visibleProperty().bind(costCategoryCreateProp.not());
        costCategoryInfoBullet.visibleProperty().bind(costCategoryCreateProp.not());
     
        //business
        businessCreateLine.visibleProperty().bind(businessCreateProp);
        businessEditLine.visibleProperty().bind(businessCreateProp.not());
        businessInfoLabel.visibleProperty().bind(businessCreateProp.not());
        businessInfoBullet.visibleProperty().bind(businessCreateProp.not());

        departmentCreateProp.set(true);
        projectCreateProp.set(true);
        serviceCreateProp.set(true);
        costCategoryCreateProp.set(true);
        userCreateProp.set(true);
        businessCreateProp.set(false);
    }

    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> indexCellFactory() {
        return t -> new TableCell<T, Void>() {

                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    setText(isEmpty() ? "" : Integer.toString(i+1));
                }

            };
    }


    @FXML
    private void personBusinessSelect(ActionEvent event)
    {
        RadioButton rdb = (RadioButton)event.getSource();
        if(incomeRadioPerson == rdb)
        {
            IncomeTxtLName.setDisable(false);
            incomeTxtFName.setPromptText("First Name");
            incomeRadioMale.setDisable(false);
            incomeTxtDob.setDisable(false);
            incomeRadioFemale.setDisable(false);
        }
        else if(incomeRadioBusiness == rdb){
            IncomeTxtLName.setDisable(true);
            incomeTxtFName.setPromptText("Business Name");
            incomeRadioMale.setDisable(true);
            incomeTxtDob.setDisable(true);
            incomeRadioFemale.setDisable(true);
        }
        else if(expRadioBusiness == rdb){
             expTxtLastName.setDisable(true);
            expFirstName.setPromptText("Business Name");
            expRadioMale.setDisable(true);
            expRadioFemale.setDisable(true);
        }
        else{
            expTxtLastName.setDisable(false);
            expFirstName.setPromptText("First Name");
            expRadioMale.setDisable(false);
            expRadioFemale.setDisable(false);
        }
    }
    
    @FXML
    private void saveDepartment(ActionEvent event)
    {
        DepartmentService dpService = new DepartmentService();
        String deptName = dptTextName.getText().trim();
       if(deptName.length() < 2)
       {
           error("Name should be at least 2 characters");
           return;
       }
       if(symbolPresent(deptName) )
       {
           error("Name should be created without any symbol");
           return;
       }
       if(dpService.departmentNameExists(deptName))
       {
       error("Department name already exists");
       return;
       }
       if(departmentCreateProp.get())
       {
            if(dpService.createDepartment(deptName))
            {
                info("Department created successfully ");
                Platform.runLater(() -> {refreshDepartmentTable();});
                dptTextName.setText("");

            }
            else{
                error("Could not create Department");
            }
       }
       else if(!departmentCreateProp.get())
       {
           Departments dept = departmentListTable.getSelectionModel().getSelectedItem();
           dept.setName(deptName);
           if(dpService.updateDepartments(dept))
           {
               info("Department updated successfully ");
                Platform.runLater(() -> {
                departmentListTable.getSelectionModel().select(null);
                refreshDepartmentTable();
                departmentListTable.refresh();
                });
                dptTextName.setText("");
           }
           else{
                error("Could not update Department");
            }
       }
    }

    @FXML
    private void saveService(ActionEvent event)
    {
        ServiceService srvService = new ServiceService();
       String servName = srvTextName.getText().trim();
       double amount = 0;
       if(servName.length() < 2)
       {
           error("Name should be at least 2 characters");
           return;
       }
       if(symbolPresent(servName) )
       {
           error("Name should be created without any symbol");
           return;
       }
      
       if(serviceCreateProp.get() && srvService.serviceNameExists(servName))
       {
            error("Project name already exists");
            return;
       }
       else if(!serviceCreateProp.get() && srvService.serviceNameExists(servName,serviceListTable.getSelectionModel().getSelectedItem().getId() ))
       {

           error("Project name already exists");
            return;
       }
       try{

       amount = Double.parseDouble(srvTextAmount.getText().trim());
       if(amount < 10)
            error("Enter a valid amount greater than 10");
              }catch(Exception ec){
                  error("Enter a valid amount greater than zero");
                  return;
              }
       try{

           if(serviceCreateProp.get())
            if(srvService.createService(servName,
                    amount,
                    srvTextDescription.getText().trim(),
                    srvComboProject.getSelectionModel().getSelectedItem().getId()
                    ,adminServCheckFixed.isSelected() ))
                     {
                        info("Service created successfully ");
                        refreshServiceView();
                        Platform.runLater(() -> {serviceListTable.refresh();});
                        createService();
                    } else
                error("Error Saving Services");
           //if update 
            else if (!serviceCreateProp.get())
                if(srvService.updateService(servName, amount, srvTextDescription.getText().trim(), 
                        srvComboProject.getSelectionModel().getSelectedItem().getId(), 
                        adminServCheckFixed.isSelected(), serviceListTable.getSelectionModel().getSelectedItem().getId()))
                {
                    info("Service updated successfully ");
                        refreshServiceView();
                        editService();
                        Platform.runLater(() -> {serviceListTable.refresh();});
                }


       }catch(Exception exc)
       {
           exc.printStackTrace();
       }
    }
        
    @FXML
    private void saveProject(ActionEvent event)
    {
        ProjectService prjService = new ProjectService();
       String prjName = prjTextName.getText().trim();
       if(prjName.length() < 2)
       {
           error("Name should be at least 2 characters");
           return;
       }
       if(symbolPresent(prjName) )
       {
           error("Name should be created without any symbol");
           return;
       }

       if(projectCreateProp.get()){
           if(prjService.projectNameExists(prjName))
            {
            error("Project name already exists");
            return;
            }
       if(prjService.createProject(prjName, prjTextDescription.getText().trim(),prjComboDepartment.getSelectionModel().getSelectedItem().getId()))
       {
           info("Project created successfully ");
           refreshProjectView();
           createProject();

       }else {
               error("There wsa a problem creating project");

           }
       }else if(!projectCreateProp.get()){
           Projects project = projectListTable.getSelectionModel().getSelectedItem();
            if(prjService.projectNameExists(prjName, project.getId()))
                {
                error("Project name already exists");
                return;
                }
               if(prjService.updateProject(project.getId(),prjName, prjTextDescription.getText().trim(),prjComboDepartment.getSelectionModel().getSelectedItem().getId()))
                    {
                       info("Project updated successfully ");
                       Platform.runLater(() -> {projectListTable.getSelectionModel().select(null);
                       projectListTable.refresh();
                       refreshProjectView();
                       editProject();
                       });
                      
                      
                   }else {
                   error("There wsa a problem updating project");
              
               }
           }
          
        }
        
     

    @FXML
    private void saveCostCategory(ActionEvent event)
    {
        CostCategoryService ccService = new CostCategoryService();
       String deptName = cctgTextName.getText().trim();
       if(deptName.length() < 2)
       {
           error("Name should be at least 2 characters");
           return;
       }
       if(symbolPresent(deptName) )
       {
           error("Name should be created without any symbol");
           return;
       }
       if(costCategoryCreateProp.get() )
       {
           if( ccService.costCategoryNameExists(deptName))
           {
                error("Cost Category name already exists");
                return;
           }
            if(ccService.createCategories(deptName, cctgTextDescription.getText().trim(),
               cctgComboType.getSelectionModel().getSelectedItem()))
            {
                info("Cost Category created successfully ");
                refreshCostCategoryView();
                cctgTextName.setText("");
                cctgTextDescription.setText("");     
            }
            else{
                error("Could not create Department");
         }
       }
       if((!costCategoryCreateProp.get()) )
       {
           int id = costCategoryListTable.getSelectionModel().getSelectedItem().getId();
           if(ccService.costCategoryNameExists(deptName, id))
           {
                error("Cost Category name already exists");
                return;
           }
            if(ccService.updateCategories(deptName, cctgTextDescription.getText().trim(),
               cctgComboType.getSelectionModel().getSelectedItem(), id))
            {
                info("Cost Category updated successfully ");
                refreshCostCategoryView();
                costCategoryListTable.getSelectionModel().select(null);
                costCategoryListTable.refresh();
                editCostCategory();
            }
            else{
                error("Could not update Department");
         }
       }

    }

    @FXML 
    private void setIncomeAmount(ActionEvent event){
        try{
        incomeTxtUnit.setText("1");
        try{
            Services service = incomeComboService.getSelectionModel().getSelectedItem();
            if(null != service)
            {
                //if(incomeRadioNewIncome.isSelected())
                incomeTxtAmount.setText(service.getAmount()+"");
                if(incomeRadioNewIncome.isSelected())
                incomeTxtAmount.setDisable((service.getFixedAmount() == 1));

            }
        }catch(Exception e){}
        setIncomeAmount();
        }catch(Exception exc){}
    }

    private void setIncomeAmount(){
        double discount = 0;
        double amount  = 0;
        int unit = 1;
         NumberFormat formatter = new DecimalFormat("#,###.00"); 
        try{
            amount  = Double.parseDouble(incomeTxtAmount.getText().trim());
        }catch(Exception e){}
        try{
             unit = Integer.parseInt(incomeTxtUnit.getText().trim());
        }catch(Exception e){}
        try{
             discount = Double.parseDouble(incomeTxtDiscount.getText().trim());

        }catch(Exception e){}            
        amountReceived = amount*unit - discount;
       // if(!incomeRadioPart.isSelected())
        incomeLabelAmountRecieved.setText("NGN "+ formatter.format(amountReceived));
    }

    @FXML
    private void saveIncome(ActionEvent event){
        DisplayIncome income = new DisplayIncome();
              try{
           LocalDate localDate = incomeTxtDate.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, localDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDate.getMonthValue()-1);
            calendar.set(Calendar.YEAR, localDate.getYear());


            LocalDate localDobDate = incomeTxtDob.getValue();

             String dob = null ;
            if(null != localDobDate)
            {
                Calendar bdayCalendar = Calendar.getInstance();
                bdayCalendar.set(Calendar.DATE, localDobDate.getDayOfMonth());
                bdayCalendar.set(Calendar.MONTH, localDobDate.getMonthValue()-1);
                bdayCalendar.set(Calendar.YEAR, localDobDate.getYear());
                dob = bdayCalendar.getTimeInMillis()+"";
            }
       
         LocalDate localDueDate = IncomeTxtDueDate.getValue();
         Calendar dueCalendar = Calendar.getInstance();
            dueCalendar.set(Calendar.DATE, localDueDate.getDayOfMonth());
            dueCalendar.set(Calendar.MONTH, localDueDate.getMonthValue()-1);
            dueCalendar.set(Calendar.YEAR, localDueDate.getYear());
            
                  Persons incomePerson = new Persons();

                     String fname = incomeTxtFName.getText().trim(); 

                    if(incomeRadioPerson.isSelected()){
                        String lname = IncomeTxtLName.getText().trim();
                        incomePerson.setFirstName(fname);
                        incomePerson.setLastName(lname);
                        incomePerson.setDateOfBirth(dob);
                        incomePerson.setGender(incomeRadioMale.isSelected() ? "Male": "Female"  );
                    }
                    String email = incomeTxtEmail.getText().trim();
                    String phone = incomeTxtPhone.getText().trim();
                    String uid = incomeTxtUid.getText().trim();
                    //incomeClient.setPersonId(incomePerson);
                    String add = incomeTxtAdd.getText();
                     if(null == incomeClient)
                     {
                        incomeClient = new Clients();
                        incomeClient.setUId(uid);
                        incomeClient.setAddress(null == add?null:add.trim() );
                        incomeClient.setPhone(phone);
                        incomeClient.setEmail(email);
                        incomeClient.setId(0);
                        incomeClient.setBusinessName(incomeRadioBusiness.isSelected() ? fname : null);
                     }
                    income.setPerson(incomeRadioPerson.isSelected()? incomePerson : null);
                   income.setClient(incomeClient);
                   income.setAmountReceived(amountReceived);                   
                   income.setDate(calendar.getTimeInMillis());
                   income.setDateDue(dueCalendar.getTimeInMillis());
                   income.setType(incomeRadioNewIncome.isSelected() ?"New" :"Balance");
                   income.setPaymentType(incomeComboPType.getSelectionModel().getSelectedItem());
                   income.setUser(model.getUser());
                   income.setServiceIdnum(incomeComboService.getSelectionModel().getSelectedItem().getId());
            if(null != email && email.trim().length() > 1 && !validateEmail(incomeClient.getEmail()))
            {
                error("Invalid Email address");
                return;
            }
            if(incomeTxtPhone.getText().trim().length() != 11)
            {
                 error("Invalid  phone number");
                 return;
            }

            double discount = 0 ;
                try{
                    if(incomeRadioPart.isSelected() && incomeRadioNewIncome.isSelected()){
                    discount =  Double.parseDouble(incomeTxtDiscount.getText().trim());
                    income.setAmountReceivable(discount);

                     }else
                    {income.setDiscount(discount);
                        if(discount >= amountReceived)
                         {
                             error("Can not have a discount higher than the amount received ");
                         }
                    }
                if(incomeRadioBalance.isSelected())
                    income.setParentIncomeId(parentIncomeId);
                }catch(Exception exc)
                {
                    error("Enter a remaining balance!");
                    return;
                }

       }catch(Exception x){ x.printStackTrace();}

        if(new IncomeService().createIncome(income))
        {
            info("Income entered successfully");
            incomeClient = null;
               newIncomeRecord(event);
            Platform.runLater(() -> {
                refreshIncomeView(false);
                refreshClientView();
            });
        }
    }

    @FXML 
    private void newIncomeClient(ActionEvent evt)
    {
        incomeClient = null;
         incomeTxtEmail.setText("");
         incomeTxtPhone.setText("");
         incomeTxtAdd.setText("");
         incomeTxtUid.setText("");    
         IncomeTxtLName.setText("");
         incomeTxtFName.setText("");
         incomeTxtDob.setValue(null);
         incomeRadioPerson.setSelected(true);
        disableIncomeClientForm(false);

    }

    @FXML
    private void newExpenditureClient(ActionEvent evt)
    {
        newExpenditureClient();
    }

    private void newExpenditureClient(){
                    disableExpForm(false);
                    expTxtLastName.setText("");
                    expFirstName.setText("");
                    expTxtAddress.setText("");
                    expTxtPhone.setText("");
                    expTxtEmail.setText("");
                    expenditureClient = null;
                    validateExpenditureForm();
    }

    private void disableIncomeClientForm(boolean disable){
         incomeTxtEmail.setDisable(disable);
         incomeTxtPhone.setDisable(disable);
         incomeTxtEmail.setDisable(disable);
         incomeTxtUid.setDisable(disable);
         IncomeTxtLName.setDisable(disable);
         incomeTxtFName.setDisable(disable);
         incomeTxtDob.setDisable(disable);
         incomeTxtAdd.setDisable(disable);
         incomeRadioMale.setDisable(disable);
         incomeRadioFemale.setDisable(disable);
        // incomeRadioNew.setDisable(disable);
         //existingClientRadio.setDisable(disable);
         incomeRadioBusiness.setDisable(disable);
         incomeRadioPerson.setDisable(disable);

    }

    @FXML 
    private void searchIncome(KeyEvent evt){
       Platform.runLater(() -> {
           try
               {
           String search = incomeTxtSearch.getText().toLowerCase().trim();
                incomesFiltered.setPredicate(
                    p -> p.getClientName().toLowerCase().contains(search.trim()) ||
                    p.getServiceName().toLowerCase().contains(search.trim()) ||
                    p.getDisplayPaymentType().toLowerCase().contains(search.trim()) || 
                    (p.getAmountReceived()+"").contains(search.trim()));
                incomeListTable.setItems(incomesFiltered); 
               }catch(Exception exc){
               exc.printStackTrace();}
       });

    }

    @FXML 
    private void searchExpenditure(KeyEvent evt){
       Platform.runLater(() -> {
           try
               {
           String search = expenditureTxtSearch.getText().toLowerCase().trim();
                expenditureFiltered.setPredicate(
                    p -> 
                            //p.getDescription().toLowerCase().contains(search.trim()) ||
                    p.getBusinessName().contains(search.trim()) ||
                    p.getFirstName().toLowerCase().contains(search.trim()) || 
                    p.getLastName().toLowerCase().contains(search.trim()) || 
                    (p.getAmount()+"").contains(search.trim()));
                expenditureListTable.setItems(expenditureFiltered); 
               }catch(Exception exc){
               exc.printStackTrace();}
       });

    }

    @FXML
    private void validateDepartmentForm(KeyEvent evt)
    {
         Departments dep  = departmentListTable.getSelectionModel().getSelectedItem();
         TextField tf = ((TextField)evt.getSource());
         if(!departmentCreateProp.get() && dep!=null)
         dptSaveBtnDisableProp.set(tf.getText().trim().length() < 1 || 
                 tf.getText().trim().compareTo(dep.getName().trim()) ==0);
         else if(departmentCreateProp.get())
             dptSaveBtnDisableProp.set(tf.getText().trim().length() <1 );

    }

    @FXML
    private void validateProjectForm(KeyEvent evt)
    {
         Platform.runLater(() ->{validateProjectForm();});
    }

    @FXML
    private void validateServiceForm(KeyEvent evt)
    {
        Platform.runLater(() -> { validateServiceForm();});
    }
        
    @FXML
    private void validateServiceForm(ActionEvent evt)
    {
        Platform.runLater(() ->{ validateServiceForm();});
    }

    private void validateProjectForm(){
        try{
            Projects prj = projectListTable.getSelectionModel().getSelectedItem();
            Departments comboDept = prjComboDepartment.getSelectionModel().getSelectedItem();
           boolean disable = prjTextName.getText().trim().length() < 1 
                    || prjTextDescription.getText().trim().length() < 1
                    || prjComboDepartment.getSelectionModel().isEmpty()
                   || (!projectCreateProp.get() && null == prj)
                   ||(!projectCreateProp.get() && prj.getName()
                           .compareTo(prjTextName.getText().trim()) == 0 
                                && prj.getDepartment().getId() == comboDept.getId()
                                && ( ((null == prj.getDescription() || prj.getDescription().trim().length()<1) 
                                        && (null == prjTextDescription.getText() || prjTextDescription.getText().trim().length()<1)) 
                                     || prj.getDescription().trim().compareTo(prjTextDescription.getText().trim())==0));

         prjSaveBtnDisableProp.set(disable);
        }catch(Exception exc){
            prjSaveBtnDisableProp.set(true);
        }
    }

    private void editDepartment(){
        departmentListTable.getSelectionModel().select(null);
        departmentCreateProp.set(false);
        dptTextName.setDisable(true);
        dptTextName.setText("");
       

    }

    private void createDepartment(){
        departmentCreateProp.set(true);
        dptTextName.setDisable(false);
        dptTextName.setText("");
    }

    private void editProject(){
        projectCreateProp.set(false);
         prjTextName.setText("");
                prjTextDescription.setText("");
                prjComboDepartment.getSelectionModel().select(null);
                prjTextName.setDisable(true);
                prjTextDescription.setDisable(true);
                prjComboDepartment.setDisable(true);
                
    }

    private void createProject(){
         projectCreateProp.set(true);
         prjTextName.setText("");
                prjTextDescription.setText("");
                prjComboDepartment.getSelectionModel().select(null);
                prjTextName.setDisable(false);
                prjTextDescription.setDisable(false);
                prjComboDepartment.setDisable(false);
    }

    private void createService(){
                serviceCreateProp.set(true);
                clearServiceForm(false);

    }

    private void clearServiceForm(boolean disable){
        srvTextName.setText("");
                srvTextAmount.setText("");
                srvTextDescription.setText("");
                adminServCheckFixed.setSelected(false);
                srvComboProject.getSelectionModel().select(null);
                srvTextAmount.setDisable(disable);
                srvTextDescription.setDisable(disable);
                adminServCheckFixed.setDisable(disable);
                srvTextName.setDisable(disable);
                srvComboProject.setDisable(disable);
                serviceListTable.getSelectionModel().select(null);

    }

    private void editService(){
         serviceCreateProp.set(false);
                clearServiceForm(true);
    }

    public static boolean validateEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = 
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
            return matcher.find();
    }

    private void validateExpenditureForm()
    {
        String amountStr = expTxtAmount.getText().trim();    
        String description = expTxtDescription.getText().trim();
        String lastName = "";
        String phone = expTxtPhone.getText();
        if(expRadioPerson.isSelected())
        {
            lastName =  expTxtLastName.getText().trim();
        }
        String firstName = expFirstName.getText().trim();
        boolean invalid = true;
        try{
            double amount = Double.parseDouble(amountStr);
                invalid = expComboCost.getSelectionModel().isEmpty() || 
                            expComboProject.getSelectionModel().isEmpty() ||
                            expComboIssuer.getSelectionModel().isEmpty() ||
                            amount < 1 ||  description.length() < 2 || phone.length() < 1 ||
                            (expRadioNew.isSelected() && expRadioPerson.isSelected() && (lastName.length() <  1 ||
                                 firstName.length() < 1 )) ||
                        (expRadioBusiness.isSelected() && ( firstName.length() < 1));
                expenditureSaveBtnDisableProp.set(invalid);
        }catch(Exception exc)
        {
            expenditureSaveBtnDisableProp.set(true);
            exc.printStackTrace();
        }
        expenditureSaveBtnDisableProp.set(invalid);

    }

    @FXML
    private void saveExpenditure(ActionEvent actEvt){
        try{
            int costCatId = expComboCost.getSelectionModel().getSelectedItem().getId();
            int prjId = expComboProject.getSelectionModel().getSelectedItem().getId();
            int userId = expComboIssuer.getSelectionModel().getSelectedItem().getId();
            double amount = Double.parseDouble(expTxtAmount.getText().trim());
            Persons person; 
            Clients client;
            String description = expTxtDescription.getText().trim();
            String phone =  expTxtPhone.getText().trim();
            String email = expTxtEmail.getText().trim();
            String firstName = expFirstName.getText().trim();
            
            if(phone.length() != 11 && phone.length() > 0)
            {
                error("Phone number should be 11 digits");
                return ;
            }
            if(email.length() > 0 && !validateEmail(email) )
            {
                error("Invalid Email.");
                return ;
            } 
            ExpenditureService expService = new ExpenditureService();
            LocalDate localDate = expTxtDate.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, localDate.getDayOfMonth());
            calendar.set(Calendar.MONTH, localDate.getMonthValue()-1);
            calendar.set(Calendar.YEAR, localDate.getYear());
             DisplayExpenditure ds = new DisplayExpenditure(costCatId,prjId,userId,null  == expenditureClient?0:expenditureClient.getId() ,amount, description);
             ds.setDate(calendar.getTimeInMillis());
             if(expRadioNew.isSelected())
             {
                 client = new Clients();
                 person = new Persons();

                 client.setEmail(email);
                 client.setPhone(phone);
                 client.setUId("");
                 if(expRadioBusiness.isSelected())
                 client.setBusinessName(firstName);
                    if(expRadioPerson.isSelected())
                    {
                        String lastName = expTxtLastName.getText().trim();
                        person.setFirstName(firstName);
                        person.setLastName(lastName);
                        person.setGender(expRadioMale.isSelected()?"Male":"Female" );
                        ds.setPerson(person);
                    }
                 ds.setClient(client);

             }
             else {
                    ds.setClient(expenditureClient);
                    
             }
             if(expService.createExpenditure(ds))
                    {
                        Platform.runLater(() -> {
                            refreshExpenditureView(false);
                        }
                        );


                        info("Expenditure saved successfully");
                    }
                        else
                            error("There was a problem entering the expenditure");
                    expenditureClient = null;
        }catch(Exception ec)
        {
            error("There was a problem saving the expenditure");
            ec.printStackTrace();
        }
    }

    private void disableExpForm(boolean disable)
    {
        expRadioPerson.setDisable(disable);
        expRadioBusiness.setDisable(disable);
        expTxtLastName.setDisable(disable);
        expFirstName.setDisable(disable);
        expTxtEmail.setDisable(disable);
        expTxtPhone.setDisable(disable);
        expTxtAddress.setDisable(disable);
        expRadioMale.setDisable(disable);
        expRadioFemale.setDisable(disable);
    }

    private void createCostCategory(){
       costCategoryCreateProp.set(true);
       resetCostCategoryForm();
       disableCostCategoryForm(false);
   }

    private void editCostCategory(){
       costCategoryCreateProp.set(false);
       resetCostCategoryForm();
       disableCostCategoryForm(true);
       costCategoryListTable.getSelectionModel().select(null);
   }

    private void disableCostCategoryForm(boolean disable){
       cctgComboType.setDisable(disable);
       cctgTextDescription.setDisable(disable);
       cctgTextName.setDisable(disable);
   }
       
    private void resetCostCategoryForm(){
        cctgComboType.getSelectionModel().select(null);
        cctgTextDescription.setText("");
        cctgTextName.setText("");
    }

    private void validateServiceForm(){
         try{
          boolean edit = !serviceCreateProp.get();

             DisplayService service = (DisplayService) serviceListTable.getSelectionModel().getSelectedItem();
             DisplayProject project = (DisplayProject) srvComboProject.getSelectionModel().getSelectedItem();
             double amount = Double.parseDouble(srvTextAmount.getText().trim());
             if(serviceCreateProp.get()){
                 boolean disable = srvTextName.getText().trim().length() < 1
                 || srvTextDescription.getText().trim().length() < 1
                 || Double.parseDouble(srvTextAmount.getText().trim()) < 1 
                 || srvComboProject.getSelectionModel().isEmpty();
                
                 srvSaveBtnDisableProp.set(disable);
             }
         
         else{
                boolean dis =   (srvTextName.getText().trim().compareTo(service.getName()) == 0
                              && srvTextDescription.getText().trim().compareTo(service.getDescription())== 0
                              &&  amount == service.getAmount()
                              &&  ((adminServCheckFixed.isSelected() && service.getFixedAmount() == 1) || (!adminServCheckFixed.isSelected() && service.getFixedAmount() == 0 ))
                              &&  project.getId() == service.getProjectsId()
                 );
                srvSaveBtnDisableProp.set(dis);
                 }
                 }catch(Exception ex){
                     srvSaveBtnDisableProp.set(true);
                     ex.printStackTrace();
                 }
     }

    private void disableIcomeForm(boolean disable){
        incomeComboService.setDisable(disable);
        //incomeComboPType.setDisable(disable);
        IncomeTxtDueDate.setDisable(disable);
        incomeTxtUnit.setDisable(disable);
        incomeTxtAmount.setDisable(disable);
        incomeRadioFull.setDisable(disable);
        incomeRadioPart.setDisable(disable);
    }

    @FXML
    private void filterIncomeTable(ActionEvent evt){
        refreshIncomeView(true);
    }

    @FXML
    private void filterExpenditureTable(ActionEvent evt){
        refreshExpenditureView(true);
    }

    @FXML
    private void showClientListPopup(ActionEvent Evt){
        if(existingClientRadio.isSelected()){ 
            try{
            disableIncomeClientForm(true);
           
            Stage stage;
            Parent root;
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientPopupList.fxml"));
            ClientListPopup popupController = new ClientListPopup(clientList, true);
            loader.setController(popupController);
            root = loader.load();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(existingClientRadio.getScene().getWindow());
            stage.setTitle("Select Client from the list");
                stage.showAndWait();
                if (popupController.isConfirmed()) {
                    DisplayClient client =  popupController.getClient();
                    incomeTxtEmail.setText(client.getEmail());
                    incomeTxtPhone.setText(client.getPhone());
                    incomeTxtAdd.setText(client.getAddress());
                    incomeTxtUid.setText(client.getUId());    
                    IncomeTxtLName.setText( client.getLastName());
                    incomeTxtFName.setText((null == client.getFirstName() 
                            || client.getFirstName().trim().length() < 1)
                                ?client.getBusinessName() : client.getFirstName() );
                     Platform.runLater(() -> {
                        incomeClient = new ClientService().getClientById(client.getId());
                        if(null == incomeClient.getPerson())
                             incomeRadioBusiness.setSelected(true);
                             validateIncomeForm();
                     });
                     
                }
                else 
                {
                    incomeRadioNew.setSelected(true);
                    disableIncomeClientForm(false);
                    
                }

                stage.close();
            }catch(Exception exc){
            incomeRadioNew.setSelected(true);
            disableIncomeClientForm(false);
           
               }
            validateIncomeForm();
        }


    }

    @FXML
    private void newIncomeRecord(ActionEvent evt)
    {
        //if(incomeRadioNewIncome.isSelected())
        { 
        incomeRadioNewIncome.setSelected(true);
            incomeLabelAmountRecieved.setText("");
            disableIncomeClientForm(false);
                disableIcomeForm(false);
                incomeComboService.getSelectionModel().select(null);
                incomeComboPType.getSelectionModel().select(null);
                incomeTxtAmount.setText("");
                incomeTxtUnit.setText("");
                incomeTxtDiscount.setText("");
                Date date = new Date();
                   SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate= formatter.format(date);
                incomeTxtDate.setValue(LOCAL_DATE(strDate));
                IncomeTxtDueDate.setValue(LOCAL_DATE(strDate));
                IncomeTxtLName.setText("");
                incomeTxtFName.setText("");
                incomeTxtPhone.setText("");
                incomeTxtEmail.setText("");
                incomeTxtUid.setText("");
                incomeTxtAdd.setText("");
                incomeTxtDob.setValue(null);
                incomeRadioFull.setSelected(true);
                incomeRadioNew.setSelected(true);
                incomeLabelAmountRecieved.setText("");
                incomeRadioPerson.setSelected(true);

        }
    }

    @FXML 
    private void showIncomeListPopup(ActionEvent evt){
          if(incomeRadioBalance.isSelected()){ 
            try{
                disableIncomeClientForm(true);
                disableIcomeForm(true);
                incomeRadioNew.setDisable(true);
                    existingClientRadio.setDisable(true);
            Stage stage;
            Parent root;
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/IncomeListForBalancePayment.fxml"));
            IncomeBalanceList popupController = new IncomeBalanceList();
            loader.setController(popupController);
            root = loader.load();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(existingClientRadio.getScene().getWindow());
            stage.setTitle("Select Income from the list");
                stage.showAndWait();
                if (popupController.isConfirmed()) {
                    DisplayIncome popIncome =  popupController.getIncome();
                    incomeComboService.getSelectionModel().select(incomeComboService.getConverter().fromString(popIncome.getServiceName()));
                    incomeComboPType.getSelectionModel().select(popIncome.getDisplayPaymentType());
                    incomeTxtAmount.setText(popIncome.getAmountReceivable()+"");
                    incomeRadioFull.setDisable(false);
                    incomeRadioPart.setDisable(false);
                    Clients client = popIncome.getClient();
                    incomeTxtAdd.setText(client.getAddress());
                    incomeTxtUid.setText(client.getUId()); 
                    parentIncomeId = popIncome.getId();
                    if(null != client.getPerson()){
                        IncomeTxtLName.setText( client.getPerson().getLastName());
                        incomeTxtFName.setText(client.getPerson().getFirstName());
                    }else 
                    {
                        incomeTxtFName.setText(client.getBusinessName());
                    }
                    //incomeTxtDiscount.setPromptText("Remaining Balance");
                    incomeTxtPhone.setText(client.getPhone());
                    incomeTxtEmail.setText(client.getEmail());
                     Platform.runLater(() -> {
                        incomeClient = new ClientService().getClientById(client.getId());
                        if(null == incomeClient.getPerson())
                             incomeRadioBusiness.setSelected(true);
                         setIncomeAmount();
                     });
                    
                }
                else 
                {
                    incomeComboService.getSelectionModel().select(null);
                    incomeComboPType.getSelectionModel().select(null);
                    incomeRadioNew.setSelected(true);
                    disableIncomeClientForm(false);
                    disableIcomeForm(false);
                    incomeRadioNewIncome.setSelected(true);
                    incomeTxtDiscount.setPromptText("Discount");
                    incomeLabelAmountRecieved.setText("");
                    incomeRadioNew.setDisable(false);
                    existingClientRadio.setDisable(false);
                }

                stage.close();
            }catch(Exception exc){
            incomeRadioNew.setSelected(true);
            exc.printStackTrace();
            disableIncomeClientForm(false);
            incomeRadioNew.setDisable(false);
                    existingClientRadio.setDisable(false);
               }
            validateIncomeForm();
        }
    }

    @FXML
    private void showClientListExp(ActionEvent Evt){
        if(expRadioExisting.isSelected()){ 
            try{
                disableExpForm(true);
            Stage stage;
            Parent root;
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientPopupList.fxml"));
            ClientListPopup popupController = new ClientListPopup(clientList, false);
            loader.setController(popupController);
            root = loader.load();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(existingClientRadio.getScene().getWindow());
            stage.setTitle("Select Client from the list");
                stage.showAndWait();
                if (popupController.isConfirmed()) {
                    DisplayClient client =  popupController.getClient();
                    expTxtEmail.setText(client.getEmail());
                    expTxtPhone.setText(client.getPhone());
                    expTxtAddress.setText(client.getAddress());
                    expTxtLastName.setText( client.getLastName());
                    expFirstName.setText((null == client.getFirstName() 
                            || client.getFirstName().trim().length() < 1)
                                ?client.getBusinessName() : client.getFirstName() );
                     Platform.runLater(() -> {
                        expenditureClient = new ClientService().getClientById(client.getId());
                        if(null == expenditureClient.getPerson()){
                             expRadioBusiness.setSelected(true);}
                         validateExpenditureForm();    
                     });

                }
                else 
                {
                    expRadioNew.setSelected(true);
                    disableExpForm(false);
                }

                stage.close();
            }catch(Exception exc){
            expRadioNew.setSelected(true);
            disableExpForm(false);
               }
             validateExpenditureForm();
        }


    }

    @FXML
    private void validateCctgForm(KeyEvent evt)
    {  
      Platform.runLater(() -> {
          validateCctgForm();
      });  
    }
    
    

    private void validateCctgForm()
    {
        String tblName = "";
        String tblDesc = "";
        String tblType = "";
        if(!costCategoryCreateProp.get()){
         tblName = costCategoryListTable.getSelectionModel().getSelectedItem().getName();
         tblDesc = costCategoryListTable.getSelectionModel().getSelectedItem().getDescription();
         tblType = costCategoryListTable.getSelectionModel().getSelectedItem().getType();      
        }
        String type = cctgComboType.getSelectionModel().getSelectedItem().trim();
        String name = cctgTextName.getText().trim();
        String desc =  cctgTextDescription.getText().trim();
        boolean disable = name.length() < 1
                || desc.length() < 1
                || cctgComboType.getSelectionModel().isEmpty() ||
               ( (!costCategoryCreateProp.get()) && (name.compareTo(tblName)==0
                && tblDesc.compareTo(desc) == 0 && type.compareTo(tblType) == 0));
        cctgSaveBtnDisableProp.set(disable);
    }

    @FXML
    private void printReceipt(ActionEvent evt){
        try{
            if (SelectedPrinterIndex < 0)
            {
                error("Select a default printer");
                return;
            }
             NumberFormat formatter = new DecimalFormat("#,###.00"); 
          DisplayIncome income = (DisplayIncome) incomeListTable.getSelectionModel().getSelectedItem();
         String data ="      LISTAACC LTD.\n" 
                       +"Service: " + income.getServiceName()+ "\n"
                       + "Fee: NGN " + formatter.format(income.getAmountReceived()) + "\n"      
                       + "Date:  "+ income.getDateStringWithTime() + "\n"
                       + "Remaining Balance: NGN " + formatter.format(income.getAmountReceivable())+"\n"
                       + "Due Date For Balance: " +income.getDateDue()
                       + "\n"
                       + "\n"
                       + "\n"
                       + "\n"
                       + "Thank you for serving you."+"\n"
                       + "call: "+"09078766951,08138699452"+"\n"
                       + "Annunciation ICT Center \n St. Patrick's Parish \n Kpiri-Kpiri Abakaliki"+"\n"
                       + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _"+"\n"
                       + "\n"
                       + "\n"
                       + "\n";
         byte[] bytes = data.getBytes("CP437");
         DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
         PrintService[] pservice = PrintServiceLookup.lookupPrintServices(flavor,null);
        DocPrintJob pjob = pservice[SelectedPrinterIndex].createPrintJob();
        Doc doc2 = new SimpleDoc(bytes, flavor, null);
        //pjob.print(doc, null);
        pjob.print(doc2, null);

        }catch(Exception exc){exc.printStackTrace();}
    }

    @FXML
    private void validateIncomeForm(KeyEvent evt)
    {
         Platform.runLater(() -> {

              setIncomeAmount();
               validateIncomeForm();
         });

    }
        
    @FXML
    private void validateExpenditureForm(KeyEvent evt)
    {
         Platform.runLater(() -> {
              validateExpenditureForm();
         });

    }

    private void validateIncomeForm(){
        double discount =0;
              try{

                String discountString = incomeTxtDiscount.getText().trim();
                String lname = incomeRadioPerson.isSelected()? IncomeTxtLName.getText().trim():"";
                String fname =  incomeTxtFName.getText().trim();
                String email = incomeTxtEmail.getText().trim();
                String phone = incomeTxtPhone.getText().trim();
                String uid = incomeTxtUid.getText().trim();

               // String dob = localDobDate.toString();

                try{
                discount = discountString.length() > 1 ? Double.parseDouble(discountString): 0;
                }catch(Exception exc){}
                        boolean disable = incomeRadioPerson.isSelected() && (incomeComboService.getSelectionModel().isEmpty() ||
                                        amountReceived < 10 || incomeComboPType.getSelectionModel().isEmpty()
                                        || (incomeRadioFull.isSelected() && discount > amountReceived/2) || amountReceived < 1
                                         || lname.length() < 1 || fname.length()<1 || (email.length() < 1 &&
                                            phone.length() < 1)
                                        || uid.length() < 1 );
                        incomeSaveBtnDisableProp.set(disable);
                       // Clients incomeClient = new Clients();
                        if(incomeRadioBusiness.isSelected())
                                {
                                    boolean temp =  fname.length() < 1 || (email.length() < 1 && phone.length() < 1 )
                                            || uid.length() < 1 || (incomeRadioFull.isSelected() && discount > amountReceived/2) 
                                            || amountReceived < 1;
                                    incomeSaveBtnDisableProp.set( temp );
                                   // incomeClient.setBusinessName(fname);

                                }



                }catch(Exception xc){
                    xc.printStackTrace();
                    incomeSaveBtnDisableProp.set(true);
                    }


    }

    private boolean symbolPresent(String text)
    {
       Pattern p = Pattern.compile("[^A-Za-z ]++$");
       Matcher m = p.matcher(text);
       boolean found = m.find();
       return found;
    }

    @FXML 
    private void fullPartIncome(ActionEvent act)
    {
        RadioButton src = (RadioButton)act.getSource();
        if(src == incomeRadioFull)
        {
//                incomeTxtAmount.setPromptText("Amount");
//                incomeTxtAmount.setText("");
            IncomeTxtDueDate.setDisable(true);
            incomeTxtDiscount.setText("");
            incomeTxtDiscount.setPromptText("Discount");
        }
        else if(src == incomeRadioPart)
        {
            IncomeTxtDueDate.setDisable(false);
//                incomeTxtAmount.setPromptText("Amount Received");
//                incomeTxtAmount.setText("");
            incomeTxtDiscount.setText("");
            incomeTxtDiscount.setPromptText("Balance To be paid");
           // incomeLabelAmountRecieved.setText("");
        }
        validateIncomeForm();
    }

    @FXML
    private void switchView(MouseEvent event)
    {
        try{
             Glow glow = new Glow();
             glow.setInput(new Bloom());
             adminDisplayProp.set(false);
             incomeDisplayProp.set(false);
             expenditureDisplayProp.set(false);
             settingsDisplayProp.set(false);
             adminLabel.setEffect(null);
             incomeLabel.setEffect(null);
             expenditureLabel.setEffect(null);
             settingsLabel.setEffect(null);

        String sourceID = ((Label)event.getSource()).getId();
        switch(sourceID){

            case "adminLabel":
                adminLabel.setEffect(glow);
                adminDisplayProp.set(true);
                break;
            case "incomeLabel":
                incomeLabel.setEffect(glow);
                incomeDisplayProp.set(true);
                break;
            case "expenditureLabel":
                expenditureLabel.setEffect(glow);
                expenditureDisplayProp.set(true);
                break;
            case "settingsLabel":
                settingsLabel.setEffect(glow);
                settingsDisplayProp.set(true);
            default:;
        }
        }catch(Exception ex){}
    }
        
    @FXML
    private void switchCreateEditView(MouseEvent event)
    {
        try{
        String sourceID = ((Label)event.getSource()).getId();
        switch(sourceID){

            case "departmentCreateLabel":
                createDepartment();
                break;
            case "departmentEditLabel":
                editDepartment();
                break;
            case "projectCreateLabel":
               createProject();
                break;
            case "projectEditLabel":
                editProject();
                break;
           case "serviceCreateLabel":
                   createService();
                //serviceCreateProp.set(true);
                break;
            case "serviceEditLabel":
                editService();
                break;
            case "costCategoryCreateLabel":
                createCostCategory();
                break;
            case "costCategoryEditLabel":
                editCostCategory();
                break;
            case "businessCreateLabel":
                businessCreateProp.set(false);
                break;
            case "businessEditLabel":
                clientListTable.getSelectionModel().select(null);
                businessCreateProp.set(false);
                break;
            default:;
        }
        }catch(Exception ex){}
    }
        
    PrintService[] pservice;
    int  SelectedPrinterIndex = -1;
    static double totalIncome, totalExpenditure;
    List<DisplayClient> clientList;
    List<DisplayIncome> incomeList;
    private Label infoLabel;
    Popup popup = new Popup();
    Region popupRegion = new Region();
    Clients incomeClient, expenditureClient;
    final AppModel model;
    public static Users loginUser;
    @FXML
    TableView
            incomeListTable, expenditureListTable;
    @FXML
    TableView<DisplayClient> clientListTable;
    @FXML
     TableView<Departments> departmentListTable;
    @FXML
     TableView<DisplayProject> projectListTable;
    @FXML
    TableView<DisplayService> serviceListTable;
    @FXML
    TableView<CostCategories> costCategoryListTable;
    @FXML
    TabPane adminPane, settingsPane; 
    @FXML
    AnchorPane incomePane, expenditurePane;
    @FXML
    Label adminLabel, departmentCreateLabel, departmentEditLabel, departmentInfoLabel,projectCreateLabel, projectEditLabel, projectInfoLabel,
            serviceCreateLabel, serviceEditLabel, serviceInfoLabel,costCategoryCreateLabel, costCategoryEditLabel, costCategoryInfoLabel, 
            userCreateLabel,businessCreateLabel, businessEditLabel, businessInfoLabel,
            incomeLabelAmountRecieved;
    @FXML
    Label incomeLabel, expenditureLabel, settingsLabel, totalLabel, totalLabel1, connectionStatus, syncStatus;
        
    @FXML
    Line departmentCreateLine, departmentEditLine, projectCreateLine, projectEditLine,serviceCreateLine, serviceEditLine, costCategoryCreateLine, 
            costCategoryEditLine, businessCreateLine, businessEditLine;
    @FXML
    Circle departmentInfoBullet, projectInfoBullet, serviceInfoBullet, costCategoryInfoBullet, businessInfoBullet;

    @FXML
    TextField dptTextName, prjTextName, srvTextName, srvTextAmount, cctgTextName, 
            incomeTxtAmount, incomeTxtDiscount, IncomeTxtLName, incomeTxtFName,
            incomeTxtPhone, incomeTxtEmail, incomeTxtUid, incomeTxtSearch, expenditureTxtSearch,
            expTxtAmount, expTxtLastName, expFirstName, expTxtEmail, expTxtPhone, incomeTxtUnit,
            clientTxtLastName, clientTxtFirstName, clientTxtPhone, clientTxtEmail, clientTxtUid;
    @FXML
    TextArea prjTextDescription, srvTextDescription, cctgTextDescription, incomeTxtAdd, 
            expTxtDescription, expTxtAddress, clientTxtAddress;
          
    SimpleStringProperty connectionSp;
    SimpleObjectProperty connectionColorProperty;
    BooleanProperty connectionDisplayPro, syncDisplayPro;
    private final BooleanProperty adminDisplayProp = new SimpleBooleanProperty();
    private final BooleanProperty incomeDisplayProp = new SimpleBooleanProperty();
    private final BooleanProperty expenditureDisplayProp = new SimpleBooleanProperty();
    private final BooleanProperty settingsDisplayProp = new SimpleBooleanProperty();
    private final BooleanProperty departmentCreateProp = new SimpleBooleanProperty();
    private final BooleanProperty projectCreateProp = new SimpleBooleanProperty();
    private final BooleanProperty serviceCreateProp = new SimpleBooleanProperty();
    private final BooleanProperty costCategoryCreateProp = new SimpleBooleanProperty();

    private final BooleanProperty userCreateProp = new SimpleBooleanProperty();
    private final BooleanProperty businessCreateProp = new SimpleBooleanProperty();
    private final BooleanProperty dptSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty prjSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty srvSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty cctgSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty incomeSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty clientSaveBtnDisableProp = new SimpleBooleanProperty();
    private final BooleanProperty expenditureSaveBtnDisableProp = new SimpleBooleanProperty();
    private final StringProperty incomeTotalProp = new SimpleStringProperty();
    private final StringProperty expendtureTotalProp = new SimpleStringProperty();

        
        
        
    public static FilteredList departmentsFiltered, projectsFiltered, servicesFiltered, costCategoriesFiltered,
                                           usersFiltered, clientsFiltered, printerFiltered;
    private static FilteredList<DisplayIncome> incomesFiltered;
    private static FilteredList<DisplayExpenditure> expenditureFiltered;
    @FXML
    private TableColumn dptColSerial, dptColName, dptColId, prjColSerial, prjColName, prjColDepartment, prjColId,
                        srvColSerial, srvColName, srvColProject, srvColAmount, srvColId, cctgColSerial, cctgColName,
                        cctgColType, cctgColId, userColId, 
                        //userColFirstName, userColLastName, userColEmail, userColPhone,userColRole, userColSerial, userColDepartment, 
                        clientColSerial, clientColName, clientColPhone, 
                        clientColEmail, clientColAddress, clientColId, incomeColSerial, incomeColService, incomeColClient,
                        incomeColAmount, incomeColDate, incomeColPaymentType, incomeColId, expColSerial,expColRecipient, 
                        expColProject, expColCostCat, expColAmt, expColDate, expIdCol;
    @FXML
    private Button dptBtnSave, prjBtnSave,expBtnEnter, srvBtnSave, cctgBtnSave, 
            incomeBtnSave, inocmePrintBtn, clientSaveBtn, makeDefaultBtn;

    @FXML
    private ComboBox<Departments> prjComboDepartment ;
    @FXML
    private ComboBox<DisplayProject> srvComboProject ;
    @FXML
    private ComboBox<DisplayService> incomeComboService;
    @FXML
    private ComboBox<CostCategories> expComboCost;
    @FXML
    private ComboBox<DisplayProject> expComboProject;
    @FXML
    private ComboBox<DisplayUser> expComboIssuer;
    @FXML
    private ComboBox<String> cctgComboType, incomeComboPType ;
    @FXML 
    private DatePicker incomeTxtDate, IncomeTxtDueDate, incomeTxtDob, incomeFromDate, 
            expTxtDate, incomeToDate, expFromDate, expToDate, clientTxtDob;
    @FXML
    private RadioButton incomeRadioNew, incomeRadioPerson, incomeRadioBusiness, 
            incomeRadioMale, incomeRadioFemale,  expRadioNew, expRadioBusiness , 
            incomeRadioNewIncome, existingClientRadio, expRadioPerson, 
            expRadioMale, expRadioFemale, expRadioExisting, incomeRadioFull, incomeRadioPart,
            incomeRadioBalance, clientRadioMale, clientRadioFemale, clientRadioBusiness, clientRadioPerson;
    double amountReceived;
    int parentIncomeId;
    @FXML
    private CheckBox adminServCheckFixed;
    @FXML 
    private ListView printerList;
        
}
