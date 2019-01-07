/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Color;
import pool.gui.PoolDisplay;
import static pool.utils.ApplicationConstants.*;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class Ball implements LWJGLDrawable {

    private CircleCollisionModel collisionModel;
    private Color color;
    private boolean fallen;
    private boolean stationary;

    public Ball(boolean isCueBall, Coordinate2D centerPosition, Color color) {
        this.color = color;
        if (isCueBall) {
            collisionModel = new CircleCollisionModel(CUE_BALL_MASS, CUE_BALL_RADIUS, centerPosition);
        } else {
            collisionModel = new CircleCollisionModel(BALL_MASS, BALL_RADIUS, centerPosition);
        }
    }

    public void inspectListOfElements(List<LWJGLDrawable> poolElements) {
        collisionModel.move();
        for (LWJGLDrawable poolElement : poolElements) {
            if (poolElement instanceof Hole) {
                fallen = collisionModel.hitHole((Hole) poolElement);
                if (fallen) {
                    return;
                }
            } else if (poolElement instanceof Boundary) {
                collisionModel.collideWithQuadrilateral(((Boundary) poolElement).getCollisionModel());
            }
        }
    }
    
    public boolean updateBallState() {
        stationary = collisionModel.isStatic();
        return stationary;
    }

    public boolean isFallen() {
        return fallen;
    }
    
    public void setFallen(boolean fallen) {
        this.fallen = fallen;
    }
    
    public void interact(Ball another) {
        if (this != another) {
            collisionModel.collideWithCircular(another.getCollisionModel());
        }
    }

    public CircleCollisionModel getCollisionModel() {
        return collisionModel;
    }

    @Override
    public void draw() {
        glColor3ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte());
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(collisionModel.getCenterPosition().x, collisionModel.getCenterPosition().y);
        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            double theta = (2d * Math.PI * ((double) i / (double) CIRCLE_SEGMENTS));
            double x = collisionModel.getRadius() * Math.cos(theta);
            double y = collisionModel.getRadius() * Math.sin(theta);
            glVertex2d(x + collisionModel.getCenterPosition().x, y + collisionModel.getCenterPosition().y);
        }
        glEnd();
        if (PoolDisplay.DRAW_COLLISION_BOUNDARIES) {
            collisionModel.drawCollisionOutline();
        }
    }
}
