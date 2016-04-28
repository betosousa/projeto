package projeto;

import java.util.ArrayList;
import java.util.Random;

import robocode.AdvancedRobot;

public class MinimumRisk {

	ArrayList<EnemyData> enemies;
	ArrayList<Ponto> teammates;
	
	Ponto target;
	Ponto center;
	AdvancedRobot bot;

	Ponto corner1, corner2, corner3, corner4;
	
	double factor = 1;
	
	public MinimumRisk(AdvancedRobot r){
		bot = r;
		
		double width = bot.getBattleFieldWidth(), height = bot.getBattleFieldHeight();
		center = new Ponto(width/2, height/2);
		corner1 = new Ponto(0, 0);
		corner2 = new Ponto(width, 0);
		corner3 = new Ponto(0, height);
		corner4 = new Ponto(width, height);
		
		enemies = new ArrayList<EnemyData>();
		teammates = new ArrayList<Ponto>();
		
	}
	
	public void addEnemy(EnemyData e){
		if(!enemies.contains(e))
			enemies.add(e);
	}
	
	public void setTarget(EnemyData e){
		target = new Ponto(e.x, e.y);
	}
	
	public void addTeammate(EnemyData friend){
		Ponto pt = new Ponto(friend.x, friend.y);
		if(!teammates.contains(pt))
			teammates.add(pt);
	}
	
	public double risk(Ponto pt){
		double riskValue = 0;
		// inversamente a dist pros inimigos e diretamente com energia
		for (EnemyData enemy : enemies){
			riskValue += enemy.energy/enemy.relativeDistance(pt);
		}
		// inversamente distancia pro centro 
		riskValue += 1/pt.distance(center);

		if(target != null){
			// inversamente distancias pras linesights
			for (Ponto mate : teammates){
				riskValue += 1/pt.distanceToLine(mate, target);
			}
		}
		// inversamente a dist pras paredes
		riskValue += 1/(pt.distanceToLine(corner1, corner2)*factor);
		riskValue += 1/(pt.distanceToLine(corner1, corner3)*factor);
		riskValue += 1/(pt.distanceToLine(corner2, corner4)*factor);
		riskValue += 1/(pt.distanceToLine(corner3, corner4)*factor);
		
		return riskValue; 
	}
	//////////////////////////////////qq
		
		public int lowerRiskIndex(double[] Enemy) {
			double menor = Enemy[0];
			int index = 0;
			for (int i = 1; i < Enemy.length; i++) {
				if (Enemy[i] < menor) {
					menor = Enemy[i];
					index = i;
				}
			}
			return index;
		}
		
		public Ponto[] pontosRandom (Ponto eu) {
			int raio = 100;
			int raio2 = raio*raio;
			int qtdPontos = 25;
			Ponto[] vetorPoint = new Ponto[qtdPontos];
			
			for (int i = 0; i < qtdPontos; i++) {
				
				double x = eu.getX() - raio + new Random().nextInt(2*raio);
				double y = eu.getY() + Math.sqrt(raio2 - (x - eu.getX())*(x - eu.getX()));
						
				vetorPoint[i] = new Ponto(x, y);
			}
			
			bot.out.println("pts: ");
			
			for(Ponto pt : vetorPoint){
				bot.out.println(pt.getX() + ", " + pt.getY() );
			}
			
			return vetorPoint;
		}
		
	////////////////////////////////////qqq
	public void move(){
		Ponto botPt = new Ponto(bot.getX(), bot.getY());
		Ponto[] pontos = pontosRandom(botPt);
		
		double[] risks = new double[pontos.length];
		
		for (int i = 0; i < risks.length; i++){
			risks[i] = risk(pontos[i]);
		}
		
		Ponto destino = pontos[lowerRiskIndex(risks)]; 
		
		bot.setTurnRadarRightRadians(botPt.relativeBearing(destino) - bot.getHeadingRadians());
		
		bot.ahead(botPt.distance(destino));
	}
	 
}
