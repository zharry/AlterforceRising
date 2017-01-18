import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Handler {

	ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
	
	// Boss Spawning Variable
	double bossNum = 1;
	
	public void tick() {
		if (Game.player.level <= 3){
			if (Game.random.nextInt(150) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}
		}
		else if (Game.player.level <= 6){
			if (Game.random.nextInt(300) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(300) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_TANK));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}
		}
		else if (Game.player.level <= 9){
			if (Game.random.nextInt(200) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(400) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_TANK));
			}if (Game.random.nextInt(400) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_SCOUT));
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}
		}
		else if (Game.player.level == 10){
			if (this.bossNum == 1){
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_1));
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_1));
				this.bossNum ++;
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}
		}
		else if (Game.player.level < 15){
			if (Game.random.nextInt(200) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_TANK));
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_SCOUT));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_SPEED));
			}
		}
		else if (Game.player.level == 15){
			if (this.bossNum == 2){
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_2));
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_2));
				this.bossNum ++;
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_SPEED));
			}
		}
		else if (Game.player.level < 20){
			if (Game.random.nextInt(200) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(400) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_TANK));
			}if (Game.random.nextInt(400) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_SCOUT));
			}if (Game.random.nextInt(400) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_ASSASSIN));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}if (Game.random.nextInt(3000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_SPEED));
			}
		}
		else if (Game.player.level == 20){
			if (this.bossNum == 1){
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_3));
				this.bossNum ++;
			}if (Game.random.nextInt(500) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(5000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_EXP));
			}if (Game.random.nextInt(2000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(2000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}if (Game.random.nextInt(2000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_SPEED));
			}
		}
		else{
			if (Game.random.nextInt(500) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_DEFAULT));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_SCOUT));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.ENEMY_ASSASSIN));
			}if (Game.random.nextInt(2000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_1));
			}if (Game.random.nextInt(4000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_2));
			}if (Game.random.nextInt(10000) == 1) {
				this.add(new Enemy(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_ENEMY, Game.BOSS_3));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_HEALTH));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_SPEED));
			}if (Game.random.nextInt(1000) == 1) {
				this.add(new Potion(Game.random.nextInt(Game.panelWidth), Game.random.nextInt(Game.panelHeight), Game.TYPE_POTION, Game.POTION_DAMAGE));
			}
		}
		try {
		for (GameObject object : gameObjects)
			object.tick();
		while (!toRemove.isEmpty()) {
			gameObjects.remove(toRemove.get(0));
			toRemove.remove(toRemove.get(0));
		}
		} catch (ConcurrentModificationException e) {}
	}

	public void render(Graphics g) {
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
			int tw = obj.sprite[0].getWidth();
			int th = obj.sprite[0].getHeight();
			int rw = obj1.sprite[0].getWidth();
			int rh = obj1.sprite[0].getHeight();
			if (!(rw <= 0 || rh <= 0 || tw <= 0 || th <= 0)) {
				int tx = (int) Math.round(obj.x);
				int ty = (int) Math.round(obj.y);
				int rx = (int) Math.round(obj1.x);
				int ry = (int) Math.round(obj1.y);
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
			Rectangle spriteBoundingBox = new Rectangle((int) Math.round(obj1.x), (int) Math.round(obj1.y), obj1.sprite[0].getWidth(),
					obj1.sprite[0].getHeight());
			if (l.intersects(spriteBoundingBox))
				col.add(obj1);
		}
		return col;
	}

}
