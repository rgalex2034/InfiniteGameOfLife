/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.core.utils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alex
 */
public class Point {
	
	public final int x, y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + this.x;
		hash = 11 * hash + this.y;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point other = (Point) obj;
		if (this.x != other.x || this.y != other.y) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param points - Array of arrays. The inner array has two elements, where
	 * the first is X and the second is Y.
	 * @return A set of Point objects.
	 */
	public static Set<Point> fromArrayPairs(int[][] points){
		Set<Point> set = new HashSet<Point>();
		for(int[] point : points){
			set.add(new Point(point[0], point[1]));
		}
		return set;
	}
	
}
