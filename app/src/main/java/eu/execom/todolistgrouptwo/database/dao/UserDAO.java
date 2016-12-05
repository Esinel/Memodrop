package eu.execom.todolistgrouptwo.database.dao;


import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;

import eu.execom.todolistgrouptwo.model.User;

public class UserDAO extends RuntimeExceptionDao<User, Long> {

    private static final String TAG = UserDAO.class.getSimpleName();

    public UserDAO(Dao<User, Long> dao) {
        super(dao);
    }

    public User findByUsernameAndPassword(String username, String password) {
        try {
            return queryBuilder().where()
                    .eq("username", username)
                    .and()
                    .eq("password", password)
                    .queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    public User findByUsername(String username) {
        try {
            return queryBuilder().where()
                    .eq("username", username)
                    .queryForFirst();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }
}
