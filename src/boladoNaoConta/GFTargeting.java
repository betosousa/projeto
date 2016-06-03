package boladoNaoConta;

import java.util.ArrayList;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.util.Utils;

public class GFTargeting {
	AdvancedRobot bot;
	double bulletPower = Rules.MAX_BULLET_POWER, minBullet = 2*(Rules.MAX_BULLET_POWER+Rules.MIN_BULLET_POWER)/3, 
			diag, closeRange;

	ArrayList<BulletWave> bulletWaves = new ArrayList<BulletWave>();
	static double[] stats = new double[31]; 
	int midIndex = (stats.length-1)/2;
	int direction = 1;

	
	public GFTargeting(AdvancedRobot r){
		bot = r;
		closeRange = bot.getBattleFieldWidth()/5;
		diag = Math.sqrt(bot.getBattleFieldWidth()*bot.getBattleFieldWidth()*2) - closeRange; 
	}

	public void waveProcessing(Ponto enemyPt, long time){
		for (BulletWave wave : bulletWaves){
			if(wave.checkHit(enemyPt, time)){
				bulletWaves.remove(wave);
			}
		}
	}
	
	public void aime(EnemyData enemy){
		Ponto enemyPt = new Ponto(enemy.x, enemy.y);
		
		waveProcessing(enemyPt, bot.getTime());
		
		// pega melhor fator
		int index = midIndex;
		for (int i = 1; i < stats.length; i++){
			if(stats[i] > stats[index]){
				index = i;
			}
		}
		
		double distance = enemy.relativeDistance(bot);
		// ajusta bulletPower
		shootPower(distance);
		
		double enemyAbsBearing = enemy.relativeBearing(bot);
		
		if (enemy.velocity != 0){
			direction = (int) Math.signum(Math.sin(enemy.heading - enemyAbsBearing) * enemy.velocity);
		}
		
		BulletWave newWave = new BulletWave(bot.getX(), bot.getY(), enemyAbsBearing, bulletPower, direction, bot.getTime(), stats);
		
		/*
		 *  index = (guessFactor+1) * midIndex
		 *  i = g*m + m
		 *  i - m = g*m
		 *  g = (i-m)/m
		 *  
		 *  guessFactor =  (offset / maxEscapeAngle) * direction
		 *  o = (g / d) * max
		 *  o = (+- g) * max
		 *  
		 */
		
		double gFactor = (index - midIndex) / midIndex;
		double offset = direction * gFactor * newWave.maxEscapeAngle();
		
		double enemyBearing = enemyAbsBearing - bot.getGunHeading();
		
		// ajusta arma
		bot.setTurnGunRightRadians(Utils.normalRelativeAngle(enemyBearing + offset));
		
		// se conseguiu atirar
		if( (bot.getGunHeat() == 0) && (Math.abs(bot.getGunTurnRemaining()) <= 5) && (bot.setFireBullet(bulletPower) != null) ){
			bulletWaves.add(newWave);
		}
		
	}

	public void shootPower(double distance) {
		if (distance < closeRange) {
			bulletPower = Rules.MAX_BULLET_POWER;
		} else {
			double distanceRelativa = diag - distance;
			if (distanceRelativa < 0)
				bulletPower = minBullet;
			else
				bulletPower = Math.max(Rules.MAX_BULLET_POWER*(distanceRelativa)/diag, minBullet);			
		}
	}	
	
	
	//*
	
	public void aim(EnemyData enemy){
		double enemyBearing = enemy.relativeBearing(bot), enemyDistance = enemy.relativeDistance(bot);
		// Let's process the waves now:
		for (int i=0; i < bulletWaves.size(); i++){
			BulletWave currentWave = bulletWaves.get(i);
			if (currentWave.checkHit(enemy.x, enemy.y, bot.getTime())){
				bulletWaves.remove(currentWave);
				i--;
			}
		}

		if (enemy.velocity != 0){
			if (Math.sin(enemy.heading - enemyBearing) * enemy.velocity < 0)
				direction = -1;
			else
				direction = 1;
		}
		double[] currentStats = stats; // This seems silly, but I'm using it to show something else later

		shootPower(enemyDistance);
		
		BulletWave newWave = new BulletWave(bot.getX(), bot.getY(), enemyBearing, bulletPower, direction, bot.getTime(), currentStats);

		int bestindex = midIndex;	// initialize it to be in the middle, guessfactor 0.
		for (int i=0; i<currentStats.length; i++)
			if (currentStats[bestindex] < currentStats[i])
				bestindex = i;

		// this should do the opposite of the math in the WaveBullet:
		double guessfactor = (double)(bestindex - midIndex) / midIndex;
		double angleOffset = direction * guessfactor * newWave.maxEscapeAngle();
		double gunAdjust = Utils.normalRelativeAngle(enemyBearing - bot.getGunHeadingRadians() + angleOffset);
		bot.setTurnGunRightRadians(gunAdjust);

		if ( ((( bot.getEnergy() > 0.5 )))  &&  bot.getGunHeat() == 0 && gunAdjust < Math.atan2(9, enemyDistance) && bot.setFireBullet(bulletPower) != null) {
			bulletWaves.add(newWave);
		}

	}
	//*/
}
