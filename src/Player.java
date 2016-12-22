import java.awt.Color;
import java.awt.Graphics;

public class Player extends GameObject {

	public Player(int x, int y, int type) {
		super(x, y, type);
	}

	@Override
	public void tick() {
		this.x += this.velX;
		this.y += this.velY;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this.x, this.y, 32, 32);
	}

}
