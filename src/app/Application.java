/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.gof.view.GameOfLifePanel;
import javax.swing.JFrame;

/**
 *
 * @author alex
 */
public class Application {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Game of Life");
		window.add(new GameOfLifePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1024, 768);
		window.setVisible(true);
	}
	
}
