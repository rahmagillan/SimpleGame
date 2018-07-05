import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
	//fields
	private int playerx,playery;
	private boolean dead = false;
	private boolean on_log = false; //monitor if player is on log
	private boolean done_round = false;
	
	Obstacle currLog = new Obstacle("",0,0,0,0,0,0); //temporary obstacle used to keep teack of the current log user is on
	
	Rectangle playerRect;
	
	Image[] playerSprites = new Image[4]; //list of player sprites
	Image[] deadSprites = new Image[10];
	
	int spriteCounter = 0;
	int interval = 0; //counter to slow down sprite
	int currSprite = 0;
	
	//constructor
	public Player(int x,int y) {
		playerx = x;
		playery = y;
	
		//load images
		for (int i = 0; i < playerSprites.length; i++) {
			try {
				Image img = ImageIO.read(new File("dragonspritesblue"+i+".png"));
				playerSprites[i] = img;
			} catch (IOException e) {
			}
		}
		for (int i = 0; i < deadSprites.length; i++) {
			try {
				Image img = ImageIO.read(new File("death_sprites/death"+i+".png"));
				deadSprites[i] = img;
			}catch(IOException e) {
			}
		}
	}
	
	//getters
	public int get_x() {
		return playerx;
	}
	public int get_y() {
		return playery;
	}
	public boolean get_on_log() {
		return on_log;
	}
	public Image[] getDeadSprites() {
		return deadSprites;
	}
	
	//setters
	public void set_dead(boolean b) {
		dead = b;
	}

	//methods
	//methods move player according to user keeping it on screen
	public void moveUp() {
		if (playery-50 >= 0) {
			playery -= 50;
		}
	}
	public void moveDown() {
		if (playery+50 <= 750) {
			playery += 50;
		}
	}
	public void moveRight() {
		if (playerx+50 <= 950) {
			playerx += 50;
		}
	}
	public void moveLeft() {
		if (playerx-50 >= 0) {
			playerx -= 50;
		}
	}
	
	public void move(int vx, int interval, int counter) {
		if (counter%interval == 0){ //move player at the same interval as the obstacle by using its counter
			if (playerx > 1000){
				dead = true;
			}
			else if (playerx < 0-50) {
				dead = true;
			}
			playerx += vx;
		}
	}
	
	//method checks if the player collides with obstacle and takes action
	public void collide(Obstacle obstacle) {
		playerRect = new Rectangle(playerx+1,playery+1,48,48); //add/subtract 1 from each dimension to prevent premature collision
		if (obstacle.getType() == "log") {
			if (playerRect.intersects(obstacle.getRect())) {//check if playerRect intersects obstacle rectangle
				currLog = obstacle;
				on_log = true;
				this.move(obstacle.get_vx(),obstacle.getInterval(),obstacle.getCounter());
			}
			else {
				if (playerRect.intersects(currLog.getRect())==false) {
					on_log = false;
				}
			}
		}
		else if (obstacle.getType() == "car") {
			if (playerRect.intersects(obstacle.getRect())) {
				dead = true;
			}
		}
		else if (obstacle.getType() == "smoke") {
			if (playerRect.intersects(obstacle.getRect())) {
				dead = true;
			}
		}
	}
	
	//method checks for collision with scenery 
	public void collide(Scene scene) {
		playerRect = new Rectangle(playerx+1,playery+1,48,48);
		if (scene.getType() == "fire") {
			if (playerRect.intersects(scene.getRect())) {
				if (on_log == false) { //only if player is not on the log
					dead = true;
				}
			}
		}
	}
	
	//method checks which gem spot is landed on
	public void collide(Gem gem) {
		playerRect = new Rectangle(playerx+1,playery+1,48,48);
		//get all locations of gems
		int[] locations = gem.getLocations();
		
		Rectangle[] gemRects = new Rectangle[locations.length];
		//create rectangles using locations of each gem 
		for (int i = 0; i < gemRects.length; i++) {
			gemRects[i] = new Rectangle(locations[i],0,50,50);
		}
		
		//check if player collides with any of the rectangles
		for (int i = 0; i < gemRects.length; i++) {
			if (playerRect.intersects(gemRects[i])) {
				gem.setGem(true, i);
				done_round = true;
			}
		}
	}
	
	//method checks if player collides with token and returns true if it does
	public boolean collide(Token token,GamePanel curr) {
		playerRect = new Rectangle(playerx+1,playery+1,48,48);
		if (playerRect.intersects(token.getRect())) {
			curr.setScore(200); //add 200 to score when egg is collected
			return true;
		}
		return false;
	}
	
	//method checks if player is dead
	public boolean dead(GamePanel curr) {
		if (dead) {
			return true;
		}
		else if(curr.getTimeLimit().getTime() <= 0) {
			return true;
		}
		return false;
	}
	
	//method used to respawn player
	public boolean respawn() {
		if(done_round) {
			return true;
		}
		return false;
	}
	
	//method draws the player
	public void draw(Graphics g) {
		
			if (interval%3 == 0) {
			if (spriteCounter%3==0) {
				g.drawImage(playerSprites[3],playerx,playery,null);//adjustments for image placements
				currSprite = 3;
			}
			else if (spriteCounter%2==0) {
				g.drawImage(playerSprites[2],playerx,playery,null);//adjustments for image placements
				currSprite = 2;
			}
			else if (spriteCounter%1==0) {
				g.drawImage(playerSprites[1],playerx,playery,null);//adjustments for image placements
				currSprite = 1;
			}
			else {
				g.drawImage(playerSprites[0],playerx,playery,null);//adjustments for image placements
				currSprite = 0;
			}
		
			spriteCounter++;
		}
		else {
			g.drawImage(playerSprites[currSprite],playerx,playery,null);//adjustments for image placements
		}
		
		interval++;
	}
	
	public void drawDeath(Graphics g, GamePanel curr) {
		g.drawImage(deadSprites[curr.getSpriteCounter()], playerx, playery, null);
	}
}
