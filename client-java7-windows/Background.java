import java.awt.*;
import javax.swing.*;

public class Background extends JPanel {
	
	Image bg;
	
	public Background(String bgName) {
		bg = new ImageIcon(getClass().getResource("interface/" + bgName + ".png")).getImage();
	}
	   
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
	}
}