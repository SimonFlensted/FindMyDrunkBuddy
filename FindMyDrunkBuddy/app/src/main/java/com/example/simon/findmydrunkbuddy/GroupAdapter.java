package com.example.simon.findmydrunkbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Simon-PC on 29-05-2017.
 */

public class GroupAdapter extends BaseAdapter {

    private List<ListItem> groups = new LinkedList<>();
    @Override
    public int getCount() {
        return groups.size();
    }

    public GroupAdapter(List<ListItem> groups) {
        this.groups = groups;
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.group_list_item, parent, false);
        }

        ListItem group = groups.get(position);

        TextView groupName = (TextView) convertView.findViewById(R.id.groupname_view);
        groupName.setText(group.getName());

        TextView duration = (TextView) convertView.findViewById(R.id.duration_view);
        duration.setText("Duration: " + group.getDuration());

        return convertView;
    }
}
