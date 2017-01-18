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
		g.drawString("X: " + displayNum(Game.player.x) + ", Y: " + displayNum(Game.player.y) + ", Rotate: "
				+ displayNum(Game.player.rotateDegs), 10, drawY += incY);
		g.drawString("Vel X: " + displayNum(Game.player.velX) + ", Vel Y: " + displayNum(Game.player.velY), 10,
				drawY += incY);
		g.drawString("TP: " + Game.player.goTp, 10, drawY += incY);
		g.drawString("TP X: " + Game.player.tpLocX + ", TP Y: " + Game.player.tpLocY, 10, drawY += incY);
		g.drawString(
				"TP Cooldown: " + Game.player.tpCooldownTimer + ", TP Cooldown Amount: " + Game.player.tpCooldownAmount,
				10, drawY += incY);
		g.drawString("Knockback: " + Game.player.underKnockback + ", KB Step: " + Game.player.kbStep, 10,
				drawY += incY);
		g.drawString("KB Vel X: " + displayNum(Game.player.kbVelX) + ", KB Vel Y: " + displayNum(Game.player.kbVelY),
				10, drawY += incY);
		// Player Rotation Visualization
		g.setColor(Color.yellow);
		g.drawLine((int) Math.round(Game.player.p2x), (int) Math.round(Game.player.p2y),
				(int) Math.round(Game.player.p1x), (int) Math.round(Game.player.p1y));
		// TP Damage Visualization
		g.setColor(Color.red);
		g.drawLine((int) Math.round(Game.player.p2xTP), (int) Math.round(Game.player.p2yTP),
				(int) Math.round(Game.player.p1xTP), (int) Math.round(Game.player.p1yTP));

		// Draw Debug Collision Boxes
		Graphics2D g2d = (Graphics2D) g;
		Stroke origStroke = g2d.getStroke();
		// Add transparency and border width
		g2d.setStroke(new BasicStroke(3));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		for (GameObject obj : Game.gameController.gameObjects) {
			// Draw Collision Boxes
			g.setColor(Color.magenta);
			g.drawRect(obj.colBox.x, obj.colBox.y, obj.colBox.width, obj.colBox.height);
		}
		for (GameObject obj : Game.gameController.gameObjects) {
			// Draw Sprite Bounding Boxes
			g.setColor(Color.pink);
			g.drawRect((int) Math.round(obj.x), (int) Math.round(obj.y), obj.sprite[0].getWidth(),
					obj.sprite[0].getHeight());
		}
		// Reset Graphics to original
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2d.setStroke(origStroke);
	}

	static void debugConsole() {
	}

	static double displayNum(double num) {
		return Math.round(num * 10) / 10.0;
	}
}
