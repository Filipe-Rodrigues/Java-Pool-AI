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
import static pool.utils.ThreadUtils.*;
import static java.lang.Math.*;
import java.util.Random;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class PoolGame {

    private List<Ball> balls;
    private Ball cueBall;
    private List<LWJGLDrawable> poolElements;
    private PoolComponents components;
    private volatile Coordinate2D parsedNewBallCoord;
    private volatile boolean hasNewBall;

    public PoolGame() {
        balls = new ArrayList<>();
        poolElements = new ArrayList<>();
        cueBall = new Ball(true, new Coordinate2D(WIDTH / 2, HEIGHT * 1 / 4), new Color(255, 255, 255));
        balls.add(cueBall);
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2, HEIGHT * 3 / 4), new Color(255, 255, 0)));
        int llxPosition = (WIDTH - HEIGHT / 2) / 2;
        int urxPosition = llxPosition + HEIGHT / 2;
        poolElements.add(new PoolTable(new Coordinate2D(llxPosition, 0), new Coordinate2D(urxPosition, HEIGHT), new Color(80, 0, 0)));
        Color color = new Color(161, 136, 127);

        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, HEIGHT / 2)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, HEIGHT / 2)));
        poolElements.add(new Hole(new Coordinate2D(llxPosition + BALL_RADIUS, HEIGHT - BALL_RADIUS)));
        poolElements.add(new Hole(new Coordinate2D(urxPosition - BALL_RADIUS, HEIGHT - BALL_RADIUS)));

        poolElements.add(new Boundary(new Coordinate2D(480, 0), new Coordinate2D(0, 0), new Coordinate2D(320, 0),
                new Coordinate2D(300, 15), new Coordinate2D(20, 15), color));

        poolElements.add(new Boundary(new Coordinate2D(480, 720), new Coordinate2D(0, 0), new Coordinate2D(20, -15),
                new Coordinate2D(300, -15), new Coordinate2D(320, 0), color));

        poolElements.add(new Boundary(new Coordinate2D(460, 20), new Coordinate2D(0, 0), new Coordinate2D(15, 20),
                new Coordinate2D(15, 320), new Coordinate2D(0, 325), color));

        poolElements.add(new Boundary(new Coordinate2D(820, 20), new Coordinate2D(0, 0), new Coordinate2D(0, 325),
                new Coordinate2D(-15, 320), new Coordinate2D(-15, 20), color));

        poolElements.add(new Boundary(new Coordinate2D(460, 375), new Coordinate2D(0, 0), new Coordinate2D(15, 5),
                new Coordinate2D(15, 305), new Coordinate2D(0, 325), color));

        poolElements.add(new Boundary(new Coordinate2D(820, 375), new Coordinate2D(0, 0), new Coordinate2D(0, 325),
                new Coordinate2D(-15, 305), new Coordinate2D(-15, 5), color));

        List<LWJGLDrawable> allPoolElements = new ArrayList<>();

        allPoolElements.addAll(poolElements);
        allPoolElements.addAll(balls);
        components = new PoolComponents(allPoolElements);

        hasNewBall = false;
        parsedNewBallCoord = null;

        PoolDisplay display = new PoolDisplay(this);
        Thread displayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                display.start();
            }
        });
        Thread physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (display.isRunning()) {
                    addBall();
                    managePhysics();
                    holdOn(PHYSICS_THREAD_DELAY);
                }
            }
        });
        System.out.println(poolElements.size());
        displayThread.start();
        physicsThread.start();
    }

    public PoolComponents getPoolComponents() {
        return components;
    }

    public Coordinate2D getCuePosition() {
        return cueBall.getCollisionModel().getCenterPosition();
    }

    public void addRandomColoredBall(Coordinate2D position) {
        hasNewBall = true;
        parsedNewBallCoord = new Coordinate2D(position);
    }

    public void cue(double angle, double intensity) {
        Coordinate2D momentum = new Coordinate2D(MAX_CUE_MOMENTUM * intensity * cos(angle), MAX_CUE_MOMENTUM * intensity * sin(angle));
        cueBall.getCollisionModel().applyMomentum(momentum);
    }

    private void addBall() {
        if (hasNewBall) {
            Random rnd = new Random();
            Ball newBall = new Ball(false, parsedNewBallCoord, new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            balls.add(newBall);
            components.addComponent(newBall);
            hasNewBall = false;
        }
    }

    private void managePhysics() {
        for (int i = 0; i < balls.size(); i++) {
            if (!balls.get(i).isFallen()) {
                balls.get(i).inspectListOfElements(poolElements);
            }
            if (balls.get(i).isFallen()) {
                if (balls.get(i) != cueBall) {
                    components.removeComponent(balls.get(i));
                    balls.remove(i);
                    continue;
                } else {
                    cueBall.setFallen(false);
                    cueBall.getCollisionModel().getCenterPosition().x = WIDTH / 2;
                    cueBall.getCollisionModel().getCenterPosition().y = HEIGHT * 1 / 4;
                    cueBall.getCollisionModel().getVelocity().x = 0;
                    cueBall.getCollisionModel().getVelocity().y = 0;
                }
            }
            for (int j = i + 1; j < balls.size(); j++) {
                if (!balls.get(i).isFallen()) {
                    balls.get(i).interact(balls.get(j));
                }
            }
        }
    }
}
