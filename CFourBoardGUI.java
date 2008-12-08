
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.AbstractList;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class CFourBoardGUI extends JFrame implements Runnable{
	
	private Vector screens = new Vector();
	private Screen screen = new Screen("default");

	private RenderPanel _renderer;
	private int _rows = 7;
	private int _cols = 7;
	private int _margin = 0;
	private boolean runFlag = false;
	private boolean resetFlag = false;
	
	public CFourBoardGUI(){
		super("Connect 4");
		
 		//add widgets here

 		screen.setVisible(true);
 		
 		_renderer = new RenderPanel(screens);
 		 		
 		screens.addElement(screen);
 		_renderer.setGrids(7);
 		
 		Container c = this.getContentPane();
 		c.setLayout(new BorderLayout());
 		c.add(_renderer,BorderLayout.CENTER);
 		
 		setSize(500, 535);
 		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
 		this.setLocationRelativeTo(null);
 		this.setVisible(true);
 		this.setResizable(true);
 		
 		
	}//end of graphics end constructor


	public void resetBoard(){
		
	}
	
	public void setBoard(byte[][] slots){
		screen.clearAll();
		screen.setReady(false);
		System.gc();

		for(int x = 0;x<7;x++){
			for(int y = 0;y<7;y++){
				byte tslot = slots[x][y];
				Widget slot;
				if(tslot == Constants.BLACK){
					slot = new Widget("slotB","con4_black.JPG",0,0,30,30,"slot");
				}else
				if(tslot == Constants.WHITE){
					slot = new Widget("slotB","con4_white.JPG",0,0,30,30,"slot");
				}else{
					slot = new Widget("slotB","con4_blank.JPG",0,0,30,30,"slot");
				}
				this.setWidgetPos(slot, x, y);
				screen.add(slot);
			}
		}
		screen.setReady(true);
	}
	
	public void run() {
		while(true){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(runFlag!=false){
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//real time rendering

			}			
		}
	}
		

	
	public void setWidgetPos(Widget thing, int y, int x){
		int total = _rows * _cols;
		
		//rendering board size
		int boardW = 500;
		int boardH = 500;
			
		int cubeDx = 500/_cols;
		int cubeDy = 500/_rows;
		int height = thing.getbY();
		
		if(height>cubeDx){
			thing.setbx(cubeDx - 2);

		}
		if(thing.getbY()>cubeDy){
			thing.setby(cubeDy - 2);
		}
		
		int counter = 0;
		
		for(int coly = 0; coly<_rows; coly++){
			for(int colx = 0; colx<_cols; colx++){
				if(x==colx&&y==coly){
					thing.setbx(cubeDx);
					thing.setby(cubeDy);
					thing.setX((((colx*cubeDx))));
					thing.setY((((coly*cubeDy))));
				}
				counter++;
			}
		}
	}
	
	
	
}//end of class
