package com.anjanatec.pos.view;

import javafx.scene.control.Button;

import javax.xml.crypto.Data;
import java.util.Date;

public class OrderTm {
    private String orderId;
    private String name;
    private Date date;
    private double total;
    private Button btn;

    public OrderTm(String orderId, String name, Date date, double total, Button btn) {
        this.orderId = orderId;
        this.name = name;
        this.date = date;
        this.total = total;
        this.btn = btn;
    }

    public OrderTm() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
