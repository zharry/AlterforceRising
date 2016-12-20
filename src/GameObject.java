import java.awt.Graphics;

public abstract class GameObject {
	
	int x, y, type, velX, velY;
	
	public GameObject(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public abstract void tick();
	public abstract void render(Graphics g);
	
}
