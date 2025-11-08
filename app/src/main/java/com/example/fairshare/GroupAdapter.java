package com.example.fairshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<String> {

    final private Context context;
    final private ArrayList<String> groups;

    public GroupAdapter(Context context, ArrayList<String> groups) {
        super(context, 0, groups);
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.show_group_adapter, parent, false);
        }

        TextView groupName = convertView.findViewById(R.id.textView);
        String currentGroupName = groups.get(position);
        groupName.setText(currentGroupName);
        return convertView;
    }
}
