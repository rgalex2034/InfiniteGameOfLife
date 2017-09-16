/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.core;

import app.gof.core.utils.Point;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class GameOfLife implements Runnable{
	
	//Cell list
	private HashMap<Point, Cell> cellMap;
	private HashMap<Point, Cell> nextCellMap;

	//Check for running cycles
	private boolean running;
	private boolean paused;
	
	//List of listeners who get the event every cycle
	private LinkedList<ActionListener> listeners;
	
	public GameOfLife(){
		this(new HashMap<Point, Cell>());
	}
	
	public GameOfLife(HashMap<Point, Cell> cellMap){
		this.cellMap = cellMap;
		this.nextCellMap = null;
		this.running = true;
		this.paused = false;
		this.listeners = new LinkedList<>();
	}

	public void addStepListener(ActionListener listener){
		this.listeners.addFirst(listener);
	}

	public boolean isPaused(){
		return this.paused;
	}

	public boolean isRunning(){
		return this.running;
	}

	public void pause(){
		this.paused = true;
	}

	public void resume(){
		this.paused = false;
	}

	public void stop(){
		this.running = false;
	}
	
	public Set getPointSet(){
		return this.cellMap.keySet();
	}

	public Cell getSafeCell(Point p){
		Cell c = this.cellMap.get(p);
		return c == null ? new Cell(false) : c;
	}
	
	public boolean isAlive(Point p){
		Cell c = this.cellMap.get(p);
		return c == null ? false : c.isAlive();
	}
	
	private void addPointSet(Set<Point> pointSet){
		Iterator<Point> it = pointSet.iterator();
		while(it.hasNext()){
			Point p = it.next();
			Cell actual = this.cellMap.get(p);
			if(actual != null){
				actual.revive();
			}else{
				this.cellMap.put(p, new Cell(true));
			}
		}
	}

	public void resetRandom(int numberOfCells, int maxX, int maxY){
		this.resetRandom(numberOfCells, maxX, maxY, 0, 0);
	}

	public void resetRandom(int numberOfCells, int width, int height, int offsetX, int offsetY){
		Set<Point> points = new HashSet<>();
		for (int i = 0; i < numberOfCells; i++) {
			int x = (int)(Math.random() * width) + offsetX;
			int y = (int)(Math.random() * height) + offsetY;
			Point p = new Point(x, y);
			if(points.contains(p)){
				i--;
			}else{
				points.add(p);
			}
		}
		this.reset(points);
	}
	
	public void reset(Set<Point> pointSet){
		this.cellMap = new HashMap<>();
		this.addPointSet(pointSet);
	}

	public void nextStep(){
		this.nextCellMap = new HashMap<>();
		Iterator<Point> it = this.cellMap.keySet().iterator();
		while(it.hasNext()){
			Point p = it.next();
			if(this.isAlive(p)){
				Cell nextCell = this.nextCellStatus(p);
				this.nextCellMap.put(p, nextCell);
				List<Point> neighbours = this.getAdjacentPoints(p);
				for (Point neighbour : neighbours) {
					nextCell = this.nextCellStatus(neighbour);
					this.nextCellMap.put(neighbour, nextCell);
				}
			}
		}
		this.cellMap = this.nextCellMap;
		this.nextCellMap = null;
	}

	private List<Point> getAdjacentPoints(Point p){
		LinkedList<Point> list = new LinkedList<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if(x!=0 || y!=0){
					list.addFirst(new Point(p.x+x, p.y+y));
				}
			}
		}
		return list;
	}

	private Cell nextCellStatus(Point p){
		Cell c = this.getSafeCell(p);
		int livingCells = 0;
		List<Point> neighbours = this.getAdjacentPoints(p);
		
		for (Point neighbour : neighbours) {
			Cell sideCell = this.getSafeCell(neighbour);
			if(sideCell.isAlive()){
				livingCells++;
			}
		}

		if(c.isAlive()){
			if(livingCells < 2){
				return new Cell(false);
			}else if(livingCells >= 2 && livingCells <=3){
				return new Cell(true);
			}else{
				return new Cell(false);
			}
		}else{
			if(livingCells == 3){
				return new Cell(true);
			}else{
				return new Cell(false);
			}
		}
	}

	@Override
	public void run() {
		while(this.running){
			try {
				Thread.sleep(100);
				if(!this.paused){
					this.nextStep();
				}
				for (ActionListener listener : listeners) {
					listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "next_step"));
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
}
