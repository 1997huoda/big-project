package com.example.huoda.left_ship;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
class DatabaseConnection {
//    private static final String CREATE_TABLE_TWITTER = "CREATE TABLE IF NOT EXISTS twitter (id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, msg varchar(300) not null) DEFAULT CHARSET=utf8";

    final static String HOST = "pcohd.uicp.cn";
    final static String PORT = "24967";

//    final static String HOST = "10.63.34.170";
//    final static String PORT = "3306";

    final static String DB_NAME = "librarydb";
    final static String USERNAME = "test";
    final static String PASSWORD = "";
    final static String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useUnicode=true&characterEncoding=utf-8";

    private static final DatabaseConnection instance = new DatabaseConnection();
    private final int INIT_COUNT = 5;
    private final int MAX_COUNT = 30;
    private int count = 0;

    private final Object wait = new Object();

    private LinkedList<Connection> CONN_POOL;


    public DatabaseConnection() {
        CONN_POOL = new LinkedList<Connection>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for (int i = 0; i < INIT_COUNT; i++) {
                Connection connection = createConnection();
                if(connection != null) {
                    CONN_POOL.add(createConnection());
                    count++;
                }
            }
            //       Connection connection = getConnection();
            //       Statement stmt = connection.createStatement();
            //       stmt.execute(CREATE_TABLE_TWITTER);
            //       stmt.execute("set names 'utf-8'");
            //       stmt.close();
            //       releaseConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    private static Connection createConnection() {
        try {
            return (Connection) DriverManager.getConnection(url, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        synchronized (CONN_POOL) {
            while(CONN_POOL.size() > 0) {
                Connection conn = CONN_POOL.removeLast();
                try {
                    if(conn.isValid(1000)) {
                        return conn;
                    } else {
                        count--;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
            if(count < MAX_COUNT) {
                count++;
                return createConnection();
            }
            synchronized (wait) {
                try {
                    wait.wait(100);
                    if(CONN_POOL.size() > 0) {
                        return CONN_POOL.removeLast();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }
        return null;
    }

    public void releaseConnection(Connection connection) {
        CONN_POOL.add(connection);
        synchronized (wait) {
            wait.notify();
        }
    }
}
