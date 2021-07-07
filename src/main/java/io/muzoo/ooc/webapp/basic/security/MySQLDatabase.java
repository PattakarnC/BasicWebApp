package io.muzoo.ooc.webapp.basic.security;

import io.muzoo.ooc.webapp.basic.config.ConfigProperties;
import io.muzoo.ooc.webapp.basic.config.ConfigurationLoader;

import java.sql.*;

public class MySQLDatabase {

    public Connection getConnection() {
        try {
            ConfigProperties configProperties = ConfigurationLoader.load();

            if (configProperties == null) {
                throw new RuntimeException("Unable to read the config.properties");
            }

            String jdbcDriver =  configProperties.getDatabaseDriverClassName();
            String jdbcURL =  configProperties.getDatabaseConnectionUrl();
            String username = configProperties.getDatabaseUsername();
            String password = configProperties.getDatabasePassword();
            Class.forName(jdbcDriver);

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            return connection;

        } catch (SQLException | ClassNotFoundException e) {
            return null;
        }
    }


//    public static void main(String[] args) {
//        try {
//            String jdbcDriver = "com.mysql.jdbc.Driver";
//            String jdbcURL = "jdbc:mysql://localhost:3306/login_webapp";
//            String username = "ssc";
//            String password = "hardpass";
//
//            Class.forName(jdbcDriver);
//            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
//
//            String sql = "INSERT INTO tbl_user (username, password, display_name) VALUES (?, ?, ?);";
//            PreparedStatement ps = connection.prepareStatement(sql);
//
//            //setting username column 1
//            ps.setString(1, "my_username");
//            //setting password column 2
//            ps.setString(2, "my_password");
//            //setting display name column 3
//            ps.setString(3, "my_display_name");
//
//            ps.execute();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
