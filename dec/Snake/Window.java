package Snake;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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
    public JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
	
	public Window(){
        JPanel gui = new JPanel(new BorderLayout((playArea + 1), playArea));
		
		Grid = new ArrayList<ArrayList<DataOfSquare>>();
		ArrayList<DataOfSquare> data;
		
		// Creates Threads and its data and adds it to the arrayList
		for(int i=0;i<playArea;i++){
			data= new ArrayList<DataOfSquare>();
			for(int j=0;j<playArea;j++){
				DataOfSquare c = new DataOfSquare(0);
				data.add(c);
			}
			Grid.add(data);
		}
		
        content = new JPanel(new GridLayout(playArea,playArea,0,0));
        content.setMaximumSize(new Dimension(50, 50));
        gui.add(content);
		
		for(int i=0;i<playArea;i++){
			for(int j=0;j<playArea;j++){
				content.add(Grid.get(i).get(j).square);
			}
		}

		getContentPane().add(gui, BorderLayout.CENTER);
		
        vertical.setFloatable(false);

        vertical.add(lengthLbl);
        vertical.add(maxLbl);
        vertical.add(avgLbl);
        vertical.add(totalLbl);
        vertical.add(runsLbl);
        
        vertical.setLayout(new BoxLayout(vertical, BoxLayout.Y_AXIS));
        vertical.setAlignmentY(CENTER_ALIGNMENT);
        getContentPane().add(vertical, BorderLayout.WEST);
		
		Tuple position = new Tuple(10,10);
		c = new ThreadsController(position);
		c.start();

		this.addKeyListener((KeyListener) new KeyboardListener());
	}
	public void resetVertical(int vertHeight, int frameHeight, JToolBar panel) {
        int topMargin = (frameHeight / 2) - (vertHeight / 2);
        panel.setMargin(new Insets(topMargin, 0, 0, 10));
	}
}
