/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import static org.lwjgl.opengl.GL11.*;
import static pool.utils.ApplicationConstants.*;
import static java.lang.Math.*;
import java.util.Random;
import org.lwjgl.util.Color;
import pool.utils.Coordinate2D;
import static pool.utils.Coordinate2D.*;

/**
 *
 * @author filip
 */
public class CircleCollisionModel extends CollisionModel {

    private static final double CORRECTION_FACTOR = ((double) PHYSICS_THREAD_DELAY / 1000d);

    private final double radius;
    private final Coordinate2D centerPosition;
    private final Coordinate2D velocity;

    public CircleCollisionModel(double mass, double radius, Coordinate2D centerPosition) {
        super(mass);
        this.radius = radius;
        this.centerPosition = centerPosition;
        this.velocity = new Coordinate2D(0, 0);
    }

    @Override
    public void drawCollisionOutline() {
        glColor3f(1.0f, 0.75f, 0.0f);
        glLineWidth(LINE_WIDTH);
        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double theta = (2f * PI * ((double) i / (double) CIRCLE_SEGMENTS));
            double x = radius * cos(theta);
            double y = radius * sin(theta);
            glVertex2d(x + centerPosition.x, y + centerPosition.y);
        }
        glEnd();
    }

    private boolean isPointInside(Coordinate2D point) {
        Coordinate2D dist = Coordinate2D.getVector(centerPosition, point);
        return dist.getMagnitude() <= radius;
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

    private boolean collidedWithBall(CircleCollisionModel obj) {
        if (centerPosition.getDistance(obj.centerPosition) < (obj.radius + radius)) {
            return true;
        }
        return false;
    }

    public void collideWithCircular(CircleCollisionModel obj) {
        if (collidedWithBall(obj)) {
            // get the mtd
            Coordinate2D delta = getVector(centerPosition, obj.centerPosition);
            double d = delta.getMagnitude();
            if (d == 0) {
                Random rnd = new Random();
                centerPosition.x += rnd.nextDouble() - 0.5d;
                centerPosition.y += rnd.nextDouble() - 0.5d;
                return;
            }
            // minimum translation distance to push balls apart after intersecting
            Coordinate2D mtd = delta.getScaled((radius + obj.radius - d) / d);

            // resolve intersection --
            // inverse mass quantities
            double im1 = 1d / mass;
            double im2 = 1d / obj.mass;

            // push-pull them apart based off their mass
            centerPosition.sum(mtd.getScaled(-im1 / (im1 + im2)));
            obj.centerPosition.sum(mtd.getScaled(im2 / (im1 + im2)));

            Coordinate2D normal = delta.getUnitVector();
            Coordinate2D tangent = normal.getOrthogonal(normal);

            // Dot Product Tangent
            double dpTan1 = velocity.getDotProduct(tangent);
            double dpTan2 = obj.velocity.getDotProduct(tangent);

            // Dot Product Normal
            double dpNorm1 = velocity.getDotProduct(normal);
            double dpNorm2 = obj.velocity.getDotProduct(normal);

            // Conservation of momentum in 1D
            double m1 = (dpNorm1 * (mass - obj.mass) + 2.0f * obj.mass * dpNorm2) / (mass + obj.mass);
            double m2 = (dpNorm2 * (obj.mass - mass) + 2.0f * mass * dpNorm1) / (mass + obj.mass);

            // Update ball velocities
            velocity.copy(Coordinate2D.sum(tangent.getScaled(dpTan1), normal.getScaled(m1)));
            obj.velocity.copy(Coordinate2D.sum(tangent.getScaled(dpTan2), normal.getScaled(m2)));
        }
    }

    private Coordinate2D getCollisionMTV(Coordinate2D p1, Coordinate2D p2) {
        Coordinate2D closestPointInside = null;
        Coordinate2D segment1 = getVector(p1, p2);
        Coordinate2D segment2 = getVector(p1, centerPosition);
        Coordinate2D projection = getProjectionVector(segment2, segment1);
        Coordinate2D rejection = getRejectionVector(segment2, segment1);
        double distance = rejection.getMagnitude();
        if (distance <= radius) {
            double projMagnitude = projection.getMagnitude();
            if (projMagnitude <= segment1.getMagnitude() && projection.getDotProduct(segment1) >= 0) {
                closestPointInside = sum(projection, p1);
            }
        }
        if (closestPointInside == null) {
            if (isPointInside(p1)) {
                closestPointInside = p1;
            } else if (isPointInside(p2)) {
                closestPointInside = p2;
            }
        }
        Coordinate2D mtv = null;
        if (closestPointInside != null) {
            mtv = getVector(closestPointInside, centerPosition);
            mtv = mtv.getScaled((radius / mtv.getMagnitude()) - 1.0d);
            System.err.println("Segment1: " + segment1);
            System.err.println("Segment2: " + segment2);
            System.err.println("Projection: " + projection);
            System.err.println("P1: " + p1);
            System.err.println("Rejection: " + rejection + "\n");
        }

        return mtv;
    }

    private void translate(Coordinate2D translation) {
        System.err.println("MTV: " + translation);
        centerPosition.sum(translation);
    }

    public void collideWithQuadrilateralV2(QuadCollisionModel obj) {
        Coordinate2D mtv = getCollisionMTV(obj.p1, obj.p2);
        if (mtv != null) {
            translate(mtv);
            deflect(mtv);
            return;
        }
        mtv = getCollisionMTV(obj.p2, obj.p3);
        if (mtv != null) {
            translate(mtv);
            deflect(mtv);
            return;
        }
        mtv = getCollisionMTV(obj.p3, obj.p4);
        if (mtv != null) {
            translate(mtv);
            deflect(mtv);
            return;
        }
        mtv = getCollisionMTV(obj.p4, obj.p1);
        if (mtv != null) {
            translate(mtv);
            deflect(mtv);
            return;
        }
    }

    public void collideWithQuadrilateral(QuadCollisionModel obj) {
        Coordinate2D d1 = getVector(obj.p1, obj.p2);
        Coordinate2D d2 = getVector(obj.p2, obj.p3);
        Coordinate2D d3 = getVector(obj.p3, obj.p4);
        Coordinate2D d4 = getVector(obj.p4, obj.p1);
        Coordinate2D f1 = getVector(centerPosition, obj.p1);
        Coordinate2D f2 = getVector(centerPosition, obj.p2);
        Coordinate2D f3 = getVector(centerPosition, obj.p3);
        Coordinate2D f4 = getVector(centerPosition, obj.p4);

        double a = d1.getDotProduct(d1);
        double b = 2 * f1.getDotProduct(d1);
        double c = f1.getDotProduct(f1) - radius * radius;
        double discriminant = b * b - 4 * a * c;
        if (discriminant >= 0) {
            discriminant = sqrt(discriminant);
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);
            if ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) {
                deflect(d1.getNormal());
                return;
            }
        }

        a = d2.getDotProduct(d2);
        b = 2 * f2.getDotProduct(d2);
        c = f2.getDotProduct(f2) - radius * radius;
        discriminant = b * b - 4 * a * c;
        if (discriminant >= 0) {
            discriminant = sqrt(discriminant);
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);
            if ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) {
                deflect(d2.getNormal());
                return;
            }
        }

        a = d3.getDotProduct(d3);
        b = 2 * f3.getDotProduct(d3);
        c = f3.getDotProduct(f3) - radius * radius;
        discriminant = b * b - 4 * a * c;
        if (discriminant >= 0) {
            discriminant = sqrt(discriminant);
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);
            if ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) {
                deflect(d3.getNormal());
                return;
            }
        }
        a = d4.getDotProduct(d4);
        b = 2 * f4.getDotProduct(d4);
        c = f4.getDotProduct(f4) - radius * radius;
        discriminant = b * b - 4 * a * c;
        if (discriminant >= 0) {
            discriminant = sqrt(discriminant);
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);
            if ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) {
                deflect(d4.getNormal());
                return;
            }
        }
    }

    private void deflect(Coordinate2D surfaceNormal) {
        Coordinate2D v = sum(velocity, surfaceNormal.getScaled(-2 * getDotProduct(velocity, surfaceNormal)));
        velocity.x = v.x;
        velocity.y = v.y;
    }

    public boolean hitHole(Hole hole) {
        if (centerPosition.getDistance(hole.getCenterPosition()) <= (HOLE_RADIUS + radius / 2)) {
            return true;
        }
        return false;
    }

    public void applyMomentum(Coordinate2D momentum) {
        velocity.x = momentum.x / mass;
        velocity.y = momentum.y / mass;
    }

    public void move() {
        double accelRate = 1 - CORRECTION_FACTOR;
        velocity.x *= accelRate;
        velocity.y *= accelRate;
        double velMod = sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
        if (velMod < 2) {
            velocity.x *= accelRate;
            velocity.y *= accelRate;
        } else if (velMod < 0.5) {
            velocity.x = 0;
            velocity.y = 0;
        }
        if (abs(velocity.x) < 0.5) {
            velocity.x = 0;
        }
        if (abs(velocity.y) < 0.5) {
            velocity.y = 0;
        }
        centerPosition.x += (velocity.x * CORRECTION_FACTOR * 10);
        centerPosition.y += (velocity.y * CORRECTION_FACTOR * 10);

        if (centerPosition.x + radius > 820) {
            centerPosition.x = 820 - radius;
            velocity.x *= -1;
        } else if (centerPosition.x - radius < 460) {
            centerPosition.x = 460 + radius;
            velocity.x *= -1;
        }
        if (centerPosition.y + radius > 720) {
            centerPosition.y = 720 - radius;
            velocity.y *= -1;
        } else if (centerPosition.y - radius < 0) {
            centerPosition.y = 0 + radius;
            velocity.y *= -1;
        }
    }
}
