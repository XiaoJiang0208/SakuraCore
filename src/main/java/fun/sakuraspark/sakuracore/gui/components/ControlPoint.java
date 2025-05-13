package fun.sakuraspark.sakuracore.gui.components;

import org.jline.utils.Log;

import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class ControlPoint extends AbstractWidget {
    private int color = 0xFF00FF00;
    public ControlPoint(int x, int y, int width, int height) {
        super(x-width/2, y-height/2, width, height, null);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // TODO Auto-generated method stub
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, color);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // TODO Auto-generated method stub
        this.defaultButtonNarrationText(narrationElementOutput);
        
    }

    public void addChildren(AbstractWidget widget) {
        //this.addRenderableWidget(widget);
    }

    public void setColor(int color) {
        this.color = color;
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
    //拖拽事件
    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        LogUtils.getLogger().info("Dragging ControlPoint: " + "mouseX: " + mouseX + ", mouseY: " + mouseY + ", deltaX: " + deltaX + ", deltaY: " + deltaY);
        this.setPosition((int)mouseX-width/2, (int)mouseY-height/2);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        // TODO Auto-generated method stub
        LogUtils.getLogger().info("Clicked ControlPoint: " + "mouseX: " + mouseX + ", mouseY: " + mouseY);
    }

}
