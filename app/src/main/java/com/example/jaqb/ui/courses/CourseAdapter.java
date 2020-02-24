package com.example.jaqb.ui.courses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;
import java.util.List;

/**
 * @author amanjotsingh
 *
 * Custom adapter to display the details of courses in a listview that uses more than
 * 1 textview.
 * */

public class CourseAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses) {
        //super(context, R.layout.course_list_item, courses);
        this.inflater = LayoutInflater.from(context);
        this.courses = courses;
    }

    public int getCount() {
        return courses.size();
    }

    public Course getItem(int position) {
        return courses.get(position);
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
        holder.courseCode.setText(courses.get(position).getCode());
        holder.courseName.setText(courses.get(position).getCourseName());
        holder.courseDays.setText(courses.get(position).getDays());
        holder.courseInstructor.setText(courses.get(position).getInstructorName());
        return convertView;
    }
}