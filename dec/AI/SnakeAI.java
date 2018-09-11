package AI;

import java.util.ArrayList;
import java.util.List;

import Snake.ThreadsController;
import Snake.Tuple;
import AI.AStar;
import Snake.Window;

public class SnakeAI {
	static Tuple foodPosition = ThreadsController.foodPosition;
	public static ArrayList < Tuple > oldTuples = new ArrayList<Tuple>();
	
	public SnakeAI() {
		
	}
	
	public void setFoodPos(Tuple foodPos) {
		foodPosition = foodPos;
	}
	
	public List<Integer> getMoveDirection(Tuple snakeHead, ArrayList<Tuple> snakeBody) {
		int[][] blocked = new int[][] {};
		List<Integer> steps = new ArrayList<>();
		//for(int i = 0;i<=snakeBody.size()-1;i++){
			//Tuple tup = snakeBody.get(i);
			//int[] block = new int[]{snakeBody.get(i).getX(),snakeBody.get(i).getY()};
			//blocked[i] = block;
		//}
		AStar astar = new AStar();
		Tuple tup = astar.test(Window.playArea, Window.playArea, snakeHead.getX(), snakeHead.getY(), foodPosition.getX(), foodPosition.getY(), blocked);
		//System.out.println(tup.x + " " + tup.y);
		
		// moveDirection 
		// 1 > Right
		// 2 > Left
		// 3 > Up
		// 4 > Down
		
		if (tup != null) {
			if (snakeHead.x - tup.x > 0) {
				if(ThreadsController.directionSnake!=1) {
					steps.add(2);
					//System.out.println("x<: " + (snakeHead.x - tup.x));
				} else {
					steps.add(1);
				}
			}
			if (snakeHead.x - tup.x < 0) {
				if(ThreadsController.directionSnake!=2) {
					steps.add(1);
					//System.out.println("x>: " + (snakeHead.x - tup.x));
				} else {
					steps.add(2);
				}
			}
			if (snakeHead.y - tup.y < 0) {
				if(ThreadsController.directionSnake!=3) {
					steps.add(4);
					//System.out.println("y<: " + (snakeHead.y - tup.y));
				} else {
					steps.add(3);
				}
			} 
			if (snakeHead.y - tup.y > 0) {
				if(ThreadsController.directionSnake!=4) {
					steps.add(3);
					//System.out.println("y>: " + (snakeHead.y - tup.y));
				} else {
					steps.add(4);
				}
			}
		}
		
		return steps;
	}
}
