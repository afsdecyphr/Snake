package AI;

import java.util.*;

import Snake.ThreadsController;
import Snake.Tuple;

public class Star {
    public final int DIAGONAL_COST = 14;
    public final int V_H_COST = 10;
    public ArrayList < Tuple > oldTuples = null;

    class Cell {
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    }

    //Blocked cells are just null Cell values in grid
    Cell[][] grid = new Cell[50][50];
    //Blocked cells are just null Cell values in grid
    Cell[][] grid2 = new Cell[50][50];

    PriorityQueue < Cell > open;

    boolean closed[][];
    int startI, startJ;
    int endI, endJ;

    public void setBlocked(int i, int j) {
        grid[i][j] = null;
    }

    public void setStartCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    void checkAndUpdateCost(Cell current, Cell t, int cost) {
        if (t == null || closed[t.i][t.j]) return;
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
            if (!inOpen) open.add(t);
        }
    }

    public void AStar() {

        //add the start location to open list.
        //System.out.println(startI + " ; " + startJ);
        if (grid[startI][startJ] == null) {
            System.out.println("null");
        } else {
            open.add(grid[startI][startJ]);
        }

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            closed[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }
    }

    /*
    Params :
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    */
    public Tuple test(int x, int y, int si, int sj, int ei, int ej, int[][] blocked) {
        boolean printGrid = true;
        //Reset
        grid = new Cell[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
	        Cell c1 = (Cell)o1;
	        Cell c2 = (Cell)o2;
	        return c1.finalCost<c2.finalCost?-1:
	        	c1.finalCost>c2.finalCost?1:0;
        });
        //Set start position
        setStartCell(si, sj); //Setting to 0,0 by default. Will be useful for the UI part

        //Set End Location
        setEndCell(ei, ej);

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
                //                  System.out.print(grid[i][j].heuristicCost+" ");
            }
            //              System.out.println();
        }
        grid[si][sj].finalCost = 0;

        /*
          Set blocked cells. Simply set the cell values to null
          for blocked cells.
        */
        if (oldTuples != null) {
            for (int ii = 0; ii <= oldTuples.size() - 1; ii++) {
                grid[oldTuples.get(ii).getX()][oldTuples.get(ii).getY()] = new Cell(oldTuples.get(ii).getX(), oldTuples.get(ii).getY());
            }
        }
        for (int i = 0; i <= ThreadsController.positions.size() - 1; i++) {
            Tuple tup = ThreadsController.positions.get(i);
            grid[tup.getX()][tup.getY()] = null;
        }
        
        AStar();
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < x; ++j) {
                grid2[i][j] = new Cell(i, j);
            }
        }

        Tuple tup = null;
        Tuple tup2 = null;
        boolean gotTup = false;
        ArrayList < Cell > cells = new ArrayList < Cell > ();
        if (closed[endI][endJ]) {
            //Trace back the path 
            System.out.println("Path: ");
            Cell current = grid[endI][endJ];
            Cell currentCounter = grid[endI][endJ];
            Cell current_bak = grid[endI][endJ];
            cells.add(current);
            grid2[current.i][current.j] = null;
            System.out.println(current);
            gotTup = true;
            int ii = 0;
            int count = 0;
            while (currentCounter.parent != null) {
            	count++;
            	currentCounter = currentCounter.parent;
            }
            System.out.println("count: " + count);
            while (current_bak.parent != null) {
                //System.out.print(" -> " + current_bak.parent);
                grid2[current_bak.parent.i][current_bak.parent.j] = null;
                current_bak = current_bak.parent;
                cells.add(current_bak);
                System.out.println("ii: " + ii);
                if (ii == 1) {
                    tup2 = new Tuple(current.i, current.j);
                    System.out.println("ii == 1");
                } else if (ii == count-2) {
                    tup = new Tuple(current_bak.i, current_bak.j);
                    System.out.println("ii == count-1");
                } else {
                    tup2 = new Tuple(current.i, current.j);
                    System.out.println("else");
                }
                ii++;
            }
            System.out.println();
        } else {
            System.out.println("No possible path");
            Cell current = grid[endI][endJ];
            cells.add(current);
            //System.out.print(current);
            tup = new Tuple(current.i, current.j);
        }

        if (printGrid) {
            //Display initial map
            System.out.println("Grid: ");
            for (int i = 0; i < x; ++i) {
                for (int j = 0; j < y; ++j) {
                    if (i == si && j == sj) {
                        System.out.print("S"); //Source
                    } else if (i == ei && j == ej) {
                        System.out.print("D"); //Destination
                    } else if (grid2[i][j] == null) {
                        System.out.print("%");
                    } else if (grid[i][j] != null) {
                        System.out.print("#");
                    } else {
                        System.out.print("B");
                    }
                }

                System.out.println();
            }
        }
        
        oldTuples = ThreadsController.positions;

        System.out.println("headpos: [" + si + ", " + sj + "]");
        if (tup != null) {
            System.out.println("tup: " + tup);
            return tup;
        } else {
            System.out.println("tup2: " + tup2);
            return tup2;
        }
    }
}