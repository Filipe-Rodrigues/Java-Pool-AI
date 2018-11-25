/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.Color;
import pool.gui.PoolDisplay;
import static pool.utils.ApplicationConstants.*;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class PoolGame {
    
    private List<Ball> balls;
    private List<LWJGLDrawable> poolElements;
    
    public PoolGame() {
        balls = new ArrayList<>();
        poolElements = new ArrayList<>();
        balls.add(new Ball(true, new Coordinate2D(WIDTH/2, HEIGHT * 1 / 4), new Color(255, 255, 255)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH/2, HEIGHT * 3 / 4), new Color(255, 255, 0)));
        int llxPosition = (WIDTH - HEIGHT / 2) / 2;
        int urxPosition = llxPosition + HEIGHT/2;
        poolElements.add(new PoolTable(new Coordinate2D(llxPosition, 0), new Coordinate2D(urxPosition, HEIGHT), new Color(80, 0, 0)));
        Color color = new Color(161, 136, 127);
        
        RectCollisionModel collisionModel1 = new RectCollisionModel(Double.MAX_VALUE, new Coordinate2D(llxPosition + 3 * BALL_RADIUS, 0), 
                new Coordinate2D(urxPosition - 3 * BALL_RADIUS, 1.5 * BALL_RADIUS));
        poolElements.add(new Boundary(new Coordinate2D(llxPosition + 1.5 * BALL_RADIUS, C), new Coordinate2D(urxPosition - 1.5 * BALL_RADIUS, C), 
                new Coordinate2D(urxPosition - 3 * BALL_RADIUS, 1.5 * BALL_RADIUS), new Coordinate2D(llxPosition + 3 * BALL_RADIUS, 1.5 * BALL_RADIUS), collisionModel1, color));
        
        RectCollisionModel collisionModel2 = new RectCollisionModel(Double.MAX_VALUE, 
                new Coordinate2D(llxPosition + 3 * BALL_RADIUS, HEIGHT - 1.5 * BALL_RADIUS), new Coordinate2D(urxPosition - 3 * BALL_RADIUS, HEIGHT));
        poolElements.add(new Boundary(new Coordinate2D(llxPosition + 3 * BALL_RADIUS, HEIGHT - 1.5 * BALL_RADIUS), 
                new Coordinate2D(urxPosition - 3 * BALL_RADIUS, HEIGHT - 1.5 * BALL_RADIUS), new Coordinate2D(urxPosition - 3 * BALL_RADIUS, HEIGHT), 
                new Coordinate2D(llxPosition + 3 * BALL_RADIUS, HEIGHT), collisionModel2, color));
        
        RectCollisionModel collisionModel3 = new RectCollisionModel(Double.MAX_VALUE, new Coordinate2D(llxPosition, 2 * BALL_RADIUS), 
                new Coordinate2D(llxPosition + 2 * BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS));
        poolElements.add(new Boundary(new Coordinate2D(llxPosition, 2 * BALL_RADIUS), 
                new Coordinate2D(llxPosition + 2 * BALL_RADIUS, 2 * BALL_RADIUS), new Coordinate2D(llxPosition + 2 * BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS), 
                new Coordinate2D(llxPosition, HEIGHT / 2 - BALL_RADIUS), collisionModel3, color));
        
        RectCollisionModel collisionModel4 = new RectCollisionModel(Double.MAX_VALUE, new Coordinate2D(llxPosition, HEIGHT / 2 + BALL_RADIUS), 
                new Coordinate2D(llxPosition + 2 * BALL_RADIUS, HEIGHT - 2 * BALL_RADIUS));
        poolElements.add(new Boundary(new Coordinate2D(llxPosition, HEIGHT / 2 + BALL_RADIUS), 
                new Coordinate2D(llxPosition + 2 * BALL_RADIUS, HEIGHT / 2 + BALL_RADIUS), new Coordinate2D(llxPosition + 2 * BALL_RADIUS, HEIGHT - 2 * BALL_RADIUS), 
                new Coordinate2D(llxPosition, HEIGHT - 2 * BALL_RADIUS), collisionModel4, color));
        
        RectCollisionModel collisionModel5 = new RectCollisionModel(Double.MAX_VALUE, new Coordinate2D(urxPosition - 2 * BALL_RADIUS, 2 * BALL_RADIUS), 
                new Coordinate2D(urxPosition, HEIGHT / 2 - BALL_RADIUS));
        poolElements.add(new Boundary(new Coordinate2D(urxPosition - 2 * BALL_RADIUS, 2 * BALL_RADIUS), new Coordinate2D(urxPosition, 2 * BALL_RADIUS), 
                new Coordinate2D(urxPosition, HEIGHT / 2 - BALL_RADIUS), 
                new Coordinate2D(urxPosition - 2 * BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS), collisionModel5, color));
        
        RectCollisionModel collisionModel6 = new RectCollisionModel(Double.MAX_VALUE, 
                new Coordinate2D(urxPosition - 2 * BALL_RADIUS, HEIGHT / 2 + BALL_RADIUS), new Coordinate2D(urxPosition, HEIGHT - 2 * BALL_RADIUS));
        poolElements.add(new Boundary(new Coordinate2D(urxPosition - 2 * BALL_RADIUS, HEIGHT / 2 + BALL_RADIUS),
                new Coordinate2D(urxPosition, HEIGHT / 2 + BALL_RADIUS), new Coordinate2D(urxPosition, HEIGHT - 2 * BALL_RADIUS), 
                new Coordinate2D(urxPosition - 2 * BALL_RADIUS, HEIGHT - 2 * BALL_RADIUS), collisionModel6, color));
        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, HEIGHT / 2)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, HEIGHT / 2)));
        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, HEIGHT - BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, HEIGHT - BALL_RADIUS)));
        poolElements.addAll(balls);
        PoolDisplay display = new PoolDisplay(poolElements);
        display.start();
    }
}
