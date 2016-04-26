package projeto;

import java.io.Serializable;

public class Point implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double x = 0.0, y = 0.0;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
}
