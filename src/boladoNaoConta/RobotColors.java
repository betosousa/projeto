package boladoNaoConta;

import java.awt.Color;
import java.io.Serializable;

import robocode.Robot;

public class RobotColors  implements Serializable {
	private static final long serialVersionUID = 1L;
	public Color bodyColor, gunColor, radarColor, scanColor, bulletColor;
	
	public RobotColors(){
		bodyColor = Color.red;
		gunColor = Color.black;
		radarColor = Color.black;
		scanColor = Color.red;
		bulletColor = Color.white;
	};
	public void setCRFColors(Robot r){
		r.setBodyColor(bodyColor);
		r.setGunColor(gunColor);
		r.setRadarColor(radarColor);
		r.setScanColor(scanColor);
		r.setBulletColor(bulletColor);
	}
}
