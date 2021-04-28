/*
 * DataConnection.java
 * 
 * All Rights Reserved.
 * Copyright (c) 2019 FPT University
 */
package context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Data connection<br>
 *
 * <pre>
 * Class thực hiện thao tác kết nối với cơ sở dữ liệu với thông tin lấy trong file config.xml
 * Trong class này sẽ tiến hành các xử lí dưới đây
 *
 * ・initConfig
 * ・getImagefolder
 * ・closeConn
 * ・closeResultSet
 * ・closePreparedStatement
 * ・connToMssql
 * </pre>
 *
 * @author thinh
 */
public class DataConnection {

    private static Connection conn = null;
    private static String connURL = "";
    private static String user = "";
    private static String pass = "";

    private static void initConfig() {
        try {
            InitialContext initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");
            connURL = (String) environmentContext.lookup("myConnection");
            user = (String) environmentContext.lookup("dbUser");
            pass = (String) environmentContext.lookup("dbPass");
            System.out.println("Config read");
        } catch (NamingException e) {
            System.out.println("Cannot read config");
            System.out.println(e.getMessage());
        }
    }

    public String getImageFolder() throws Exception {
        String imageFolder = null;
        try {
            InitialContext initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:comp/env");
            imageFolder = (String) environmentContext.lookup("imagePath");
        } catch (NamingException e) {
            System.out.println("Cannot read config");
            System.out.println(e.getMessage());
            throw e;
        }
        return imageFolder;
    }

    
    public void closeConnection(ResultSet rs, PreparedStatement ps, Connection con) throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
        if (ps != null && !ps.isClosed()) {
            ps.close();
        }
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    
    public static Connection connToMssql() throws SQLException, ClassNotFoundException {
//        String url = "jdbc:sqlserver://"+serverName+":"+portNumber +";databaseName="+dbName;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        initConfig();
        return DriverManager.getConnection(connURL, user, pass);
    }
}
