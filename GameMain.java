import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class GameMain extends JFrame implements ActionListener{
	
	GamePanel game = new GamePanel();
	MenuPanel menu = new MenuPanel();
		
	Timer myTimer;
	int counter = 0;

	public GameMain() {
		super("Dragon Quest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	
		menu.setPreferredSize(new Dimension(1000,800));
		game.setPreferredSize(new Dimension(1000,800));
		
		game.setScenes();
		game.setObstacles();
		game.setTokens(game.getLevel()+2);
		
		getContentPane().add(menu);
		
		pack();
		
		setVisible(true);
		
		while (true) {
			System.out.print("");
			if (menu.menuButtonPressed().equals("start")) {
				getContentPane().remove(menu);
				getContentPane().add(game);
				break;
			}
			if (menu.menuButtonPressed().equals("quit")) {
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); //end the program 
			}
		}
		
		//refresh frame
		setVisible(true);
		
					
		myTimer = new Timer(100,this); //every 5 times is half a second
		myTimer.start();
				
	}
	
	public void actionPerformed(ActionEvent evt) {	 		
		game.move();
		game.repaint();
		game.gameOver();
		game.getUser().collide(game.getFire());
		//change timer every 5 loops
		if (counter%10 == 0) {
			game.getTimeLimit().setTime(game.getTimeLimit().getTime()-1); //subtract 1 from timer 
		}
		game.setDisplayScore();
		counter++;
	}
	
	public static void main(String[] arguments) {
		GameMain frame = new GameMain();		
	}
}

class GamePanel extends JPanel implements KeyListener{
	
	//fields
	private boolean[] keys;
	Player user = new Player(950,0); //players starting position
	
	Image heart,win_screen,lose_screen;
	
	ArrayList<Scene> scenes = new ArrayList<Scene>();
	ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	ArrayList<Token> tokens = new ArrayList<Token>();
	ArrayList<Integer> lives = new ArrayList<Integer>();
	Gem gems = new Gem(10);
	
	int level = 1;
	int spriteCounter = 0;
	int win = -1;	
	int score = 0;
	
	JLabel displayScore = new JLabel();

	//list of time limits according to level
	int[] levelTimes = {0,45,30};	
	TimeBar timeLimit = new TimeBar(levelTimes[level]);
	
	Scene fire;
	
