/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import static pool.utils.ApplicationConstants.LINE_WIDTH;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class QuadCollisionModel extends CollisionModel {

    final Coordinate2D p1;
    final Coordinate2D p2;
    final Coordinate2D p3;
    final Coordinate2D p4;
    final Coordinate2D position;

    public QuadCollisionModel(Coordinate2D position, Coordinate2D p1, Coordinate2D p2, Coordinate2D p3, Coordinate2D p4) {
        super(Double.MAX_VALUE);
        this.position = position;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        p1.sum(position);
        p2.sum(position);
        p3.sum(position);
        p4.sum(position);
    }

    @Override
    public void drawCollisionOutline() {
        glColor3f(1.0f, 0.75f, 0.0f);
        glLineWidth(LINE_WIDTH);
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(p1.x, p1.y);
            glVertex2d(p2.x, p2.y);
            glVertex2d(p3.x, p3.y);
            glVertex2d(p4.x, p4.y);
        }
        glEnd();
    }

}
