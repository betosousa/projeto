package projeto;

import java.util.ArrayList;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.util.Utils;

public class GFTargeting {
	AdvancedRobot bot;
	double bulletPower = Rules.MAX_BULLET_POWER, diag, closeRange;

	ArrayList<BulletWave> bulletWaves = new ArrayList<BulletWave>();
	static int[] stats = new int[31]; // 31 is the number of unique GuessFactors we're using
	int direction = 1;

	
	public GFTargeting(AdvancedRobot r){
		bot = r;
		closeRange = bot.getBattleFieldWidth()/5;
		diag = Math.sqrt(bot.getBattleFieldWidth()*bot.getBattleFieldWidth()*2) - closeRange; 
	}

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
		int[] currentStats = stats; // This seems silly, but I'm using it to show something else later

		shootPower(enemyDistance);
		
		BulletWave newWave = new BulletWave(bot.getX(), bot.getY(), enemyBearing, bulletPower, direction, bot.getTime(), currentStats);

		int bestindex = 15;	// initialize it to be in the middle, guessfactor 0.
		for (int i=0; i<31; i++)
			if (currentStats[bestindex] < currentStats[i])
				bestindex = i;

		// this should do the opposite of the math in the WaveBullet:
		double guessfactor = (double)(bestindex - (stats.length - 1) / 2) / ((stats.length - 1) / 2);
		double angleOffset = direction * guessfactor * newWave.maxEscapeAngle();
		double gunAdjust = Utils.normalRelativeAngle(enemyBearing - bot.getGunHeadingRadians() + angleOffset);
		bot.setTurnGunRightRadians(gunAdjust);

		if (bot.getGunHeat() == 0 && gunAdjust < Math.atan2(9, enemyDistance) && bot.setFireBullet(bulletPower) != null) {
			bulletWaves.add(newWave);
		}

	}
	
	public void shootPower(double distance) {
		
		if (distance < closeRange) {
			bulletPower = Rules.MAX_BULLET_POWER;
		} else {
			double distanceRelativa = diag - distance;
			if (distanceRelativa < 0)
				bulletPower = Rules.MIN_BULLET_POWER;
			else
				bulletPower = Rules.MAX_BULLET_POWER*(distanceRelativa)/diag;			
		}
		bot.out.println(bulletPower);
	}	
	
	
}
