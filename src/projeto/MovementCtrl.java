package projeto;

import java.util.Random;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MovementCtrl {
	AdvancedRobot bot;
	double movementFactor = 100, direction = 1;
	Random random;
	public MovementCtrl(AdvancedRobot r){
		bot = r;
		random = new Random();
	}
	
	public void move(Ponto pt){
		// Calculate x and y to target
		double dx = pt.getX() - bot.getX();
		double dy = pt.getY() - bot.getY();
		// Calculate angle to target
		double theta = Math.toDegrees(Math.atan2(dx, dy));
		bot.setTurnRight(Utils.normalRelativeAngleDegrees(theta - bot.getHeading()) + 90 );
	}
	
	public double getMovement(){
		direction = -direction;
		return random.nextDouble() * movementFactor * direction;
	}
	
}
