// 
// Decompiled by Procyon v0.5.30
// 

package derp;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class Button extends Rectangle
{
    private String text;
    private boolean depressed;
    private Color color;
    
    public Button(final float x, final float y, final String text, Color color) {
        super(x, y, text.length() * 10, 16);
        this.text = text;
        depressed = false;
        this.color = color;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String str) {
        this.text = str;
    }
    
    public boolean getDepressed() {
    	return depressed;
    	
    }
    
    public void setDepressed(boolean depressed) {
    	this.depressed = depressed;
    	
    }
    
    public Color getColor() {
    	return color;
    	
    }
    
    public void setColor(Color color) {
    	this.color = color;
    	
    }
    
}
