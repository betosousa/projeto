package projeto;

import java.io.Serializable;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class EnemyData implements Serializable {
	private static final long serialVersionUID = 1L;
	public double velocity, heading, x, y, bearing;
	
	public EnemyData(ScannedRobotEvent e, AdvancedRobot r){
		bearing = r.getHeadingRadians() + e.getBearingRadians();
		x = r.getX() + e.getDistance() * Math.sin(bearing);
		y = r.getY() + e.getDistance() * Math.cos(bearing);
		velocity = e.getVelocity();
		heading = e.getHeadingRadians();
	}
	
	public double relativeBearing(AdvancedRobot r){
		double dx = x - r.getX(), dy = y - r.getY();
		return (Math.PI/2.0) - Math.atan2(dy, dx);
	}
	
	public double relativeDistance(AdvancedRobot r){
		double dx = x - r.getX(), dy = y - r.getY();
		return Math.sqrt(dx*dx + dy*dy);	
	}
	
	
}
