/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.core;

/**
 *
 * @author alex
 */
public class Cell {
	
	private boolean alive;
	
	public Cell(){
		this(true);
	}
	
	public Cell(boolean isAlive){
		this.alive = isAlive;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
	public void die(){
		this.alive = false;
	}
	
	public void revive(){
		this.alive = true;
	}
	
}
