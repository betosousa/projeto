package boladoNaoConta;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.util.Utils;

public class RadarCtrl {
	private AdvancedRobot bot;
	public RadarCtrl(AdvancedRobot r){
		bot = r;
	}
	
	public void widthLock(double bearingRadians, double distance){
		// angulo total do inimigo = agulo do robo + offset do inimigo;
		double absBearing = bot.getHeadingRadians() + bearingRadians;
		// angulo do inimigo pro radar
		double radarBearing = Utils.normalRelativeAngle(absBearing - bot.getRadarHeadingRadians());
		// offset(tamanho do arco envolvendo inimigo), menor entre atan(qtd de pxls entre arco e centro do robo / distancia para o robo) e maximo scan 
		double extraTurn = Math.min( Math.atan( 20.0 / distance), Rules.RADAR_TURN_RATE_RADIANS );
		// junta tudo
		radarBearing += Math.signum(radarBearing) * extraTurn;

		bot.setTurnRadarRightRadians(radarBearing);
	}
	
	public void checkRadar(){
		if(bot.getRadarTurnRemaining() == 0){
			bot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
		bot.execute();
	}
}
