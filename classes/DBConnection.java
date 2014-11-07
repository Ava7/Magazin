package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static Statement stmt;
    private static final String host = "jdbc:derby:Store";
    private static final String userName = "Meniak";
    private static final String password = "darvin";

    public static Statement connect() {
        try {
            Connection con = DriverManager.getConnection(host, userName, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("DBConection error" + e.getMessage());
        }
        return stmt;
    } 
}
