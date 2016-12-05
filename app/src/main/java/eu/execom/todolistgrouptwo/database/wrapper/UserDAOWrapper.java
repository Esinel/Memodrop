package eu.execom.todolistgrouptwo.database.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import eu.execom.todolistgrouptwo.database.DatabaseHelper;
import eu.execom.todolistgrouptwo.database.dao.UserDAO;
import eu.execom.todolistgrouptwo.model.User;

@EBean
public class UserDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserDAO userDAO;

    public User findByUsernameAndPassword(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public boolean create(User user) {
        if (userDAO.findByUsername(user.getEmail()) != null) {
            return false;
        }
        userDAO.create(user);
        return true;
    }

    public User findById(long userId) {
        return userDAO.queryForId(userId);
    }
}
