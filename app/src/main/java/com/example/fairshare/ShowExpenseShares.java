package com.example.fairshare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ShowExpenseShares extends AppCompatActivity {

    TextView tv;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_expense_shares);
        MyDatabase db = new MyDatabase(this);

        tv = findViewById(R.id.tvDis);
        lv = findViewById(R.id.lv);

        String description = getIntent().getStringExtra("description");
        double amount = getIntent().getDoubleExtra("expenseAmount",0);
        int expenseId = getIntent().getIntExtra("expenseId",0);

        tv.setText(""+description);

        //  Get expenseShares
        ArrayList<ExpenseShare> expenseShares = db.getExpenseShareByExpense(expenseId);

        // Display expenseShares in listview (simple)
        ArrayList<String> expenseShareStrings = new ArrayList<>();
        for (ExpenseShare e : expenseShares) {
            String status="Not Paid";
            if(e.paidStatus==1){
                status = "Paid";
            }
            expenseShareStrings.add(e.memberName + " --> " + String.format("%.2f", e.shareAmount) + " (" + status + ")");
        }
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseShareStrings));



    }
}