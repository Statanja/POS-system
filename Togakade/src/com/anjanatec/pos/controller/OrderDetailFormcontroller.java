package com.anjanatec.pos.controller;

import com.anjanatec.pos.db.DBConnection;
import com.anjanatec.pos.db.Database;
import com.anjanatec.pos.model.Order;
import com.anjanatec.pos.view.CustomerTm;
import com.anjanatec.pos.view.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OrderDetailFormcontroller {
    public AnchorPane orderDetailContext;
    public TableView tblOrders;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colOption;

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        LoadOrders();
    }

    private void LoadOrders() {

        try {

            String sql = "SELECT * FROM `Order`";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();

            ObservableList<OrderTm> tmList = FXCollections.observableArrayList();
            while (set.next()){
                Button btn = new Button("View");
                OrderTm tm = new OrderTm(set.getString(1),
                        set.getString(4),
                        new Date(),
                        set.getDouble(3),btn);
                tmList.add(tm);

                btn.setOnAction(e->{
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ItemDetailForm.fxml"));
                        Parent parent=loader.load();
                        ItemDetailFormController controller = loader.getController();
                        controller.loadOrderDetails(tm.getOrderId());
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.show();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            tblOrders.setItems(tmList);
        } catch (SQLException| ClassNotFoundException e){
            e.printStackTrace();
        }


    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) orderDetailContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load((getClass().getResource("../view/DashbordForm.fxml")))));
    }
}
