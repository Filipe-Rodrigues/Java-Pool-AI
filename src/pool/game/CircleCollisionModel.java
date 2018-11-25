/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import static pool.utils.ApplicationConstants.*;
import pool.utils.Coordinate2D;

/**
 *
 * @author filip
 */
public class CircleCollisionModel extends CollisionModel {

    private double radius;
    private Coordinate2D centerPosition;
    private Coordinate2D velocity;

    public CircleCollisionModel(double mass, double radius, Coordinate2D centerPosition) {
        super(mass);
        this.radius = radius;
        this.centerPosition = centerPosition;
        this.velocity = new Coordinate2D(0, 0);
    }
    
    @Override
    public void drawCollisionOutline() {
        glColor3f(1.0f, 0.75f, 0.0f);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double theta =  (2f * Math.PI * (double)(i / CIRCLE_SEGMENTS));
            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);
            glVertex2d(x + centerPosition.x, y + centerPosition.y);
        }
        glEnd();
    }

    public double getRadius() {
        return radius;
    }

    public Coordinate2D getCenterPosition() {
        return centerPosition;
    }

    public Coordinate2D getVelocity() {
        return velocity;
    }

    public boolean isStatic() {
        return velocity.x == 0 && velocity.y == 0;
    }
    
    public void collideWithCircular(CircleCollisionModel obj) {
        Coordinate2D thisVelocity = new Coordinate2D(velocity);
        Coordinate2D objVelocity = new Coordinate2D(obj.velocity);
        
        velocity.x = C * obj.mass * (objVelocity.x - thisVelocity.x) 
                + mass * thisVelocity.x + obj.mass * objVelocity.x;
        velocity.x /= (mass + obj.mass);
        velocity.y = C * obj.mass * (objVelocity.y - thisVelocity.y) 
                + mass * thisVelocity.y + obj.mass * objVelocity.y;
        velocity.y /= (mass + obj.mass);
        
    }
}
