import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

	boolean goUp = false, goDown = false, goLeft = false, goRight = false, goTp = false;
	int tpX, tpY, tpXi, tpYi, tpStep, tpNumFrames;

	final int MOVEDIST = 3, TPDIST = 32;

	public Player(int x, int y, int type, BufferedImage img) {
		super(x, y, type, img);
	}

	@Override
	public void tick() {
		if (this.goTp) {
			for (int i = 0; i < this.TPDIST; i++) {
				double dx = (this.tpX - this.tpXi) / ((double) this.tpNumFrames);
				double dy = (this.tpY - this.tpYi) / ((double) this.tpNumFrames);
				if (this.tpStep <= this.tpNumFrames) {
					this.tpStep++;
					setX((int) (this.tpXi + dx * this.tpStep));
					setY((int) (this.tpYi + dy * this.tpStep));
				} else {
					this.goTp = false;
					break;
				}
			}
		}
		this.setVelX(0);
		this.setVelY(0);
		if (this.goUp)
			setVelY(-1);
		if (this.goDown)
			setVelY(getVelY() + 1);
		if (this.goLeft)
			setVelX(-1);
		if (this.goRight)
			setVelX(getVelX() + 1);

		this.x += this.MOVEDIST * this.velX;
		this.y += this.MOVEDIST * this.velY;

		this.x = clamp(this.x, 0, Game.WIDTH - 38);
		this.y = clamp(this.y, 0, Game.HEIGHT - 60);
	}

	@Override
	public void render(Graphics g) {
		AffineTransformOp op = new AffineTransformOp(
				AffineTransform.getRotateInstance(Math.toRadians(this.rotateDegs), this.rotateLocX, this.rotateLocY),
				AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(this.sprite, null), this.x, this.y, null);
	}

	public void setTp(int x, int y) {
		this.goTp = true;
		this.tpX = x;
		this.tpY = y;
		this.tpXi = this.x;
		this.tpYi = this.y;
		this.tpStep = 1;
		this.tpNumFrames = (int) (Math.sqrt((this.tpX - this.tpXi) * (this.tpX - this.tpXi) + (this.tpY) * (this.tpYi))
				/ 1);
	}

}
