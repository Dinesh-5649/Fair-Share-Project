package com.example.fairshare;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

public class ExpenseChartActivity extends AppCompatActivity {

    PieChart pieChart;
    RecyclerView recyclerView;
    ExpenseChartAdapter adapter;
    MyDatabase db;
    int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_chart);

        recyclerView = findViewById(R.id.rvExpenseCharts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new MyDatabase(this);
        groupId = getIntent().getIntExtra("groupId", -1);

        ArrayList<Expense> expenses = db.getExpensesByGroup(groupId);

        adapter = new ExpenseChartAdapter(this, expenses);
        recyclerView.setAdapter(adapter);
    }


    // -----------------------------------
    // LOAD PIE CHART FROM DB
    // -----------------------------------
    private void loadExpenseChartFromDB() {

        HashMap<String, Float> expenseMap = db.getExpenseSplitByGroup(groupId);

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : expenseMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        if (entries.isEmpty()) {
            Toast.makeText(this, "No expenses yet", Toast.LENGTH_SHORT).show();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Split");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.setCenterText("Group Expenses");
        pieChart.setCenterTextSize(16f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }


}
