/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

import java.awt.Image;

/**
 *
 * @author sinko
 */
public class Dragon extends Sprite{
    
    private double velX;
    private double velY;
    
   
    /**
     * Auxilary variable to create random velocities
     */
    private static int[] directs = new int[] {-2,-1,0,1,2};
    
    /**
     * Creating instance with random starting velocities
     * @param x
     * @param y
     * @param width
     * @param height
     * @param image 
     */

    public Dragon( int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        int a;
        int b;
        do{
         a = (int) Math.floor((Math.random() * 5));
         b = (int)Math.floor((Math.random() * 5));
        } while(directs[a] == 0 || directs[b] == 0 );
        this.velX =  directs[a];
        this.velY = directs[b]; 
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }
     
    
    
     public void moveX() {
        x += velX;
        if (x + width >= 800 || x <= 0) {
            invertVelX();
        }
    }
     
    public void moveY() {
        y += velY;
        if (y + height >= 600 || y <= 0) {
            invertVelY();
        }
    } 
    
    public void invertVelX(){
        setVelX(-velX);
    }
    
    public void invertVelY(){
        setVelY(-velY);
    }
    
    
    /**
     * If collides, invertes the velocities and then modifies to
     * make directions random
     * @param w 
     */
    public void changeMove(Wall w){
        if(collides(w)){
            
         invertVelX();
         invertVelY();
         int nx ;
         int ny ;
         
         do{
             nx = (int) Math.floor((Math.random() * 5));
             ny = (int) Math.floor((Math.random() * 5));
         } while(directs[nx] == 0 || directs[ny] == 0);
         
         int randX = Math.abs(directs[nx]);
         int randY = Math.abs(directs[ny]); 
          
         double newX = randX*velX + 0.5*randX;
         double newY = randY*velY + 0.5*randY;
         
         if(Math.abs(newX) > 3){
             newX = newX%3;
         }
         if(Math.abs(newY) > 3){
             newY = newY%3;
         }
         setVelX(newX);
         setVelY(newY);   
        }
        
    }
    
}
