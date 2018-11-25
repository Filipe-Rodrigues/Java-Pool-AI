/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class Boundary implements LWJGLDrawable {

    private final Coordinate2D p1;
    private final Coordinate2D p2;
    private final Coordinate2D p3;
    private final Coordinate2D p4;
    private final RectCollisionModel collisionModel;
    private final Color color;

    public Boundary(Coordinate2D p1, Coordinate2D p2, Coordinate2D p3, Coordinate2D p4, RectCollisionModel collisionModel, Color color) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.collisionModel = collisionModel;
        this.color = color;
    }
    
    @Override
    public void draw() {
        glColor3ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
        glBegin(GL_QUADS);
        {
            glVertex2d(p1.x, p1.y);
            glVertex2d(p2.x, p2.y);
            glVertex2d(p3.x, p3.y);
            glVertex2d(p4.x, p4.y);
        }
        glEnd();
    }

    public RectCollisionModel getCollisionModel() {
        return collisionModel;
    }
    
}
