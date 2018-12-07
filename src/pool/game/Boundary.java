/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;
import pool.gui.PoolDisplay;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class Boundary implements LWJGLDrawable {

    private final QuadCollisionModel collisionModel;
    private final Color color;

    public Boundary(Coordinate2D position, Coordinate2D p1, Coordinate2D p2, Coordinate2D p3, Coordinate2D p4, Color color) {
        this.collisionModel = new QuadCollisionModel(position, p1, p2, p3, p4);
        this.color = color;
    }

    @Override
    public void draw() {
        glColor3ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
        glBegin(GL_QUADS);
        {
            glVertex2d(collisionModel.p1.x, collisionModel.p1.y);
            glVertex2d(collisionModel.p2.x, collisionModel.p2.y);
            glVertex2d(collisionModel.p3.x, collisionModel.p3.y);
            glVertex2d(collisionModel.p4.x, collisionModel.p4.y);
        }
        glEnd();
        if (PoolDisplay.DRAW_COLLISION_BOUNDARIES) {
            collisionModel.drawCollisionOutline();
        }
    }

    public QuadCollisionModel getCollisionModel() {
        return collisionModel;
    }

}
