package Snake;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Main {
	public static Window f1;
	public static void main(String[] args) {
		f1 = new Window();
		f1.setTitle("Snake");
		f1.pack();
		Rectangle r = f1.getBounds();
		f1.setSize(r.width-200, r.height-200);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
