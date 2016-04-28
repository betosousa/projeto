package projeto;

import robocode.*;

import java.io.IOException;



// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Leader
 */
public class Leader extends TeamRobot {
	private String target = null, leader = null;
	private boolean leaderDead = false;
	
	RadarCtrl radar; 
	MovementCtrl mvmt;
	GFTargeting targeting;
	MinimumRisk minRisk;
		
	boolean teamPlay;

	public void run() {
		// Prepare RobotColors object
		RobotColors c = new RobotColors();
		c.setCRFColors(this);
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
		radar = new RadarCtrl(this);
		mvmt = new MovementCtrl(this);
		targeting = new GFTargeting(this);
		minRisk = new MinimumRisk(this);
		
		teamPlay = (getTeammates() != null);
				
		try {
			// Send RobotColors object to our entire team
			broadcastMessage(c);
			broadcastMessage(getName());
		} catch (IOException ignored) {
		}
		// Normal behavior
		while (true) {
			
			if(getRadarTurnRemaining() == 0)
				setTurnRadarRight(1000);
			
			execute();
			if(getDistanceRemaining() <= 0.1){
				minRisk.move();
				//ahead(mvmt.getMovement());
			}
			else{
				out.println(getDistanceRemaining());
			}
		}
	}

	
	
	public void onScannedRobot(ScannedRobotEvent e) {
		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			minRisk.addTeammate(new EnemyData(e, this));
			return;
		}

		if (e.getEnergy() > 150.0 && target == null) {
			target = e.getName();// lider adversario
			leader = e.getName();
			minRisk.setTarget(new EnemyData(e, this));
		}

		if (leaderDead && target == null){
			target = e.getName();
			minRisk.setTarget(new EnemyData(e, this));
		}
		if (e.getName().equals(target) || !teamPlay) {
			
			EnemyData enemy = new EnemyData(e, this);
			
			try {
				// Send enemy data to teammates
				broadcastMessage(enemy);
			} catch (IOException ex1) {
				out.println("Unable to send order: ");
				ex1.printStackTrace(out);
			}
			
			mvmt.turnBot(enemy);
			radar.widthLock(e.getBearingRadians(), e.getDistance());
			targeting.aim(enemy);
		}
	}
	
	public double adjustFire(double distance){
		double bulletPower = Rules.MAX_BULLET_POWER;
		if(distance > 250 && distance < 500){
			bulletPower = (Rules.MAX_BULLET_POWER - Rules.MIN_BULLET_POWER)/2.0;
		}else if(distance >= 500){
			bulletPower = Rules.MIN_BULLET_POWER;
		}
		return bulletPower;
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

}
