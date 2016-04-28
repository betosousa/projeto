package projeto;

import java.io.Serializable;


public class Ponto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private double x = 0.0, y = 0.0;
	
	public Ponto(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Ponto(double bearing, Ponto pt, double distance){
		this.x = pt.getX() + distance * Math.sin(bearing);
		this.y = pt.getY() + distance * Math.cos(bearing);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double distance(Ponto pt){
		double dx = x - pt.getX(), dy = y - pt.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double distanceToLine(Ponto pt1, Ponto pt2){
		return (distance(pt1)+distance(pt2))/2.0;
	}
	
	public double relativeBearing(Ponto r){
		double dx = x - r.getX(), dy = y - r.getY();
		return (Math.PI/2.0) - Math.atan2(dy, dx);
	}
}
