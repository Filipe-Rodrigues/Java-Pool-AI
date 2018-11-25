/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import org.lwjgl.util.Color;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class PoolTable implements LWJGLDrawable{
    public Coordinate2D upperLeft;
    public Coordinate2D lowerRight;
    public Color color;
    
    public PoolTable(Coordinate2D upperLeft, Coordinate2D lowerRight, Color color) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.color = color;
    }
    
    public void draw() {
        glColor3ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
        glBegin(GL_QUADS);
        {
            glVertex2d(upperLeft.x, upperLeft.y);
            glVertex2d(upperLeft.x, lowerRight.y);
            glVertex2d(lowerRight.x, lowerRight.y);
            glVertex2d(lowerRight.x, upperLeft.y);
        }
        glEnd();
    }
}
