/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.view;

import app.gof.core.GameOfLife;
import app.gof.core.utils.MouseDistanceListener;
import app.gof.core.utils.Point;
import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author alex
 */
public class GameOfLifePanel extends JPanel{
	
	//Core game
	private GameOfLife gof;

	//Cell visual properties
	private Color cellColor;
	private int cellSize;

	//Offset position for mouse dragging
	private int offsetX;
	private int offsetY;
	
	public GameOfLifePanel(){
		this(10);
	}
	
	public GameOfLifePanel(int cellSize){
		this(cellSize, Color.BLACK, Color.WHITE);
	}
	
	public GameOfLifePanel(int cellSize, Color bg, Color cell){
		this.cellSize = cellSize;
		this.offsetX = 0;
		this.offsetY = 0;
		this.gof = new GameOfLife();
		//int[][] points  = {{2,2}, {3,1}, {3,2}, {3, 3}, {4,1}, {4,3}, {5,2}};
		//int[][] points  = {{6,6}, {7,5}, {7,6}, {7, 7}, {8,5}, {8,7}, {9,6}};
		//int[][] points  = {{40,41}, {40,42}, {40,43}, {40,44}, {40,45}, {40,46}, {40,47}, {40,48}, {40,49}, {40,50}, {40,51}, {40,52}, {40,53}, {40,54}, {40,55}};
		//this.gof.reset(Point.fromArrayPairs(points));
		this.gof.resetRandom(1000, 50, 50, 20, 10);
		this.gof.pause();
		this.setBackground(bg);
		this.cellColor = cell;

		this.setupListeners();

		Thread game = new Thread(this.gof);
		game.start();
	}

	private void setupListeners(){
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				if(gof.isPaused()){
					gof.resume();
				}else{
					gof.pause();
				}
			}
		});

		MouseDistanceListener mouseListener = new MouseDistanceListener() {

			@Override
			public void mouseDistanceDrag(MouseEvent me) {
				//System.out.println("X:" + me.getX() + " - Y:" + me.getY());
				offsetX += me.getX();
				offsetY += me.getY();
			}

			@Override
			public void mouseDistanceReleased(MouseEvent me) {
			}
		};
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);

		this.gof.addStepListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics grphcs) {
		super.paint(grphcs);
		Set<Point> points = this.gof.getPointSet();
		Iterator<Point> pointsIterator = points.iterator();
		
		while(pointsIterator.hasNext()){
			Point p = pointsIterator.next();
			if(!this.gof.isAlive(p)){
				continue;
			}
			int x = p.x * this.cellSize + offsetX;
			int y = p.y * this.cellSize + offsetY;
			
			grphcs.setColor(this.cellColor);
			grphcs.fillRect(x, y, this.cellSize, this.cellSize);
			grphcs.setColor(this.getBackground());
			grphcs.drawRect(x, y, this.cellSize, this.cellSize);
		}
	}
	
}