	//constructor
	public GamePanel() {
		
		setLayout(null);
		
		keys = new boolean[KeyEvent.KEY_LAST+1];
		addKeyListener(this);
		
		//generate lives
		for(int i = 0; i < 3; i++) { //start with 3 lives
			lives.add(0);
		}
		try { //load pictures for lives
			heart = (ImageIO.read(new File("heart.png")).getScaledInstance(50,50,Image.SCALE_DEFAULT));
		} catch (IOException e) {
		}
		
		//score label
		displayScore.setLocation(650,750);
		displayScore.setSize(new Dimension(200,50));
		displayScore.setFont(new Font("Serif", Font.BOLD, 20));//set label font
		displayScore.setForeground(new Color(0,0,0));
		add(displayScore);
		
		//load images
		try {
			win_screen = ImageIO.read(new File("win.jpg"));
			lose_screen = ImageIO.read(new File("lose.png"));
		} catch (IOException e) {
		}
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()]=true;
	}
	public void keyReleased(KeyEvent e) {	
		keys[e.getKeyCode()]=false;
	}
	public void keyTyped(KeyEvent e) {
	}
	
	//getters
	public Player getUser() {
		return user;
	}
	public int getLevel() {
		return level;
	}
	public Scene getFire() {
		return fire;
	}
	public int getSpriteCounter() {
		return spriteCounter;
	}
	public TimeBar getTimeLimit() {
		return timeLimit;
	}
	public int getScore() {
		return score;
	}
	public JLabel getDisplayScore() {
		return displayScore;
	}
	public int[] getLevelTimes() {
		return levelTimes;
	}
	//setters
	public void setDisplayScore() {
		displayScore.setText("Score: "+score);
	}
	public void setScore(int numToAdd) {
		score += numToAdd;
	}
	public void setTokens(int numTokens) {
		for (int i = 0; i < numTokens; i++) {
			tokens.add(new Token());
		}
	}
	//setting different scenes
	public void setScenes() {
		scenes.add(new Scene("grass",0,0,1000,50));
		scenes.add(new Scene("grass",0,400,1000,50));
			
		scenes.add(new Scene("rocks",0,450,1000,300));
			
		scenes.add(new Scene("fire",0,50,1000,350));
		fire = new Scene("fire",0,50,1000,350); //store fire object
	}
	
	//setting list of obstacles
	public void setObstacles() {
		if (level == 1) {
			//add logs
			for (int i = 0; i < 7; i++) {
				if (i%2 == 0) {
					obstacles.add(new Obstacle("log",0+100*i,50+50*i,150,50,5,1));
					obstacles.add(new Obstacle("log",500+100*i,50+50*i,150,50,5,1));
				}
				else {
					obstacles.add(new Obstacle("log",0+100*i,50+50*i,150,50,-20,1));
					obstacles.add(new Obstacle("log",350+100*i,50+50*i,150,50,-20,1));
					obstacles.add(new Obstacle("log",650+100*i,50+50*i,150,50,-20,1));
				}
			}
			//add cars
			for (int i = 0; i < 6; i++) {
				if (i%2 == 0) {
					obstacles.add(new Obstacle("car",0+100*i,450+50*i,100,50,-5,1));
					obstacles.add(new Obstacle("car",500+100*i,450+50*i,100,50,-5,1));
				}
				else {
					obstacles.add(new Obstacle("car",0+100*i,450+50*i,50,50,5,1));
					obstacles.add(new Obstacle("car",333+100*i,450+50*i,50,50,5,1));
					obstacles.add(new Obstacle("car",667+100*i,450+50*i,50,50,5,1));
				}
			}
		}
		if (level == 2) {
			//add logs
			//get smaller in level 2
			for (int i = 0; i < 7; i++) {
				if (i%2 == 0) {
					obstacles.add(new Obstacle("log",0+100*i,50+50*i,100,50,10,1));
					obstacles.add(new Obstacle("log",500+100*i,50+50*i,150,50,10,1));
				}
				else {
					obstacles.add(new Obstacle("log",0+100*i,50+50*i,100,50,-25,1));
					obstacles.add(new Obstacle("log",350+100*i,50+50*i,50,50,-25,1));
					obstacles.add(new Obstacle("log",650+100*i,50+50*i,100,50,-25,1));
				}
			}
			//add cars
			//speed up in level 2
			for (int i = 0; i < 6; i++) {
				if (i%2 == 0) {
					obstacles.add(new Obstacle("car",0+100*i,450+50*i,100,50,-10,1));
					obstacles.add(new Obstacle("car",500+100*i,450+50*i,100,50,-10,1));
				}
				else {
					obstacles.add(new Obstacle("car",0+100*i,450+50*i,50,50,30,1));
					obstacles.add(new Obstacle("car",333+100*i,450+50*i,50,50,30,1));
					obstacles.add(new Obstacle("car",667+100*i,450+50*i,50,50,30,1));
				}
			}
		}
		//add smoke(spikes)
		for (int i = 0; i < 10; i++) {
			obstacles.add(new Obstacle("smoke",12+100*i,12,25,25,0,1));
		}	
	}

	public void move(){		
		requestFocus();
		if (keys[KeyEvent.VK_UP]){
			user.moveUp();
			score += 10; //every forward step adds 10 to score
		}
		else if (keys[KeyEvent.VK_DOWN]){
			user.moveDown();
			score -= 10; //to prevent up down spam 
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			user.moveRight();
		}
		else if (keys[KeyEvent.VK_LEFT]){
			user.moveLeft();
		}

		//draw and check collisions for all obstacles
		for (int i = 0; i < obstacles.size(); i++) {
			user.collide(obstacles.get(i)); //collision check
			obstacles.get(i).move();
		}
		//check collisions with tokens
		for(int i = 0; i < tokens.size(); i++) {
			if (user.collide(tokens.get(i), this)) {
				tokens.remove(i);
			}
		}
		//check collision with gem squares
		user.collide(gems);
	}
	
	//method checks if game is over
	public void gameOver() {
		
		if (user.dead(this)) { //let death animation run
			if(spriteCounter == 9) { //run through animation
				if (lives.size()>0) {
					lives.remove(0); //remove a life
					spriteCounter = 0; //reset counter
					
					timeLimit = new TimeBar(levelTimes[level]); //reset time
				}
				else {
					win = 0; //set win to 0 to indicate loss
				}
				user = new Player(500,750); //create new player at starting position
			}
			else {
				spriteCounter++;
			}
		}
		
		//check if player needs to respawn (when the player drops a diamond)
		if (user.respawn()) {
			//check if all diamonds are in place
			boolean done = false;
			for (int i = 0; i < gems.getGems().length; i++) {
				if (gems.getGems()[i] == true) { //boolean done determines if level/game is done
					done = true;
				}
				else {
					done = false;
				}
			}
			if (done) {
				//win game
				if (level == 2) {
					win = 1; //setting win = 1 indicated the game was won
				}
				level++;
				
				//clear lists and reset for level change
				obstacles = new ArrayList<Obstacle>();
				tokens = new ArrayList<Token>();
				gems = new Gem(10);
				timeLimit = new TimeBar(levelTimes[level]);
				user = new Player(500,750);
				score += 10000; //score + 10000 when level is complete
				
				this.setObstacles(); //reset obstacles to change them
				
			}
			else {
				score += 50; //add 50 when gem placed
				score += timeLimit.getTime()*5;//add 5 points for every second left
				
				timeLimit = new TimeBar(levelTimes[level]);
				user = new Player(500,750);
			}
		}
	}
	
	//drawing code
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//draw on top of everything
		if (win == 1) {
			g.drawImage(win_screen,0,0,null);
		}
		else if(win == 0) {
			g.drawImage(lose_screen,0,0,null);
		}
		//drawing during most of the game
		else {
			//drawing scenes
			for(int i = 0; i < scenes.size(); i++) {
				scenes.get(i).draw(g);
			}
			//drawing obstacles
			for (int i = 0; i < obstacles.size(); i++){
				obstacles.get(i).draw(g);
			}
			//drawing eggs
			for (int i = 0; i < tokens.size(); i++) {
				tokens.get(i).draw(g);
			}
			//drawing gems
			gems.draw(g);

			//drawing player according to state
			if (user.dead(this) == false) {
				user.draw(g);
			}
			else {
				user.drawDeath(g,this);
			}
			
			//draw timer
			timeLimit.draw(g);
			
			//draw lives
			for (int i = 0; i < lives.size(); i++) {
				g.drawImage(heart,950-i*50,750,null);
			}
		}
	}
}

