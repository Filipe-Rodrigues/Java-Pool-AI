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

    private Coordinate2D centerPosition;

    public Hole(Coordinate2D centerPosition) {
        this.centerPosition = centerPosition;
    }

    public Coordinate2D getCenterPosition() {
        return centerPosition;
    }
    
    @Override
    public void draw() {
        glColor3f(0f, 0f, 0f);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(centerPosition.x, centerPosition.y);
        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            double theta =  (2d * Math.PI * ((double)i / (double)CIRCLE_SEGMENTS));
            double x = BALL_RADIUS * Math.cos(theta);
            double y = BALL_RADIUS * Math.sin(theta);
            glVertex2d(x + centerPosition.x, y + centerPosition.y);
        }
        glEnd();
    }
}
