

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;

import javax.imageio.ImageIO;

//default Widget template

public class Widget {
	
	private int wX = 0; // x location of the widget
	private int wY = 0; // y location of the widget
	private int wbX = 0; // x boundary of the widget
	private int wbY = 0; // y boundary of the widget
	protected Vector imgs = new Vector();
	protected Vector imgOver = new Vector();
	protected Vector imgClick = new Vector();
	private int frameNumber = 0;
	protected long beforeTime;
	protected long period;
	protected long currentTime;
	private wAction action;
	protected int w_state = 0; //0, normal: 1, mouse over
	private boolean pressed = false;
	protected String hint="";
	private boolean visible = true;
	private String ID="";
	
	public Widget(String wID,String imageLink, int X, int Y, int bX, int bY, String cmd){
		ID=wID;
		period = 500000000;
		wX = X;
		wY = Y;
		wbX = bX;
		wbY = bY;
		addImage(imageLink);
		action = new wAction(wID);
		action.setAction(cmd);
	}
	
	public void addHint(String hints){
		hint=hints;
	}
	
	public String getHint(){
		return hint;
	}
	
	public void setImage(String imageLink){
		imgs.clear();
		addImage(imageLink);
	}
	
	public void addImage_mouseClick(String imageLink){
		BufferedImage img = null;
		try {
			img = (BufferedImage) ImageIO.read(new File(imageLink));
		} catch (IOException e) {
		}
		imgClick.addElement(img);
		
	}

	public void addImage_mouseOver(String imageLink){
		BufferedImage img = null;
		try {
			img = (BufferedImage) ImageIO.read(new File(imageLink));
		} catch (IOException e) {
		}
		imgOver.addElement(img);
		
	}
	
	public void addImage(String imageLink){
		BufferedImage img = null;
		try {
		    img = (BufferedImage) ImageIO.read(new File(imageLink));
		} catch (IOException e) {
		}
		imgs.addElement(img);
		
	}
	
	public boolean isOver(MouseEvent e){
		//check if the widget has been clicked. by checking the location and boundaries
		int bbX = wX + wbX;
		int bby = wY + wbY;
		if(e.getX()>wX && e.getX()<bbX){
			if(e.getY()>wY && e.getY()<bby){
				return true;
			}
		}
		return false;
	}
	
	
	public void setX(int x){
		//sets X location
		wX = x;
	}
	
	public void setY(int y){
		//sets Y location
		wY = y;
	}
	
	public void setOver(){
		w_state = 1;
	}
	
	public void setNotOver(){
		w_state = 0;
	}
	
	public void setPress(boolean n){
		pressed = n;
	}
	
	public void setVisible(boolean n){
		visible = n;
	}
	
	public boolean isID(String wID){
		if(ID.compareToIgnoreCase(wID)!=0){
			return false;
		}else{
			return true;
		}
	}
	
	public void setAction(String a){
		action.setAction(a);
	}
	
	public void setTarget(String t){
		action.setTarget(t);
	}
	
	public void setActionString(String a){
		action.setStringVar(a);
	}
	public void setActionInt(int i){
		action.setIntVar(i);
	}
	public void setActionBoolean(boolean n){
		action.setBoolean(n);
	}
	
	public String getID(){
		return ID;
	}
	
	public boolean getVisible(){
		return visible;
	}
	
	public int getX(){ //get x location
		return wX;
	}
	
	public int getY(){ //get y location
		return wY;
	}
	
	public int getbX(){ // get x boundary
		return wbX;
	}
	
	public int getbY(){ // get y boundary
		return wbY;
	}
	
	public void setby(int i){
		wbY = i;
	}
	
	public void setbx(int i){
		wbX = i;
	}
	
	public Image getImage(){
		currentTime = System.nanoTime();
		if(w_state==1){
			
			BufferedImage temp= null;
			try{
				if(pressed){
					temp = (BufferedImage) imgClick.elementAt(0);
				}else{
					temp = (BufferedImage) imgOver.elementAt(0);
				}
			}catch(Exception e){
				//catch the exception
			}
			if(temp==null){
					BufferedImage tempFillIn = (BufferedImage) imgs.elementAt(0);
					return tempFillIn;
				}
			return temp;
			
		}else{
			if(currentTime - beforeTime >= period){
				beforeTime = currentTime;
				frameNumber++;
				if(frameNumber>=imgs.size()){
					frameNumber = 0;
				}
			}
			
			BufferedImage temp = (BufferedImage) imgs.elementAt(frameNumber);
			return temp;
		}
	}
	
	
	public wAction executeWidget(){
		return action;
	}
}
