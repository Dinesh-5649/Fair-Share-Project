package com.example.fairshare;

public class Expense {
    public int id;
    public int groupId;
    public String paidBy;
    public double amount;
    public String type;
    public String description;
    public String timestamp;

    public Expense(int id, int groupId, String paidBy, double amount, String type, String description, String timestamp) {
        this.id = id;
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
