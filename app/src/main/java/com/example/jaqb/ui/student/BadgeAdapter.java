package com.example.jaqb.ui.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Badge;

import java.util.List;

public class BadgeAdapter extends BaseAdapter {
    private Context context;
    private List<Badge> badges;

    public BadgeAdapter(Context context, List<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    // number of cells to be rendered
    @Override
    public int getCount() {
        return badges.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Badge badge = badges.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.badge_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.badge_art);

        imageView.setImageResource(badge.getImage());

        return convertView;
    }
}
