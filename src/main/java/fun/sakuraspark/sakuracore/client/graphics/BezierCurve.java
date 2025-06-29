package fun.sakuraspark.sakuracore.client.graphics;

import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * 贝塞尔曲线工具类，提供二次和三次贝塞尔曲线的计算方法
 */
public class BezierCurve {
    
    /**
     * 计算二次贝塞尔曲线上的点
     * 
     * @param p0 起点
     * @param p1 控制点
     * @param p2 终点
     * @param t 参数 t (0.0 到 1.0)
     * @return 曲线上的点
     */
    public static Vector2f quadraticPoint(Vector2f p0, Vector2f p1, Vector2f p2, float t) {
        float x = (1 - t) * (1 - t) * p0.x + 2 * (1 - t) * t * p1.x + t * t * p2.x;
        float y = (1 - t) * (1 - t) * p0.y + 2 * (1 - t) * t * p1.y + t * t * p2.y;
        return new Vector2f(x, y);
    }
    
    /**
     * 计算三次贝塞尔曲线上的点
     * 
     * @param p0 起点
     * @param p1 第一控制点
     * @param p2 第二控制点
     * @param p3 终点
     * @param t 参数 t (0.0 到 1.0)
     * @return 曲线上的点
     */
    public static Vector2f cubicPoint(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3, float t) {
        float mt = 1 - t;
        float mt2 = mt * mt;
        float mt3 = mt2 * mt;
        float t2 = t * t;
        float t3 = t2 * t;
        
        float x = mt3 * p0.x + 3 * mt2 * t * p1.x + 3 * mt * t2 * p2.x + t3 * p3.x;
        float y = mt3 * p0.y + 3 * mt2 * t * p1.y + 3 * mt * t2 * p2.y + t3 * p3.y;
        return new Vector2f(x, y);
    }
    
    /**
     * 生成二次贝塞尔曲线上的点列表
     * 
     * @param p0 起点
     * @param p1 控制点
     * @param p2 终点
     * @param segments 分段数量
     * @return 曲线上的点列表
     */
    public static List<Vector2f> quadraticCurve(Vector2f p0, Vector2f p1, Vector2f p2, int segments) {
        List<Vector2f> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            points.add(quadraticPoint(p0, p1, p2, t));
        }
        return points;
    }
    
    /**
     * 生成三次贝塞尔曲线上的点列表
     * 
     * @param p0 起点
     * @param p1 第一控制点
     * @param p2 第二控制点
     * @param p3 终点
     * @param segments 分段数量
     * @return 曲线上的点列表
     */
    public static List<Vector2f> cubicCurve(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3, int segments) {
        List<Vector2f> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            points.add(cubicPoint(p0, p1, p2, p3, t));
        }
        return points;
    }
}
