/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author sinko
 */
public class GameEngine extends JPanel {
    
    
    private final int FPS = 240;
    private final int HERO_MOVE = 1;
    private final int HERO_SIZE = 50;
    
    
   
    private boolean paused = false;
    private Image background;
    private int levelNum = 1;
    private Level level;
    private Timer newFrameTimer;
    private Clock clock;
    private JLabel showTime;
    
    /**
     * Creating the engine, setting up controlling keys, background and
     * the timer
     */

    public GameEngine() {
        super();
        clock = new Clock();
        showTime = new JLabel(clock.getTime() + " s ");
        showTime.setBackground(Color.WHITE);
        showTime.setForeground(Color.red);
        showTime.setPreferredSize(new Dimension(40,40));
        background = new ImageIcon("res/black.png").getImage();
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                level.hero.setVelX(-HERO_MOVE);
                level.hero.setVelY(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                level.hero.setVelY(0);
                level.hero.setVelX(HERO_MOVE);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                level.hero.setVelX(0);               
                level.hero.setVelY(HERO_MOVE);
               
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                level.hero.setVelX(0);
                level.hero.setVelY(-HERO_MOVE);
                
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                paused = !paused;
            }
        });
        
        
        add(showTime);
        restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
        
    }
    
    /**
     * Reloading the game, checking if the file exists
     */
    
    public void restart() {
        
        try {
            level = new Level("res/levels/level" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        showTime.setText(clock.getTime() + " s ");
        Image heroImg = new ImageIcon("res/knight_egy.png").getImage();
        level.hero = new Hero(0,500,HERO_SIZE,HERO_SIZE, heroImg);
        
        Image dragonImg = new ImageIcon("res/dragon3.png").getImage();
        level.dragon = new Dragon(level.dragon.getX(), level.dragon.getY(), 
                level.dragon.getWidth(), level.dragon.getHeight(), dragonImg);
        
        Image finishImg = new ImageIcon("res/flag_egy.png").getImage();
        level.finish = new Finish(level.finish.getX(), level.finish.getY(), 
                level.finish.getWidth(), level.finish.getHeight(), finishImg);
        
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 800, 600, null);
        level.draw(grphcs);
       
    }
    
    /**
     * Main game loop: checking conditions: if game is paused and if hero
     * is dead or won. 
     * Automaticly loads the next level if it is not the last level.
     * Ensures input to save the score with name.
     */
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            clock.setPaused(paused);
            if (!paused) {

                level.dragonMoving();
                level.hero.move();
                if (level.checkCollide()) {
                    level.hero.setVelX(0);
                    level.hero.setVelY(0);
                }

                if (level.isOver()) {
                    
                    String name;
                    name = JOptionPane.showInputDialog("GAME OVER! GIVE YOUR NAME: ");
                    if (!name.isEmpty()) {
                        try {
                            HighScores highScores = new HighScores(10);
                            highScores.putHighScore(name, levelNum -1, clock.getTime());
                        } catch (SQLException ex) {
                            Logger.getLogger(LabyrinthMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        }
                    }
                    levelNum = 1;
                    clock.setTime(0);
                    restart();
                }
                if (level.isWin()) {
                    if (levelNum <= 10) {
                        ++levelNum;
                        restart();
                    } else {
                        showTime.setText("HERO: " + clock.getTime() + " s ");
                        String name;
                        name = JOptionPane.showInputDialog("YOU ARE THE CHAMPION! GIVE YOUR NAME: ");
                        if (!name.isEmpty()) {
                            try {
                                HighScores highScores = new HighScores(10);
                                highScores.putHighScore(name, levelNum , clock.getTime());
                            } catch (SQLException ex) {
                                Logger.getLogger(LabyrinthMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else {
                    repaint();
                    showTime.setText(clock.getTime() + " s ");
                }
            }

        }

    }
}
