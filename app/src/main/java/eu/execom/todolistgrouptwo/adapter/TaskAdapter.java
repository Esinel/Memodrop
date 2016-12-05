package eu.execom.todolistgrouptwo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.view.TaskItemView;
import eu.execom.todolistgrouptwo.view.TaskItemView_;

/**
 * {@link BaseAdapter Adapter} that provides a data source to a
 * {@link android.widget.ListView ListView}.
 */
@EBean
public class TaskAdapter extends BaseAdapter {

    @RootContext
    Context context;

    /**
     * {@link List List} of {@link Task tasks}.
     */
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Task task = getItem(position);
        if (convertView == null) {
            // If convertView hasn't been created yet, create a new view.
            return TaskItemView_.build(context).bind(task);
        } else {
            // If convertView exists just bind the task to it.
            return ((TaskItemView) convertView).bind(task);
        }
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyDataSetChanged();
    }
}
