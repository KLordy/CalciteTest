package org.klordy.calcite;

import java.sql.*;
import java.util.Properties;

/**
 * @Description TODO
 * @Author klordy
 * @Date 2019-09-09 00:40
 */
public class MemorySqlTest {
    public static void main(String[] args) throws SQLException {
        Properties info = new Properties();
        Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + args[0], info);
        ResultSet result;
        Statement st = connection.createStatement();
        result = st.executeQuery("select * from \"Student\"");
        while(result.next()) {
            System.out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3));
        }
        result.close();
        connection.close();
    }
}
