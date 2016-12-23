import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {

	boolean goUp = false, goDown = false, goLeft = false, goRight = false;

	public Player(int x, int y, int type) {
		super(x, y, type);
	}

	@Override
	public void tick() {

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

		x += velX;
		y += velY;

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

}
