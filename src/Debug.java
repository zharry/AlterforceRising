import java.awt.Color;
import java.awt.Graphics;

public class Debug {

	static void drawDebug(Graphics g) {
		g.setColor(Color.black);
		int drawY = 0, incY = 15;
		// Game Debug
		g.drawString("FPS: " + Game.fps, 10, drawY += incY);
		g.drawString("Mouse X: " + Game.mouseX + ", Mouse Y: " + Game.mouseY, 10, drawY += incY);
		// Player Debug
		g.drawString("X: " + Game.player.x + ", Y: " + Game.player.y + ", Rotate: " + (int) Game.player.rotateDegs, 10,
				drawY += incY);
		g.drawString("Vel X: " + Game.player.x + ", Vel Y: " + Game.player.y, 10,
				drawY += incY);
		g.drawString("TP: " + Game.player.goTp, 10, drawY += incY);
		g.drawString("TP X: " + Game.tpLocX + ", TP Y: " + Game.tpLocY, 10, drawY += incY);
		g.drawString("TP Cooldown: " + Game.player.tpCooldownTimer + ", TP Cooldown Amount: " + Game.player.tpCooldownAmount, 10, drawY += incY);
		g.drawString("Under Knockback: " + Game.player.underKnockback + ", KB Step: " + Game.player.kbStep, 10, drawY += incY);
		// Player Rotation Visualization
		g.drawLine(Game.player.p2x, Game.player.p2y, Game.player.p1x, Game.player.p1y);
	}
	
	static void debugConsole() {
	}
}
