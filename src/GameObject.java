import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject {

	int x, y, velX, velY;
	int type;
	double rotateDegs;
	BufferedImage[] sprite;

	/*
	 * Collision Box, in the GameObject constructor a. If no colBox is passed,
	 * the object will have a ineffective Rectangle b. If there is colBox
	 * passed, the object will use the Rect's coordinates relative to it's
	 * current x and y In this case as well, the Rect's height and width will be
	 * passed and used as is.
	 */
	Rectangle colBox;
	int colBoxOffsetX, colBoxOffsetY;

	public GameObject(int x, int y, int type, BufferedImage[] sprite) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.colBox = new Rectangle(-10, -10, 1, 1);
		this.colBoxOffsetX = colBox.x - x;
		this.colBoxOffsetY = colBox.y - y;
		this.sprite = sprite;
	}

	public GameObject(int x, int y, int type, BufferedImage[] sprite, Rectangle colBox) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.colBox = colBox;
		this.colBox.x += x;
		this.colBox.y += y;
		this.colBoxOffsetX = colBox.x - x;
		this.colBoxOffsetY = colBox.y - y;
		this.sprite = sprite;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract void tryDespawn();

	public void move() {
		move(true);
	}

	/**
	 * @param clamp
	 *            If True, move GameObject without clamping
	 */
	public void move(boolean clamp) {
		this.x += this.velX;
		this.y += this.velY;
		if (clamp) {
			// TO-DO CHANGE CLAMPING TO USE COLLISION BOX INSTEAD OF SPRITE BOX
			this.x = clamp(this.x, 0, Game.panelWidth - this.sprite[0].getWidth());
			this.y = clamp(this.y, 0, Game.panelHeight - this.sprite[0].getHeight());
		}
		moveColBox();
	}

	public void moveColBox() {
		this.colBox.x = this.x + this.colBoxOffsetX;
		this.colBox.y = this.y + this.colBoxOffsetY;
		// Ensure that the player's Collision box moves during TP
		if (this.type == Game.TYPE_PLAYER) {
			Player player = (Player) this;
			if (player.goTp) {
				player.colBox.x = (int) (player.tpXi + player.tpDX * player.tpStep);
				player.colBox.y = (int) (player.tpYi + player.tpDY * player.tpStep);
			}
		}
	}

	// Overload clamp to allow for double and int arguments
	public double clamp(double i, double min, double max) {
		return i < min ? min : i > max ? max : i;
	}

	public int clamp(int i, int min, int max) {
		return i < min ? min : i > max ? max : i;
	}

}
