

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.applet.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class RenderPanel extends JPanel implements Runnable
{
	private static final int PWIDTH = 500; // size of panel
	private static final int PHEIGHT = 500;
	private Thread _Engine; // for the animation
	private boolean _running = false; // stops the animation
	private boolean _renderOver = false; // for termination
	private Graphics2D _dbg;
	private Graphics2D _cache;
	private BufferedImage _buffer = null;
	private int _period = 10; //100fps = 10ms per frame
	private Vector _screens;
	private float _lastLoad = 0;
	private float _lastLoad2 = 0;
	private float _alpha = 0;
	private Composite _oldComp;
	private float _offset = 0;
	private Screen _currentScreen;
	
	private boolean _gridFlag = false;
	private int _x = 0;
	private int _y = 0;
	private int _loaded_cellsx = 0;
	private int _loaded_cellsy = 0;
	private boolean _gridLoaded = false;
	BufferedImage _imgBG;
	
	
	
	public RenderPanel(Vector screens)
	{
		super.setBackground(Color.white); // white background
		super.setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
		this._screens = screens;
		try {
			_imgBG = (BufferedImage) ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
		}
	} // end of RenderPanel()
	
	public void setGrids(int xx){
		_gridFlag = true;
		_gridLoaded = false;
		_loaded_cellsx = 0;
		_loaded_cellsy = 0;
		_x = xx;
		_y = xx;
	}
	
	public void setGrids(int xx, int yy){
		_gridFlag = true;
		_gridLoaded = false;
		_loaded_cellsx = 0;
		_loaded_cellsy = 0;
		_x = xx;
		_y = yy;
	}
	
	public void addNotify()
	/* Wait for the JPanel to be added to the
	JFrame/JApplet before starting. */
	{
		super.addNotify();
		startRender(); // start the thread
	}
	
	private void startRender(){
		if (_Engine == null || !_running) {
			_Engine = new Thread(this);
			_Engine.start();
		}
	} // end of startRender()
	
	public void stopRender(){ //	 called by the user to stop execution
		_running = false; 
		}
	
	
	public void run(){
		
		_running = true;
		
		long beforeTime, timeDiff, sleepTime,wakeupTime,jetLag,fpsStart,fpsEnd,secondDif;
		int frameCount = 0;
		jetLag = 0;
		fpsStart = System.nanoTime();
		secondDif=0;
		
		while(_running) {
			
				frameCount++;
				beforeTime = System.nanoTime();
				screenRender(); // render to a buffer
				screenUpdate(); // screen state is updated
				paintScreen(); // paint with the buffer
				timeDiff = System.nanoTime() - beforeTime;
				sleepTime = _period - timeDiff + jetLag; // time left in this loop
				
				if (sleepTime <= 0) // update/render took longer than period
				sleepTime = 5; // sleep a bit anyway
				
				try {
					//System.out.println("Sleep time: "+sleepTime);
					Thread.sleep(sleepTime); // sleep a bit
					wakeupTime = System.nanoTime();
					jetLag = _period - (wakeupTime - beforeTime);
					fpsEnd = System.nanoTime();
					
					secondDif = fpsEnd - fpsStart;
					if(secondDif>=1000000000){
						frameCount = 0;
						fpsStart = System.nanoTime();
					}
					
				}
				catch(InterruptedException ex){}
				beforeTime = System.currentTimeMillis();
			}
		System.exit(0); // so enclosing JFrame/JApplet exits
	} // end of run()
	
	private void screenRender() {
		
		if (_buffer == null){ // create the buffer
			_buffer = (BufferedImage)createImage(PWIDTH, PHEIGHT);
			if (_buffer == null) {
				System.out.println("buffer is null");
				return;
			}
		}else{
			
			
			try{
			_dbg = _buffer.createGraphics();
			
			if(_cache!=null){
				_dbg = _cache;
			}
			
			/*
			_dbg.setColor(Color.white);
			//dbg.setPaint(new GradientPaint(0, 0, Color.white, 500, 502, Color.blue, true));
	        _dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
	        */
			
	        if(_alpha<=1f){
	        	_offset=_alpha-1f;
	        	
				if(System.nanoTime()-_lastLoad>=17000000){
						_alpha+=0.01;
						if(_alpha>=1f){
							_alpha=0;//1f;
							_offset=_alpha-1f;
						}
						_lastLoad = System.nanoTime();
				}
				//Save the original composite.
			    _oldComp = _dbg.getComposite();

			    // Create an AlphaComposite with alpha% translucency.
			    Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _alpha);

			    // Set the composite on the Graphics2D object.
			    _dbg.setComposite(alphaComp);
			}
	       
	        if(_currentScreen==null||_currentScreen.isVisible()==false){
	        	
		        Enumeration x = _screens.elements();
		        
		        while(x.hasMoreElements()){
		        	Screen tempScreen = (Screen) x.nextElement();
		        	if(tempScreen.isVisible()){
		        		_currentScreen = tempScreen;
		        		
						Enumeration e = tempScreen.getObjects();
						
						while(e.hasMoreElements()){
						    
							Widget tempW = (Widget) e.nextElement();
							
							_dbg.drawImage(tempW.getImage(),tempW.getX(), tempW.getY(), tempW.getbX(),tempW.getbY(), null);
							_cache = _dbg;
						}
						
		        	}
		        }
		        
		        }else{
	        	
	        	Screen tempScreen = (Screen) _currentScreen;
	        	
	        	if(tempScreen.isVisible()){
			      	if(tempScreen.ready()==true){
						Enumeration e = tempScreen.getObjects();
						while(e.hasMoreElements()){
						    
							Widget tempW = (Widget) e.nextElement();
							//dbg.drawImage(tempW.getImage(), 0, 0, tempW.getX(), tempW.getY(), 0, 0, tempW.getbX(), tempW.getbY(),  null);
							_dbg.drawImage(tempW.getImage(),tempW.getX(), tempW.getY(), tempW.getbX(),tempW.getbY(), null);
							_cache = _dbg;
						}
			      	}else{
			      		_dbg = _cache;
			      	}

	        	}
	        }
	        
			}catch(Exception e){
				//catch rendering errors
			}
		}
			//drawing needs to be done here 
	}
	
	public void screenChanged(){
		this._alpha = 0;
	}


	private void screenUpdate(){ 
		//more screen updating here
		if (!_renderOver){
			//display an error message if engine breaks
		}
	}
	
	private void paintScreen()
		//actively render the buffer image to the screen
	{
		Graphics2D g;
		
		try {
		g = (Graphics2D) this.getGraphics(); // get the panel�s graphic context
		if ((g != null) && (_buffer != null))
			g.drawImage(_buffer, 0, 0, null);
			g.dispose();
		}
		catch (Exception e)
		{ System.out.println("Graphics context error: " + e); }
	} // end of paintScreen()
	
	public void setAlpha(){
		_alpha = 0;
	}
	
} // end of RenderPanel class

