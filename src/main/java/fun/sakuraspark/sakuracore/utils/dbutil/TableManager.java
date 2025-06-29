package fun.sakuraspark.sakuracore.utils.dbutil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableManager<T extends Model<T>> {
    private final Connection conn;
    private T model;
    Statement stmt;
    ResultSet rs;

    public TableManager(Connection conn, T model) {
        this.conn = conn;
        this.model = model;
        stmt = null;
        rs = null;
    }

    public void Query(String condition) {
        try {
            if (rs!=null||!rs.isClosed()){
                rs.close();
            }
            if (rs!=null||!stmt.isClosed()){
                stmt.close();
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(model.getQuerySQL().replace("?", condition));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean update(String where) {
        try {
            if (rs!=null||!rs.isClosed()){
                rs.close();
            }
            if (rs!=null||!stmt.isClosed()){
                stmt.close();
            }
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(model.getUpdateSQL().replace("?", where));
            return rowsAffected > 0; // 返回true表示更新成功
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 返回false表示更新失败
    }

    public boolean insert() {
        try {
            if (rs!=null||!rs.isClosed()){
                rs.close();
            }
            if (rs!=null||!stmt.isClosed()){
                stmt.close();
            }
            stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate(model.getInsertSQL());
            return rowsAffected > 0; // 返回true表示插入成功
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 返回false表示插入失败
    }

    public boolean next() {

        try {
            if(rs.next()) {
                // 这里假设Model有一个静态方法fromResultSet来从ResultSet创建实例
                for(Field field : model.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    // 获取字段名
                    String fieldName = field.getName();
                    // 获取字段值
                    Object value = rs.getObject(fieldName);
                    // 设置到model实例中
                    field.set(model, value);
                    return true; // 返回true表示有下一条记录
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    T getModel() {
        return model;
    }
}
