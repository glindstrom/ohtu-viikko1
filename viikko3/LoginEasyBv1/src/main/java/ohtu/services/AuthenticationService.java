package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ohtu.data_access.UserDao;

public class AuthenticationService {

    private UserDao userDao;

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }

        if (invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        if (password.length() < 8) return true;
        if (username.length() < 4) return true;
        if (stringContainsOnlyLetters(password)) return true;
        return !stringContainsNumber(password);
    }

    private boolean matchesRegEx(String password, String patternString)
    {        
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean stringContainsOnlyLetters(String string)
    {
        String patternString = "([a-z]*)|([A-Z]*)";
        return matchesRegEx(string, patternString);
    }

    private boolean stringContainsNumber(String string)
    {
        String patternString = ".*[0-9].*";
        return matchesRegEx(string, patternString);
    }
}
