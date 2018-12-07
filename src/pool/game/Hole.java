/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import static pool.utils.ApplicationConstants.*;
import pool.utils.Coordinate2D;

public class Hole implements LWJGLDrawable {

    private static int idCount = 0;
    private final Coordinate2D centerPosition;
    private final int id;

    public Hole(Coordinate2D centerPosition) {
        this.centerPosition = centerPosition;
        id = ++idCount;
    }

    public Coordinate2D getCenterPosition() {
        return centerPosition;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public void draw() {
        glColor3f(0f, 0f, 0f);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(centerPosition.x, centerPosition.y);
        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            double theta =  (2d * Math.PI * ((double)i / (double)CIRCLE_SEGMENTS));
            double x = HOLE_RADIUS * Math.cos(theta);
            double y = HOLE_RADIUS * Math.sin(theta);
            glVertex2d(x + centerPosition.x, y + centerPosition.y);
        }
        glEnd();
    }
}
