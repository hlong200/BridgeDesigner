package derp;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.GameContainer;

import java.util.ArrayList;
import org.newdawn.slick.BasicGame;

public class Main extends BasicGame
{
    static final int WIDTH = 1360;
    static final int HEIGHT = 768;
    Tool currentTool;
    boolean toolActive;
    boolean showFPS;
    int screenState;
    ArrayList<Member> members;
    ArrayList<Node> nodes;
    ArrayList<GridLine> gridLines;
    Node rolling;
    Node fixed;
    GridLine xGrid;
    GridLine yGrid;
    Circle mouseCircle;
    float x;
    float y;
    float xx;
    float yy;
    Button nodeButton;
    Button memberButton;
    
    public Main(final String title) {
        super(title);
        currentTool = Tool.NONE;
        toolActive = false;
        showFPS = false;
        screenState = 0;
        members = new ArrayList<Member>();
        nodes = new ArrayList<Node>();
        gridLines = new ArrayList<GridLine>();
        xGrid = null;
        yGrid = null;
        mouseCircle = new Circle(0, 0, 5.0f);
        nodeButton = new Button(WIDTH - 128, 0, "Node Tool", Color.red);
        memberButton = new Button(WIDTH - 128, 20, "Member Tool", Color.red);
        
    }
    
    @Override
    public void init(final GameContainer gc) {
        for (int i = 0; i < 1360; ++i) {
            if (i % 25 == 0) {
                gridLines.add(new GridLine(i, 0.0f, i, 768.0f, LineType.GRID_X));
            }
        }
        for (int i = 0; i < 768; ++i) {
            if (i % 25 == 0) {
                gridLines.add(new GridLine(0.0f, i, 1360.0f, i, LineType.GRID_Y));
            }
        }
    }
    
    public double getDistance(final float x1, final float y1, final float x2, final float y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
    }
    
    public double getDistance(final Shape s1, final Shape s2) {
        return Math.sqrt(Math.pow(s2.getCenterX() - s1.getCenterX(), 2.0) + Math.pow(s2.getCenterY() - s1.getCenterY(), 2.0));
    }
    
    public void updateTools(final GameContainer gc, final int delta) {
        final Input in = gc.getInput();
        xGrid = null;
        yGrid = null;
        if (in.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            System.out.println("Click");
            if(mouseCircle.intersects(nodeButton)) {
            	this.currentTool = Tool.NODE;
            	
            } else if(mouseCircle.intersects(memberButton)) {
            	this.currentTool = Tool.MEMBER;
            	
            } else {
            	switch (currentTool) {
                case MEMBER: {
                	memberButton.setDepressed(true);
                	nodeButton.setDepressed(false);
                	Member m = new Member(0, 0, 0, 0);
                    if (toolActive) {
                        toolActive = false;
                        for (Node n : nodes) {
                            if (getDistance(n.getCenterX(), n.getCenterY(), in.getMouseX(), in.getMouseY()) <= 5.0) {
                                xx = n.getCenterX();
                                yy = n.getCenterY();
                                m.setN2(n);
                                n.connectMember(m);
                            }
                        }
                        m.set(x, y, xx, yy);
                        System.out.println(m.getAngle());
                        System.out.println(Math.toDegrees(m.getAngle()));
                        break;
                    } else {
                    	for (Node n : nodes) {
                            if (getDistance(n.getCenterX(), n.getCenterY(), in.getMouseX(), in.getMouseY()) <= 5.0) {
                                toolActive = true;
                                x = n.getCenterX();
                                y = n.getCenterY();
                                m.setN1(n);
                                n.connectMember(m);
                            }
                        }
                    	
                    }
                    break;
                }
                case FIXED_NODE: {
                    for (Node n : nodes) {
                        if (getDistance(n.getCenterX(), n.getCenterY(), in.getMouseX(), in.getMouseY()) <= 5.0) {
                            for (final Node nn : nodes) {
                                if (nn.getType() == 1) {
                                    nn.setType(0);
                                }
                            }
                            n.setType(1);
                            fixed = n;
                        }
                    }
                    break;
                }
                case NODE: {
                	memberButton.setDepressed(false);
                	nodeButton.setDepressed(true);
                    for (GridLine gl : gridLines) {
                        if (gl.type == LineType.GRID_X) {
                            if (getDistance(gl.getCenterX(), in.getMouseY(), in.getMouseX(), in.getMouseY()) > 5.0) {
                                continue;
                            }
                            xGrid = gl;
                        } else {
                            if (gl.type != LineType.GRID_Y || getDistance(in.getMouseX(), gl.getCenterY(), in.getMouseX(), in.getMouseY()) > 5.0) {
                                continue;
                            }
                            yGrid = gl;
                        }
                    }
                    if (xGrid != null) {
                        x = xGrid.getCenterX();
                    } else {
                        x = in.getMouseX();
                    }
                    if (yGrid != null) {
                        y = yGrid.getCenterY();
                    } else {
                        y = in.getMouseY();
                    }
                    nodes.add(new Node(x, y, 3.0f));
                }
                case ROLLING_NODE: {
                    for (final Node n : nodes) {
                        if (getDistance(n.getCenterX(), n.getCenterY(), in.getMouseX(), in.getMouseY()) <= 5.0) {
                            for (final Node nn : nodes) {
                                if (nn.getType() == 2) {
                                    nn.setType(0);
                                }
                            }
                            n.setType(2);
                            rolling = n;
                        }
                    }
                    break;
                }
			default:
				break;
            }
            	
            }
            System.out.println(toolActive);
        }
        if (in.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            Node todoNode = null;
            Member todoMember = null;
            System.out.println("Mouse x: " + mouseCircle.getCenterX() + " -- y: " + mouseCircle.getCenterY());
            switch (currentTool) {
                case NODE:
                    for (final Node n2 : nodes) {
                        if (getDistance(n2.getCenterX(), n2.getCenterY(), in.getMouseX(), in.getMouseY()) <= 7.0) {
                            todoNode = n2;
                            System.out.println("Connected members: " + n2.getConnectedMembers().size());
                            n2.getConnectedMembers().clear();
                        }
                    }
                    break;
                case MEMBER:
                	for(Member m : members) {
                		if(mouseCircle.intersects(m)) {
                			todoMember = m;
                		}
                	}
			default:
				break;
            }
            if (todoNode != null) {
                nodes.remove(todoNode);
            }
            if(todoMember != null) {
            	members.remove(todoMember);
            	
            }
        }
        if (in.isKeyPressed(Input.KEY_F1)) {
            currentTool = Tool.NODE;
        } else if (in.isKeyPressed(Input.KEY_F2)) {
            currentTool = Tool.MEMBER;
            toolActive = false;
        } else if (in.isKeyPressed(Input.KEY_F3)) {
            currentTool = Tool.FIXED_NODE;
        } else if (in.isKeyPressed(Input.KEY_F4)) {
            currentTool = Tool.ROLLING_NODE;
        }
        
        if(in.isKeyPressed(Input.KEY_F12)) {
        	showFPS = !showFPS;
        	gc.setShowFPS(showFPS);
        	
        }
    }
    
