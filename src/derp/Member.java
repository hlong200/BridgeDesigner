package derp;

import org.newdawn.slick.geom.Line;

public class Member extends Line {
	private Node n1;
	private Node n2;
	
    public Member(final float x, final float y, final float xx, final float yy) {
        super(x, y, xx, yy);
    }
    
    public double getAngle() {
    	double distX = this.getEnd().getX() - this.getStart().getX();
    	System.out.println(distX);
    	double distY = this.getEnd().getY() - this.getStart().getY();
    	System.out.println(distY);
    	double angle;
    	
    	if(distY == 0) {
    		angle = 0.0;
    		
    	} else {
    		angle = Math.tan(distX / distY);
    		
    	}
    	
    	return angle;
    	
    }
    
    public Node getN1() {
    	return n1;
    	
    }
    
    public Node getN2() {
    	return n2;
    	
    }
    
    public void setN1(Node n1) {
    	this.n1 = n1;
    	
    }
    
    public void setN2(Node n2) {
    	this.n2 = n2;
    	
    }
    
}
