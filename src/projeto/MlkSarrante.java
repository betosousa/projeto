package projeto;

import robocode.*;
import robocode.util.Utils;

//import java.io.IOException;

public class MlkSarrante extends AdvancedRobot{
	private double bulletPower = Rules.MAX_BULLET_POWER;
	private boolean targetAcquired = false;
	public void run(){
		RobotColors.setCRFColors(this);
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		execute();
		while (true) {
			if(targetAcquired)
				ahead(200);	
			if(getRadarTurnRemaining() == 0){
				setTurnRadarRight(Double.POSITIVE_INFINITY);
				targetAcquired = false;
			}
			execute();
		}	
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		// Radar code
		
		// angulo total do inimigo = agulo do robo + offset do inimigo;
	    double absBearing = getHeadingRadians() + e.getBearingRadians();
	    
	    // angulo do inimigo pro radar
 		double radarBearing = Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians());
 		// offset(tamanho do arco envolvendo inimigo), menor entre atan(qtd de pxls entre arco e centro do robo / distancia para o robo) e maximo scan 
 		double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS );
 		// junta tudo
 		radarBearing += (radarBearing < 0 ? -extraTurn : extraTurn);

 	    setTurnRadarRightRadians(radarBearing);
	    
	    // angulo da projecao do mov linear 
	    double linearBearing = absBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bulletPower) * Math.sin(e.getHeadingRadians() - absBearing));
	    
	    setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - getGunHeadingRadians()));
		setTurnRightRadians(Utils.normalRelativeAngle(linearBearing - getHeadingRadians()));
	    if(getGunHeat() == 0){
	    	//energyManagement(e.getDistance(), e.getEnergy());
	    	setFire(bulletPower);
	    	targetAcquired = true;
	    }
		//ahead(e.getDistance());
	}	
}