    @Override
    public void update(final GameContainer gc, final int delta) {
    	mouseCircle.setCenterX(gc.getInput().getMouseX());
    	mouseCircle.setCenterY(gc.getInput().getMouseY());
        updateTools(gc, delta);
    }
    
    @Override
    public void render(final GameContainer gc, final Graphics g) {
        g.setBackground(Color.white);
        g.setColor(new Color(128, 0, 255));
        g.setLineWidth(1.0f);
        for (final GridLine gl : gridLines) {
            g.draw(gl);
        }
        g.setColor(Color.red);
        g.drawString("Current Tool: " + currentTool.getValue(), 0.0f, 0.0f);
        g.setColor(Color.black);
        if(showFPS) {
        	g.fillRect(8, 12, 80, 16);
        	
        }
        if(nodeButton.getDepressed() == true) {
        	g.setColor(Color.gray);
        	
        } else {
        	g.setColor(nodeButton.getColor());
        	
        }
        g.fillRect(nodeButton.getX(), nodeButton.getY(), nodeButton.getWidth(), nodeButton.getHeight());
        if(memberButton.getDepressed() == true) {
        	g.setColor(Color.gray);
        	
        } else {
        	g.setColor(memberButton.getColor());
        	
        }
        g.fillRect(memberButton.getX(), memberButton.getY(), memberButton.getWidth(), memberButton.getHeight());
        g.setColor(Color.white);
        g.drawString(nodeButton.getText(), nodeButton.getX(), nodeButton.getY());
        g.drawString(memberButton.getText(), memberButton.getX(), memberButton.getY());
        g.setColor(Color.blue);
        g.draw(mouseCircle);
        if (toolActive) {
            g.drawLine(x, y, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        }
        g.setLineWidth(2.0f);
        /*for (Member m : members) {
            g.draw(m);
        }*/
        for (Node n : nodes) {
            if (n.getType() == 0) {
                g.setColor(Color.red);
            } else if (n.getType() == 1) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.magenta);
            }
            g.draw(n);
            for(Member m : n.getConnectedMembers()) {
            	g.draw(m);
            	
            }
            g.drawString( "(" + n.getCenterX() + ", " + n.getCenterY() + ")", n.getCenterX(), n.getCenterY());
        }
    }
    
    public static void main(final String[] args) {
        try {
            final AppGameContainer appgc = new AppGameContainer(new Main("Bridge Designer"));
            appgc.setDisplayMode(1360, 768, false);
            appgc.setShowFPS(true);
            appgc.start();
        }
        catch (SlickException ex) {}
    }
}
