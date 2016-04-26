package projeto;


import java.awt.Color;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.TeamRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Atacante extends TeamRobot implements Droid {

	private String leader;
	
	/**
	 * run:  Droid's default behavior
	 */
	public void run() {
		out.println("MyFirstDroid ready.");

		while(true){
			ahead(100);
			back(100);
		}
		
	}

	/**
	 * onMessageReceived:  What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		out.println("msg recvd: "+e.getMessage());
		if (e.getMessage() instanceof Point) {
			Point p = (Point) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));

			// Turn gun to target
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		} // Set our colors
		else if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();

			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}
		else if(e.getMessage() instanceof String){
			leader = (String) e.getMessage();
		}
	}
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals(leader)){
			setBodyColor(Color.yellow);
		}
	}
		
}
