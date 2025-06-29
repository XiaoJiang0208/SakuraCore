package fun.sakuraspark.sakuracore.utils.dbutil;

import java.lang.reflect.Field;
import java.util.List;

public class SQLiteModel implements Model<SQLiteModel> {

    SQLiteModel() {
        // 默认构造函数
    }

    @Override
    public String getTypeName(Class<?> type) {
        if (type == String.class) {
            return "TEXT";
        } else if (type == Integer.class || type == int.class) {
            return "INTEGER";
        } else if (type == Long.class || type == long.class) {
            return "BIGINT";
        } else if (type == Double.class || type == double.class) {
            return "REAL";
        } else if (type == Boolean.class || type == boolean.class) {
            return "BOOLEAN";
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
    }

    @Override
    public List<String> getTableHeader() {
        List<String> headers = new java.util.ArrayList<>();
        //获取所有属性
        for (Field header : this.getClass().getDeclaredFields()) {
            headers.add(header.getName() + " " + getTypeName(header.getType()));
        }
        return headers;
    }

    @Override
    public String getInsertSQL() {
        // 根据子类的属性生成插入SQL语句
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.getClass().getSimpleName()).append(" (");
        List<String> headers = getTableHeader();
        sql.append(String.join(", ", headers));
        sql.append(") VALUES (");
        for (int i = 0; i < headers.size(); i++) {
            sql.append("?");
            if (i < headers.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String getUpdateSQL() {
        // 根据子类的属性生成更新SQL语句
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(this.getClass().getSimpleName()).append(" SET ");
        List<String> headers = getTableHeader();
        for (int i = 0; i < headers.size(); i++) {
            sql.append(headers.get(i)).append(" = ?");
            if (i < headers.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE id = ?"); // 假设有一个主键字段id
        return sql.toString();
    }

    @Override
    public String getDeleteSQL() {
        // 根据子类的属性生成删除SQL语句
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(this.getClass().getSimpleName());
        sql.append(" WHERE id = ?"); // 假设有一个主键字段id
        return sql.toString();
    }

    @Override
    public String getQuerySQL() {
        // 根据子类的属性生成查询SQL语句
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.getClass().getSimpleName());
        sql.append(" WHERE id = ?"); // 假设有一个主键字段id
        return sql.toString();
    }

    
}
