package boladoNaoConta;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MinimumRisk {

	Vector<EnemyData> enemies;
	Vector<Ponto> teammates;
	
	Ponto target;
	Ponto center;
	AdvancedRobot bot;
	
	double movementFactor = 200, direction = -1;
	Random random;
	
	Ponto corner1, corner2, corner3, corner4;
	
	double energyFactor = 300, wallFactor, wallNm = 5, centerFactor, lineSightFactor;
	double width, height;
	double botMaxSize;
	
	double minX,maxX,minY,maxY;
	
	
	public MinimumRisk(AdvancedRobot r){
		bot = r;
		botMaxSize = 2 * Math.max(bot.getWidth(), bot.getHeight()); 
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
		
		enemies = new Vector<EnemyData>();
		teammates = new Vector<Ponto>();
		random = new Random();
	}
	///////////////////////////////////////////////////////////
	public void addEnemy(EnemyData e){
		if((enemies.isEmpty()) && !enemies.contains(e))
			enemies.add(e);
	}
	
	public void setTarget(EnemyData e){
		target = new Ponto(e.x, e.y);
	}
	
	public void addTeammate(EnemyData friend){
		addTeammate(new Ponto(friend.x, friend.y));
	}
	
	public void addTeammate(Ponto pt){
		if(pt!=null)
		if(!teammates.contains(pt))
			teammates.add(pt);
	}
	
	public void clearLists(){
		teammates.clear();
		enemies.clear();
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
				riskValue += 10000*lineSightFactor / pt.distanceToLine(mate, target);
			}
		}
		
		for (Ponto mate : teammates){
			System.out.println("caraii");
			riskValue += 1e222*lineSightFactor / pt.distance(mate);
		}
		
		// inversamente a dist pras paredes
		//parede cima
		riskValue += wallNm * wallFactor/(height - pt.getY());
		//parede direita
		riskValue += wallNm * wallFactor/(width - pt.getX());
		//parede baixo
		riskValue += wallNm * wallFactor / pt.getY();
		//parede esquerda
		riskValue += wallNm * wallFactor / pt.getX();

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
		int qtdPontos = 5;
		Ponto[] vetorPoint = new Ponto[qtdPontos];

		for (int i = 0; i < qtdPontos; i++) {
			int rand = new Random().nextInt(2*raio);
			double x = eu.getX() - raio + rand;
			//bot.out.println(rand);
			double raiz = Math.sqrt(raio2 - (x - eu.getX())*(x - eu.getX()));
			double y = eu.getY() + raiz;
			Ponto p = new Ponto(x, y);
			if(inMap(p)){
				vetorPoint[i]  = p;
			}else{
				vetorPoint[i]  = toMap(p);
			}
		}

		return vetorPoint;
	}
	
	public double clamp(double x, double min, double max){
		return  Math.max(min, Math.min(x, max) );
	}
	
	public Ponto toMap(Ponto p){
		return center;
		//return new Ponto( clamp(p.getX(), minX, maxX), clamp(p.getY(), minY, maxY));
	}
	
	
		//*/		
	////////////////////////////////////qqq
	public void move(){
		//*
		//Ponto dest;
		Ponto botPt = new Ponto(bot.getX(), bot.getY());
		//
		if(inMap(botPt)){
			/*
			Ponto[] pontos = pontosRandom(botPt);
			
			double[] risks = new double[pontos.length];
			
			for (int i = 0; i < risks.length; i++){
				risks[i] = risk(pontos[i]);
			}
			
			dest = pontos[lowerRiskIndex(risks)]; 
		
			/*/
			double r1 = getRandomMovementDistance(), r2 = -r1;//getRandomMovementDistance();
			Ponto p1 = new Ponto(bot.getHeadingRadians(), botPt, r1);
			Ponto p2 = new Ponto(bot.getHeadingRadians()+Math.PI, botPt, r2);
			/*
			if(!inMap(p1)){
				p1 = toMap(p1);
			}
			
			if(!inMap(p2)){
				p2 = toMap(p2);
			}
			*/
			double movePt = (risk(p1) <= risk(p2)) ? r1 : r2;
			
			bot.setAhead(movePt);
			
			//dest = movePt;
			//*/
			//goTo(movePt);//, botPt);
			//*
		}else{
			goTo(center);//, botPt);
			//dest = center;
		}
		//return dest;
		/*/
			/*
			
		Ponto botPt = new Ponto(bot.getX(), bot.getY());
		Ponto[] pontos = pontosRandom(botPt);
		
		double[] risks = new double[pontos.length];
		
		for (int i = 0; i < risks.length; i++){
			risks[i] = risk(pontos[i]);
		}
		
		Ponto destino = pontos[lowerRiskIndex(risks)]; 
		
		bot.setTurnRightRadians(botPt.relativeBearing(destino) - bot.getHeadingRadians());
		bot.setAhead(botPt.distance(destino));
			
		//bot.out.println(bot.getTurnRemaining());
		//*/
	}
	
	public void goTo(Ponto pt) {
	    int x = (int) pt.getX(), y = (int) pt.getY(); 
		double a;
	    bot.setTurnRightRadians(Math.tan(
	        a = Math.atan2(x -= (int) bot.getX(), y -= (int) bot.getY()) 
	              - bot.getHeadingRadians()));
	    bot.setAhead(Math.hypot(x, y) * Math.cos(a));
	}
	
	void gofghjTo(Ponto pt, Ponto botPt){
		double dist =  pt.distance(botPt);//( -Math.signum(bot.getVelocity()) + 0.01 ) *
		double turn = angle(pt);
		if(turn > Math.PI/2){
			turn = -(turn - Math.PI/2); 
			dist = -dist;
		}
		bot.setTurnRightRadians(turn);
		bot.setAhead(-dist);
		System.out.println(dist + ", " + turn);
	}
	
	double angle(Ponto pt){
		double x = bot.getX(), y = bot.getY();
		
		x -= pt.getX();
		y -= pt.getY();
		double theta = Math.atan2(x, y);
		if(theta < 0){
			theta += 2*Math.PI;
		}
		
		return theta - bot.getHeadingRadians();
		
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
