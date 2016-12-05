package eu.execom.todolistgrouptwo.database.dao;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.model.User;

public class TaskDAO extends RuntimeExceptionDao<Task, Long> {

    private static final String TAG = TaskDAO.class.getSimpleName();

    public TaskDAO(Dao<Task, Long> dao) {
        super(dao);
    }

    public List<Task> findByUser(User user) {
        try {
            return queryBuilder().where()
                    .eq("user", user.getId())
                    .query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return new ArrayList<>();
    }
}
