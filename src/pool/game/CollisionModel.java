/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

/**
 *
 * @author filip
 */
public abstract class CollisionModel {

    protected double mass;

    public CollisionModel(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public abstract void drawCollisionOutline();
}
