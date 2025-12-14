package com.example.fairshare;

public class MemberModel {
    private int id;
    private String name;
    private boolean isSelected;
    private double amount;

    public MemberModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.isSelected = false;
        this.amount = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}

