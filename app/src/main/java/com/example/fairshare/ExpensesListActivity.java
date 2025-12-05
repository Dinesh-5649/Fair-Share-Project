package com.example.fairshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.*;

import java.util.*;

public class ExpensesListActivity extends AppCompatActivity {

    MyDatabase myDatabase;
    int groupId;
    RecyclerView rvBalances;
    ListView lvExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);

        myDatabase = new MyDatabase(this);
        rvBalances = findViewById(R.id.rvBalances);
        lvExpenses = findViewById(R.id.lvExpenses);

        groupId = getIntent().getIntExtra("group_id", -1);
        if (groupId == -1) {
            Toast.makeText(this, "Group missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadData();
    }

    private void loadData() {
        // 1. Get members
        ArrayList<String> members = myDatabase.getMembersByGroup(groupId);

        // initialize balance map
        Map<String, Double> balanceMap = new HashMap<>();
        for (String m : members) balanceMap.put(m, 0.0);

        // 2. Get expenses
        ArrayList<Expense> expenses = myDatabase.getExpensesByGroup(groupId);

        // Display expenses in listview (simple)
        ArrayList<String> expenseStrings = new ArrayList<>();
        for (Expense e : expenses) {
            expenseStrings.add(e.paidBy + " paid " + String.format("%.2f", e.amount) + " (" + e.description + ")");
        }
        lvExpenses.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseStrings));

        // 3. For each expense: split equally among members in group (simple equal-split logic)
        for (Expense e : expenses) {
            // Equal split among all members in that group (change if needed)
            int n = members.size();
            if (n == 0) continue;
            double share = e.amount / n;

            for (String m : members) {
                double prev = balanceMap.getOrDefault(m, 0.0);
                if (m.equals(e.paidBy)) {
                    // payer paid full amount; net change = + (amount - share)
                    balanceMap.put(m, prev + (e.amount - share));
                } else {
                    // others owe share
                    balanceMap.put(m, prev - share);
                }
            }
        }

        // 4. Show balanceMap in RecyclerView
        rvBalances.setLayoutManager(new LinearLayoutManager(this));
        BalanceAdapter adapter = new BalanceAdapter(balanceMap);
        rvBalances.setAdapter(adapter);
    }
}
