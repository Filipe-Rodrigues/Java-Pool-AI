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

    private Coordinate2D lowerLeft;
    private Coordinate2D upperRight;

    public RectCollisionModel(double mass, Coordinate2D lowerLeft, Coordinate2D upperRight) {
        super(mass);
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    @Override
    public void drawCollisionOutline() {
        glColor3f(1.0f, 0.75f, 0.0f);
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(lowerLeft.x, lowerLeft.y);
            glVertex2d(lowerLeft.x, upperRight.y);
            glVertex2d(upperRight.x, upperRight.y);
            glVertex2d(upperRight.x, lowerLeft.y);
        }
        glEnd();
    }
    
    public boolean intersected(Coordinate2D point) {
        if (point.x >= lowerLeft.x && point.x <= upperRight.x) {
            if (point.y >= lowerLeft.y && point.y >= upperRight.y) {
                return true;
            }
        }
        return false;
    }
}
