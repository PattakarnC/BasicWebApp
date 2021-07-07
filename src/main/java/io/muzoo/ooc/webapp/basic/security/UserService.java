package io.muzoo.ooc.webapp.basic.security;


import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";

    @Setter
    private MySQLDatabase database = new MySQLDatabase();

    public void createUser(String username, String password, String displayName) {
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
            // TODO: check what happen if the user is duplicated
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.createUser("test3", "12345A", "Tom");
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

}
