

import java.util.Enumeration;
import java.util.Vector;

public class Screen {
	
	private Vector objects = new Vector();
	private boolean visible = false;
	private String sID="";
	private boolean _ready=false;
	
	public Screen(String id){
		sID = id;
		//do nothing
	}
	public Screen(String id, Vector objectV){
		sID = id;
		objects = objectV;
	}
	
	public String getID(){
		return sID;
	}
	
	public void add(Widget wtemp){
		objects.addElement(wtemp);
	}

	
	public void setVisible(boolean n){
		this.visible = n;
		Enumeration e = objects.elements();
		while(e.hasMoreElements()){
			Widget tempW = (Widget) e.nextElement();
			tempW.setVisible(n);
		}

	}
	
	public boolean isVisible(){
		return this.visible;
	}
	
	public Enumeration getObjects(){
		Enumeration e = objects.elements();
		return e;
	}


	
	public void clearObjects(){
		objects.clear();
	}

	public void clearAll(){
		objects.clear();
	}
	public boolean ready() {
		return _ready;
	}
	public void setReady(boolean b) {
		_ready = b;
		
	}
}
