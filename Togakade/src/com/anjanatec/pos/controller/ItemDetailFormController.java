package com.anjanatec.pos.controller;

import com.anjanatec.pos.db.DBConnection;
import com.anjanatec.pos.db.Database;
import com.anjanatec.pos.model.ItemDetails;
import com.anjanatec.pos.model.Order;
import com.anjanatec.pos.view.ItemDetailTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ItemDetailFormController {
    public AnchorPane itemDetailContext;
    public TableView<ItemDetailTm> tblItem;
    public TableColumn colItemCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;

    public void initialize(){

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    public void loadOrderDetails(String id){

        try {
            String sql = "SELECT o.orderid,d.itemCode,d.orderId,d.unitPrice,d.qty" +
                    " FROM `Order` o INNER JOIN `Order Details` d ON o.orderId =d.orderId AND o.orderId=?  ";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,id);
            ResultSet set = statement.executeQuery();
            ObservableList<ItemDetailTm> tmList = FXCollections.observableArrayList();
            while (set.next()){

                double tempUnitPrice =set.getDouble(4);
                int tempQtyOnHand = set.getInt(5);
                double tempTotalCost = tempUnitPrice*tempQtyOnHand;
                tmList.add(new ItemDetailTm(
                        set.getString(2),tempUnitPrice,tempQtyOnHand,tempTotalCost
                ));
            }

            tblItem.setItems(tmList);

        } catch (Exception e){
             e.printStackTrace();
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) itemDetailContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load((getClass().getResource("../view/DashbordForm.fxml")))));
    }
}
