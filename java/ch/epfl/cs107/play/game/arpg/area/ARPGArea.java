package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public abstract class ARPGArea extends Area {

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    @Override
    public final float getCameraScaleFactor() {
        return ARPG.CAMERA_SCALE_FACTOR;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            ARPGBehavior behavior = new ARPGBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Simple method shared by all ARPG areas chain registering actors
     *
     * @param actors array of actors
     */
    protected void registerActors(Actor... actors) {
        for (Actor actor : actors) {
            registerActor(actor);
        }
    }
}