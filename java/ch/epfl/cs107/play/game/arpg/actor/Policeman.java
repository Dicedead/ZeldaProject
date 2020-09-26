package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Policeman extends Monster {

    private final static int CHANGE_ORIENT_PROBAB = 15;
    private Animation[] animationWalk;
    private final static int MOVEMENT_SPEED = 10;
    private final static int ANIM_DURATION = 2;
    private boolean activeDialogs;
    private Keyboard keyboard;
    private int countOfInteractions;
    private Dialog dialogHjelp;
    private Dialog dialogDanger;
    private final static Vulnerabilities[] VULNERABILITIES = {};
    private boolean isReading;
    private Animation currentAliveAnim;

    /**
     * Augmented policeman constructor including the dialogs
     *
     * @param area
     * @param orientation
     * @param position
     * @param vulnerabilities
     */
    public Policeman(Area area, Orientation orientation, DiscreteCoordinates position,
                     Vulnerabilities[] vulnerabilities) {
        super(area, orientation, position, vulnerabilities);
        setAnimation();
        activeDialogs = false;
        countOfInteractions = 0;
        dialogDanger = new Dialog("Apparently, you want to leave the village. Be careful, you are entering a very dangerous area... ", "zelda/dialog", getOwnerArea());
        dialogHjelp = new Dialog("Once, my son left the village and took the same path as you. He never came back...", "zelda/dialog", getOwnerArea());
        currentAliveAnim = animationWalk[0];
        isReading = false;
    }

    /*
     * Default Policeman constructor (augmented minus the orientation and the vulnerabilities)
     *
     * @param area
     * @param position
     */
    public Policeman(Area area, DiscreteCoordinates position) {
        this(area, Orientation.values()[RandomGenerator.getInstance().nextInt(4)], position, VULNERABILITIES);
    }

    public void setDialogs() {

        if (!activeDialogs) {
            activeDialogs = true;
            isReading = true;
        } else {
            activeDialogs = false;
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }


    @Override
    public boolean isCellInteractable() {
        return false;
    }


    @Override
    public void draw(Canvas canvas) {
        currentAliveAnim.draw(canvas);

        if (activeDialogs) {
            if (countOfInteractions % 2 == 0) {
                dialogHjelp.draw(canvas);
            } else if (countOfInteractions % 2 == 1) {
                dialogDanger.draw(canvas);
            }
            if (keyboard.get(Keyboard.V).isPressed()) {
                activeDialogs = false;
                isReading = false;
            }
        }
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
        ++countOfInteractions;
    }

    public void acceptInteraction(ARPGPlayer player) {
        isReading = player.getIsReading();
    }

    @Override
    public boolean isViewInteractable() {
        keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.E).isPressed();
    }

    @Override
    public void update(float deltaTime) {
        if (!isDisplacementOccurs() && !isReading) {
            orientate(orientateMonster(CHANGE_ORIENT_PROBAB));
            move(MOVEMENT_SPEED);
            currentAliveAnim.update(deltaTime);
        }
        currentAliveAnim = animationWalk[getOrientation().ordinal()];
        super.update(deltaTime);

    }

    @Override
    protected Animation getCurrentAnimation() {
        return currentAliveAnim;
    }

    private void setAnimation() {
        Sprite[][] spritesWalk = RPGSprite.extractSprites("policeman", 4, 1, 1.5f, this, 16, 21, new Orientation[]
                {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                spritesWalk[i][j].setAnchor(Vector.ZERO);
            }
        }
        animationWalk = RPGSprite.createAnimations(ANIM_DURATION / 2, spritesWalk);
    }


    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(null);
    }


    @Override
    public boolean wantsCellInteraction() {
        return false;
    }


    @Override
    public boolean wantsViewInteraction() {
        return false;
    }
}
