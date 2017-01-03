import java.awt.Graphics;
import java.util.ArrayList;

public class Handler {

	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

	public void tick() {
		for (GameObject object : gameObjects)
			object.tick();
	}

	public void render(Graphics g) {
		for (GameObject object : gameObjects) {
			object.render(g);
		}
	}

	public void add(GameObject obj) {
		this.gameObjects.add(obj);
	}

	public void remove(GameObject obj) {
		this.gameObjects.remove(obj);
	}

}