//panel is the menu panel
class MenuPanel extends JPanel implements ActionListener{

	//fields
	JButton startButton = new JButton("Start");
	JButton quitButton = new JButton("Quit");
	
	boolean start = false;
	boolean quit = false;
	
	Image background;
	
	//constructor
	public MenuPanel() {
		
		setLayout(null); //line buttons top to bottom
		
		startButton.addActionListener(this);
		quitButton.addActionListener(this);
		
		startButton.setLocation(450,300);
		startButton.setSize(new Dimension(100,50));
		startButton.setFont(new Font("Serif",Font.BOLD,24));
		startButton.setBackground(new Color(0,0,0));
		startButton.setForeground(new Color(255,255,255));
		
		quitButton.setLocation(450,370);
		quitButton.setSize(new Dimension(100,50));
		quitButton.setFont(new Font("Serif",Font.BOLD,24));
		quitButton.setBackground(new Color(0,0,0));
		quitButton.setForeground(new Color(255,255,255));

		add(startButton);
		add(quitButton);
		
		//load image
		try {
			background = (ImageIO.read(new File("menu.png")));
		} catch (IOException e) {
		}
	}
	
	//methods
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == startButton) {
			start = true;
		}
		else if (source == quitButton) {
			quit = true;
		}
	}
	
	//method used to return a string to frame instructing it which panel to open
	public String menuButtonPressed() {
		if (start) {
			return "start";
		}
		else if (quit) {
			return "quit";
		}
		return "";
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background,0,0,null);
	}
}