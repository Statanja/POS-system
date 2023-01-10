package com.anjanatec.pos.controller;

import com.anjanatec.pos.db.DBConnection;
import com.anjanatec.pos.db.Database;
import com.anjanatec.pos.model.Customer;
import com.anjanatec.pos.model.Item;
import com.anjanatec.pos.model.ItemDetails;
import com.anjanatec.pos.model.Order;
import com.anjanatec.pos.view.cartTm;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class PlaceOrdeFormcontroller {
    public TextField txtDate;
    public ComboBox<String> cmbCustomerIds;
    public ComboBox<String> cmbItemIds;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public TableView tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQTY;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblTotal;
    public TextField txtOrderId;
    public AnchorPane placeOrderFormContext;

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("qyt"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

         setDateAndOrderId();
         loadAllCustomerId();
         loadAllItemId();
         setOrderId();

         cmbCustomerIds.getSelectionModel()
                 .selectedItemProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     if(newValue!=null){
                         setCustomerDetails();
                     }
                 });

         cmbItemIds.getSelectionModel().selectedIndexProperty()
                 .addListener((observable, oldValue, newValue) ->{
                     if(newValue!=null){
                         setItemDetails();
                     }
                 });
         
    }

    private void setOrderId() {

        try {
            String sql ="SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                String tempOrderId = set.getString(1);
                String[] array = tempOrderId.split("-");
                int tempNumber = Integer.parseInt(array[1]);
                int finalizedOderId = tempNumber+1;
                txtOrderId.setText("D-"+finalizedOderId);
            }else {
                txtOrderId.setText("D-1");
                return;
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    private void setItemDetails() {

        try{
            String sql ="SELECT * FROM  Item WHERE code=?";
            PreparedStatement statement3 = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement3.setString(1,cmbItemIds.getValue());
            ResultSet set = statement3.executeQuery();
            if (set.next()){
                txtDescription.setText(set.getString(2));
                txtUnitPrice.setText(String.valueOf(set.getDouble(3)));
                txtQtyOnHand.setText(String.valueOf(set.getInt(4)));
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private void setCustomerDetails() {

        try{
            String sql = "SELECT * FROM  Customer WHERE id=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,cmbCustomerIds.getValue());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                txtName.setText(set.getString(2));
                txtAddress.setText(set.getString(3));
                txtSalary.setText(String.valueOf(set.getInt(4)));
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    private void loadAllItemId() {

        for(Item i: Database.itemTable){
            cmbItemIds.getItems().add(i.getCode());
        }
        try{
            String sql = "SELECT code FROM Item";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);

            ResultSet set = statement.executeQuery();
            ArrayList<String> idList = new ArrayList<>();
            while (set.next()){
                idList.add(set.getString(1));
            }
            ObservableList<String> obList=FXCollections.observableArrayList(idList);
            cmbItemIds.setItems(obList);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }


    }

    private void loadAllCustomerId() {
        for (Customer c: Database.customersTable){
            cmbCustomerIds.getItems().add(c.getId());
        }
    }

    private void setDateAndOrderId() {
        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String d = df.format(date);
//        txtDate.setText(d);
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private boolean checkQty(String code,int qty) {

        try {
            String sql = "SELECT qtyOnHand FROM Item WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, code);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                int tempQty = set.getInt(1);
                if (tempQty >= qty) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

            return false;
    }
    ObservableList<cartTm> obList = FXCollections.observableArrayList();
    public void addToCartOnAction(ActionEvent actionEvent) {

        if(!checkQty(cmbItemIds.getValue(),Integer.parseInt(txtQty.getText()))){
            new Alert(Alert.AlertType.WARNING,"Invalid Qty ").show();
            return;
        }

        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = unitPrice*qty;
        Button btn = new Button("Delete");

        int row =isAlreadyExists(cmbItemIds.getValue());

        if(row==-1) {
            cartTm tm = new cartTm(cmbItemIds.getValue(), txtDescription.getText(), unitPrice, qty, total, btn);
            obList.add(tm);
            tblCart.setItems(obList);

        } else {

            int tempQty = obList.get(row).getQyt()+ qty;
            double tempTotal =unitPrice*tempQty;

            if(!checkQty(cmbItemIds.getValue(),tempQty)){
                new Alert(Alert.AlertType.WARNING,"Invalid Qty ").show();
                return;
            }

            obList.get(row).setQyt(tempQty);
            obList.get(row).setTotal(tempTotal);
            tblCart.refresh();
        }
        calculateTotal();
        clearFields();
        cmbItemIds.requestFocus();

        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
             for(cartTm tm : obList){

                if(buttonType.get()==ButtonType.YES){
                    obList.remove(tm);
                    calculateTotal();
                    tblCart.refresh();
                    return;
                }
            }
        });

    }

    private void clearFields()
    {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
    }

    private int isAlreadyExists(String code){
        for (int i = 0; i < obList.size(); i++) {
            if(obList.get(i).getCode().equals(code)){
                return i;
            }
        }
                return -1;
    }

    private void calculateTotal(){
        double total =0.0;
        for(cartTm tm : obList){
            total+= tm.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        if(obList.isEmpty()) return;
        ArrayList<ItemDetails> details = new ArrayList<>();
        for (cartTm tm : obList){
            details.add(new ItemDetails(tm.getCode(),tm.getUnitPrice(),tm.getQyt()));
        }
        Order order = new Order(
               txtOrderId.getText(),new Date(),Double.parseDouble(lblTotal.getText()),cmbCustomerIds.getValue(),details
        );

        Connection con = null;


        try {

            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            String sql = " INSERT `Order` VALUES(?,?,?,?) ";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, order.getOrderId());
            statement.setString(2,txtDate.getText());
            statement.setDouble(3,order.getTotalCost());
            statement.setString(4,order.getCustomer());


            boolean isOrderSaved = statement.executeUpdate()>0;
            if(isOrderSaved){
                boolean isAllUpdated = manageQty(details);
                if (isAllUpdated){
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION,"Order Placed").show();
                    clearAll();
                }else {
                    System.out.println("2");
                    con.setAutoCommit(true);
                     con.rollback();
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }

            }else {
                System.out.println("2");
                con.setAutoCommit(true);
                con.rollback();
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    private boolean manageQty(ArrayList<ItemDetails> details) {

        try {
            for (ItemDetails d : details){

                String sql = " INSERT `Order Details` VALUES(?,?,?,?) ";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, d.getCode());
                statement.setString(2,txtOrderId.getText());
                statement.setDouble(3,d.getUnitPrice());
                statement.setInt(4,d.getQty());

                boolean isOderDetailsSaved = statement.executeUpdate()>0;
                if(isOderDetailsSaved){
                    boolean isQtyUpdate = update(d);
                    if(!isQtyUpdate){
                        return false;
                }else {
                        return false;
                    }


                }
            }

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }


                return true;
        }

    private boolean update(ItemDetails d) {
        try {
            String sql = "UPDATE Item SET qtyOnHand=(qtyOnHand-?) WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1,d.getQty());
            statement.setString(2,d.getCode());
            return statement.executeUpdate()>0;
        }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
            return false;
        }

    }


    private void clearAll() {
        obList.clear();
        calculateTotal();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        cmbCustomerIds.setValue(null);
        cmbItemIds.setValue(null);
        clearFields();
        setOrderId();



    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage)placeOrderFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load((getClass().getResource("../view/DashbordForm.fxml")))));
    }
}
