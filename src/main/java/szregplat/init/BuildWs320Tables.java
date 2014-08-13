package szregplat.init;

import szregplat.omc.Ws320RuntimeException;
import szregplat.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hch on 2014/4/26.
 */
public class BuildWs320Tables {
    private static Config config = Config.getInstance("db.properties");

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new Ws320RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        String sql = "" +
                "create database if not exists ws320 " +
                "default character set = utf8 " +
                "default collate = utf8_general_ci;";

        execute(sql);


        sql = "" +
                "create table if not exists ws320.hospitals" +
                "(" +
                "   id      varchar(10)     not null," +
                "   name    varchar(20)     not null," +
                "   address varchar(100)    not null," +
                "   phone   varchar(20)     not null," +
                "   intro   varchar(500)        null," +
                "   grade   varchar(10)         null," +
                "   kine    varchar(10)         null," +

                "   wsdl    varchar(200)        null," +

                "   primary key using hash (id)," +
                "   index name_idx using hash (name)" +

                ");";


        execute(sql);

    }

    public static void execute(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(
                config.getString("db.url"),
                "root",
                config.getString("db.rootpass")
        );
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
        conn.close();
    }
}
