package pool.game;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.Color;
import static pool.utils.ApplicationConstants.*;
import pool.utils.Coordinate2D;

public class PoolDefaultObjects {

    private static PoolDefaultObjects singleton;
    private final List<LWJGLDrawable> poolElements;
    private final Coordinate2D cueBallDefaultPosition;
    
    private PoolDefaultObjects() {
        cueBallDefaultPosition = new Coordinate2D(WIDTH / 2, HEIGHT * 1 / 4);
        poolElements = new ArrayList<>();
        createPoolElements();
    }
    
    private void createPoolElements() {
        int llxPosition = (WIDTH - HEIGHT / 2) / 2;
        int urxPosition = llxPosition + HEIGHT / 2;
        poolElements.add(new PoolTable(new Coordinate2D(llxPosition, 0), new Coordinate2D(urxPosition, HEIGHT), new Color(0, 80, 0)));
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
    }
    
    private static void createBalls(List<Ball> balls) {
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2, HEIGHT * 3 / 4), new Color(255, 255, 0)));

        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 2 * (BALL_RADIUS + 1)), new Color(0, 32, 255)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 2 * (BALL_RADIUS + 1)), new Color(255, 0, 0)));

        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - 2 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 3 * (BALL_RADIUS + 1)), new Color(64, 0, 127)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2, HEIGHT * 3 / 4 + 3 * (BALL_RADIUS + 1)), new Color(255, 80, 0)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + 2 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 3 * (BALL_RADIUS + 1)), new Color(0, 127, 0)));

        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - 3 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 4 * (BALL_RADIUS + 1)), new Color(200, 100, 32)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 4 * (BALL_RADIUS + 1)), new Color(16, 16, 16)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 4 * (BALL_RADIUS + 1)), new Color(255, 255, 64)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + 3 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 4 * (BALL_RADIUS + 1)), new Color(0, 64, 255)));

        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - 4 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 5 * (BALL_RADIUS + 1)), new Color(255, 32, 32)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 - 2 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 5 * (BALL_RADIUS + 1)), new Color(80, 0, 200)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2, HEIGHT * 3 / 4 + 5 * (BALL_RADIUS + 1)), new Color(255, 127, 0)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + 2 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 5 * (BALL_RADIUS + 1)), new Color(0, 200, 0)));
        balls.add(new Ball(false, new Coordinate2D(WIDTH / 2 + 4 * (BALL_RADIUS + 1), HEIGHT * 3 / 4 + 5 * (BALL_RADIUS + 1)), new Color(200, 127, 64)));
    }
    
    private static void checkSingleton() {
        if (singleton == null) {
            singleton = new PoolDefaultObjects();
        }
    }
    
    public static Ball getCueBallCopy() {
        checkSingleton();
        return new Ball(true, new Coordinate2D(singleton.cueBallDefaultPosition), new Color(255, 255, 255));
    }
    
    public static List<LWJGLDrawable> getDefaultPoolElements() {
        checkSingleton();
        return singleton.poolElements;
    }
    
    public static List<Ball> getDefaultBalls() {
        List<Ball> balls = new ArrayList<>();
        createBalls(balls);
        return balls;
    }
}
