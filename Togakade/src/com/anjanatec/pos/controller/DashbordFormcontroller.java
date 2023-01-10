package com.anjanatec.pos.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.IsoEra;
import java.time.format.DateTimeFormatter;

public class DashbordFormcontroller {
    public Label lbltime;
    public Label lbldate;
    public AnchorPane dashbordcontext;

    public void initialize(){
        setDateandTime();
    }

    private void setDateandTime(){
        //set times

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            lbltime.setText(LocalDateTime.now().format(formatter));
        }),
               new KeyFrame(javafx.util.Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void oncustermerOnAction(ActionEvent actionEvent) throws IOException {
            setUi("CustomerForm");
    }

    public void onitemOnAction(ActionEvent actionEvent) throws IOException {
        setUi("itrmform");
    }

    public void onordermanagmentOnAction(ActionEvent actionEvent) throws IOException {
              setUi("OrderDetailForm");
    }

    public void onorderdetailOnAction(ActionEvent actionEvent) throws IOException {
                setUi("PlaceOrdeForm");
    }

    private void setUi(String ui) throws IOException {
        System.out.println("UI Name: "+ui);
        Stage stage = (Stage) dashbordcontext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+ui+".fxml"))));
        stage.setTitle("New Customer Registration");
        stage.centerOnScreen();
    }
}
