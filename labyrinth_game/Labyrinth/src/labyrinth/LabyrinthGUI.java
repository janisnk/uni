/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author sinko
 */
public class LabyrinthGUI {
    private JFrame frame;
    private GameEngine gameArea;
    private JFrame results;


    public LabyrinthGUI() {
                
        frame = new JFrame("Labyrinth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Menu");
        
        results = new JFrame("TOP 10");
        results.setSize(new Dimension(200,600));
        
        JMenuItem newGame = new JMenuItem(new AbstractAction("New Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
               gameArea.restart();
            }
        });
        JMenuItem highScores = new JMenuItem(new AbstractAction("High Scores") {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    HighScores highScores = new HighScores(10);
                    JTextArea points = new JTextArea(highScores.getHighScores().toString().replace(',', '\n'));
                    points.setEditable(false);
                    results.add(points);
                    results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    results.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(LabyrinthMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

            }
        });
        
        menuGame.add(newGame);
        menuGame.add(highScores);
        
        menuBar.add(menuGame);
        
                
        
        frame.setJMenuBar(menuBar);

        
        gameArea = new GameEngine();
        
        frame.getContentPane().add(gameArea);
        
          
       
        
        frame.setPreferredSize(new Dimension(800, 620));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
    
}
