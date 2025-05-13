package fun.sakuraspark.sakuracore.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;

import fun.sakuraspark.sakuracore.graphics.LineBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;

public class ControlHandle extends AbstractWithChildrenWidget {
    private ControlPoint p0;
    private ControlPoint p1;
    private int line_width = 2;

    public ControlHandle(int x, int y, int width, int height) {
        super(x-width/x, y-height/2, width, width, null);
        p0 = new ControlPoint(x-100, y, 5, 5);
        p1 = new ControlPoint(x+100, y, 5, 5);
        this.addRenderableWidget(p0);
        this.addRenderableWidget(p1);
    }

    public void setCenter(int x, int y) {
        this.setPosition(x-width/2, y-height/2);
    }
    public void setCenterX(int x) {
        this.setCenter(x, this.getCenterY());
    }
    public void setCenterY(int y) {
        this.setCenter(this.getCenterX(), y);
    }
    public int getCenterX() {
        return this.getX() + this.width / 2;
    }
    public int getCenterY() {
        return this.getY() + this.height / 2;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        LineBuilder lineBuilder = new LineBuilder(guiGraphics);
        lineBuilder.vertex(p0.getCenterX(), p0.getCenterY()).color(0xFF00FF00).weight(line_width).endVertex();
        lineBuilder.vertex(p1.getCenterX(), p1.getCenterY()).color(0xFF00FF00).endVertex();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        //RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(lineBuilder.end());
        guiGraphics.fill(getX(), getY(), getX()+width, getY()+height, 0xFF000000);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // TODO Auto-generated method stub
        this.defaultButtonNarrationText(narrationElementOutput);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            if (p0.isFocused()) {
                int centerX = this.getX() + this.width / 2;
                int centerY = this.getY() + this.height / 2;
                double fixedRadius = Math.hypot(p1.getCenterX() - centerX, p1.getCenterY() - centerY);
                double angle = Math.atan2(p0.getCenterY() - centerY, p0.getCenterX() - centerX);
                double newAngle = angle + Math.PI;
                int newP1CenterX = (int)Math.round(centerX + fixedRadius * Math.cos(newAngle));
                int newP1CenterY = (int)Math.round(centerY + fixedRadius * Math.sin(newAngle));
                p1.setPosition(newP1CenterX - p1.getWidth() / 2, newP1CenterY - p1.getHeight() / 2);
            } else if (p1.isFocused()) {
                int centerX = this.getX() + this.width / 2;
                int centerY = this.getY() + this.height / 2;
                double fixedRadius = Math.hypot(p0.getCenterX() - centerX, p0.getCenterY() - centerY);
                double angle = Math.atan2(p1.getCenterY() - centerY, p1.getCenterX() - centerX);
                double newAngle = angle + Math.PI;
                int newP0CenterX = (int)Math.round(centerX + fixedRadius * Math.cos(newAngle));
                int newP0CenterY = (int)Math.round(centerY + fixedRadius * Math.sin(newAngle));
                p0.setPosition(newP0CenterX - p0.getWidth() / 2, newP0CenterY - p0.getHeight() / 2);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        // 在 ControlHandle 移动之前，计算 p0 和 p1 相对于 ControlHandle 左上角的原始偏移量
        // offsetX = child.X - parent.X
        // offsetY = child.Y - parent.Y
        int p0_relativeX_to_handle = p0.getX() - this.getX();
        int p0_relativeY_to_handle = p0.getY() - this.getY();
        int p1_relativeX_to_handle = p1.getX() - this.getX();
        int p1_relativeY_to_handle = p1.getY() - this.getY();

        // 更新 ControlHandle 的位置，使其中心对齐鼠标光标
        // 使用 Math.round() 以获得更平滑的居中效果
        int newHandleX = (int)Math.round(mouseX - this.width / 2.0);
        int newHandleY = (int)Math.round(mouseY - this.height / 2.0);
        this.setPosition(newHandleX, newHandleY);

        // ControlHandle 移动后，this.getX() 和 this.getY() 将返回新坐标
        // 将原始的相对偏移量应用于新的 ControlHandle 位置，以确定 p0 和 p1 的新位置
        // new_child.X = new_parent.X + offsetX
        p0.setPosition(this.getX() + p0_relativeX_to_handle, this.getY() + p0_relativeY_to_handle);
        p1.setPosition(this.getX() + p1_relativeX_to_handle, this.getY() + p1_relativeY_to_handle);
    }
    
}
