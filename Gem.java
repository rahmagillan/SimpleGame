import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Gem {
	
	//fields
	private boolean[] gems;
	private int[] locations;

	Image pic;

	//constructor 
	public Gem(int num) {
		gems = new boolean[num];
		locations = new int[num];
		
		for (int i = 0; i < gems.length-1;i++) {
			gems[i] = true;
		}
		
		//set locations
		for (int i = 0; i < locations.length; i++) {
			locations[i] = 50+i*100; //50 + the first smoke
		}

		//load image of gem
		try {
			pic = ImageIO.read(new File("gem.png"));
			pic = pic.getScaledInstance(50,50,Image.SCALE_DEFAULT); //resize image
		} catch (IOException e) {
		}
	}
	
	//getters
	public int[] getLocations() {
		return locations;
	}
	public boolean[] getGems() {
		return gems;
	}
	
	//setters
	public void setGem(boolean b, int i) { //methods set specified location in boolean list to specified boolean
		gems[i] = b;
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < gems.length; i++) {
			if (gems[i] == true) {
				g.drawImage(pic,locations[i],0,null);
			}
		}
	}
}
