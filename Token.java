import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Token {
	//fields
	int x,y;
	Image pic;
	Random rand = new Random();
	
	//constructor
	public Token() {
		
		x = (rand.nextInt(21))*50; //random number from 1 to 1000 in increments of 50
		y = (rand.nextInt(17))*50; //random number from 1 to 800 in increments of 50
		
		//load image
		try {
			pic = (ImageIO.read(new File("egg.png")).getScaledInstance(50,50,Image.SCALE_DEFAULT));
		} catch (IOException e) {
		}
	}
	
	//getter
	public Rectangle getRect(){
		return new Rectangle(x,y,50,50);
	}
	
	//method
	public void draw(Graphics g) {
		g.drawImage(pic, x, y, null);
	}
	
}
