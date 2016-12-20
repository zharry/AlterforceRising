import java.awt.Graphics;

public abstract class GameObject {

	int x, y, velX, velY;
	int type;

	public GameObject(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public void setX(int i) {
		this.x = i;
	}

	public void setY(int i) {
		this.y = i;
	}

	public void setVelX(int i) {
		this.velX = i;
	}

	public void setVelY(int i) {
		this.velY = i;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getVelX() {
		return this.velX;
	}

	public int getVelY() {
		return this.velY;
	}

	public int getType() {
		return this.type;
	}

}
