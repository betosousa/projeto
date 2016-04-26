package projeto;

import static robocode.util.Utils.normalRelativeAngle;
import robocode.*;

import java.awt.Color;
import java.io.IOException;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Leader
 */
public class Leader extends TeamRobot {
	private String target = null, leader = null;
	private boolean leaderDead = false;

	public void run() {
		// Prepare RobotColors object
		RobotColors c = new RobotColors();

		c.bodyColor = Color.red;
		c.gunColor = Color.black;
		c.radarColor = Color.black;
		c.scanColor = Color.black;
		c.bulletColor = Color.red;

		// Set the color of this robot containing the RobotColors
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);
		try {
			// Send RobotColors object to our entire team
			broadcastMessage(c);
			broadcastMessage(getName());
		} catch (IOException ignored) {
		}
		// Normal behavior
		while (true) {
			setTurnRadarRight(10000);
			ahead(100);
			back(100);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}

		if (e.getEnergy() > 150.0 && target == null) {
			target = e.getName();// lider adversario
			leader = e.getName();
		}

		if (leaderDead && target == null)
			target = e.getName();

		if (e.getName().equals(target)) {

			// Calculate enemy bearing
			double enemyBearing = this.getHeading() + e.getBearing();
			// Calculate enemy's position
			double enemyX = getX() + e.getDistance()
					* Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance()
					* Math.cos(Math.toRadians(enemyBearing));
			// *
			double dx = enemyX - this.getX();
			double dy = enemyY - this.getY();
			double theta = Math.atan2(dx, dy);
			turnGunRightRadians(normalRelativeAngle(theta - getGunHeadingRadians()));
			
			// radar width lock
			double radarMove = theta - getRadarHeadingRadians(); // where to move radar
			double extra = Math.min( Math.atan(36.0/e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS); // extra arc 
			radarMove += (radarMove > 0 ? extra : -extra); // add up
			setTurnRadarRightRadians(radarMove);
			
			fire(3);
			// */
			try {
				// Send enemy position to teammates
				broadcastMessage(new Point(enemyX, enemyY));
			} catch (IOException ex) {
				out.println("Unable to send order: ");
				ex.printStackTrace(out);
			}
		}
	}

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}

	public void onHitWall(HitWallEvent e) {
		back(20);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		if (leaderDead) {
			if (e.getName().equals(target))
				target = null;
		} else {
			if (e.getName().equals(leader)) {
				leaderDead = true;
				target = null;
			}
		}
	}
	
	public void onDeath(DeathEvent e){
		try {
			broadcastMessage(new RobotColors());
			out.println("msg sent");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
