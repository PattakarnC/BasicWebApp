package io.muzoo.ooc.webapp.basic.security;

import java.sql.*;

public class MySQLDatabase {

    private Statement statement;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        init();
    }

    public static void init() {
        try {
            String jdbcDriver = "com.mysql.jdbc.Driver";
            String jdbcURL = "jdbc:mysql://localhost:3306/login_webapp";
            String username = "ssc";
            String password = "hardpass";
            Class.forName(jdbcDriver);

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            String sql = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            //setting username column 1
            ps.setString(1, "my_username");
            //setting password column 2
            ps.setString(2, "my_password");
            //setting display name column 3
            ps.setString(3, "my_display_name");

            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
