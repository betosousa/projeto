package projeto;

import java.awt.Color;
import java.io.Serializable;

import robocode.Robot;

public class RobotColors  implements Serializable {
	private static final long serialVersionUID = 1L;
	public Color bodyColor, gunColor, radarColor, scanColor, bulletColor;
	
	public RobotColors(){};
	public static void setCRFColors(Robot r){
		r.setBodyColor(Color.black);
		r.setGunColor(Color.black);
		r.setRadarColor(Color.red);
		r.setScanColor(Color.red);
		r.setBulletColor(Color.red);
	}
}
