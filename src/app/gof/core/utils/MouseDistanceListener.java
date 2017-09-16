/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gof.core.utils;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

/**
 *
 * @author alex
 */
public abstract class MouseDistanceListener extends MouseAdapter{

	private int initialX;
	private int initialY;

	private int dragStepX;
	private int dragStepY;

	/*private LinkedList<MouseMotionListener> draggingListeners;
	private LinkedList<MouseListener> endListeners;

	public MouseDistanceListener(){
		this.draggingListeners = new LinkedList<>();
		this.endListeners = new LinkedList<>();
	}

	public void addDraggedListener(MouseMotionListener listener){
		this.draggingListeners.add(listener);
	}

	public void addReleasedListeners(MouseListener listener){
		this.endListeners.add(listener);
	}*/

	@Override
	public void mouseDragged(MouseEvent me) {
		MouseEvent event = new MouseEvent(me.getComponent(), me.getID(), me.getWhen(), me.getModifiers(), me.getX()-dragStepX, me.getY()-dragStepY, me.getClickCount(), false);
		this.mouseDistanceDrag(event);
		/*for (MouseMotionListener dragListener : draggingListeners) {
			dragListener.mouseDragged(event);
		}*/
		this.dragStepX = me.getX();
		this.dragStepY = me.getY();
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		MouseEvent event = new MouseEvent(me.getComponent(), me.getID(), me.getWhen(), me.getModifiers(), me.getX()-initialX, me.getY()-initialX, me.getClickCount(), false);
		this.mouseDistanceReleased(event);
		/*for (MouseListener endListener : endListeners) {
			endListener.mouseReleased(event);
		}*/
	}

	@Override
	public void mousePressed(MouseEvent me) {
		this.initialX = me.getX();
		this.initialY = me.getY();
		this.dragStepX = this.initialX;
		this.dragStepY = this.initialY;
		//System.out.println("pressed");
	}

	public abstract void mouseDistanceDrag(MouseEvent me);

	public abstract void mouseDistanceReleased(MouseEvent me);
}
