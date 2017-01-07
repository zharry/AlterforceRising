import java.awt.Graphics;
import java.util.ArrayList;

public class Handler {

	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	public void tick() {
		for (GameObject object : gameObjects)
			object.tick();
		if (Game.random.nextInt(Game.tps * 12) == 0) {
			this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.sprAssassin1, 3, 48));
		}
		while (!toRemove.isEmpty()) {
			gameObjects.remove(toRemove.get(0));
			toRemove.remove(toRemove.get(0));
		}
	}

	public void render(Graphics g) {
		for (GameObject object : gameObjects)
			if (object.type != Game.TYPE_PLAYER)
				object.render(g);
		Game.player.render(g);
	}

	public void add(GameObject obj) {
		this.gameObjects.add(obj);
	}

	public void remove(GameObject obj) {
		this.gameObjects.remove(obj);
	}

	/**
	 * @param obj
	 *            Target object to check if it is colliding with something
	 * @return An array of all objects that obj has collided with
	 * @author java.awt.Rectangle.intersects(Rectangle r)
	 */
	public ArrayList<GameObject> isColliding(GameObject obj) {
		ArrayList<GameObject> col = new ArrayList<GameObject>();
		for (GameObject obj1 : gameObjects) {
			int tw = obj.sprite.getWidth();
			int th = obj.sprite.getHeight();
			int rw = obj1.sprite.getWidth();
			int rh = obj1.sprite.getHeight();
			if (!(rw <= 0 || rh <= 0 || tw <= 0 || th <= 0)) {
				int tx = obj.x;
				int ty = obj.y;
				int rx = obj1.x;
				int ry = obj1.y;
				rw += rx;
				rh += ry;
				tw += tx;
				th += ty;
				if ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry))
					if (obj1 != obj)
						col.add(obj1);
			}
		}
		return col;
	}

}
