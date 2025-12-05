package com.example.fairshare;

import android.view.*;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.VH> {
    private List<Map.Entry<String, Double>> data;

    public BalanceAdapter(Map<String, Double> map) {
        this.data = new ArrayList<>(map.entrySet());
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Map.Entry<String, Double> e = data.get(position);
        holder.title.setText(e.getKey());
        holder.subtitle.setText(String.format("%.2f", e.getValue()));
        // positive => they are owed money; negative => they owe
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        VH(View v) {
            super(v);
            title = v.findViewById(android.R.id.text1);
            subtitle = v.findViewById(android.R.id.text2);
        }
    }
}
