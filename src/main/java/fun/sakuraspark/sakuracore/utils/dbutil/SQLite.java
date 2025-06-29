package fun.sakuraspark.sakuracore.utils.dbutil;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLite implements Database {

    private static String URL;

    /**
     * SQLite数据库
     * @param url 数据库文件的路径
     */
    public SQLite(String url) {
        URL = url;
    }
    @Override
    public Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return java.sql.DriverManager.getConnection("jdbc:sqlite:"+URL);
    }

    @Override
    public String createTableSQL() {

        // 实现获取创建表的SQLite语句的逻辑
        //StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS "+model.getClass().getSimpleName()+ " (" + model.getTableHeader() + ");");
        return null; // 返回null表示未实现
        
    }
    @Override
    public String delTableSQL(String tableName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delTableSQL'");
    }

}
