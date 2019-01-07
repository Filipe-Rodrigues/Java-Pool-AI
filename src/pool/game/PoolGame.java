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
public final class PoolGame {

    private List<Ball> balls;
    private Ball cueBall;
    private List<LWJGLDrawable> poolElements;
    private PoolVolatileComponents components;
    private volatile Coordinate2D parsedNewBallCoord;
    private volatile boolean hasNewBall;
    private volatile boolean canShoot;

    public PoolGame() {
        initBasicElements();
        components = new PoolVolatileComponents(poolElements);

        PoolDisplay display = new PoolDisplay(this);
        Thread displayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                display.start();
            }
        });
        displayThread.start();
        startPhysicsThread();
    }

    public PoolGame(PoolVolatileComponents components) {
        initBasicElements();
        this.components = components;
        startPhysicsThread();
    }
    
    private void initBasicElements() {
        balls = new ArrayList<>();
        cueBall = PoolDefaultObjects.getCueBallCopy();
        balls.add(cueBall);
        poolElements = new ArrayList<>();
        poolElements.addAll(PoolDefaultObjects.getDefaultPoolElements());
    }
    
    private void startPhysicsThread() {
        Thread physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (components.isStillRunning()) {
                    addBall();
                    managePhysics();
                    holdOn(PHYSICS_THREAD_DELAY);
                }
            }
        });
        physicsThread.start();
    }
    
    private void newGame() {
        balls.addAll(PoolDefaultObjects.getDefaultBalls());
        components.addBalls(balls);
        hasNewBall = false;
        parsedNewBallCoord = null;
        resetCueBall();
    }

    private void resetCueBall() {
        cueBall.setFallen(false);
        cueBall.getCollisionModel().getCenterPosition().x = WIDTH / 2;
        cueBall.getCollisionModel().getCenterPosition().y = HEIGHT * 1 / 4;
        cueBall.getCollisionModel().getVelocity().x = 0;
        cueBall.getCollisionModel().getVelocity().y = 0;
    }

    public PoolVolatileComponents getPoolComponents() {
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
        interactObjects();
        verifyBallsStates();
    }

    private void interactObjects() {
        if (balls.size() == 1) {
            newGame();
        }
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
                    resetCueBall();
                }
            }
            for (int j = i + 1; j < balls.size(); j++) {
                if (!balls.get(i).isFallen()) {
                    balls.get(i).interact(balls.get(j));
                }
            }
        }
    }

    private void verifyBallsStates() {
        for (Ball ball : balls) {
            if (!ball.updateBallState()) {
                canShoot = false;
                return;
            }
        }
        canShoot = true;
    }

    public boolean isCueAvailable() {
        return canShoot;
    }
}
