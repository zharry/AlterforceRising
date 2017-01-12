import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Handler {

	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	public synchronized void tick() {
		for (GameObject object : gameObjects)
			object.tick();
		while (!toRemove.isEmpty()) {
			gameObjects.remove(toRemove.get(0));
			toRemove.remove(toRemove.get(0));
		}
	}

	public synchronized void render(Graphics g) {
		for (GameObject object : gameObjects)
			if (object.type != Game.TYPE_PLAYER)
				object.render(g);
		// Ensure that the player stays on top
		Game.player.render(g);
	}

	public void add(GameObject obj) {
		this.gameObjects.add(obj);
	}

	public void remove(GameObject obj) {
		this.gameObjects.remove(obj);
	}

	public ArrayList<GameObject> isColliding(GameObject obj) {
		ArrayList<GameObject> col = new ArrayList<GameObject>();
		for (GameObject obj1 : gameObjects)
			if (obj1 != obj)
				if (obj1.colBox.intersects(obj.colBox))
					col.add(obj1);
		return col;
	}

	public ArrayList<GameObject> isCollidingRay(Line2D l) {
		ArrayList<GameObject> col = new ArrayList<GameObject>();
		for (GameObject obj1 : gameObjects)
			if (l.intersects(obj1.colBox))
				col.add(obj1);
		return col;
	}

	public ArrayList<GameObject> isCollidingSprite(GameObject obj) {
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

	public ArrayList<GameObject> isCollidingRaySprite(Line2D l) {
		ArrayList<GameObject> col = new ArrayList<GameObject>();
		for (GameObject obj1 : gameObjects) {
			Rectangle spriteBoundingBox = new Rectangle(obj1.x, obj1.y, obj1.sprite.getWidth(),
					obj1.sprite.getHeight());
			if (l.intersects(spriteBoundingBox))
				col.add(obj1);
		}
		return col;
	}

}
