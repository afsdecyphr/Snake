package Snake;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;


public class Window extends JFrame{
	private static final long serialVersionUID = -2542001418764869760L;
	public static ArrayList<ArrayList<DataOfSquare>> Grid;

	public static JLabel lengthLbl = new JLabel("Length: ");
	public static JLabel maxLbl = new JLabel("Max Length: ");
	public static JLabel avgLbl = new JLabel("Average: ");
	public static JLabel totalLbl = new JLabel("Total: ");
	public static JLabel runsLbl = new JLabel("Runs: ");
    
	public static JLabel xL = new JLabel("x<: ");
	public static JLabel xG = new JLabel("x>: ");
	public static JLabel yL = new JLabel("y<: ");
	public static JLabel yG = new JLabel("y>: ");
	public static int playArea = 50;
	public static int height = 50;
	public static ThreadsController c;
	public static Container content;
	public static int maxLength = 3;
	public Window(){
        JPanel gui = new JPanel(new BorderLayout((playArea + 1), playArea));
		
		Grid = new ArrayList<ArrayList<DataOfSquare>>();
		ArrayList<DataOfSquare> data;
		
		// Creates Threads and its data and adds it to the arrayList
		for(int i=0;i<playArea;i++){
			data= new ArrayList<DataOfSquare>();
			for(int j=0;j<playArea;j++){
				DataOfSquare c = new DataOfSquare(2);
				data.add(c);
			}
			Grid.add(data);
		}
		
        content = new JPanel(new GridLayout(playArea,playArea,0,0));
        content.setMaximumSize(new Dimension(400, 400));
        gui.add(content);
		
		for(int i=0;i<playArea;i++){
			for(int j=0;j<playArea;j++){
				content.add(Grid.get(i).get(j).square);
			}
		}

		getContentPane().add(gui, BorderLayout.CENTER);
		
        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(0, 0, 0, 10));

        vertical.add(lengthLbl);
        vertical.add(maxLbl);
        vertical.add(avgLbl);
        vertical.add(totalLbl);
        vertical.add(runsLbl);

        getContentPane().add(vertical, BorderLayout.WEST);
		
		Tuple position = new Tuple(10,10);
		c = new ThreadsController(position);
		c.start();

		this.addKeyListener((KeyListener) new KeyboardListener());
	}
}
