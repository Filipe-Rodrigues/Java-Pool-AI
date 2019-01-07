/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.main;

import pool.game.PoolGame;
import pool.game.PoolTrainingManager;

/**
 *
 * @author filip
 */
public class ApplicationMain {

    public static void main(String[] args) {
        //PoolGame game = new PoolGame();
        PoolTrainingManager manager = new PoolTrainingManager(100);
    }
}
