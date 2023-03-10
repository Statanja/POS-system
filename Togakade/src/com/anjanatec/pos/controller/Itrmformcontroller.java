package com.anjanatec.pos.controller;

import com.anjanatec.pos.db.DBConnection;
import com.anjanatec.pos.db.Database;
import com.anjanatec.pos.model.Customer;
import com.anjanatec.pos.model.Item;
import com.anjanatec.pos.view.CustomerTm;
import com.anjanatec.pos.view.ItemTm;
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

public class Itrmformcontroller {
    public AnchorPane itemsFormContext;
    public TextField txtCode;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public Button btnSaveItem;
    public TextField txtSearch;
    public TableView<ItemTm> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOption;

    private String searchTex="";

    public void initialize() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        searchItem(searchTex);

        tblItem.getSelectionModel().selectedItemProperty()
                .addListener((observable,oldValue,newValue) ->{
                    if(null!= newValue){

                        setData(newValue);
                    }


                } );
        txtSearch.textProperty().
                addListener((observable, oldValue, newValue) -> {
                    searchTex=newValue;
                    searchItem(searchTex);
                });

    }

    private void setData(ItemTm tm){
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
        btnSaveItem.setText("Update Item");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) itemsFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load((getClass().getResource("../view/DashbordForm.fxml")))));
    }

    public void newItemOnAction(ActionEvent actionEvent) {
    }

    public void saveItemOnAction(ActionEvent actionEvent) {
            Item i1 = new Item(txtCode.getText(),txtDescription.getText(),Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText()));
            if(btnSaveItem.getText().equalsIgnoreCase("Save Item")){

                try {

                    String sql = "INSERT INTO Item VALUES (?,?,?,?)";
                    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    statement.setString(1,i1.getCode());
                    statement.setString(2,i1.getDescription());
                    statement.setInt(3,i1.getQtyOnHand());
                    statement.setDouble(4,i1.getUnitPrice());

                    if(statement.executeUpdate()>0){
                        searchItem(searchTex);
                        clearField();
                        new Alert(Alert.AlertType.INFORMATION,"Item Saved").show();

                    }else {

                        new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    }
                } catch (ClassNotFoundException | SQLException e){
                    e.printStackTrace();
                }

            }else {

                try {
                    String sql = "UPDATE Customer SET description=?,unitPrice=?,qtyOnHand=? WHERE code=? ";
                    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    statement.setString(1,i1.getDescription());
                    statement.setInt(2,i1.getQtyOnHand());
                    statement.setDouble(3,i1.getUnitPrice());
                    statement.setString(4,i1.getCode());
                    if(statement.executeUpdate()>0){
                        searchItem(searchTex);
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

    private void clearField() {
        txtCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    private void searchItem(String text) {

        try {
            ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Item WHERE description LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchTex);
            ResultSet set = statement.executeQuery();

            while (set.next()) {

                Button btn = new Button("Delete");
                ItemTm tm = new ItemTm(
                        set.getString(1),
                        set.getString(2),
                        set.getDouble(3),
                        set.getInt(4),
                        btn);
                tmList.add(tm);
                btn.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure Delete customer",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {

                        try {
                            String sql1 = "DELETE FROM Item WHERE id=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getCode());

                            if (statement1.executeUpdate() > 0) {
                                searchItem(searchTex);
                                new Alert(Alert.AlertType.INFORMATION, "Item Delete").show();

                            } else {

                                new Alert(Alert.AlertType.WARNING, "Try Again").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }


                    }

                });
            }
            tblItem.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

