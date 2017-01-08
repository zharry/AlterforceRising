import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public abstract class GameObject {

	int x, y, velX, velY;
	int type;
	double rotateDegs;
	BufferedImage sprite;
	double rotateLocX, rotateLocY;
	
	/* Collision Box, in the GameObject constructor
	 * a. If no colBox is passed, the object will have a ineffective Rectangle
	 * b. If there is colBox passed, the object will use the Rect's coordinates
	 *    relative to it's current x and y
	 *    In this case as well, the Rect's height and width will be passed and used
	 *    as is.
	*/
	Rectangle colBox;

	public GameObject(int x, int y, int type, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.sprite = img;
		this.rotateLocX = img.getWidth(null) / 2;
		this.rotateLocY = img.getHeight(null) / 2;
		this.colBox = new Rectangle(-10, -10, 1, 1);
	}
	
	public GameObject(int x, int y, int type, BufferedImage img, Rectangle colBox) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.sprite = img;
		this.rotateLocX = img.getWidth(null) / 2;
		this.rotateLocY = img.getHeight(null) / 2;
		this.colBox = colBox;
		this.colBox.x += x;
		this.colBox.y += y;
	}

	public abstract void tick();

	public abstract void render(Graphics g);
	
	// Overload clamp to allow for double and int arguments
	public double clamp(double i, double min, double max) {
		return i < min ? min : i > max ? max : i;
	}

	public int clamp(int i, int min, int max) {
		return i < min ? min : i > max ? max : i;
	}

}
