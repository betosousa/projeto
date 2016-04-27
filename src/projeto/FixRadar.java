package projeto;


import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class FixRadar extends AdvancedRobot {
	
	public void run(){
		new RobotColors().setCRFColors(this);
		//setAdjustGunForRobotTurn(true);
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		while (true){
			scan();
			//ahead(1000);
		}
	}
	
	public void simpleLock(){
		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
	}
	
	public void keepTrackLock(double bearing){
		double radarBearing = bearing - getRadarHeadingRadians();
		setTurnRadarLeftRadians(1.0 * Utils.normalRelativeAngle(radarBearing));
	}
	
	public void widthLock(double bearing, double distance){
		// angle between enemy and radar
		double radarBearing = Utils.normalRelativeAngle(bearing - getRadarHeadingRadians());
		// offset angle for scan 
		double extraTurn = Math.min( Math.atan( 36.0 / distance ), Rules.RADAR_TURN_RATE_RADIANS );
		// add up
		radarBearing += (radarBearing < 0 ? -extraTurn : extraTurn);
		// turn
	    setTurnRadarRightRadians(radarBearing);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		 double absBearing = getHeadingRadians() + e.getBearingRadians();
		 widthLock(absBearing, e.getDistance());
		 setTurnGunRightRadians(absBearing - getGunHeadingRadians());
		 //setTurnRight(e.getBearing());
		 setFire(Rules.MAX_BULLET_POWER);
	}

}
