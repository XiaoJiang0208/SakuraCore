package fun.sakuraspark.sakuracore.client.graphics;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gui.GuiGraphics;

/**
 * 线条构建器，用于在Minecraft的GUI中绘制平滑、有厚度的线条。
 * 使用三角形带（triangle strip）渲染模式来创建线条，支持颜色和线宽的动态调整。
 * 处理了线条连接处的平滑过渡，避免了锐角处的视觉问题。
 */
public class LineBuilder {
    private final Matrix4f pose;
    private final BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();;
    private Vector2f last_vertex;
    private int last_color = 0xFFFFFFFF;
    private float last_weight = 1.0f;
    private Vector2f this_vertex;
    private int this_color = 0xFFFFFFFF;
    private float this_weight = 1.0f;
    private Vector2f next_vertex;
    private int next_color = 0xFFFFFFFF;
    private float next_weight = 1.0f;

    /**
     * 创建一个新的线条构建器
     * 
     * @param graphics GUI图形上下文，用于获取变换矩阵
     */
    public LineBuilder(@NonNull GuiGraphics graphics) {
        pose = graphics.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
    }

    /**
     * 设置下一个顶点的坐标
     * 
     * @param x 顶点的X坐标
     * @param y 顶点的Y坐标
     * @return 返回线条构建器实例，支持链式调用
     */
    public LineBuilder vertex(float x, float y) {
        next_vertex = new Vector2f(x, y);
        return this;
    }

    /**
     * 设置下一个顶点的颜色
     * 
     * @param color ARGB格式的颜色值
     * @return 返回线条构建器实例，支持链式调用
     */
    public LineBuilder color(int color) {
        next_color = color;
        return this;
    }

    /**
     * 设置下一段线条的粗细
     * 
     * @param weight 线条粗细，默认为1.0f
     * @return 返回线条构建器实例，支持链式调用
     */
    public LineBuilder weight(float weight) {
        next_weight = weight;
        return this;
    }

