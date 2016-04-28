package projeto;

import robocode.util.Utils;

public class BulletWave {
	
	private Ponto shotPt;
	private double startX, startY, startBearing, power;
	private long   shotTime;
	private int    direction;
	private int[]  stats;

	public BulletWave(double x, double y, double bearing, double power,	int direction, long time, int[] segment){
		shotPt = new Ponto(x,y);
		startX = x;
		startY = y;
		startBearing      = bearing;
		this.power     = power;
		this.direction = direction;
		shotTime       = time;
		stats  		   = segment;
	}
	
	public double getBulletSpeed(){
		return 20 - power * 3;
	}

	public double maxEscapeAngle(){
		return Math.asin(8 / getBulletSpeed());//8 eh a vel max do bot
	}
	
	public double distance(double x1, double y1, double x2, double y2){
		double dx = x1-x2, dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	}

	public double bulletDistance(long time){
		double speed = 20 - power * 3;
		double dTime = time - shotTime; 
		return dTime * speed;
	}
	
	//retorna se a bala ja passou do enemy	
	public boolean checkHit(Ponto enemyPt, long currentTime){
		
		if (shotPt.distance(enemyPt) <= bulletDistance(currentTime)){
			// calcula diferenca do angulo de tiro para o angulo q acertaria o enemy
			double dx = enemyPt.getX() - shotPt.getX(), dy = enemyPt.getY() - shotPt.getY();  
			double actualDirection = Math.atan2(dx, dy);
			double offset = Utils.normalRelativeAngle(actualDirection - startBearing);
			
			// proporcao de quanto o enemy se moveu em relacao ao maximo escape 
			double guessFactor = Math.max(-1, Math.min(1, offset / maxEscapeAngle())) * direction;
			
			int midIndex = (stats.length-1)/2;
			
			int index = (int) Math.round((guessFactor+1) * midIndex);
			stats[index]++;
			
			return true;
		}
		return false;
	}
	
	public boolean checkHit(double enemyX, double enemyY, long currentTime){
		// if the distance from the wave origin to our enemy has passed
		// the distance the bullet would have traveled...
		if (distance(startX, startY, enemyX, enemyY) <= (currentTime - shotTime) * getBulletSpeed()){
			
			double desiredDirection = Math.atan2(enemyX - startX, enemyY - startY);
			double angleOffset = Utils.normalRelativeAngle(desiredDirection - startBearing);
			double guessFactor =
				Math.max(-1, Math.min(1, angleOffset / maxEscapeAngle())) * direction;
			int index = (int) Math.round((stats.length - 1) /2 * (guessFactor + 1));
			stats[index]++;
			return true;
		}
		return false;
	}
	
}
