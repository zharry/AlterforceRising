import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {

	boolean goUp = false, goDown = false, goLeft = false, goRight = false, goTp = false;
	int tpX, tpY, tpXi, tpYi, tpStep, tpNumFrames;

	final int MOVEDIST = 3, TPDIST = 32;

	public Player(int x, int y, int type) {
		super(x, y, type);
	}

	@Override
	public void tick() {
		if (goTp) {
			double dx = (tpX - tpXi) / ((double) tpNumFrames);
			double dy = (tpY - tpYi) / ((double) tpNumFrames);
			if (tpStep <= tpNumFrames) {
				tpStep++;
				setX((int) (tpXi + dx * tpStep));
				setY((int) (tpYi + dy * tpStep));
			} else
				goTp = false;
		}
		setVelX(0);
		setVelY(0);
		if (goUp)
			setVelY(-1);
		if (goDown)
			setVelY(getVelY() + 1);
		if (goLeft)
			setVelX(-1);
		if (goRight)
			setVelX(getVelX() + 1);

		x += MOVEDIST * velX;
		y += MOVEDIST * velY;

		x = clamp(x, 0, Game.WIDTH - 38);
		y = clamp(y, 0, Game.HEIGHT - 60);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this.x, this.y, 32, 32);
		g.setColor(Color.WHITE);
		g.fillRect(this.x + 16 - 8, this.y, 16, 16);
	}

	public void setTp(int x, int y) {
		goTp = true;
		tpX = x;
		tpY = y;
		tpXi = this.x;
		tpYi = this.y;
		tpStep = 1;
		tpNumFrames = (int) (Math.sqrt((tpX - tpXi) * (tpX - tpXi) + (tpY) * (tpYi)) / TPDIST);
	}

}
