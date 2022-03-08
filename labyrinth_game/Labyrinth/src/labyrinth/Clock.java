/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *Class for implementing clock with 1s delay timer
 * @author sinko
 */
public class Clock {

    private long time;
    private Timer t;
    private boolean paused;
     
    public Clock() {
        time = 0;
        paused = false;
        t = new Timer(1000, new NewFrameListener());
        t.start();
    }
    
class NewFrameListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!paused){
                time++;
            }
        }
   
}

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
    

}
