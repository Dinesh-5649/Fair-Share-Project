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

        btnSplitDetails.setOnClickListener(v -> showSplitDialog());
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


        // Insert expense
        boolean expenseAdded =
                myDatabase.addExpense(groupId, paidBy, totalAmount, splitType, desc);

        if (!expenseAdded) {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get last inserted expense id
        int expenseId = myDatabase.getLastExpenseId();

        // =============================
        // SAVE SHARES
        // =============================
        if (splitType.equals("equal")) {

            double eachShare = totalAmount / members.size();

            for (String member : members) {
                int memberId = getMemberIdByName(member);
                myDatabase.insertExpenseShare(expenseId, memberId, eachShare);
            }

        } else if (splitType.equals("custom")) {

            for (int memberId : splitMap.keySet()) {
                myDatabase.insertExpenseShare(
                        expenseId,
                        memberId,
                        splitMap.get(memberId)
                );
            }

        } else if (splitType.equals("percentage")) {

            for (int memberId : splitMap.keySet()) {
                double percent = splitMap.get(memberId);
                double share = (percent / 100.0) * totalAmount;
                myDatabase.insertExpenseShare(expenseId, memberId, share);
            }
        }

        Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    // retrying
    // =============================
    // SPLIT INPUT DIALOG
    // =============================
    private void showSplitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_split_input, null);
        LinearLayout container = view.findViewById(R.id.containerMembers);

        for (String member : members) {
            EditText et = new EditText(this);
            et.setHint(member + " amount / %");
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et.setTag(member);
            container.addView(et);
        }

        builder.setView(view)
                .setTitle("Enter Split Details")
                .setPositiveButton("Save", (d, w) -> collectSplit(container))
                .setNegativeButton("Cancel", null)
                .show();
    }

    // =============================
    // COLLECT SPLIT VALUES
    // =============================
    private void collectSplit(LinearLayout container) {

        splitMap.clear();

        int emptyCount = 0;
        int emptyMemberId = -1;
        double enteredTotal = 0;

        for (int i = 0; i < container.getChildCount(); i++) {
            EditText et = (EditText) container.getChildAt(i);
            String memberName = et.getTag().toString();
            int memberId = getMemberIdByName(memberName);

            String text = et.getText().toString().trim();

            if (text.isEmpty()) {
                emptyCount++;
                emptyMemberId = memberId;
            } else {
                double value = Double.parseDouble(text);
                if (value < 0) {
                    Toast.makeText(this, "Invalid percentage", Toast.LENGTH_SHORT).show();
                    return;
                }
                splitMap.put(memberId, value);
                enteredTotal += value;
            }
        }

        // Allow only ONE empty field
        if (emptyCount > 1) {
            Toast.makeText(this,
                    "Leave only one member empty for auto split",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Auto-calculate remaining percentage
        if (emptyCount == 1) {
            double remaining = 100 - enteredTotal;

            if (remaining < 0) {
                Toast.makeText(this,
                        "Total percentage exceeds 100",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            splitMap.put(emptyMemberId, remaining);
        }

        Toast.makeText(this, "Split saved", Toast.LENGTH_SHORT).show();
    }


    // =============================
    // HELPER METHODS
    // =============================
    private int getMemberIdByName(String memberName) {
        return myDatabase.getMemberIdByName(memberName, groupId);
    }
}
