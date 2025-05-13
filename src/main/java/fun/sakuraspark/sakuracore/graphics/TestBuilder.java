package fun.sakuraspark.sakuracore.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;

public class TestBuilder {
    private final Matrix4f pose;
    private final BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();;
    private Vector2f last_vertex;
    private int last_color = 0xFFFFFFFF;
    private Vector2f this_vertex;
    private int this_color = 0xFFFFFFFF;
    private Vector2f next_vertex;
    private int next_color = 0xFFFFFFFF;

    public TestBuilder(GuiGraphics graphics) {
        pose = graphics.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
    }
    public void end() {
        bufferbuilder.vertex(pose, 10, 10, 0).color(0xFF888888).endVertex();
        bufferbuilder.vertex(pose, 200, 100, 0).color(0xFFCCCCCC).endVertex();
        bufferbuilder.vertex(pose, 200, 300, 0).color(0xFFCCCCCC).endVertex();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // 绘制缓冲区到屏幕
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

}
