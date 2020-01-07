/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.AppModel;
import model.CostCategories;
import model.Departments;
import model.display.DisplayProject;
import model.display.DisplayService;
import services.data.CostCategoryService;
import services.data.DepartmentService;
import services.data.ProjectService;
import services.data.ServiceService;

/**
 *
 * @author Agozie
 */
public class MaiinUI implements Initializable {
    
      
        public MaiinUI(AppModel model){
            this.model = model;
        }
        
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            adminPane.visibleProperty().bind(adminDisplayProp);
            incomePane.visibleProperty().bind(incomeDisplayProp);
            expenditurePane.visibleProperty().bind(expenditureDisplayProp);
            settingsPane.visibleProperty().bind(settingsDisplayProp);
            initializeAdminComponents();
            populateTables();
            InitializePopup();
        }
        
        private void populateTables()
        {
            //Department
              List<Departments> departmentList = new DepartmentService().getAllDepartments();
               List<DisplayProject> projectList = new ProjectService().getAllProjects();
               List<DisplayService> serviceList = new ServiceService().getAllServices();
               List<CostCategories> costCategoryList = new CostCategoryService().getAllCostCategories();
        
        
        ObservableList<Departments> departmentData
            = FXCollections.observableArrayList(departmentList);
                       departmentsFiltered =  new FilteredList(departmentData,(p -> true ));
        ObservableList<DisplayProject> projectData
            = FXCollections.observableArrayList(projectList);
                       projectsFiltered =  new FilteredList(projectData,(p -> true ));
        ObservableList<DisplayService> serviceData
            = FXCollections.observableArrayList(serviceList);
                       servicesFiltered =  new FilteredList(serviceData,(p -> true ));
        ObservableList<CostCategories> costCategoryData
            = FXCollections.observableArrayList(costCategoryList);
                       costCategoriesFiltered =  new FilteredList(costCategoryData,(p -> true ));
       
       departmentListTable.setItems(departmentsFiltered);
       
       projectListTable.setItems(projectsFiltered);
       serviceListTable.setItems(servicesFiltered);
       costCategoryListTable.setItems(costCategoriesFiltered);
            initializeTableCells();
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
            srvColName.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("name"));
            srvColId.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("id"));
            srvColAmount.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("amount"));
            srvColProject.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("projectName"));
            srvColSerial.setCellFactory(indexCellFactory());
            //costCAtegory
            cctgColName.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("name"));
            cctgColId.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("id"));
            cctgColType.setCellValueFactory(new PropertyValueFactory<DisplayProject, String>("type"));
           cctgColSerial.setCellFactory(indexCellFactory());
            
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
             //user
            userCreateLine.visibleProperty().bind(userCreateProp);
            userEditLine.visibleProperty().bind(userCreateProp.not());
            userInfoLabel.visibleProperty().bind(userCreateProp.not());
            userInfoBullet.visibleProperty().bind(userCreateProp.not());
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
            businessCreateProp.set(true);
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
        private void CreateDepartment(ActionEvent event)
        {
            info("This is a popup");
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
                    departmentCreateProp.set(true);
                    break;
                case "departmentEditLabel":
                    departmentCreateProp.set(false);
                    break;
                case "projectCreateLabel":
                    projectCreateProp.set(true);
                    break;
                case "projectEditLabel":
                    projectCreateProp.set(false);
                    break;
               case "serviceCreateLabel":
                    serviceCreateProp.set(true);
                    break;
                case "serviceEditLabel":
                    serviceCreateProp.set(false);
                    break;
                case "costCategoryCreateLabel":
                    costCategoryCreateProp.set(true);
                    break;
                case "costCategoryEditLabel":
                    costCategoryCreateProp.set(false);
                    break;
                case "userCreateLabel":
                    userCreateProp.set(true);
                    break;
                case "userEditLabel":
                    userCreateProp.set(false);
                    break;
                case "businessCreateLabel":
                    businessCreateProp.set(true);
                    break;
                case "businessEditLabel":
                    businessCreateProp.set(false);
                    break;
                default:;
            }
            }catch(Exception ex){}
        }
        
        private Label infoLabel;
        Popup popup = new Popup();
        Region popupRegion = new Region();
        final AppModel model;
         @FXML
         TableView departmentListTable, projectListTable, serviceListTable, costCategoryListTable;
         @FXML
         TabPane adminPane, settingsPane;
         @FXML
         AnchorPane incomePane, expenditurePane;
         @FXML
         Label adminLabel, departmentCreateLabel, departmentEditLabel, departmentInfoLabel,projectCreateLabel, projectEditLabel, projectInfoLabel,
                 serviceCreateLabel, serviceEditLabel, serviceInfoLabel,costCategoryCreateLabel, costCategoryEditLabel, costCategoryInfoLabel, 
                 userCreateLabel, userEditLabel, userInfoLabel,businessCreateLabel, businessEditLabel, businessInfoLabel;
         @FXML
         Label incomeLabel, expenditureLabel, settingsLabel;
        
         @FXML
         Line departmentCreateLine, departmentEditLine, projectCreateLine, projectEditLine,serviceCreateLine, serviceEditLine, costCategoryCreateLine, 
                 costCategoryEditLine,userCreateLine, userEditLine, businessCreateLine, businessEditLine;
         @FXML
         Circle departmentInfoBullet, projectInfoBullet, serviceInfoBullet, costCategoryInfoBullet, userInfoBullet, businessInfoBullet;
         
         @FXML
         TextField dptTextName;
         
          
        private BooleanProperty adminDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty incomeDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty expenditureDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty settingsDisplayProp = new SimpleBooleanProperty();
        private BooleanProperty departmentCreateProp = new SimpleBooleanProperty();
        private BooleanProperty projectCreateProp = new SimpleBooleanProperty();
        private BooleanProperty serviceCreateProp = new SimpleBooleanProperty();
        private BooleanProperty costCategoryCreateProp = new SimpleBooleanProperty();
        private BooleanProperty userCreateProp = new SimpleBooleanProperty();
        private BooleanProperty businessCreateProp = new SimpleBooleanProperty();
        
        public static FilteredList<Departments> departmentsFiltered, projectsFiltered, servicesFiltered, costCategoriesFiltered;
        @FXML
        private TableColumn dptColSerial, dptColName, dptColId, prjColSerial, prjColName, prjColDepartment, prjColId,
                            srvColSerial, srvColName, srvColProject, srvColAmount, srvColId, cctgColSerial, cctgColName,
                            cctgColType, cctgColId;
        
}
