package fun.sakuraspark.sakuracore.utils.dbutil;

import java.sql.Connection;

public interface Database {
    /**
     * 获取数据库连接
     * @return Connection 数据库连接
     */
    public Connection getConnection() throws Exception;
    /**
     * 获取创建表的SQL语句
     * @param table 表对象
     * @return String 创建表的SQL语句
     */
    public String createTableSQL();
    
    public String delTableSQL(String tableName);
    
}
