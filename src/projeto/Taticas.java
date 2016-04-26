package projeto;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;



public class Taticas {

	public static void mira(AdvancedRobot r, ScannedRobotEvent e){
		// angulo total do inimigo = agulo do robo + offset do inimigo;
	    double absBearing = r.getHeadingRadians() + e.getBearingRadians();
	    
	    // angulo do inimigo pro radar
 		double radarBearing = Utils.normalRelativeAngle(absBearing - r.getRadarHeadingRadians());
 		// offset(tamanho do arco envolvendo inimigo), menor entre atan(qtd de pxls entre arco e centro do robo / distancia para o robo) e maximo scan 
 		double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS );
 		// junta tudo
 		radarBearing += (radarBearing < 0 ? -extraTurn : extraTurn);

 	    //r.setTurnRadarRight(radarBearing);
	    
	    // angulo da projecao do mov linear 
	    double linearBearing = e.getBearing(); 
	    		//absBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bulletPower) * Math.sin(e.getHeadingRadians() - absBearing));
	    
	    r.setTurnGunRight(Utils.normalRelativeAngleDegrees(linearBearing - r.getGunHeading()));
		r.setTurnRight(Utils.normalRelativeAngleDegrees(linearBearing - r.getHeading()));
	    if(r.getGunHeat() == 0){
	    	//setFire(bulletPower);
	    	r.setFire(3);
	    	//targetAcquired = true;
	    }

	}
	
	
	
}
