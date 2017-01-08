import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Debug {

	static void drawDebug(Graphics g) {
		g.setColor(Color.black);
		int drawY = 0, incY = 15;
		// Game Debug
		g.drawString("FPS: " + Game.fps + ", TPS: " + Game.curTps, 10, drawY += incY);
		g.drawString("Mouse X: " + Game.mouseX + ", Mouse Y: " + Game.mouseY, 10, drawY += incY);
		// Player Debug
		g.drawString("X: " + Game.player.x + ", Y: " + Game.player.y + ", Rotate: " + (int) Game.player.rotateDegs, 10,
				drawY += incY);
		g.drawString("Vel X: " + Game.player.velX + ", Vel Y: " + Game.player.velY, 10, drawY += incY);
		g.drawString("TP: " + Game.player.goTp, 10, drawY += incY);
		g.drawString("TP X: " + Game.player.tpLocX + ", TP Y: " + Game.player.tpLocY, 10, drawY += incY);
		g.drawString(
				"TP Cooldown: " + Game.player.tpCooldownTimer + ", TP Cooldown Amount: " + Game.player.tpCooldownAmount,
				10, drawY += incY);
		g.drawString("Knockback: " + Game.player.underKnockback + ", KB Step: " + Game.player.kbStep, 10,
				drawY += incY);
		g.drawString("KB Vel X: " + Game.player.kbVelX + ", KB Vel Y: " + Game.player.kbVelY, 10, drawY += incY);
		// Player Rotation Visualization
		g.setColor(Color.yellow);
		g.drawLine(Game.player.p2x, Game.player.p2y, Game.player.p1x, Game.player.p1y);
		// TP Damage Visualization
		g.setColor(Color.red);
		g.drawLine(Game.player.p2xTP, Game.player.p2yTP, Game.player.p1xTP, Game.player.p1yTP);
		
		// Draw Debug Collision Boxes
		for (GameObject obj : Game.gameController.gameObjects) {
			// Add transparency and border width
			Graphics2D g2d = (Graphics2D) g;
			Stroke origStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(3));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
			// Draw Collision Boxes
			g.setColor(Color.magenta);
			g.drawRect(obj.colBox.x, obj.colBox.y, obj.colBox.width, obj.colBox.height);
			// Reset Graphics to original
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g2d.setStroke(origStroke);
		}
		for (GameObject obj : Game.gameController.gameObjects) {
			// Add transparency and border width
			Graphics2D g2d = (Graphics2D) g;
			Stroke origStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(3));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.725f));
			// Draw Sprite Bounding Boxes
			g.setColor(Color.pink);
			g.drawRect(obj.x, obj.y, obj.sprite.getWidth(), obj.sprite.getHeight());
			// Reset Graphics to original
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g2d.setStroke(origStroke);
		}
	}

	static void debugConsole() {
	}
}
