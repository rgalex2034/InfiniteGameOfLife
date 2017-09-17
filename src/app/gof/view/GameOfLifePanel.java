/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.view;

import app.gof.core.GameOfLife;
import app.gof.core.utils.MouseDistanceListener;
import app.gof.core.utils.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
		//Setup visual properties
		this.cellSize = cellSize;
		this.cellColor = cell;
		this.setBackground(bg);

		//Setup camera
		this.offsetX = 0;
		this.offsetY = 0;

		//Setup Game of Life
		this.gof = new GameOfLife();
		//int[][] points  = {{6,6}, {7,5}, {7,6}, {7, 7}, {8,5}, {8,7}, {9,6}};
		//int[][] points  = {{40,41}, {40,42}, {40,43}, {40,44}, {40,45}, {40,46}, {40,47}, {40,48}, {40,49}, {40,50}, {40,51}, {40,52}, {40,53}, {40,54}, {40,55}};
		//this.gof.reset(Point.fromArrayPairs(points));
		this.gof.resetRandom(1000, 50, 50, 20, 10);
		this.gof.pause(); //We want the game to start paused.

		//Setup listeners
		this.setupListeners();

		//Start game of life
		Thread game = new Thread(this.gof);
		game.start();
	}

	private void setupListeners(){
		/**
		 * We add a listener when clicking a mouse button
		 * to pause or resume the game.
		 */
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

		/**
		 * We add a listener to get the distance when the mouse
		 * is dragged and the mouse wheel moved to move the offset
		 * for moving the screen, resize the cells and repaint the game.
		 */
		MouseDistanceListener mouseListener = new MouseDistanceListener() {

			@Override
			public void mouseDistanceDrag(MouseEvent me) {
				//System.out.println("X:" + me.getX() + " - Y:" + me.getY());
				offsetX += me.getX();
				offsetY += me.getY();
				repaint();
			}

			@Override
			public void mouseDistanceReleased(MouseEvent me) {
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent mwe) {
				//System.out.println("Amount:" + mwe.getScrollAmount() + " - Type:" + mwe.getScrollType() + " more:" + mwe.getUnitsToScroll());
				cellSize += mwe.getUnitsToScroll() > 0 ? -1 : 1;
				repaint();
			}
		};
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		this.addMouseWheelListener(mouseListener);

		/**
		 * We add an ActionListener to repaint the game of life
		 * everytime we make a new step.
		 */
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
