package projeto;

import java.util.ArrayList;
import java.util.Random;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MinimumRisk {

	ArrayList<EnemyData> enemies;
	ArrayList<Ponto> teammates;
	
	Ponto target;
	Ponto center;
	AdvancedRobot bot;
	
	double movementFactor = 200, direction = -1;
	Random random;
	
	Ponto corner1, corner2, corner3, corner4;
	
	double energyFactor = 300, wallFactor, centerFactor, lineSightFactor;
	double width, height;
	double botMaxSize;
	
	double minX,maxX,minY,maxY;
	
	
	public MinimumRisk(AdvancedRobot r){
		bot = r;
		botMaxSize = Math.max(bot.getWidth(), bot.getHeight()) * 5; 
		width = bot.getBattleFieldWidth();
		height = bot.getBattleFieldHeight();
		
		wallFactor = width/2;
		centerFactor = wallFactor * Math.sqrt(2);
		lineSightFactor = width * Math.sqrt(2);
		
		minX = botMaxSize;
		minY = botMaxSize;
		maxX = width - botMaxSize;
		maxY = height - botMaxSize;
		
		center = new Ponto(width/2, height/2);
		corner1 = new Ponto(0, 0);
		corner2 = new Ponto(width, 0);
		corner3 = new Ponto(0, height);
		corner4 = new Ponto(width, height);
		
		enemies = new ArrayList<EnemyData>();
		teammates = new ArrayList<Ponto>();
		random = new Random();
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
		// inversamente a dist pros inimigos e diretamente com energia
		for (EnemyData enemy : enemies){
			riskValue +=  enemy.energy / energyFactor;// - enemy.relativeDistance(bot); 
		}
		
		// inversamente distancia pro centro
		riskValue += centerFactor / (pt.distance(center) + 1); 

		if(target != null){
			// inversamente distancias pras linesights
			for (Ponto mate : teammates){
				riskValue += lineSightFactor / pt.distanceToLine(mate, target);
			}
		}
		// inversamente a dist pras paredes
		//parede cima
		riskValue += wallFactor/(height - pt.getY());
		//parede direita
		riskValue += wallFactor/(width - pt.getX());
		//parede baixo
		riskValue += wallFactor / pt.getY();
		//parede esquerda
		riskValue += wallFactor / pt.getX();

		return riskValue; 
	}
	//////////////////////////////////qq
		
	public boolean inMap(Ponto p){
		return (p.getX() < maxX && p.getX() > minX) && (p.getY() < maxY && p.getY() > minY);
	}
	///*
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


	///*
	public Ponto[] pontosRandom (Ponto eu) {
		int raio = 100;
		int raio2 = raio*raio;
		int qtdPontos = 30;
		Ponto[] vetorPoint = new Ponto[qtdPontos];

		for (int i = 0; i < qtdPontos; i++) {
			int rand = new Random().nextInt(2*raio);
			double x = eu.getX() - raio + rand;
			bot.out.println(rand);
			double raiz = Math.sqrt(raio2 - (x - eu.getX())*(x - eu.getX()));
			double y = eu.getY() + raiz;
			Ponto p = new Ponto(x, y);
			if(inMap(p)){
				vetorPoint[i]  = center;
			}else{
				vetorPoint[i]  = center;
			}
		}

		return vetorPoint;
	}
	
	public double clamp(double x, double min, double max){
		return  Math.max(min, Math.min(x, max) );
	}
	
	public Ponto toMap(Ponto p){
		return new Ponto( clamp(p.getX(), minX, maxX), clamp(p.getY(), minY, maxY));
	}
	
	
		//*/		
	////////////////////////////////////qqq
	public void move(){
		Ponto botPt = new Ponto(bot.getX(), bot.getY());

		double r1 = getRandomMovementDistance(), r2 = getRandomMovementDistance();
		Ponto p1 = new Ponto(bot.getHeadingRadians(), botPt, r1);
		if(!inMap(p1)){
			p1 = toMap(p1);
		}
		Ponto p2 = new Ponto(bot.getHeadingRadians()+Math.PI, botPt, r2);
		if(!inMap(p2)){
			p2 = toMap(p2);
		}
		
		double moveDirection = (risk(p1) < risk(p2)) ? r1 : r2;
		
		bot.ahead(moveDirection);
		/*
		
		Ponto botPt = new Ponto(bot.getX(), bot.getY());
		Ponto[] pontos = pontosRandom(botPt);
		
		double[] risks = new double[pontos.length];
		
		for (int i = 0; i < risks.length; i++){
			risks[i] = risk(pontos[i]);
		}
		
		Ponto destino = pontos[lowerRiskIndex(risks)]; 
		
		//bot.setTurnRightRadians(botPt.relativeBearing(destino) - bot.getHeadingRadians());
		//bot.ahead(botPt.distance(destino));
			
		//bot.out.println(bot.getTurnRemaining());
		*/
	}
	
	public void movement(){
		if(bot.getDistanceRemaining() <= 0.1){
			move();	
		}
	}
	
	public void turnBot(EnemyData enemy){
		// Calculate x and y to target
		double dx = enemy.x - bot.getX();
		double dy = enemy.y - bot.getY();
		// Calculate angle to target
		double theta = Math.toDegrees(Math.atan2(dx, dy));
		bot.setTurnRight(Utils.normalRelativeAngleDegrees(theta - bot.getHeading()) + 90 );
	}
	
	public double getRandomMovementDistance(){
		direction = -direction;
		return random.nextDouble() * movementFactor * direction;	
	}
	
	public void changeMovementFactor(){
		movementFactor = 20;
	}
}
