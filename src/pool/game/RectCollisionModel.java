/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class RectCollisionModel extends CollisionModel {

    private Coordinate2D upperLeft;
    private Coordinate2D lowerRight;

    @Override
    public void drawCollisionOutline() {
        glColor3f(1.0f, 0.75f, 0.0f);
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(upperLeft.x, upperLeft.y);
            glVertex2d(upperLeft.x, lowerRight.y);
            glVertex2d(lowerRight.x, lowerRight.y);
            glVertex2d(lowerRight.x, upperLeft.y);
        }
        glEnd();
    }
}
