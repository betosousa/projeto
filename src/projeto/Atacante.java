package projeto;


import java.awt.Color;
import java.io.IOException;

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
			try{
				broadcastMessage(new Ponto(getX(), getY()));
			} catch (IOException ex1) {
				out.println("Unable to send order: ");
				ex1.printStackTrace(out);
			}
			if(getDistanceRemaining() <= 0.1){
				minRisk.move();
			}
			minRisk.clearLists();
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
		}else if(e.getMessage() instanceof Ponto){
			minRisk.addTeammate((Ponto) e.getMessage());
		}
		
		
	}
	
	
	public void onRobotDeath(RobotDeathEvent e) {
		if(e.getName().equals(leader)){
			setBodyColor(Color.yellow);
			//minRisk.changeMovementFactor();
		}
	}
	
		
}
