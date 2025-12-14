package com.example.fairshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;

public class ExpensesListActivity extends AppCompatActivity {

    MyDatabase myDatabase;
    int groupId;
    int memberId;
    FloatingActionButton fb;
    ListView lvExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);

        myDatabase = new MyDatabase(this);
        lvExpenses = findViewById(R.id.lvExpenses);
        fb = findViewById(R.id.fb);

        groupId = getIntent().getIntExtra("group_id", -1);
        String name = getIntent().getStringExtra("user_name");
        memberId = myDatabase.getMemberIdByName(name, groupId);
        if (groupId == -1) {
            Toast.makeText(this, "Group missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExpensesListActivity.this, ExpenseSplitActivity.class);
                i.putExtra("groupId", groupId);
                startActivity(i);
            }
        });



    }
    @Override
    protected void onResume()
    {
        super.onResume();
        loadData();
    }

    private void loadData() {


        //  Get expenses
        ArrayList<Expense> expenses = myDatabase.getExpensesByGroup(groupId);

        // Display expenses in listview (simple)
        ArrayList<String> expenseStrings = new ArrayList<>();
        for (Expense e : expenses) {
            expenseStrings.add(e.paidBy + " paid " + String.format("%.2f", e.amount) + " (" + e.description + ")");
        }
        lvExpenses.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseStrings));

        lvExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int expenseId = expenses.get(position).getId();
                double expenseAmount = expenses.get(position).getAmount();
                String description = expenses.get(position).getDescription();
                Intent i = new Intent(ExpensesListActivity.this, ShowExpenseShares.class);
                i.putExtra("expenseId",expenseId);
                i.putExtra("expenseAmount",expenseAmount);
                i.putExtra("description",description);
                startActivity(i);

            }
        });
    }


}
