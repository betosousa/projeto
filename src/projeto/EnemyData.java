package projeto;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class EnemyData {
	public double velocity, heading, x, y;
	
	public EnemyData(ScannedRobotEvent e, AdvancedRobot r){
		double bearing = r.getHeadingRadians() + e.getBearingRadians();
		x = r.getX() + e.getDistance() * Math.sin(bearing);
		y = r.getY() + e.getDistance() * Math.cos(bearing);

		velocity = e.getVelocity();
		heading = e.getHeadingRadians();
	}
	
	public double relativeBearing(AdvancedRobot r){
		double dx = x - r.getX(), dy = y - r.getY();
		return Math.atan(dx/dy);
	}
}
