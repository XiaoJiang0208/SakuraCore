package fun.sakuraspark.sakuracore.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;

public class SimpleLayout {
    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
    
    private final Orientation orientation;
    private final List<AbstractWidget> children = new ArrayList<>();
    private int spacing = 5; // 子组件之间的间距
    private int padding = 5; // 布局内边距

    public SimpleLayout(Orientation orientation) {
        this.orientation = orientation;
    }
    
    public void addChild(AbstractWidget widget) {
        children.add(widget);
    }
    
    // 根据指定区域，将子组件均匀排列
    public void doLayout(int x, int y, int width, int height) {
        if (orientation == Orientation.VERTICAL) {
            int total = children.size();
            if (total == 0) return;
            int availableHeight = height - 2 * padding - (total - 1) * spacing;
            
            int childHeight = availableHeight / total;
            int currentY = y + padding;
            for (AbstractWidget widget : children) {
                widget.setX(x + padding);
                widget.setY(currentY);
                widget.setWidth(width - 2 * padding);
                widget.setHeight(childHeight);
                currentY += childHeight + spacing;
            }
        } else {
            int total = children.size();
            if (total == 0) return;
            int availableWidth = width - 2 * padding - (total - 1) * spacing;
            int childWidth = availableWidth / total;
            int currentX = x + padding;
            for (AbstractWidget widget : children) {
                widget.setX(currentX);
                widget.setY(y + padding);
                widget.setWidth(childWidth);
                widget.setHeight(height - 2 * padding);
                currentX += childWidth + spacing;
            }
        }
    }
    
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    
    public void setPadding(int padding) {
        this.padding = padding;
    }
    
    public List<AbstractWidget> getChildren() {
        return children;
    }
}
