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
	
	public void drawColBox(Graphics g) {
		// Add transparency and border width
		Graphics2D g2d = (Graphics2D) g;
		Stroke origStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(3));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		// Draw Collision Boxes
		g.setColor(Color.magenta);
		g.drawRect(this.colBox.x, this.colBox.y, this.colBox.width, this.colBox.height);
		// Reset Graphics to original
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setStroke(origStroke);
	}
	
	// Overload clamp to allow for double and int arguments
	public double clamp(double i, double min, double max) {
		return i < min ? min : i > max ? max : i;
	}

	public int clamp(int i, int min, int max) {
		return i < min ? min : i > max ? max : i;
	}

}
