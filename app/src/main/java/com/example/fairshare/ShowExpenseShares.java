package com.example.fairshare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowExpenseShares extends AppCompatActivity {

    TextView tv;
    ListView lv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_expense_shares);

        MyDatabase db = new MyDatabase(this);

        tv = findViewById(R.id.tvDis);
        lv = findViewById(R.id.lv);
        btn = findViewById(R.id.btn);

        String description = getIntent().getStringExtra("description");
        double amount = getIntent().getDoubleExtra("expenseAmount", 0);
        int expenseId = getIntent().getIntExtra("expenseId", 0);
        int memberId = getIntent().getIntExtra("memberId", 0);

        tv.setText(description);

        // Get expense shares
        ArrayList<ExpenseShare> expenseShares =
                db.getExpenseShareByExpense(expenseId);

        // Convert to displayable strings
        ArrayList<String> expenseShareStrings = new ArrayList<>();
        for (ExpenseShare e : expenseShares) {
            String status = (e.paidStatus == 1) ? "Paid" : "Not Paid";
            expenseShareStrings.add(
                    e.memberName + "  →  ₹" +
                            String.format("%.2f", e.shareAmount) +
                            " (" + status + ")"
            );
        }

        // ✅ CUSTOM ADAPTER WITH WHITE TEXT
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_expense_share,
                R.id.tvItem,
                expenseShareStrings
        );

        lv.setAdapter(adapter);

        btn.setOnClickListener(v -> {
            if (db.updatePaidStatus(memberId, expenseId)) {
                Toast.makeText(this,
                        "Paid Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Already Paid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
