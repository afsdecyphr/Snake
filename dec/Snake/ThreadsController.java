package Snake;
import java.awt.Color;
import java.util.ArrayList;
import AI.SnakeAI;

public class ThreadsController extends Thread {
	 public static ArrayList<ArrayList<DataOfSquare>> Squares= new ArrayList<ArrayList<DataOfSquare>>();
	 public static Tuple headSnakePos;
	 public static int sizeSnake=3;
	 static long speed = 1000;
	 public static boolean end = false;
	 public static boolean pause = false;
	 public static int directionSnake;
	 public static int speedup = 0;
	 static int runs = 0;
	 static int total = 0;
	 static int avg = 0;
	 static SnakeAI snakeai = new SnakeAI();
	    public ArrayList<Tuple> oldTuples = null;
	 
	    class Cell{  
	        int heuristicCost = 0; //Heuristic cost
	        int finalCost = 0; //G+H
	        int i, j;
	        Cell parent; 
	        
	        Cell(int i, int j){
	            this.j = j; 
	        }
	        
	        @Override
	        public String toString(){
	            return "["+this.i+", "+this.j+"]";
	        }
	    }

	 public static ArrayList<Tuple> positions = new ArrayList<Tuple>();
	 public static Tuple foodPosition;
	 
	 ThreadsController(Tuple positionDepart){
		Squares=Window.Grid;
		
		headSnakePos=new Tuple(positionDepart.x,positionDepart.y);
		directionSnake = 1;

		Tuple headPos = new Tuple(headSnakePos.getX(),headSnakePos.getY());
		positions.add(headPos);
		
		foodPosition= new Tuple(Window.height-1,Window.playArea-1);
		spawnFood(foodPosition);

		sizeSnake=3;
		speed = 100;
		end = false;
		pause = false;
		System.out.println("tc");
	 }
	 
	 public void run() {
		 while(true){
			 for (int step : snakeai.getMoveDirection(headSnakePos, positions)) {
				 directionSnake = step;
				 moveInterne(directionSnake);
				 if (runs != 0) {
					 avg = total/(runs);
				 } else {
					 avg = 0;
				 }
				 Window.label.setText("Length: " + sizeSnake + "         " + "Max Length: " + Window.maxLength + "         " + "Average: " + avg + "         " + "Total: " + total + "         " + "Run Count: " + (runs));
				 if (sizeSnake > Window.maxLength) {
					 Window.maxLength = sizeSnake;
				 }
				 if (sizeSnake > speedup+5) {
					 speedup = sizeSnake;
					 if (speed > 30) {
						 speed = speed-30;
					 }
				 }
				 checkCollision();
				 moveExterne();
		         Squares.get(headSnakePos.x).get(headSnakePos.y).lightMeUp(3);
				 deleteTail();
				 pauser();
		         oldTuples = ThreadsController.positions;
			 }
	         Squares.get(headSnakePos.x).get(headSnakePos.y).lightMeUp(3);
		 }
	 }
	 
	 public static void pauser(){
		 while (pause) {
			 try {
					sleep(speed);
				 } catch (InterruptedException e) {
						e.printStackTrace();
				 }
			 if (pause == false)
				 continue;
		 }
		 try {
			sleep(speed);
		 } catch (InterruptedException e) {
				e.printStackTrace();
		 }
	 }
	 
	 private void checkCollision() {
		 Tuple posCritique = positions.get(positions.size()-1);
		 for(int i = 0;i<=positions.size()-2;i++){
			 boolean biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY()==positions.get(i).getY();
			 if(biteItself){
				 Squares.get(foodPosition.x).get(foodPosition.y).lightMeUp(2);
				 total = total + sizeSnake;
				 end = true;
				 stopTheGame();
			 }
		 }
		 
		 boolean eatingFood = posCritique.getX()==foodPosition.x && posCritique.getY()==foodPosition.y;
		 if(eatingFood){
			 sizeSnake=sizeSnake+1;
			 foodPosition = getValAleaNotInSnake();
			 spawnFood(foodPosition);	
			 System.out.println(foodPosition);
		 }

		 snakeai.setFoodPos(foodPosition);
	 }
	 
	 public void stopTheGame(){
			if(end == true)
			{
				ThreadsController.headSnakePos=new Tuple(10,10);
				ThreadsController.directionSnake = 1;
				ThreadsController.sizeSnake = 3;
				 foodPosition = getValAleaNotInSnake();
				 spawnFood(foodPosition);	
				 snakeai.setFoodPos(foodPosition);
				 deleteTail();
				ThreadsController.end = false;
				Window.label.setForeground(Color.black);
				runs++;
			} else {
				try {
					sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 }
	 
	 private static void spawnFood(Tuple foodPositionIn){
		 Squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1);
		 snakeai.setFoodPos(foodPositionIn);
	 }
	 
	 private static Tuple getValAleaNotInSnake(){
		 Tuple p ;
		 int ranX= 0 + (int)(Math.random()*(Window.playArea-1)); 
		 int ranY= 0 + (int)(Math.random()*(Window.playArea-1)); 
		 p=new Tuple(ranX,ranY);
		 for(int i = 0;i<=positions.size()-1;i++){
			 System.out.println("pX: " + p.getX() + " ; pos(i)X: " + positions.get(i).getX() + "");
			 if(p.getX()==positions.get(i).getX() && p.getY()==positions.get(i).getY()){
				 ranX= 0 + (int)(Math.random()*(Window.playArea-1)); 
				 ranY= 0 + (int)(Math.random()*(Window.playArea-1)); 
				 p=new Tuple(ranX,ranY);
				 i=0;
			 }
			 System.out.println("try");
		 }
		 return p;
	 }
	 
	 private static void moveInterne(int dir){
		 switch(dir){
		 	case 4:
				 headSnakePos.ChangeData(headSnakePos.x,(headSnakePos.y+1)%Window.playArea);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 3:
		 		if(headSnakePos.y-1<0){
		 			 headSnakePos.ChangeData(headSnakePos.x,Window.playArea - 1);
		 		 }
		 		else{
				 headSnakePos.ChangeData(headSnakePos.x,Math.abs(headSnakePos.y-1)%Window.playArea);
		 		}
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 2:
		 		 if(headSnakePos.x-1<0){
		 			 headSnakePos.ChangeData(Window.playArea - 1,headSnakePos.y);
		 		 }
		 		 else{
		 			 headSnakePos.ChangeData(Math.abs(headSnakePos.x-1)%Window.playArea,headSnakePos.y);
		 		 } 
		 		positions.add(new Tuple(headSnakePos.x,headSnakePos.y));

		 		break;
		 	case 1:
				 headSnakePos.ChangeData(Math.abs(headSnakePos.x+1)%Window.playArea,headSnakePos.y);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		 break;
		 }
	 }
	 
	 private static void moveExterne(){
		 for(Tuple t : positions){
			 int y = t.getY();
			 int x = t.getX();
			 Squares.get(x).get(y).lightMeUp(0);
			 
		 }
	 }
	 
	 private void deleteTail(){
		 int cmpt = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 Tuple t = positions.get(i);
				 Squares.get(t.x).get(t.y).lightMeUp(2);
			 }
			 else{
				 cmpt--;
			 }
		 }
		 cmpt = sizeSnake;
		 System.out.println(cmpt);
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 System.out.println("rem");
				 positions.remove(i);
			 }
			 else{
				 cmpt--;
			 }
		 }
	 }
}
