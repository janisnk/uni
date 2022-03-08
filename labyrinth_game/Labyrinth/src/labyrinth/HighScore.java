/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinth;

/**
 *Class to represent records with the name of the gamer
 * succesfully finished levels and game time
 * @author sinko
 */
public class HighScore {

    private final String name;
    private final int score;
    private final long time;

    public HighScore(String name, int score, long time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }
    
   

    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + score +", time= " + time + " s }";
    }

}
