package eu.execom.todolistgrouptwo.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.model.Task;

/**
 * Represents a {@link android.view.View view} for one item in a list.
 */
@EViewGroup(R.layout.view_item_task)
public class TaskItemView extends LinearLayout {

    /**
     * Title {@link TextView TextView}.
     */
    @ViewById
    TextView title;

    /**
     * Description {@link TextView TextView}.
     */
    @ViewById
    TextView description;

    public TaskItemView(Context context) {
        super(context);
    }

    /**
     * Binds the task model to its view.
     *
     * @param task The model.
     * @return The view.
     */
    public TaskItemView bind(Task task) {
        title.setText(task.getTitle());
        description.setText(task.getDescription());

        return this;
    }
}
