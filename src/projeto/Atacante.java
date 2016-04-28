package projeto;


import java.awt.Color;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.TeamRobot;

public class Atacante extends TeamRobot implements Droid {

	private String leader;
	
	/**
	 * run:  Droid's default behavior
	 */
	
	GFTargeting targeting;
	MinimumRisk minRisk;
	
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
		targeting = new GFTargeting(this);
		minRisk = new MinimumRisk(this);
		
		while(true){
			if(getDistanceRemaining() <= 0.1){
				minRisk.move();
			}
		}
		
	}

		
	/**
	 * onMessageReceived:  What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// Fire at a point
		//out.println("msg recvd: "+e.getMessage());
		if (e.getMessage() instanceof EnemyData) {
			EnemyData enemy = (EnemyData)e.getMessage();
			targeting.aim(enemy);
			minRisk.addEnemy(enemy);
			minRisk.turnBot(enemy);
		} // Set our colors
		else if (e.getMessage() instanceof RobotColors) {
			((RobotColors) e.getMessage()).setCRFColors(this);
		}
		else if(e.getMessage() instanceof String){
			leader = (String) e.getMessage();
		}
	}
	
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals(leader)){
			setBodyColor(Color.yellow);
			minRisk.increaseMovementFactor();
		}
	}
	
		
}
