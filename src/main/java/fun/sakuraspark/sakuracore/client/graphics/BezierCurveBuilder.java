package fun.sakuraspark.sakuracore.client.graphics;

import java.util.List;
import java.util.Queue;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Vector2f;

import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;

import net.minecraft.client.gui.GuiGraphics;

public class BezierCurveBuilder {
    LineBuilder linebuilder;
    class BezierCurveVertex {
        Vector2f vertex;
        int color = 0xFFFFFFFF;
        float weight = 1.0f;
    }
    private final Queue<BezierCurveVertex> vertices = new java.util.LinkedList<>();
    private Vector2f next_vertex;
    private int next_color = 0xFFFFFFFF;
    private float next_weight = 1.0f;
    private int segments = 30; // 分段数
    public BezierCurveBuilder(@NonNull GuiGraphics graphics) {
        linebuilder = new LineBuilder(graphics);
    }

    public BezierCurveBuilder segments(int segments) {
        this.segments = segments;
        return this;
    }

    public BezierCurveBuilder vertex(float x, float y) {
        next_vertex = new Vector2f(x, y);
        return this;
    }

    public BezierCurveBuilder color(int color) {
        next_color = color;
        return this;
    }

    public BezierCurveBuilder weight(float weight) {
        next_weight = weight;
        return this;
    }

    public BezierCurveBuilder endVertex() {
        BezierCurveVertex vertex = new BezierCurveVertex();
        vertex.vertex = next_vertex;
        vertex.color = next_color;
        vertex.weight = next_weight;
        vertices.add(vertex);
        while (vertices.size() > 4) {
            vertices.poll();
        }
        return this;
    }

    public RenderedBuffer end() {
        if (vertices.size() < 2) {
            return null;
        }
        if (vertices.size() == 2) {
            BezierCurveVertex vertex1 = vertices.poll();
            BezierCurveVertex vertex2 = vertices.poll();
            linebuilder.vertex(vertex1.vertex.x, vertex1.vertex.y).color(vertex1.color).weight(vertex1.weight).endVertex();
            linebuilder.vertex(vertex2.vertex.x, vertex2.vertex.y).color(vertex2.color).weight(vertex2.weight).endVertex();
            return linebuilder.end(); // 返回缓冲区
        }
        if (vertices.size() == 3) {
            BezierCurveVertex p0 = vertices.poll();
            BezierCurveVertex p1 = vertices.poll();
            BezierCurveVertex p2 = vertices.poll();
            
            List<Vector2f> points = BezierCurve.quadraticCurve(p0.vertex, p1.vertex, p2.vertex, segments);
            for (int i = 0; i < points.size(); i++) {
                //对颜色进行线性插值实现渐变
                float t = i / (float) (points.size() - 1);
                int a0 = (p0.color >> 24) & 0xFF;
                int r0 = (p0.color >> 16) & 0xFF;
                int g0 = (p0.color >> 8) & 0xFF;
                int b0 = p0.color & 0xFF;
                int a1 = (p2.color >> 24) & 0xFF;
                int r1 = (p2.color >> 16) & 0xFF;
                int g1 = (p2.color >> 8) & 0xFF;
                int b1 = p2.color & 0xFF;
                int a = (int) (a0 + (a1 - a0) * t);
                int r = (int) (r0 + (r1 - r0) * t);
                int g = (int) (g0 + (g1 - g0) * t);
                int b = (int) (b0 + (b1 - b0) * t);
                int interpolatedColor = (a << 24) | (r << 16) | (g << 8) | b;
                
                //对粗细进行线性插值实现渐变
                float interpolatedWeight = p0.weight + (p2.weight - p0.weight) * t;
                
                Vector2f point = points.get(i);
                linebuilder.vertex(point.x, point.y).color(interpolatedColor).weight(interpolatedWeight).endVertex();
            }
        } else {
            BezierCurveVertex p0 = vertices.poll();
            BezierCurveVertex p1 = vertices.poll();
            BezierCurveVertex p2 = vertices.poll();
            BezierCurveVertex p3 = vertices.poll();
            
            List<Vector2f> points = BezierCurve.cubicCurve(p0.vertex, p1.vertex, p2.vertex, p3.vertex, segments);
            for (int i = 0; i < points.size(); i++) {
                //对颜色进行线性插值实现渐变
                float t = i / (float) (points.size() - 1);
                int a0 = (p0.color >> 24) & 0xFF;
                int r0 = (p0.color >> 16) & 0xFF;
                int g0 = (p0.color >> 8) & 0xFF;
                int b0 = p0.color & 0xFF;
                int a1 = (p3.color >> 24) & 0xFF;
                int r1 = (p3.color >> 16) & 0xFF;
                int g1 = (p3.color >> 8) & 0xFF;
                int b1 = p3.color & 0xFF;
                int a = (int) (a0 + (a1 - a0) * t);
                int r = (int) (r0 + (r1 - r0) * t);
                int g = (int) (g0 + (g1 - g0) * t);
                int b = (int) (b0 + (b1 - b0) * t);
                int interpolatedColor = (a << 24) | (r << 16) | (g << 8) | b;
                
                //对粗细进行线性插值实现渐变
                float interpolatedWeight = p0.weight + (p3.weight - p0.weight) * t;
                
                Vector2f point = points.get(i);
                linebuilder.vertex(point.x, point.y).color(interpolatedColor).weight(interpolatedWeight).endVertex();
            }
        }
        return linebuilder.end(); // 返回缓冲区
    }
}
