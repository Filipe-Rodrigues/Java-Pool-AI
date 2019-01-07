/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pool.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author filip
 */
public class PoolVolatileComponents {

    private volatile List<LWJGLDrawable> components;
    private volatile boolean stillRunning;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    public PoolVolatileComponents(List<LWJGLDrawable> components) {
        lock = new ReentrantReadWriteLock();
        this.components = components;
        stillRunning = true;
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public List<LWJGLDrawable> getComponentList() {
        try {
            readLock.lock();
            return new ArrayList<>(components);
        } finally {
            readLock.unlock();
        }
    }

    public void addComponent(LWJGLDrawable component) {
        try {
            writeLock.lock();
            components.add(component);
        } finally {
            writeLock.unlock();
        }
    }
    
    public void addBalls(List<Ball> newComponents) {
        try {
            writeLock.lock();
            components.addAll(newComponents);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeComponent(LWJGLDrawable component) {
        try {
            writeLock.lock();
            components.remove(component);
        } finally {
            writeLock.unlock();
        }
    }
    
    public void stopRunning() {
        try {
            writeLock.lock();
            stillRunning = false;
        } finally {
            writeLock.unlock();
        }
    }
    
    public boolean isStillRunning() {
        try {
            readLock.lock();
            return stillRunning;
        } finally {
            readLock.unlock();
        }
    }
}
