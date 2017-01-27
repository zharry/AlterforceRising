import java.awt.Graphics;
import java.awt.Rectangle;

public class Potion extends GameObject {

	// Effects Variables
	double healUp = 0.25;
	double aSpeedUp = 0.05;
	double damageUp = 2;
	double speedUp = 4;
	int subtype;
	// Timer Variables
	double timeAlive = 300;

	public Potion(int x, int y, int type, int subtype) {
		super(x, y, type);

		this.subtype = subtype;
		if (subtype == Game.POTION_HEALTH) {
			this.sprite = Game.sprHealthPot;
			this.colBox = new Rectangle(0, 0, 32, 32);
		} else if (subtype == Game.POTION_SPEED) {
			this.sprite = Game.sprSpeedPot;
			this.colBox = new Rectangle(0, 0, 32, 32);
		} else if (subtype == Game.POTION_DAMAGE) {
			this.sprite = Game.sprDamagePot;
			this.colBox = new Rectangle(0, 0, 32, 32);
		} else if (subtype == Game.POTION_EXP) {
			this.sprite = Game.sprExpPot;
			this.colBox = new Rectangle(0, 0, 32, 32);
		}
		// Process the Collision box
		this.colBoxOffsetX = colBox.x;
		this.colBoxOffsetY = colBox.y;
		this.colBox.x += x;
		this.colBox.y += y;
	}

	@Override
	public void tick() {
		this.timeAlive--;

		tryDespawn();

		move();
	}

	@Override
	public void render(Graphics g) {

		g.drawImage(this.sprite[(int) Math.round(this.rotateDegs)], (int) Math.round(this.x), (int) Math.round(this.y),
				null);
	}

	public void tryDespawn() {
		if (this.timeAlive < 0)
			Game.gameController.toRemove.add(this);
	}
}
