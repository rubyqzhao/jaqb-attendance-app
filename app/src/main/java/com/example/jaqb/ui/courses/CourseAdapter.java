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
 * Custom adapter to display the details of courses in a listview that uses more than
 * 1 textview.
 *
 * @author amanjotsingh
 * @version 1.0
 * */

public class CourseAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater inflater;
    private List<Course> courses;
    private List<Course> filteredList;
    private Filter filter;

    /**
     * Constructor for courseAdapter class. Initializes context and courses.
     * @param context   the current app context
     * @param courses   a list of courses to be displayed
     */
    public CourseAdapter(Context context, List<Course> courses) {
        this.inflater = LayoutInflater.from(context);
        this.courses = courses;
        this.filteredList = courses;
    }

    /**
     * Gets the count of the course list after it's been filtered.
     * @return  int size of filteredList
     */
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Gets the current item in the course list at the given position.
     * @param position  the position of the item to get
     * @return  the item in the filtered list
     */
    public Course getItem(int position) {
        return filteredList.get(position);
    }

    /**
     * Gets the item id for the course at the given position in the list.
     * @param position the position of the item id to get
     * @return long id the number of the id
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets the view object for the course at the given position in the list.
     * @param position      the position of the item to get
     * @param convertView   the current view of the course item to be set
     * @param parent        the parent viewgroup object containing the view object
     * @return the modified view object for the course
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseViewHolder holder;
        if(convertView == null) {
            holder = new CourseViewHolder();
            convertView = inflater.inflate(R.layout.course_list_item, null);
            holder.setCourseCode((TextView) convertView.findViewById(R.id.courseCode));
            holder.setCourseName((TextView) convertView.findViewById(R.id.courseName));
            holder.setCourseDays((TextView) convertView.findViewById(R.id.courseDays));
            holder.setCourseInstructor((TextView) convertView.findViewById(R.id.courseInstructor));
            holder.setCourseTime((TextView) convertView.findViewById(R.id.courseTime));
            convertView.setTag(holder);
        } else {
            holder = (CourseViewHolder) convertView.getTag();
        }
        holder.getCourseCode().setText(filteredList.get(position).getCode());
        holder.getCourseName().setText(filteredList.get(position).getCourseName());
        holder.getCourseDays().setText(filteredList.get(position).getDays());
        holder.getCourseInstructor().setText(filteredList.get(position).getInstructorName());
        holder.getCourseTime().setText(filteredList.get(position).getTime());
        return convertView;
    }

    /**
     * Filters the course list passed into the class based on the given constraints
     * to generate the correct course list for the current student.
     * @return  filter object
     */
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