    /**
     * 完成当前顶点的添加，并计算线段的渲染信息
     * <p>
     * 处理线段的连接处，确保线条在拐角处平滑过渡。对于锐角，会自动调整以避免过度膨胀。
     * 此方法需要在设置完顶点、颜色和粗细后调用。
     * 
     * @return 返回线条构建器实例，支持链式调用
     */
    public LineBuilder endVertex() {
        if (this_vertex == null) {
            this_vertex = next_vertex;
            this_color = next_color;
            this_weight = next_weight;
            return this;
        }
        if (last_vertex == null) {
            last_vertex = this_vertex;
            last_color = this_color;
            last_weight = this_weight;
            this_vertex = next_vertex;
            this_color = next_color;
            this_weight = next_weight;
            Vector2f vector2f = new Vector2f(-(this_vertex.y - last_vertex.y), (this_vertex.x - last_vertex.x)).normalize();
            float dx = vector2f.x / 2.0f * last_weight;
            float dy = vector2f.y / 2.0f * last_weight;
            bufferbuilder.vertex(pose, last_vertex.x - dx, last_vertex.y - dy, 0).color(last_color).endVertex();
            bufferbuilder.vertex(pose, last_vertex.x + dx, last_vertex.y + dy, 0).color(last_color).endVertex();
            return this;
        }

        // 计算当前点的法向量（两条线段的角平分线）
        Vector2f prevSegment = new Vector2f(last_vertex.x - this_vertex.x, last_vertex.y - this_vertex.y).normalize();
        Vector2f nextSegment = new Vector2f(next_vertex.x - this_vertex.x, next_vertex.y - this_vertex.y).normalize();

        // 计算前一段的法线向量，用于确定角平分线的方向和共线情况下的偏移方向
        Vector2f prevNormal = new Vector2f(-(this_vertex.y - last_vertex.y), (this_vertex.x - last_vertex.x));
        if (prevNormal.lengthSquared() < 1e-12f) { // 处理 last_vertex 和 this_vertex 重合的罕见情况
            // 如果 prevNormal 长度为0，尝试使用 nextSegment 的法线
            // 这是一个边缘情况，理想情况下输入点不应导致这种情况
            // 如果 nextSegment 也无效，则无法计算法线，可能需要默认值或抛出错误
            // 这里简单地基于 nextSegment 计算一个法线 (如果 nextSegment 有效)
            if (nextSegment.lengthSquared() > 1e-12f) {
                prevNormal.set(-nextSegment.y, nextSegment.x).normalize(); // 使用 nextSegment 的法线
            } else {
                // 如果两个段都无效（例如，所有三个点都重合），则无法形成线段
                // 可以选择返回或使用一个默认的 bisector，例如 (0,1)
                // 但这通常表示输入数据有问题
                // 为了简单起见，如果 prevNormal 无法计算，后续逻辑可能会出问题
                // 但原始代码在此情况下 prevSegment.normalize() 也会产生 NaN
                // 此处我们先确保 prevNormal 被 normalize，即使它是 (0,0) normalize 后的 NaN
                 prevNormal.normalize(); // 会产生 NaN 如果长度为0
            }
        } else {
            prevNormal.normalize();
        }


        Vector2f bisector = new Vector2f(); // 初始化 bisector
        Vector2f sumPrevNext = new Vector2f(prevSegment).add(nextSegment);

        // 检查 prevSegment 和 nextSegment 是否几乎方向相反（表明三点共线且 this_vertex 在中间）
        // 当它们的和接近零向量时，表明角度接近180度。
        // 使用 lengthSquared() 比 length() 更高效，因为它避免了平方根运算。
        // 1e-8f 是一个较小的阈值 (例如 1e-4f * 1e-4f)
        if (sumPrevNext.lengthSquared() < 1e-8f) {
            // 三点共线，形成直线。此时 "角平分线" 未良好定义或计算不稳定。
            // 直接使用前一段的法向量 prevNormal 作为偏移方向。
            // prevNormal 已经是归一化的。
            bisector.set(prevNormal);
            // 在这种情况下，prevNormal.dot(bisector) 将是 prevNormal.dot(prevNormal) = 1 (因为它们相同且已归一化),
            // 这不小于0，所以不会翻转 bisector，这对于直线是正确的。
        } else {
            // 正常情况：存在一个角度，计算角平分线向量。
            bisector.set(sumPrevNext).normalize();
            // 确保 bisector 的方向与 prevNormal 定义的“外侧”一致，
            // 以保证三角形带顶点的一致缠绕顺序。
            if (prevNormal.dot(bisector) < 0) {
                // 如果方向不一致，翻转bisector
                bisector.mul(-1);
            }
        }

        // 确保生成的向量是垂直于路径方向的 // 注释：bisector 此处是斜接法线方向
        // 计算缩放因子 - 处理锐角情况
        float sinHalfAngle = (float) Math.sqrt((1.0f - prevSegment.dot(nextSegment)) / 2.0f);
        float scale = sinHalfAngle > 0.0001f ? 1.0f / sinHalfAngle : 1.0f;
        scale = Math.min(scale, 2.0f); // 限制最大缩放以避免尖角处过度膨胀

        float dx = bisector.x / 2.0f * this_weight * scale;
        float dy = bisector.y / 2.0f * this_weight * scale;

        // 保持顶点添加的一致顺序
        bufferbuilder.vertex(pose, this_vertex.x - dx, this_vertex.y - dy, 0).color(this_color).endVertex();
        bufferbuilder.vertex(pose, this_vertex.x + dx, this_vertex.y + dy, 0).color(this_color).endVertex();
        last_vertex = this_vertex;
        last_color = this_color;
        last_weight = this_weight;
        this_vertex = next_vertex;
        this_color = next_color;
        this_weight = next_weight;
        return this;
    }

    /**
     * 完成线条的构建，返回渲染缓冲区
     * <p>
     * 添加最后一个顶点并结束三角形带的构建。此方法应在所有顶点都添加完成后调用。
     * 
     * @return 包含线条渲染数据的缓冲区
     */
    public RenderedBuffer end() {
        Vector2f vector2f = new Vector2f(-(this_vertex.y - last_vertex.y), (this_vertex.x - last_vertex.x)).normalize();
        float dx = vector2f.x / 2.0f * this_weight;
        float dy = vector2f.y / 2.0f * this_weight;
        bufferbuilder.vertex(pose, this_vertex.x - dx, this_vertex.y - dy, 0).color(this_color).endVertex();
        bufferbuilder.vertex(pose, this_vertex.x + dx, this_vertex.y + dy, 0).color(this_color).endVertex();
        return bufferbuilder.end();
    }
}
