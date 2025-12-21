package com.example.fairshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseChartAdapter extends RecyclerView.Adapter<ExpenseChartAdapter.ViewHolder> {

    Context context;
    ArrayList<Expense> expenseList;
    MyDatabase db;

    public ExpenseChartAdapter(Context context, ArrayList<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        db = new MyDatabase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_expense_chart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.piePaid.setNoDataTextColor(android.R.color.transparent);
        holder.pieRemaining.setNoDataTextColor(android.R.color.transparent);
        holder.piePaid.setExtraOffsets(10f, 10f, 10f, 10f);
        holder.pieRemaining.setExtraOffsets(10f, 10f, 10f, 10f);
        holder.piePaid.setHoleRadius(55f);
        holder.piePaid.setTransparentCircleRadius(60f);

        holder.pieRemaining.setHoleRadius(55f);
        holder.pieRemaining.setTransparentCircleRadius(60f);


        Expense expense = expenseList.get(position);
        holder.tvTitle.setText(expense.getDescription());

        // -------- Pie 1: Paid vs Remaining --------
        HashMap<String, Float> paidMap = db.getPaidVsRemaining(expense.getId());
        ArrayList<PieEntry> paidEntries = new ArrayList<>();

        for (Map.Entry<String, Float> e : paidMap.entrySet()) {
            paidEntries.add(new PieEntry(e.getValue(), e.getKey()));
        }

        PieDataSet paidSet = new PieDataSet(paidEntries, "Status");
        paidSet.setColors(ColorTemplate.MATERIAL_COLORS);
        holder.piePaid.clear();
        holder.piePaid.setData(new PieData(paidSet));
        holder.piePaid.invalidate();


        // -------- Pie 2: Remaining by Member --------
        HashMap<String, Float> remMap = db.getRemainingByMember(expense.getId());
        ArrayList<PieEntry> remEntries = new ArrayList<>();

        for (Map.Entry<String, Float> e : remMap.entrySet()) {
            remEntries.add(new PieEntry(e.getValue(), e.getKey()));
        }

        PieDataSet remSet = new PieDataSet(remEntries, "Pending");
        remSet.setColors(ColorTemplate.COLORFUL_COLORS);
        holder.pieRemaining.setData(new PieData(remSet));
        holder.pieRemaining.getDescription().setEnabled(false);
        holder.pieRemaining.invalidate();
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        PieChart piePaid, pieRemaining;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvExpenseTitle);
            piePaid = itemView.findViewById(R.id.piePaidRemaining);
            pieRemaining = itemView.findViewById(R.id.pieRemainingByMember);
        }

    }

}
