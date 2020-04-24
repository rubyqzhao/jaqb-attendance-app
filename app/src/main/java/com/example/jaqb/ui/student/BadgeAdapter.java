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

/**
 * Adapter class for the badges
 *
 * @author Ruby Zhao
 * @version 1.0
 */
public class BadgeAdapter extends BaseAdapter {
    private Context context;
    private List<Badge> badges;

    /**
     * Constructor to initialize the badge adapter
     * @param context the context
     * @param badges the list of badges
     */
    public BadgeAdapter(Context context, List<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    /**
     * Constructor to initialize the badge adapter
     * @param badges the list of badges
     */
    public BadgeAdapter(List<Badge> badges) {
        this.badges = badges;
    }

    /**
     * returns number of cells to be rendered for badge
     * @return number of badges
     */
    @Override
    public int getCount() {
        return badges.size();
    }

    /**
     * Get ID of each badge
     * @param position the position of current item
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the view of badges
     * @param position the position to place badge
     * @param convertView the converted view
     * @param parent the parent of the view group
     * @return
     */
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
