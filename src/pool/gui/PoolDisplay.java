package pool.gui;

import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import pool.game.Ball;
import pool.game.LWJGLDrawable;
import pool.game.PoolGame;
import pool.game.PoolTrainingManager;
import static pool.utils.ApplicationConstants.*;
import pool.game.PoolVolatileComponents;
import pool.utils.Coordinate2D;
import static pool.utils.Coordinate2D.*;

public class PoolDisplay {

    public static final int HUMAN_X_HUMAN_MODE = 0;
    public static final int AI_DEMO_MODE = 1;

    public static boolean DRAW_COLLISION_BOUNDARIES = true;

    private final PoolGame poolMatch;
    private final PoolTrainingManager trainingManager;
    private final PoolVolatileComponents components;
    private boolean holdingShot;
    private final Coordinate2D mouseNormalizedPosition;
    private int cheatMultiplier = 1;
    private final int gamemode;

    private int windowWid = 800;
    private int windowHei = 600;
    private int viewportWid;
    private int viewportDispWid;
    private int viewportHei;
    private int viewportDispHei;
    private long lastFrame;
    private int fps;
    private long lastFPS;
    private boolean vsync;

    public PoolDisplay(PoolGame game) {
        gamemode = HUMAN_X_HUMAN_MODE;
        poolMatch = game;
        trainingManager = null;
        this.components = game.getPoolComponents();
        holdingShot = false;
        mouseNormalizedPosition = new Coordinate2D(0, 0);
    }
    
    public PoolDisplay(PoolTrainingManager manager) {
        gamemode = AI_DEMO_MODE;
        trainingManager = manager;
        poolMatch = null;
        this.components = manager.getPoolComponents();
        holdingShot = false;
        mouseNormalizedPosition = new Coordinate2D(0, 0);
    }

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            adjustViewport();
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        initGL(); // init OpenGL
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer

        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            update(delta);
            pollMouse();
            renderGL();

            Display.update();
            updateWindowState();
            Display.sync(60); // cap fps to 60fps
        }

        Display.destroy();
        components.stopRunning();
    }

    private void updateWindowState() {
        if (Display.wasResized()) {
            adjustViewport();
        }
    }

    private void adjustViewport() {
        windowWid = Display.getWidth();
        windowHei = Display.getHeight();
        if (windowWid > windowHei * WIDTH / HEIGHT) {
            viewportDispWid = (windowWid - windowHei * WIDTH / HEIGHT) / 2;
            viewportWid = windowHei * WIDTH / HEIGHT;
            viewportDispHei = 0;
            viewportHei = windowHei;
        } else {
            viewportDispWid = 0;
            viewportWid = windowWid;
            viewportDispHei = (windowHei - windowWid * HEIGHT / WIDTH) / 2;
            viewportHei = windowWid * HEIGHT / WIDTH;
        }
        glScissor(viewportDispWid, viewportDispHei, viewportWid, viewportHei);
        glViewport(viewportDispWid, viewportDispHei, viewportWid, viewportHei);
    }

    private void pollMouse() {
        mouseNormalizedPosition.x = (double) (Mouse.getX() - viewportDispWid) / (double) viewportWid * WIDTH;
        mouseNormalizedPosition.y = (double) (Mouse.getY() - viewportDispHei) / (double) viewportHei * HEIGHT;
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                evaluateMouseButtonPressed();
            } else {
                evaluateMouseButtonReleased();
            }
        }
    }

    private void evaluateMouseButtonPressed() {
        if (Mouse.getEventButton() == 0) {
            if (gamemode == HUMAN_X_HUMAN_MODE) {
                if (poolMatch.isCueAvailable()) {
                    holdingShot = true;
                }
            }
        }
    }

    private void evaluateMouseButtonReleased() {
        if (Mouse.getEventButton() == 0) {
            if (gamemode == HUMAN_X_HUMAN_MODE) {
                if (holdingShot) {
                    holdingShot = false;
                    Coordinate2D cuePosition = poolMatch.getCuePosition();
                    Coordinate2D reference = new Coordinate2D(WIDTH, cuePosition.y);
                    double intensity = cuePosition.getDistance(mouseNormalizedPosition);
                    if (intensity >= 500) {
                        intensity = 1;
                    } else {
                        intensity /= 500d;
                    }
                    poolMatch.cue(cuePosition.computeAngle(mouseNormalizedPosition, reference), cheatMultiplier * intensity);
                }
            }
        } else if (Mouse.getEventButton() == 1) {
            if (gamemode == HUMAN_X_HUMAN_MODE) {
                poolMatch.addRandomColoredBall(new Coordinate2D(mouseNormalizedPosition));
            }
        }
    }

    public void update(int delta) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {

        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {

        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_F:
                        if (Display.isFullscreen()) {
                            setDisplayMode(WIDTH, HEIGHT, false);
                            adjustViewport();
                        } else {
                            setDisplayMode(Display.getDesktopDisplayMode().getWidth(),
                                    Display.getDesktopDisplayMode().getHeight(), true);
                            adjustViewport();
                        }
                        break;
                    case Keyboard.KEY_V:
                        vsync = !vsync;
                        Display.setVSyncEnabled(vsync);
                        break;
                    case Keyboard.KEY_SPACE:
                        if (gamemode == AI_DEMO_MODE) {
                            trainingManager.testRandomizedShot();
                        }
                        break;
                    case Keyboard.KEY_O:
                        DRAW_COLLISION_BOUNDARIES = !DRAW_COLLISION_BOUNDARIES;
                        break;
                    case Keyboard.KEY_LCONTROL:
                        cheatMultiplier = 10;
                        break;
                    default:
                        break;
                }
            } else {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_LCONTROL:
                        cheatMultiplier = 1;
                        break;
                    default:
                        break;
                }
            }
        }
        updateFPS(); // update FPS Counter
    }

    /**
     * Set the display mode to be used
     *
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the 
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    /**
     * Calculate how many milliseconds have passed since last frame.
     *
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public void initGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void renderGL() {
        clearScreen();
        drawElements();
    }

    private void clearScreen() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void drawElements() {
        List<LWJGLDrawable> drawingElements = components.getComponentList();
        for (LWJGLDrawable drawingElement : drawingElements) {
            drawingElement.draw();
        }
        if (gamemode == HUMAN_X_HUMAN_MODE) {
            if (holdingShot) {
                glLineWidth(2.0f);
                Coordinate2D v = new Coordinate2D(getVector(poolMatch.getCuePosition(), mouseNormalizedPosition));
                glColor3d(v.getMagnitude() / 500d, 1 - v.getMagnitude() / 500d, 0);
                glBegin(GL_LINES);
                glVertex2d(poolMatch.getCuePosition().x, poolMatch.getCuePosition().y);
                glVertex2d(mouseNormalizedPosition.x, mouseNormalizedPosition.y);
                glEnd();
            }
        }
    }
}
