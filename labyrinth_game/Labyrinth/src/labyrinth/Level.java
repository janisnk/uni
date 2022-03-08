/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

/**
 *
 * @author sinko
 */
public class Level {
    private final int WALL_SIZE = 40;
    private final int FINISH_SIZE = 40;
    private final int DRAGON_SIZE = 60;
    
    
    ArrayList<Wall> walls;
    Hero hero;
    Dragon dragon;
    Finish finish;

    public Level(String levelMap) throws IOException {
        drawLevel(levelMap);      
    }
    
    /**
     * Loads the level from txt file
     * @param levelMap
     * @throws FileNotFoundException
     * @throws IOException 
     */
    
    public void drawLevel(String levelMap) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(levelMap));
        walls = new ArrayList<>();
        int y = 0;
        String line;
        while((line = br.readLine()) != null){
            int x = 0;
            for(char elem : line.toCharArray()){
                Image img = null;
                switch(elem){
                    case '#' : img = new ImageIcon("res/wall.png").getImage();
                               walls.add(new Wall(x*WALL_SIZE,y*WALL_SIZE, WALL_SIZE, WALL_SIZE, img));
                               break;
                    case '*' : img = new ImageIcon("res/flag_egy.png").getImage();
                               finish = new Finish(x*FINISH_SIZE,y*FINISH_SIZE,FINISH_SIZE,FINISH_SIZE, img);
                               break;           
                    case '!' : img = new ImageIcon("res/dragon3.jpg").getImage();
                               dragon = new Dragon(x*DRAGON_SIZE,y*DRAGON_SIZE,DRAGON_SIZE,DRAGON_SIZE, img);
                               break; 
                }
                x++;
            }
            y++;
        }
    }
    
    /**
     * Draws elements with the restrictios: only draws wall and finish sprites
     * if they close enough to the hero
     * @param g 
     */
    public void draw(Graphics g){
        
        hero.draw(g);
        dragon.draw(g);   
        
        for (Wall w : walls){
            if(Math.abs(w.getX() - hero.getX()) <= 120 &&
                    Math.abs(w.getY() - hero.getY()) <= 120 ){
               w.draw(g);     
            }
           
        }
        
        if( Math.abs(hero.getX() - finish.getX()) <= 120 &&
                Math.abs(hero.getY() - finish.getY()) <=120){
             finish.draw(g);   
        }
        
        
    }
    
    
   public boolean isWin(){
       return hero.inFinish(finish);
   }
   
   public boolean isOver(){
       return hero.isDead(dragon);
   }
   
   /**
    * Keeps dragon moving while checking the collision
    */
   public void dragonMoving(){
       dragon.moveY();
       dragon.moveX();
       for(Wall w: walls){
           dragon.changeMove(w);
       }
   }
   
   /**
    * Checks the collision of the hero and wall sprites
    * @return 
    */
   public boolean checkCollide(){
       boolean collide = false;               
       for (Wall w : walls){
           if(w.collides(hero)){
               collide = true;
           }
       }
       return collide;
   }
    
  
}
