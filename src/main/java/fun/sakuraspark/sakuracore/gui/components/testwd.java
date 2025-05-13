package fun.sakuraspark.sakuracore.gui.components;

import javax.annotation.Nullable;

import org.checkerframework.checker.units.qual.h;
import org.jline.utils.Log;

import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class testwd extends AbstractWithChildrenWidget {

    public testwd(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        //TODO Auto-generated constructor stub
        this.addRenderableWidget(new ControlPoint(10, 10, 5, 5));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(getX(), getY(), getX()+width, getY()+height, 0xFF000000);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        // TODO Auto-generated method stub
        LogUtils.getLogger().info("Clicked testwd: " + "mouseX: " + mouseX + ", mouseY: " + mouseY);
    }
    

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

}
