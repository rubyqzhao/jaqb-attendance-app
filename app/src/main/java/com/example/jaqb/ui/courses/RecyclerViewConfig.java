package com.example.jaqb.ui.courses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jaqb.R;
import com.example.jaqb.data.model.Course;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewConfig {

    private Context context;
    private CourseAdapter courseAdapter;

    public void setConfig(RecyclerView recyclerView, Context context,
                          List<Course> courses, List<String> keys){
        this.context = context;
        this.courseAdapter = new CourseAdapter(courses, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(courseAdapter);
    }

    class CourseItemView extends RecyclerView.ViewHolder{
        private TextView courseCode;
        private TextView courseName;
        private TextView courseDays;
        private TextView courseInstructor;

        private String key;

        public CourseItemView(ViewGroup parent){
            super(LayoutInflater.from(context).
                    inflate(R.layout.course_list_item, parent, false));

            courseCode = (TextView) itemView.findViewById(R.id.courseCode);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            courseDays = (TextView) itemView.findViewById(R.id.courseDays);
            courseInstructor = (TextView) itemView.findViewById(R.id.courseInstructor);
        }

        public void bind(Course course, String key){
            courseCode.setText(course.getCode());
            courseName.setText(course.getCourseName());
            courseDays.setText(course.getDays());
            courseInstructor.setText(course.getInstructorName());
            this.key = key;
        }
    }

    public class CourseAdapter extends RecyclerView.Adapter<CourseItemView> implements Filterable {
        private List<Course> courseList;
        private List<String> keys;

        public CourseAdapter(List<Course> courseList, List<String> keys) {
            this.courseList = courseList;
            this.keys = keys;
        }

        @Override
        public CourseItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CourseItemView(parent);
        }

        @Override
        public void onBindViewHolder(CourseItemView holder, int position) {
            holder.bind(courseList.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Course> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(courseList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Course item : courseList) {
                        if (item.getCode().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courseList.clear();
                courseList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
