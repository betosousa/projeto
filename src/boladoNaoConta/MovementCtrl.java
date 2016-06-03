package boladoNaoConta;

import java.util.Random;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MovementCtrl {
	AdvancedRobot bot;
	double movementFactor = 300, direction = 1;
	Random random;
	public MovementCtrl(AdvancedRobot r){
		bot = r;
		random = new Random();
	}
	
	public void turnBot(EnemyData enemy){
		// Calculate x and y to target
		double dx = enemy.x - bot.getX();
		double dy = enemy.y - bot.getY();
		// Calculate angle to target
		double theta = Math.toDegrees(Math.atan2(dx, dy));
		bot.setTurnRight(Utils.normalRelativeAngleDegrees(theta - bot.getHeading()) + 90 );
	}
	
	public double getMovement(){
		direction = -direction;
		return random.nextDouble() * movementFactor * direction;	
	}
	
}
