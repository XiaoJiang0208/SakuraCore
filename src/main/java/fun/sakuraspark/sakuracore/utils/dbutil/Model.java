package fun.sakuraspark.sakuracore.utils.dbutil;

import java.util.List;


public interface Model<T extends Model> {


    /**
     * 获取类型名称
     * @param type 类型
     * @return String 类型名称
     */
    public String getTypeName(Class<?> type);

    /**
     * 获取表头SQL语句
     * @return List<String> 表头SQL语句
     */
    public List<String> getTableHeader();

    public String getInsertSQL();
    public String getUpdateSQL();
    public String getDeleteSQL();
    public String getQuerySQL();
}
