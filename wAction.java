

public class wAction {
	private String origin ="";
	private String action ="";
	private String target ="";
	private String svar ="";
	private int ivar = 0;
	private boolean tfvar = true;
	
	public wAction(String oID){
		origin = oID;
		//default constructor
	}
	
	public wAction(String oID, String a, String tID){
		origin = oID;
		action = a;
		target = tID;
	}
	
	public void setStringVar(String s){
		svar = s;
	}
	
	public void setIntVar(int i){
		ivar = i;
	}
	
	public void setBoolean(boolean tf){
		tfvar = tf;
	}
	
	public String getString(){
		return svar;
	}
	
	public int getInt(){
		return ivar;
	}
	
	public boolean getBoolean(){
		return tfvar;
	}
	
	public void setAction(String a){
		action = a;
	}
	
	public void setTarget(String t){
		target = t;
	}
	
	public String getOrigin(){
		return origin;
	}
	
	public String getAction(){
		return action;
	}
	
	public String getTartget(){
		return target;
	}
	
}
