import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Scene {
	//fields
	String type;
	int x,y,width,height;

	Image img;
	
	//constructor
	public Scene(String t, int x, int y, int w, int h) {
		type = t;
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		
		//load pictures/gif and resize
		try {
			if (type.equals("grass")) { 
				img = (ImageIO.read(new File("grass.jpg")).getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}		
			else if (type.equals("rocks")) {
				img = (ImageIO.read(new File("rocks.jpg")).getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			else if (type.equals("fire")) {
				img = (ImageIO.read(new File("fire.gif")).getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			
		} catch (IOException e) {
		}					
	}
	
	//getters
	public Rectangle getRect() {
		return new Rectangle(x,y,width,height);
	}
	public String getType() {
		return type;
	}
	
	//methods
	public void draw(Graphics g) {
		g.drawImage(img,x,y,null);
	}
}
