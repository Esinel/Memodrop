package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.Iterator;
import java.util.List;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.adapter.TaskAdapter;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.wrapper.TaskDAOWrapper;
import eu.execom.todolistgrouptwo.database.wrapper.UserDAOWrapper;
import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.model.User;
import eu.execom.todolistgrouptwo.preference.UserPreferences_;

/**
 * Home {@link AppCompatActivity Activity} for navigation and listing all tasks.
 */
@EActivity(R.layout.activity_home)
@OptionsMenu(R.menu.menu_main)
public class HomeActivity extends AppCompatActivity {


    /**
     * Used for logging purposes.
     */
    private static final String TAG = HomeActivity.class.getSimpleName();

    /**
     * Used for identifying results from different activities.
     */
    protected static final int ADD_TASK_REQUEST_CODE = 42;
    protected static final int EDIT_TASK_REQUEST_CODE = 43;
    protected static final int LOGIN_REQUEST_CODE = 420; // BLAZE IT

    /**
     * Tasks are kept in this list during a user session.
     */
    private List<Task> tasks;

    /**
     * {@link FloatingActionButton FloatingActionButton} for starting the
     * {@link AddTaskActivity AddTaskActivity}.
     */
    @ViewById
    FloatingActionButton addTask;

    @ViewById
    Toolbar toolbar;
    /**
     * {@link ListView ListView} for displaying the tasks.
     */
    @ViewById
    ListView listView;

    /**
     * {@link TaskAdapter Adapter} for providing data to the {@link ListView listView}.
     */
    @Bean
    TaskAdapter adapter;

    @Bean
    UserDAOWrapper userDAOWrapper;

    @Bean
    TaskDAOWrapper taskDAOWrapper;

    @Pref
    UserPreferences_ userPreferences;

    @RestService
    RestApi restApi;



    @OptionsItem
    void logoutMe() {
        logout();
    }


    @Background
    void logout(){
        //dummy call
        restApi.logout();
        userPreferences.accessToken().remove();
        checkUser();
    }




    @AfterViews
    void setupToolbar(){
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
    }

    @AfterViews
    @Background
    void checkUser() {
        if (!userPreferences.accessToken().exists()) {
            LoginActivity_.intent(this).startForResult(LOGIN_REQUEST_CODE);
            return;
        }

        try {
            tasks = restApi.getAllTasks();
        } catch (RestClientException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        initData();
    }

    /**
     * Loads tasks from the {@link android.content.SharedPreferences SharedPreferences}
     * and sets the adapter.
     */
    @UiThread
    void initData() {
        listView.setAdapter(adapter);
        adapter.setTasks(tasks);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    // button show
                    addTask.show();
                } else {
                    // button hide
                    addTask.hide();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @ItemClick(R.id.listView)
    void taskClicked(int position){
        Task task = adapter.getItem(position);

        final Gson gson = new Gson();
        String serializedTask = gson.toJson(task);
        Intent editTaskIntent = new Intent(HomeActivity.this, EditTaskActivity_.class);
        editTaskIntent.putExtra("task", serializedTask);
        editTaskIntent.putExtra("taskOrderNumber", String.valueOf(position));
        startActivityForResult(editTaskIntent, EDIT_TASK_REQUEST_CODE);
    };

    /**
     * Called when the {@link FloatingActionButton FloatingActionButton} is clicked.
     */
    @Click
    void addTask() {
        AddTaskActivity_.intent(this).startForResult(ADD_TASK_REQUEST_CODE);
    }

    /**
     * Called when the {@link AddTaskActivity AddTaskActivity} finishes.
     *
     * @param resultCode Indicates whether the activity was successful.
     * @param task         The new task.
     */
    @OnActivityResult(ADD_TASK_REQUEST_CODE)
    @Background
    void onAddTaskResult(int resultCode, @OnActivityResult.Extra String task) {
        if (resultCode == RESULT_OK) {
//            Toast.makeText(this, task, Toast.LENGTH_SHORT).show();
            final Gson gson = new Gson();
            final Task newTask = gson.fromJson(task, Task.class);

            try {
                final Task newNewTask = taskDAOWrapper.create(newTask);
                onTaskCreated(newNewTask);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @OnActivityResult(EDIT_TASK_REQUEST_CODE)
    @Background
    void onEditTaskResult(int resultCode, @OnActivityResult.Extra String task, @OnActivityResult.Extra String taskOrderNumber){
        int taskPos = 0;
        try {
            taskPos = Integer.parseInt(taskOrderNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (resultCode == RESULT_OK) {
            final Gson gson = new Gson();
            final Task editedTask = gson.fromJson(task, Task.class);
            try {
                final String taskId = String.valueOf(editedTask.getId());
                final Task newNewTask = restApi.editTask(taskId, editedTask);
                onTaskEdited(newNewTask, taskPos);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @UiThread
    void onTaskCreated(Task task) {
        tasks.add(task);
        adapter.addTask(task);
    }

    @UiThread
    void onTaskEdited(Task editedTask, int taskPosition){

        int index = 0;


        for (Task taskListItem : tasks){
            if (taskListItem.getId() == editedTask.getId()){
                index = tasks.indexOf(taskListItem);
            }
        }

        tasks.remove(index);
        tasks.add(editedTask);

        updateView(taskPosition, editedTask);
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();

    }

    @UiThread
    public void updateView(int index, Task task){
        View v = listView.getChildAt(index -
                listView.getFirstVisiblePosition());

        if(v == null)
            return;

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView desription = (TextView) v.findViewById(R.id.description);
        LinearLayout background = (LinearLayout) v.findViewById(R.id.taskBackground);

        title.setText(task.getTitle());
        desription.setText(task.getDescription());
        if (task.isFinished()){
            background.setAlpha((float) 0.4666666);
        }else{
            background.setAlpha((float) 1);
        }

    }

    @OnActivityResult(LOGIN_REQUEST_CODE)
    void onLogin(int resultCode, @OnActivityResult.Extra("token") String token) {
        if (resultCode == RESULT_OK) {
            userPreferences.accessToken().put(token);
            checkUser();
        }
    }

}
