package boladoNaoConta;

import java.io.Serializable;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class EnemyData implements Serializable {
	private static final long serialVersionUID = 1L;
	public double velocity, heading, x, y, bearing, energy;
	private Ponto pt;
	
	public EnemyData(ScannedRobotEvent e, AdvancedRobot r){
		bearing = r.getHeadingRadians() + e.getBearingRadians();
		x = r.getX() + e.getDistance() * Math.sin(bearing);
		y = r.getY() + e.getDistance() * Math.cos(bearing);
		velocity = e.getVelocity();
		heading = e.getHeadingRadians();
		energy = e.getEnergy();
		pt = new Ponto(x, y);
	}
	
	public double relativeBearing(AdvancedRobot r){
		return pt.relativeBearing(new Ponto(r.getX(),r.getY()));
	}
	
	public double relativeDistance(AdvancedRobot r){
		return relativeDistance(new Ponto(r.getX(), r.getY()));
	}
	
	public double relativeDistance(Ponto p){
		return pt.distance(p);	
	}
}
