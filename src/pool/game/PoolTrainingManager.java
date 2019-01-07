package pool.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pool.gui.PoolDisplay;

public class PoolTrainingManager {
    private final List<PoolGame> games;
    private PoolDisplay guiDisplay;
    private final PoolVolatileComponents allComponents;
    
    public PoolTrainingManager(int numberOfGames) {
        allComponents = new PoolVolatileComponents(PoolDefaultObjects.getDefaultPoolElements());
        games = new ArrayList<>();
        for (int i = 0; i < numberOfGames; i++) {
            games.add(new PoolGame(allComponents));
        }
        startDisplayThread();
    }
    
    private void startDisplayThread() {
        guiDisplay  = new PoolDisplay(this);
        Thread displayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                guiDisplay.start();
            }
        });
        displayThread.start();
    }
    
    public PoolVolatileComponents getPoolComponents() {
        return allComponents;
    }
    
    public void testRandomizedShot() {
        for (PoolGame game : games) {
            if (!game.isCueAvailable()) return;
        }
        Random rnd = new Random();
        for (PoolGame game : games) {
            game.cue(rnd.nextDouble() * 360, 2);
        }
    }
}
