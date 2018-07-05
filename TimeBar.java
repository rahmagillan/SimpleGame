import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TimeBar {
	//fields
	int time;
	Image clock;
	
	//constructor
	public TimeBar(int t) {
		time = t; //time in half-seconds
		
		//load image
		try {
			clock = (ImageIO.read(new File("time.png")).getScaledInstance(25,25,Image.SCALE_DEFAULT));
		} catch (IOException e) {
		}
	}
	
	//getters
	public int getTime() {
		return time;
	}
	//setters
	public void setTime(int t) {
		time = t;
	}
	
	//method
	public void draw(Graphics g) {
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 762, time*4, 25);
		g.drawImage(clock,time*4,762,null);
	}
}
