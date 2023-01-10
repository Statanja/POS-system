package com.anjanatec.pos.controller;

import com.anjanatec.pos.db.DBConnection;
import com.anjanatec.pos.db.Database;
import com.anjanatec.pos.model.Customer;
import com.anjanatec.pos.view.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class  CustomerFormcontroller {
    public TextField txtAddress;
    public TextField txtName;
    public TextField txtSalary;
    public TextField txtId;
    public TableView<CustomerTm> tblCustomer;
    public TableColumn<Object, Object> colId;
    public TableColumn<Object, Object> colName;
    public TableColumn<Object, Object> colAddress;
    public TableColumn<Object, Object> colSalary;
    public TableColumn<Object, Object> colOption;
    public Button btnSaveCustomer;
    public AnchorPane customerFormContext;
    public TextField txtSearch;

    private String searchTex="";

    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        searchCustomer(searchTex);

        tblCustomer.getSelectionModel().selectedItemProperty()
                .addListener((observable,oldValue,newValue) ->{
                    if(null!= newValue){

                        setData(newValue);
                    }


                } );
        txtSearch.textProperty().
                addListener((observable, oldValue, newValue) -> {
                    searchTex=newValue;
                    searchCustomer(searchTex);
                });

    }

    private void setData(CustomerTm tm){
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }

    private void searchCustomer(String text){

        String searchTex ="%"+text+"%";

        try {
            ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Customer WHERE name LIKE ? || Address LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,searchTex);
            statement.setString(2,searchTex);
            ResultSet set = statement.executeQuery();

            while (set.next()){

                    Button btn = new Button("Delete");
                    CustomerTm tm = new CustomerTm(
                            set.getString(1),
                            set.getString(2),
                            set.getString(3),
                            set.getDouble(4),
                            btn);
                    tmList.add(tm);
                    btn.setOnAction(event -> {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                "Are you sure Delete customer",
                                ButtonType.YES,ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if(buttonType.get()==ButtonType.YES){

                            try {
                                String sql1 = "DELETE FROM Customer WHERE id=?";
                                PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                                statement1.setString(1,tm.getId());

                                if(statement1.executeUpdate()>0){
                                    searchCustomer(searchTex);
                                    new Alert(Alert.AlertType.INFORMATION,"Customer Delete").show();

                                }else {

                                    new Alert(Alert.AlertType.WARNING,"Try Again").show();
                                }
                            } catch (ClassNotFoundException | SQLException e ){
                                e.printStackTrace();
                            }




                        }

                    });
                }
            tblCustomer.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }


        for (Customer c:Database.customersTable){}
        }



    public void saveCustomerOnAction(ActionEvent actionEvent){
        Customer c1 = new Customer(txtId.getText(),txtName.getText(),txtAddress.getText(),
                Double.parseDouble(txtSalary.getText()));
        if(btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")){
                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade","root","1234");
                    String sql = "INSERT INTO Customer VALUES (?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1,c1.getId());
                    statement.setString(2,c1.getName());
                    statement.setString(3,c1.getAddress());
                    statement.setDouble(4,c1.getSalary());

                    if(statement.executeUpdate()>0){
                        searchCustomer(searchTex);
                        clearField();
                        new Alert(Alert.AlertType.INFORMATION,"Customer Saved").show();

                    }else {

                        new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    }
                } catch (ClassNotFoundException | SQLException e){
                    e.printStackTrace();
                }



        }else {

            try {
                String sql = "UPDATE Customer SET name=?,address=?,salary=? WHERE id=? ";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, c1.getName());
                statement.setString(2, c1.getAddress());
                statement.setDouble(3, c1.getSalary());
                statement.setString(4, c1.getId());
                if(statement.executeUpdate()>0){
                    searchCustomer(searchTex);
                    clearField();
                    new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();

                }else {

                    new Alert(Alert.AlertType.WARNING,"Try Again").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }


        }



    }

    private void clearField(){
        txtId.clear();
        txtAddress.clear();
        txtName.clear();
        txtSalary.clear();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
                Stage stage =(Stage) customerFormContext.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load((getClass().getResource("../view/DashbordForm.fxml")))));
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setText("Save Customer");
    }
}
