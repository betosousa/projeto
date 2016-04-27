package projeto;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Kamikaze extends AdvancedRobot {

	private double bulletPower = Rules.MAX_BULLET_POWER;
	private boolean targetAcquired = false;
	public void run(){
		new RobotColors().setCRFColors(this);
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
	    	energyManagement(e.getDistance(), e.getEnergy());
	    	setFire(bulletPower);
	    	targetAcquired = true;
	    }
		//ahead(e.getDistance());
	}
	
	public void energyManagement(double distance, double energy){
		// ajusta poder do tiro com a distancia
		if (distance > 250 && distance < 500){
			bulletPower = (Rules.MIN_BULLET_POWER + Rules.MAX_BULLET_POWER)/2.0;
		}else if(distance > 500){
			bulletPower = Rules.MIN_BULLET_POWER;
		}else{
			bulletPower = Rules.MAX_BULLET_POWER;
		}
		
		// ajusta poder pra nao atirar mais forte que necessario pra matar
		if(bulletPower > energy/4.0){
			bulletPower = energy/4.0;
		}
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		out.println("algo");
	}
}
