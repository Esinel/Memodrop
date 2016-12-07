package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.model.Task;

@EActivity(R.layout.activity_edit_task)
public class EditTaskActivity extends AppCompatActivity {

    @ViewById
    TextView taskId;

    @ViewById
    TextInputEditText title;

    @ViewById
    TextInputEditText description;

    @ViewById
    AppCompatCheckBox finished;

    @AfterViews
    void initData() {
        final Gson gson = new Gson();
        String serializedTask = getIntent().getStringExtra("task");
        final Task task = gson.fromJson(serializedTask, Task.class);

        this.taskId.setText(String.valueOf(task.getId()));
        this.title.setText(task.getTitle());
        this.description.setText(task.getDescription());
        this.finished.setChecked(task.isFinished());
    }

    @Click
    void saveTask() {
        String taskPosition = getIntent().getStringExtra("taskOrderNumber");
        final Task task = new Task(title.getText().toString(),
                description.getText().toString(), finished.isChecked());
        task.setId(Long.valueOf(taskId.getText().toString()));
        final Intent intent = new Intent();
        final Gson gson = new Gson();
        intent.putExtra("task", gson.toJson(task));
        intent.putExtra("taskOrderNumber", taskPosition);
        setResult(RESULT_OK, intent);
        finish();
    }
}
