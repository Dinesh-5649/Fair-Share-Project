package com.example.fairshare;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.InputType;

import android.view.View;

import android.widget.*;

import java.util.ArrayList;

import java.util.HashMap;

public class ExpenseSplitActivity extends AppCompatActivity {

    Spinner spinnerMembers;

    EditText etAmount, etDescription;

    Button btnAddExpense, btnSplitDetails;

    RadioGroup rgSplitType;

    MyDatabase myDatabase;

    int groupId;

    ArrayList<String> members;

    HashMap<Integer, Double> splitMap = new HashMap<>(); // memberId -> value

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_split);

        myDatabase = new MyDatabase(this);

        spinnerMembers = findViewById(R.id.spinnerMembers);

        etAmount = findViewById(R.id.etAmount);

        etDescription = findViewById(R.id.etDescription);

        btnAddExpense = findViewById(R.id.btnAddExpense);

        btnSplitDetails = findViewById(R.id.btnSplitDetails);

        rgSplitType = findViewById(R.id.rgSplitType);

        groupId = getIntent().getIntExtra("groupId", -1);

        if (groupId == -1) {

            Toast.makeText(this, "Group not selected", Toast.LENGTH_SHORT).show();

            finish();

            return;

        }

        members = myDatabase.getMembersByGroup(groupId);

        ArrayAdapter<String> adapter =

                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, members);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMembers.setAdapter(adapter);

        // Open split dialog only for Custom & Percentage

        btnSplitDetails.setOnClickListener(v -> {

            int checkedId = rgSplitType.getCheckedRadioButtonId();

            if (checkedId == R.id.rbEqual) {

                Toast.makeText(this,

                        "Equal split does not need details",

                        Toast.LENGTH_SHORT).show();

                return;

            }

            showSplitDialog();

        });

        btnAddExpense.setOnClickListener(v -> addExpense());

    }

    // =============================

    // ADD EXPENSE + SPLIT LOGIC

    // =============================

    private void addExpense() {

        String paidBy = spinnerMembers.getSelectedItem().toString();

        String amountStr = etAmount.getText().toString().trim();

        String desc = etDescription.getText().toString().trim();

        if (amountStr.isEmpty()) {

            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();

            return;

        }

        double totalAmount;

        try {

            totalAmount = Double.parseDouble(amountStr);

        } catch (Exception e) {

            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();

            return;

        }

        int checkedId = rgSplitType.getCheckedRadioButtonId();

        String splitType;

        if (checkedId == R.id.rbCustom) {

            splitType = "custom";

        } else if (checkedId == R.id.rbPercentage) {

            splitType = "percentage";

        } else {

            splitType = "equal";

        }

        // =============================

        // VALIDATION FIRST (NO DB YET)

        // =============================

        if (splitType.equals("custom")) {

            if (splitMap.isEmpty()) {

                Toast.makeText(this,

                        "Enter custom split details",

                        Toast.LENGTH_SHORT).show();

                return;

            }

            double totalCustom = 0;

            for (double amt : splitMap.values()) {

                totalCustom += amt;

            }

            if (Math.abs(totalCustom - totalAmount) > 0.01) {

                Toast.makeText(this,

                        "Custom split must equal total amount",

                        Toast.LENGTH_SHORT).show();

                return;

            }

        }

        if (splitType.equals("percentage")) {

            if (splitMap.isEmpty()) {

                Toast.makeText(this,

                        "Enter percentage split details",

                        Toast.LENGTH_SHORT).show();

                return;

            }

            double totalPercent = 0;

            for (double p : splitMap.values()) {

                totalPercent += p;

            }

            if (Math.round(totalPercent) != 100) {

                Toast.makeText(this,

                        "Total percentage must be 100%",

                        Toast.LENGTH_SHORT).show();

                return;

            }

        }

        // =============================

        // NOW INSERT EXPENSE (SAFE)

        // =============================

        boolean expenseAdded =

                myDatabase.addExpense(groupId, paidBy, totalAmount, splitType, desc);

        if (!expenseAdded) {

            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();

            return;

        }

        int expenseId = myDatabase.getLastExpenseId();

        // =============================

        // INSERT SHARES

        // =============================

        if (splitType.equals("equal")) {

            double eachShare = totalAmount / members.size();

            for (String member : members) {

                int memberId = getMemberIdByName(member);

                myDatabase.insertExpenseShare(expenseId, memberId, eachShare);

            }

        }

        else if (splitType.equals("custom")) {

            for (int memberId : splitMap.keySet()) {

                myDatabase.insertExpenseShare(

                        expenseId,

                        memberId,

                        splitMap.get(memberId)

                );

            }

        }

        else if (splitType.equals("percentage")) {

            for (int memberId : splitMap.keySet()) {

                double percent = splitMap.get(memberId);

                double share = (percent / 100.0) * totalAmount;

                myDatabase.insertExpenseShare(expenseId, memberId, share);

            }

        }

        Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();

        finish();

    }


    // =============================

    // SPLIT INPUT DIALOG

    // =============================

    private void showSplitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_split_input, null);

        LinearLayout container = view.findViewById(R.id.containerMembers);

        boolean isPercentage =

                rgSplitType.getCheckedRadioButtonId() == R.id.rbPercentage;

        for (String member : members) {

            EditText et = new EditText(this);

            et.setHint(isPercentage

                    ? member + " (%)"

                    : member + " amount");

            et.setInputType(

                    InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL

            );

            et.setTag(member);

            container.addView(et);

        }

        builder.setView(view)

                .setTitle("Enter Split Details")

                .setPositiveButton("Save", (dialog, which) ->

                        collectSplit(container))

                .setNegativeButton("Cancel", null)

                .show();

    }

    // =============================

    // COLLECT SPLIT VALUES

    // =============================

    private void collectSplit(LinearLayout container) {

        splitMap.clear();

        for (int i = 0; i < container.getChildCount(); i++) {

            EditText et = (EditText) container.getChildAt(i);

            String memberName = et.getTag().toString();

            if (et.getText().toString().trim().isEmpty()) {

                Toast.makeText(this,

                        "Fill all values",

                        Toast.LENGTH_SHORT).show();

                return;

            }

            double value = Double.parseDouble(et.getText().toString());

            if (value < 0) {

                Toast.makeText(this,

                        "Value cannot be negative",

                        Toast.LENGTH_SHORT).show();

                return;

            }

            int memberId = getMemberIdByName(memberName);

            splitMap.put(memberId, value);

        }

        Toast.makeText(this, "Split saved", Toast.LENGTH_SHORT).show();

    }

    // =============================

    // HELPER METHOD

    // =============================

    private int getMemberIdByName(String memberName) {

        return myDatabase.getMemberIdByName(memberName, groupId);

    }

}
