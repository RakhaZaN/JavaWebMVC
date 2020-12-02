/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author R. Z. Nurfirmansyah
 */
public class Koneksi {
    
    static Connection conn;
    
    public static Connection getConn() {
        if (conn == null) {
            MysqlDataSource data = new MysqlDataSource();
            data.setDatabaseName("pbo_koperasi");
            data.setUser("root");
            data.setPassword("");
            try {
                conn = data.getConnection();
                System.out.println("Connected");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        
        return conn;
    }
    
    public static void main(String[] args) {
        getConn();
    }
    
}
