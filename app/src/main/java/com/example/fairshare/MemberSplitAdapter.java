package com.example.fairshare;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberSplitAdapter extends RecyclerView.Adapter<MemberSplitAdapter.ViewHolder> {

    private List<MemberModel> memberList;
    private boolean isCustomSplit = false; // controlled by radio buttons in activity

    public MemberSplitAdapter(List<MemberModel> memberList) {
        this.memberList = memberList;
    }

    // Call this from Activity when user selects Equal or Custom
    public void setCustomSplit(boolean custom) {
        this.isCustomSplit = custom;
        notifyDataSetChanged();
    }

    public List<MemberModel> getUpdatedList() {
        return memberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_split, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberModel member = memberList.get(position);

        holder.tvName.setText(member.getName());
        holder.rbSelect.setChecked(member.isSelected());

        // enable EditText only if custom split and selected
        boolean enableInput = isCustomSplit && member.isSelected();
        holder.etShare.setEnabled(enableInput);

        // Show existing entered value
        if (member.getAmount() > 0)
            holder.etShare.setText(String.valueOf(member.getAmount()));
        else
            holder.etShare.setText("");

        // Handle radio button clicks
        holder.rbSelect.setOnClickListener(v -> {

            // Toggle selection
            member.setSelected(!member.isSelected());

            // Enable or disable EditText accordingly
            holder.etShare.setEnabled(isCustomSplit && member.isSelected());

            // If unselected – clear amount
            if (!member.isSelected()) {
                member.setAmount(0);
                holder.etShare.setText("");
            }
        });

        // Handle text input
        holder.etShare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.etShare.isEnabled()) {
                    try {
                        double entered = Double.parseDouble(s.toString());
                        member.setAmount(entered);
                    } catch (Exception e) {
                        member.setAmount(0);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton rbSelect;
        TextView tvName;
        EditText etShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rbSelect = itemView.findViewById(R.id.rbSelect);
            tvName = itemView.findViewById(R.id.tvMemberName);
            etShare = itemView.findViewById(R.id.etShare);
        }
    }
}

