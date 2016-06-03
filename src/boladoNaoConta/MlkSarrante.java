package boladoNaoConta;

import robocode.*;

public class MlkSarrante extends AdvancedRobot{
	RadarCtrl radar;
	MinimumRisk minRisk;
	GFTargeting targeting;
	
	public void run(){
		new RobotColors().setCRFColors(this);
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		
		radar = new RadarCtrl(this);
		minRisk = new MinimumRisk(this);
		targeting = new GFTargeting(this);
		
		
		while (true) {	
			radar.checkRadar();
			minRisk.movement();
		}	
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		EnemyData enemy = new EnemyData(e, this);
		minRisk.addEnemy(enemy);
		radar.widthLock(e.getBearingRadians(), e.getDistance());
		targeting.aim(enemy);
		minRisk.turnBot(enemy);
	}	
}
