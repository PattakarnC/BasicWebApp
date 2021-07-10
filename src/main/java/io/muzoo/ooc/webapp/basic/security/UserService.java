package io.muzoo.ooc.webapp.basic.security;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService is used in too many places and we only need one instance of it so we will make it singleton
 */
public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM tbl_user;";
    private static final String DELETE_USER_SQL = "DELETE FROM tbl_user WHERE username = ?;";

    @Setter
    private static MySQLDatabase database;

    private static UserService service;

    public UserService() {
    }

    public static UserService getInstance() {
        if (service == null) {
            service = new UserService();
            UserService.setDatabase(MySQLDatabase.getInstance());
        }
        return service;
    }

    public void createUser(String username, String password, String displayName) throws UserServiceException {
        // password need to be hashed and salted so we will need bcrypt library
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, displayName);

            ps.executeUpdate();

            connection.setAutoCommit(false);
            connection.commit();
            // check if the user is duplicated
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username %s has already been taken.", username));
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    public User findByUsername(String username) {
        try {
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),  // this is hashed password
                    resultSet.getString("display_name")
            );
        } catch (SQLException throwables) {
            return null;
        }
    }

    /**
     * list all users in the database
     * @return list of users, never return null
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_USERS_SQL);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                users.add(
                        new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("display_name")
                ));
            }
            return users;

        } catch (SQLException throwables) {
            return null;
        }
    }

    /**
     * Delete user by user id.
     * @param username
     * @return true if successful
     */
    public boolean deleteUserByUsername(String username) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL);
        ) {
            ps.setString(1, username);
            ps.executeUpdate();
            return true;
        }
         catch (SQLException throwables) {
            return false;
        }
    }

    /**
     * User can only change their display name when updating profile
     * @param id
     * @param displayName
     */
    public void updateUserById(long id, String displayName) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Change password method is seperated from update user method because user normally
     * never change password and update profile at the same time
     * @param newPassword
     */
    public void changePassword(String newPassword) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static void main(String[] args) throws UserServiceException {
        UserService userService = UserService.getInstance();
//        userService.createUser("admin", "123456", "Admin");
        userService.createUser("test3", "12345", "Guy");
//        User user = userService.findByUsername("test2");
//        for(User user : userService.findAll()) {
//            System.out.println(user.getUsername());
//        }
    }
}

//    private Map<String, User> users = new HashMap<>();
//
//    {
//        users.put("gigadot", new User("gigadot", "12345"));
//        users.put("admin", new User("admin", "12345"));
//    }
//
//    public User findByUsername(String username) {
//        return users.get(username);
//    }
//
//    public boolean checkIfUserExists(String username) {
//        return users.containsKey(username);
//    }
