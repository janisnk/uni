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
public class Hero extends Sprite {
    
    private double velX;
    private double velY;
    

    public Hero(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        velX = 0;
        velY = 0;
    }
    
    /**
     * Keeps the sprite on the screen while moving
     */
    
    public void move() {
        
            if ((velX < 0 && x >= 0) || (velX > 0 && x + width <= 800)) {
                x += velX;
            } 
            if((velY < 0 && y >= 0) || (velY > 0 && y + height <= 600)){
                y += velY;
            }
       
    }
    
    public boolean isDead(Dragon dragon){
        return this.collides(dragon);
                 
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
    
    public boolean inFinish(Finish fin){
        return collides(fin);
    }
   
    
}
