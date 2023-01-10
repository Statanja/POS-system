package com.anjanatec.pos.view;

import javafx.scene.control.Button;

public class cartTm {
    private String code;
    private String description;
    private double unitPrice;
    private int qyt;
    private double total;
    private Button btn;

    public cartTm(String code, String description, double unitPrice, int qyt, double total, Button btn) {
        this.code = code;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qyt = qyt;
        this.total = total;
        this.btn = btn;
    }

    public cartTm() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQyt() {
        return qyt;
    }

    public void setQyt(int qyt) {
        this.qyt = qyt;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
