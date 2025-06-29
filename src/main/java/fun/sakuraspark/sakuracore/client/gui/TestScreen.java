package fun.sakuraspark.sakuracore.client.gui;

import javax.annotation.Nonnull;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;

import fun.sakuraspark.sakuracore.client.graphics.BezierCurveBuilder;
import fun.sakuraspark.sakuracore.client.graphics.LineBuilder;
import fun.sakuraspark.sakuracore.client.gui.components.ControlHandle;
import fun.sakuraspark.sakuracore.client.gui.components.ControlPoint;
import fun.sakuraspark.sakuracore.client.gui.components.MarkdownBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class TestScreen extends Screen {

    ControlPoint p0 = new ControlPoint(100, 100, 5, 5);
    ControlPoint p1 = new ControlPoint(300, 60, 5, 5);
    ControlPoint p2 = new ControlPoint(380, 60, 5, 5);
    ControlPoint p3 = new ControlPoint(300, 200, 5, 5);
    ControlPoint p4 = new ControlPoint(500, 200, 5, 5);

    Button button1;

    public TestScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        // 初始化按钮
        this.addRenderableWidget(Button.builder(Component.translatable("button1"), (button) -> {
            LogUtils.getLogger().info("Button1 clicked!");
        }).bounds(10, 60, 100, 20).build());this.addRenderableWidget(Button.builder(Component.translatable("button2"), (button) -> {
            LogUtils.getLogger().info("Button2 clicked!");
        }).bounds(50, 60, 100, 20).build());
        this.addRenderableWidget(p0);
        this.addRenderableWidget(p1);
        this.addRenderableWidget(p2);
        this.addRenderableWidget(p3);
        this.addRenderableWidget(p4);
        this.addRenderableWidget(new ControlHandle(10, 10, 5, 5));
        this.addRenderableWidget(new MarkdownBox(20, 20, 100, 100, "# test\n## test2\n### test3\n#### test4\n##### test5\n###### test6\n**bold**\n*italic*\n~~strikethrough~~\n> blockquote\n- list item 1\n- list item 2\n- list item 3\n1. numbered item 1\n2. numbered item 2\n3. numbered item 3"));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //this.renderBackground(graphics);
        Matrix4f transformationMatrix = graphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        // 计算 GUI 中心点
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 开始三角形条缓冲，使用 POSITION_COLOR 顶点格式
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        // 写入相对中心的顶点
        bufferbuilder.vertex(transformationMatrix, centerX, centerY - 20, 0).color(0xFF888888).endVertex();
        bufferbuilder.vertex(transformationMatrix, centerX - 15, centerY, 0).color(0xFFCCCCCC).endVertex();
        bufferbuilder.vertex(transformationMatrix, centerX + 15, centerY, 0).color(0xFFCCCCCC).endVertex();
        bufferbuilder.vertex(transformationMatrix, centerX, centerY + 20, 0).color(0xFF888888).endVertex();
        bufferbuilder.vertex(transformationMatrix, centerX+20, centerY+50, 0).color(0xFF888877).endVertex();

        // 确保使用正确的着色器
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        //RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // 绘制缓冲区到屏幕
        BufferUploader.drawWithShader(bufferbuilder.end());
        //TestBuilder testBuilder = new TestBuilder(graphics);
        //testBuilder.end();
        graphics.hLine(centerX-100, centerY-100, centerY-50, 0xFFFF0000);
        LineBuilder lineBuilder = new LineBuilder(graphics);
        lineBuilder.vertex(10, 10).color(0xFFFF0000).weight(8).endVertex()
            .vertex(50, 10).color(0xFF3EC0FF).weight(5).endVertex()
            .vertex(70, 50).color(0xFFF74C30).weight(3).endVertex()
            .vertex(90, 10).color(0xFF9BE96F).endVertex()
            .vertex(110, 10).color(0xFF3EC0FF).endVertex()
            .vertex(130, 50).color(0xFFF74C30).endVertex();
        BufferUploader.drawWithShader(lineBuilder.end());
        
        // 绘制二次贝塞尔曲线
        BezierCurveBuilder bezierCurveBuilder = new BezierCurveBuilder(graphics);
        bezierCurveBuilder.vertex(50, 200).color(0xFFFF0000).endVertex()
            .vertex(p0.getCenterX(), p0.getCenterY()).color(0xFFF74C30).endVertex()
            .vertex(250, 200).color(0xFF3EC0FF).endVertex();
        BufferUploader.drawWithShader(bezierCurveBuilder.end());
        lineBuilder = new LineBuilder(graphics);
        lineBuilder.vertex(50, 200).color(0xFFFF0000).endVertex()
            .vertex(p0.getCenterX(), p0.getCenterY()).color(0xFFF74C30).endVertex();
        BufferUploader.drawWithShader(lineBuilder.end());

        // 绘制三次贝塞尔曲线
        bezierCurveBuilder = new BezierCurveBuilder(graphics);
        bezierCurveBuilder.vertex(p3.getCenterX(), p3.getCenterY()).color(0xFFFF0000).weight(10).endVertex()
            .vertex(p1.getCenterX(),p1.getCenterY()).color(0xFFF74C30).endVertex()
            .vertex(p2.getCenterX(),p2.getCenterY()).color(0xFF3EC0FF).endVertex()
            .vertex(p4.getCenterX(), p4.getCenterY()).color(0xFF93E468).weight(2).endVertex();
        BufferUploader.drawWithShader(bezierCurveBuilder.end());
        lineBuilder = new LineBuilder(graphics);
        lineBuilder.vertex(p3.getCenterX(), p3.getCenterY()).color(0xFFFF0000).endVertex()
            .vertex(p1.getCenterX(),p1.getCenterY()).color(0xFFF74C30).endVertex();
        BufferUploader.drawWithShader(lineBuilder.end());
        lineBuilder = new LineBuilder(graphics);
        lineBuilder.vertex(p4.getCenterX(),p4.getCenterY()).color(0xFFF74C30).endVertex()
            .vertex(p2.getCenterX(), p2.getCenterY()).color(0xFF3EC0FF).endVertex();
        BufferUploader.drawWithShader(lineBuilder.end());
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    //当鼠标按下
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        LogUtils.getLogger().info("Mouse clicked at: " + mouseX + ", " + mouseY + " with button: " + button);
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}