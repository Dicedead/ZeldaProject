package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableDropperEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DarkLord extends Monster implements VulnerableActor, CollectableDropperEntity {

    private final static float DARK_LORD_HP = 3.f;
    private DarkLordHandler handler;
    private DarkStates currentState;

    private FireSpell testFire;
    private FlameSkull testSkull;

    private Animation[][] statesAnimation;
    private final static int SPELL_ANIM_DURATION = 3;
    private final static int CHANGE_ORIENT_PROBAB = 17;

    private final static int MOVEMENT_SPEED = 8;
    private final static int FOV_RADIUS = 5;
    private final static int MIN_SPELL_WAIT = 20;
    private final static int MAX_SPELL_WAIT = 80;
    private final static int MAX_IDLE_TIME = 75;
    private int spellWait;
    private int idleCount;
    private final static int MIN_FIRE_FORCE = 3;
    private final static int MAX_FIRE_FORCE = 8;
    private final static int MIN_TP_RADIUS = 3;
    private final static int MAX_TP_RADIUS = 6;
    private final static int MAX_TP_TRIES = 6;

    private final static Vulnerabilities[] VULNERABILITIES = {Vulnerabilities.MAGIC};

    private Animation currentAliveAnim;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, DARK_LORD_HP, VULNERABILITIES);

        handler = new DarkLordHandler();
        currentState = DarkStates.IDLE;

        testFire = new FireSpell(getOwnerArea(), Orientation.UP, getCurrentMainCellCoordinates(), 0);
        testSkull = new FlameSkull(getOwnerArea(), getCurrentMainCellCoordinates());

        statesAnimation = new Animation[2][4];
        Sprite[][] defaultSprites = RPGSprite.extractSprites("zelda/darkLord", 3, 2, 2, this,
                32, 32, new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN,
                        Orientation.RIGHT});
        statesAnimation[0] = RPGSprite.createAnimations(MOVEMENT_SPEED / 2, defaultSprites);
        Sprite[][] summonSprites = RPGSprite.extractSprites("zelda/darkLord.spell", 3, 2, 2, this,
                32, 32, new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN,
                        Orientation.RIGHT});
        statesAnimation[1] = RPGSprite.createAnimations(SPELL_ANIM_DURATION, summonSprites);

        Vector anchorVector = new Vector(-0.5f,0);
        for (int i = 0; i < 4; ++i) {
            statesAnimation[0][i] = new Animation(MOVEMENT_SPEED / 2, defaultSprites[i], false);
            statesAnimation[0][i].setAnchor(anchorVector);
            statesAnimation[1][i] = new Animation(SPELL_ANIM_DURATION, summonSprites[i], false);
            statesAnimation[1][i].setAnchor(anchorVector);
        }

        currentAliveAnim = statesAnimation[0][getOrientation().ordinal()];
        setRandomSpellWait();
        setRandomIdleCount();
    }

    public DarkLord(Area area, DiscreteCoordinates position) {
        this(area, Orientation.values()[RandomGenerator.getInstance().nextInt(4)], position);
    }

    private void setRandomSpellWait() {
        spellWait = MIN_SPELL_WAIT + RandomGenerator.getInstance().nextInt(MAX_SPELL_WAIT - MIN_SPELL_WAIT);
    }

    private void setRandomIdleCount() {
        idleCount = RandomGenerator.getInstance().nextInt(MAX_IDLE_TIME);
    }

    @Override
    public void update(float deltaTime) {

        if (idleCount > 0) {
            --idleCount;
        }

        if (spellWait > 0) {
            --spellWait;
        } else if (idleCount == 0) {
            int nbTries = 0;
            boolean validDirection;
            Orientation[] possibleOrientations = {getOrientation()};
            Orientation testedOrientation;
            do {
                possibleOrientations = createOrientationArrayExcluding(possibleOrientations);
                testedOrientation = possibleOrientations[RandomGenerator.getInstance().nextInt(possibleOrientations.length)];
                validDirection = !getOwnerArea().canEnterAreaCells(testFire, getCellsFacing(testedOrientation));
                ++nbTries;
            } while (nbTries < 3 && !validDirection);
            //If all other 3 orientations fail, testedOrientation goes back to the Lord's orientation,
            //
            if (!validDirection) {
                testedOrientation = getOrientation();
            }
            orientate(testedOrientation);
            setRandomSpellWait();

            if (RandomGenerator.getInstance().nextInt(2) == 0) {
                currentState = DarkStates.ATTACKING;
            } else {
                currentState = DarkStates.SUMMONING;
            }
        }

        switch (currentState) {
            case IDLE:
                if (!isDisplacementOccurs() && idleCount == 0) {
                    orientate(orientateMonster(CHANGE_ORIENT_PROBAB));
                    move(MOVEMENT_SPEED);
                    if (RandomGenerator.getInstance().nextInt(100) < 15) {
                        setRandomIdleCount();
                    }
                    statesAnimation[0][getOrientation().ordinal()].reset();
                } else {
                    currentAliveAnim = statesAnimation[0][getOrientation().ordinal()];
                }
                break;
            case ATTACKING:
                currentAliveAnim = statesAnimation[1][getOrientation().ordinal()];
                if (currentAliveAnim.isCompleted()) {
                    if (getOwnerArea().canEnterAreaCells(testFire, getCellsFacing(getOrientation()))) {
                        getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(), getCellsFacing(getOrientation()).get(0),
                                MIN_FIRE_FORCE + RandomGenerator.getInstance().nextInt(MAX_FIRE_FORCE - MIN_FIRE_FORCE)));
                    }
                    currentState = DarkStates.IDLE;
                    currentAliveAnim.reset();
                }
                break;
            case SUMMONING:
                currentAliveAnim = statesAnimation[1][getOrientation().ordinal()];
                if (currentAliveAnim.isCompleted()) {
                    if (getOwnerArea().canEnterAreaCells(testSkull, getCellsFacing(getOrientation()))) {
                        getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), getCellsFacing(getOrientation()).get(0)));
                    }
                    currentState = DarkStates.IDLE;
                    currentAliveAnim.reset();
                }
                break;
            case ABOUT_TO_TP:
                currentAliveAnim = statesAnimation[1][getOrientation().ordinal()];
                if (currentAliveAnim.isCompleted()) {
                    currentState = DarkStates.TELEPORTING;
                    currentAliveAnim.reset();
                }
                break;
            case TELEPORTING:
                Vector deltaVector;
                boolean hasTped = false;
                int nbTries = 0;
                List<DiscreteCoordinates> targetCells;
                while (!hasTped && nbTries < MAX_TP_TRIES) {
                    deltaVector = new Vector(createRandomCoord(), createRandomCoord());
                    targetCells = getCellsAtVector(deltaVector);
                    ++nbTries;
                    if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, targetCells)) {

                        getOwnerArea().leaveAreaCells(this, getCurrentCells());
                        Vector targetVector = new Vector(getCellsAtVector(deltaVector).get(0).x,
                                getCellsAtVector(deltaVector).get(0).y);
                        setCurrentPosition(targetVector);
                        getOwnerArea().enterAreaCells(this, targetCells);
                        hasTped = true;
                    }
                }
                currentState = DarkStates.IDLE;
                currentAliveAnim = statesAnimation[0][getOrientation().ordinal()];
                break;
        }

        if ((idleCount <= 0 && isDisplacementOccurs()) || currentState != DarkStates.IDLE) {
            currentAliveAnim.update(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    protected Animation getCurrentAnimation() {
        return currentAliveAnim;
    }

    private int createRandomCoord() {
        int randInt = MIN_TP_RADIUS + RandomGenerator.getInstance().nextInt(MAX_TP_RADIUS - MIN_TP_RADIUS);
        if (RandomGenerator.getInstance().nextInt(2) == 0) {
            return (-1 * randInt);
        }
        return randInt;
    }

    /**
     * Auxiliary method for orientation choosing in preparation for attacking, called in doWhile loop in update
     *
     * @param excludedOrients (Orientation...) orientations that have already been tested and are invalid
     * @return Orientation[] identical to Orientation.values() minus the excluded orientations
     */
    private Orientation[] createOrientationArrayExcluding(Orientation... excludedOrients) {
        List<Orientation> newPossibOrients = new ArrayList<>(Arrays.asList(Orientation.values()));
        newPossibOrients.removeAll(Arrays.asList(excludedOrients));

        //Python lambda expressions nostalgia settling in:
        //Using the double colon operator here to create an Orientation array via implicit constructor call
        //(suggested by Intellij) -other usage: ARPGInventory.switchItem()...
        return newPossibOrients.toArray(Orientation[]::new);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        List<DiscreteCoordinates> surroundingCells = new ArrayList<>();
        DiscreteCoordinates bottomLeftCoords = getCurrentMainCellCoordinates().jump(-1 * (FOV_RADIUS - 2), -1 * (FOV_RADIUS - 2));
        for (int i = 0; i < FOV_RADIUS; ++i) {
            for (int j = 0; j < FOV_RADIUS; ++j) {
                surroundingCells.add(bottomLeftCoords.jump(i, j));
            }
        }
        //Remove current main cell
        surroundingCells.remove(getCurrentCells().get(0));
        return surroundingCells;
    }

    private List<DiscreteCoordinates> getCellsFacing(Orientation orientation) {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(orientation.toVector()));
    }

    private List<DiscreteCoordinates> getCellsAtVector(Vector addVector) {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(addVector));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public Droppables dropItem() {
        return Droppables.CASTLEKEY;
    }

    private enum DarkStates {
        IDLE,
        ATTACKING,
        SUMMONING,
        ABOUT_TO_TP,
        TELEPORTING,
        ;
    }

    private class DarkLordHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            if (currentState != DarkStates.TELEPORTING) {
                currentState = DarkStates.ABOUT_TO_TP;
            }
        }
    }
}
