package com.example.merchantapp;

public class ModelClass {
    private String title;
    private String tid;
    private  String rrn;
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public ModelClass(String title, String tid, String rrn, String amount){
    this.title = title;
    this.tid = tid;
    this.rrn = rrn;
    this.amount = amount;

    }

    public ModelClass() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
