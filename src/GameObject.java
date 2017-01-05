import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameObject {

	int x, y, velX, velY;
	int type;
	double rotateDegs;
	BufferedImage sprite;
	double rotateLocX, rotateLocY;

	public GameObject(int x, int y, int type, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.sprite = img;
		this.rotateLocX = img.getWidth(null) / 2;
		this.rotateLocY = img.getHeight(null) / 2;
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
