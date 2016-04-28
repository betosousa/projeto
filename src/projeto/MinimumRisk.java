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
	
	double factor = 1000;
	double width, height;
	double botMaxSize;
	
	
	public MinimumRisk(AdvancedRobot r){
		bot = r;
		botMaxSize = Math.max(bot.getWidth(), bot.getHeight()) * 3; 
		width = bot.getBattleFieldWidth();
		height = bot.getBattleFieldHeight();
		center = new Ponto(width/2, height/2);
		corner1 = new Ponto(0, 0);
		corner2 = new Ponto(width, 0);
		corner3 = new Ponto(0, height);
		corner4 = new Ponto(width, height);
		
		enemies = new ArrayList<EnemyData>();
		teammates = new ArrayList<Ponto>();
		
	}
	///////////////////////////////////////////////////////////
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
	//////////////////////////////////////////////////////////
	
	public double risk(Ponto pt){
		double riskValue = 0;
/*		// inversamente a dist pros inimigos e diretamente com energia
		for (EnemyData enemy : enemies){
			riskValue +=  enemy.energy/enemy.relativeDistance(pt);
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
		//parede cima
		riskValue += 1/(height - pt.getY());
		//parede direita
		riskValue += 1/(width - pt.getX());
		//parede baixo
		riskValue += 1/pt.getY();
		//parede esquerda
		riskValue += 1/pt.getX();
*/
		return riskValue; 
	}
	//////////////////////////////////qq
		
	public boolean inMap(Ponto p){
		return (p.getX() < width - botMaxSize && p.getX() > botMaxSize) && (p.getY() < height-botMaxSize && p.getY() > botMaxSize);
	}
	
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
			int qtdPontos = 30;
			Ponto[] vetorPoint = new Ponto[qtdPontos];
			
			int dir = 1;
			for (int i = 0; i < qtdPontos; i++) {
				
				double x = eu.getX() - raio + new Random().nextInt(2*raio)*dir;
				dir = -dir;
				double raiz = Math.sqrt(raio2 - (x - eu.getX())*(x - eu.getX()));
				double y = eu.getY() + raiz;
				Ponto p = new Ponto(x, y);
				if(inMap(p)){
					vetorPoint[i]  = p;
				}else{
					vetorPoint[i]  = center;
				}
			}
			
			//bot.out.println("pts: ");
			//bot.out.println(bot.getX() + "; " + bot.getY());
			
			//for(Ponto pt : vetorPoint){
				//bot.out.println(pt.getX() + ", " + pt.getY() );
			//}
			
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
		
		bot.setTurnRightRadians(botPt.relativeBearing(destino) - bot.getHeadingRadians());
		
		bot.ahead(botPt.distance(destino));
		
		bot.out.println(destino.getX() +
				",          " +destino.getY());
		bot.out.println("bot: ");
		bot.out.println(bot.getX() +
				",          " +bot.getY());
		
		
		//bot.out.println(bot.getTurnRemaining());
		
	}
	 
}
