import java.awt.*;
//import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Obstacle {
	//fields
	String type;
	private int x,y,width,height,vx,interval;
	private int counter = 0;
	
	Image pic;
	
	//constructor
	public Obstacle(String type, int x,int y,int width,int height,int vx,int interval){
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.vx = vx;
		this.interval = interval;
		
		//load pic
		if(type.equals("log")) {
			try {
				pic = ImageIO.read(new File("steel.png"));
				pic = pic.getScaledInstance(width, height, Image.SCALE_DEFAULT); //resize image
			} catch (IOException e) {
			}
		}
		else if(type.equals("car")) {
			//check orientation of image
			if (vx>=0) {
				try {
					pic = ImageIO.read(new File("rocket.png"));
					pic = pic.getScaledInstance(width, height, Image.SCALE_DEFAULT);
				} catch (IOException e) {
				}
			}
			else {
				try {
					pic = ImageIO.read(new File("rocketflipped.png"));
					pic = pic.getScaledInstance(width, height, Image.SCALE_DEFAULT);
				} catch (IOException e) {
				}
			}
		}
		else if(type.equals("smoke")) {
			try {
				pic = ImageIO.read(new File("smoke.png"));
				pic = pic.getScaledInstance(width, height, Image.SCALE_DEFAULT); //resize image
			} catch (IOException e) {
			}
		}
		
	}
	//getters
	public String getType() {
		return type;
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,width,height);
	}
	public int get_vx() {
		return vx;
	}
	public int getInterval() {
		return interval;
	}
	public int getCounter() {
		return counter;
	}
	
	//methods
	//method calls proper move method
	public void move() {
		if (type.equals("log")) {
			move_log();
		}
		else if (type.equals("car")){
			move_car();
		}
	}
	
	public void move_log(){
		
		if (counter%interval == 0) { //only move based on interval
			x += vx;
			//keep on putting obstacle back on screen
			if (vx >= 0) { //check direction log is traveling
				if (x >= 1000+width){
					x = 0-width;  
				}
			}
			else if (vx < 0) {	
				if (x <= 0-width) {
					x = 1000+width;
				}
			}
		
		}		
		counter += 1;
	}
		
	private void move_car() {
		if (counter%interval == 0) { //only move based on interval
			x += vx;
			//keep on putting obstacle back on screen
			if (vx >= 0) { //check direction car is traveling
				if (x >= 1000+width){
					x = 0-width;  
				}
			}
			else if (vx < 0) {	
				if (x <= 0-width) {
					x = 1000+width;
				}
			}
		
		}		
		counter += 1;
	}
	
	public void draw(Graphics g){
		if (type.equals("log")) {
			g.drawImage(pic,x,y,null);
		}
		else if (type.equals("car")) {
			g.drawImage(pic,x,y,null);
		}
		else if (type.equals("smoke")) {
			g.drawImage(pic,x,y,null);
		}
	}
}