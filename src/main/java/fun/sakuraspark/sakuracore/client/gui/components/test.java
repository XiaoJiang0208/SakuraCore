package fun.sakuraspark.sakuracore.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class test extends AbstractScrollWidget{

    public test(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected int getInnerHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInnerHeight'");
    }

    @Override
    protected double scrollRate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scrollRate'");
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renderContents'");
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateWidgetNarration'");
    }

}
