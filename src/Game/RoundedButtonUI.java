package Game;

import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import javax.swing.*;

public class RoundedButtonUI extends BasicButtonUI {

    private final Color backgroundColor;
    private final Color borderColor;
    private final int borderRadius;

    public RoundedButtonUI(Color backgroundColor, Color borderColor, int borderRadius) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderRadius = borderRadius;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background paint
            g2d.setColor(button.getModel().isArmed() ? backgroundColor.darker() : backgroundColor);
            g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), borderRadius, borderRadius);

            // border paint
            g2d.setColor(borderColor);
            g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, borderRadius, borderRadius);

            // text paint
            super.paint(g, c);
        } finally {
            g2d.dispose();
        }
    }
}