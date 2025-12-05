package com.example.fairshare;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import java.util.ArrayList;

public class ExpenseSplitActivity extends AppCompatActivity {

    Spinner spinnerMembers;
    EditText etAmount, etDescription;
    Button btnAddExpense;
    RadioGroup radioSplitType;
    MyDatabase myDatabase;
    int groupId;
    ArrayList<String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_split);

        myDatabase = new MyDatabase(this);

        spinnerMembers = findViewById(R.id.spinnerMembers);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        radioSplitType = findViewById(R.id.radioSplitType);

        groupId = getIntent().getIntExtra("group_id", -1);
        if (groupId == -1) {
            Toast.makeText(this, "Group not selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        members = myDatabase.getMembersByGroup(groupId); // returns list of member names
        if (members.isEmpty()) {
            Toast.makeText(this, "No members in the group", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, members);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembers.setAdapter(adapter);

        btnAddExpense.setOnClickListener(v -> addExpense());
    }

    private void addExpense() {
        String paidBy = spinnerMembers.getSelectedItem() != null ? spinnerMembers.getSelectedItem().toString() : "";
        String amountStr = etAmount.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioSplitType.getCheckedRadioButtonId();
        String type = (selectedId == R.id.radioCustom) ? "custom" : "equal";

        boolean ok = myDatabase.addExpense(groupId, paidBy, amount, type, desc);
        if (ok) {
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        }
    }
}
