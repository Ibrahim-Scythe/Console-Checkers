import javax.swing.*;
import java.awt.*;

// Swing component
// Green rounded buttons with no outline
public class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI Black", Font.PLAIN, 24));
        setContentAreaFilled(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.decode("#40c83a"));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }
}
