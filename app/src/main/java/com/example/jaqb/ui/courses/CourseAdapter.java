package com.example.jaqb.ui.courses;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amanjotsingh
 *
 * Custom adapter to display the details of courses in a listview that uses more than
 * 1 textview.
 * */

public class CourseAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater inflater;
    private List<Course> courses;
    private List<Course> filteredList;
    private Filter filter;

    public CourseAdapter(Context context, List<Course> courses) {
        this.inflater = LayoutInflater.from(context);
        this.courses = courses;
        this.filteredList = courses;
    }

    public int getCount() {
        return filteredList.size();
    }

    public Course getItem(int position) {
        return filteredList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.course_list_item, null);
            holder.courseCode = (TextView) convertView.findViewById(R.id.courseCode);
            holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
            holder.courseDays = (TextView) convertView.findViewById(R.id.courseDays);
            holder.courseInstructor = (TextView) convertView.findViewById(R.id.courseInstructor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.courseCode.setText(filteredList.get(position).getCode());
        holder.courseName.setText(filteredList.get(position).getCourseName());
        holder.courseDays.setText(filteredList.get(position).getDays());
        holder.courseInstructor.setText(filteredList.get(position).getInstructorName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    constraint = constraint.toString().toLowerCase();
                    FilterResults results = new FilterResults();
                    if(TextUtils.isEmpty(constraint)){
                        results.values = courses;
                        results.count = courses.size();
                    }
                    if (constraint != null && constraint.toString().length() > 0) {
                        List<Course> found = new ArrayList<>();
                        for (Course item : courses) {
                            if (item.getCode().toString().toLowerCase().contains(constraint)) {
                                found.add(item);
                            }
                        }
                        results.values = found;
                        results.count = found.size();
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList = (ArrayList<Course>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
        return filter;
    }
}