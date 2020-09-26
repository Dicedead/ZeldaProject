package ch.epfl.cs107.play.game.arpg.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGAggressiveVisitor;
import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.VulnerableActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player implements Inventory.Holder, Test {

    private final static int WALKING_SPEED = 4;
    private Keyboard keyboard;
    private ARPGInteractionVisitor handler = new ARPGPlayerHandlerARPG();

    private Animation[][] playerAnimations;
    private Animation currentAnimation;
    private Sprite deadSprite;

    public final static float MAX_HP = 5.f;
    private final static float SWORD_DAMAGE = 0.5f;
    private float hp;
    private boolean isAlive;
    private int takingDamage;
    private static final int TAKING_DAMAGE_RESET = 20;
    private boolean isProtected;

    private ARPGItem currentItem;
    private ARPGInventory inventory;
    private PlayerStates currentState;
    private int currentSpeed;
    private boolean usingItem;
    private boolean holdBow;
    private boolean animationHold;

    private Bomb testBomb;
    private Arrow testProjectile;
    private boolean canPlace;
    private boolean spaceIsPressed;
    private boolean spaceIsDown;
    private boolean spaceIsReleased;
    private ARPGPlayerStatusGUI playerStatusGUI;
    private final static int MAX_INTERACTIONS = 3;
    private int numberOfInteractions;
    private boolean canBuy;
    private Dialog dialogSeller;
    private boolean isReading;

    /**
     * Default ARPGPlayer constructor
     *
     * @param area        (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        playerAnimations = new Animation[7][4];
        createAnimations();
        currentAnimation = playerAnimations[3][2];

        hp = MAX_HP;
        testBomb = new Bomb(getOwnerArea(), getCurrentMainCellCoordinates());
        testProjectile = new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());

        inventory = new ARPGInventory();

        inventory.addItem(ARPGItem.Bomb, 5);
        inventory.addItem(ARPGItem.Sword, 1);
        inventory.addItem(ARPGItem.Arrow, 10);
        inventory.addItem(ARPGItem.Bow, 1);

        if (MODE) {
            inventory.addItem(ARPGItem.Staff, 1);
            inventory.addItem(ARPGItem.Shield, 1);
            inventory.addItem(ARPGItem.Bomb, 50);
            inventory.addItem(ARPGItem.Arrow, 60);
            inventory.addItem(ARPGItem.CastleKey, 1);
            inventory.addItem(ARPGItem.Cherry, 10);
            inventory.addItem(ARPGItem.Bicycle, 1);
        }

        currentItem = inventory.switchItem();
        usingItem = false;
        holdBow = false;
        animationHold = false;

        takingDamage = TAKING_DAMAGE_RESET;

        playerStatusGUI = new ARPGPlayerStatusGUI();
        resetMotion();

        currentState = PlayerStates.DEFAULT;

        deadSprite = new Sprite("ghost.1", 1, 1, this);
        numberOfInteractions = 0;
        isProtected = false;
        canBuy = false;
        isReading = false;
    }

    /**
     * Creates all 7 of the player's animations
     */
    private void createAnimations() {
        String[] paths = {".sword", ".shield", ".bow", ".staff_water", "", ".carry.bomb"};
        Sprite[][][] sprites32by32 = new Sprite[4][4][4];
        Vector anchor = new Vector(-0.5f, 0);
        for (int i = 0; i < 4; ++i) {
            sprites32by32[i] = RPGSprite.extractSprites("zelda/player" + paths[i], 4, 2, 2, this, 32, 32, new Orientation[]
                    {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
            playerAnimations[i] = RPGSprite.createAnimations(WALKING_SPEED / 2, sprites32by32[i], false);
            for (int j = 0; j < 4; ++j) {
                playerAnimations[i][j].setAnchor(anchor);
            }
        }
        Sprite[][][] sprites16by32 = new Sprite[2][4][4];
        for (int i = 0; i < 2; ++i) {
            sprites16by32[i] = RPGSprite.extractSprites("zelda/player" + paths[i + 4], 4, 1, 2, this, 16, 32, new Orientation[]
                    {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
            playerAnimations[i + 4] = RPGSprite.createAnimations(WALKING_SPEED / 2, sprites16by32[i]);
        }
        Sprite[][] spritesBicycle = RPGSprite.extractSprites("zelda/bicyclesprites", 4, 1, 1.25f, this, 16, 16, new Orientation[]
                {Orientation.DOWN, Orientation.LEFT, Orientation.UP, Orientation.RIGHT});
        playerAnimations[6] = RPGSprite.createAnimations(1, spritesBicycle);
    }

    private enum PlayerStates {
        SWORD_FIGHT(WALKING_SPEED),
        SHIELD_FIGHT(WALKING_SPEED * 2),
        BOW_FIGHT(WALKING_SPEED),
        STAFF_FIGHT(WALKING_SPEED),
        DEFAULT(WALKING_SPEED),
        BOMB_MOVING(WALKING_SPEED),
        BICYCLE_MOVE(WALKING_SPEED / 2),
        ;

        private final int movementSpeed;

        PlayerStates(int speed) {
            movementSpeed = speed;
        }

        private int getMoveSpeed() {
            return movementSpeed;
        }
    }

    /**
     * Updates walking moves & animations
     *
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {
        if (!MODE) {
            isAlive = hp > 0;
        } else {
            isAlive = true;
        }

        useItem();

        playerStatusGUI.setGUIInformation(hp, inventory.getMoney(), currentItem, inventory.getNumberOfItems(currentItem));

        //Allowing movements even if player is dead/in ghost mode
        if (!isReading) {
            //this condition prevents the player from walking away while reading a dialog
            if (!usingItem) {
                currentSpeed = currentState.getMoveSpeed();
                keyboard = getOwnerArea().getKeyboard();
                moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT), currentSpeed);
                moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP), currentSpeed);
                moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT), currentSpeed);
                moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN), currentSpeed);
            }

            currentAnimation = playerAnimations[currentState.ordinal()][getOrientation().ordinal()];
            if (isDisplacementOccurs() || (usingItem && (!currentAnimation.isCompleted() && (currentState != PlayerStates.BOW_FIGHT ||
                    animationHold) && !holdBow))) {
                currentAnimation.update(deltaTime);
            } else {
                currentAnimation.reset();
            }
        } else {
            if (keyboard.get(Keyboard.V).isPressed()) {
                isReading = false;
            }
        }
        super.update(deltaTime);
    }

    /**
     * Walking and orientating method
     *
     * @param orientation (Orientation, enum) direction
     * @param b           (Button) arrow button
     */
    private void moveOrientate(Orientation orientation, Button b, int speed) {
        if ((currentState != PlayerStates.SHIELD_FIGHT && b.isDown()) ||
                (currentState == PlayerStates.SHIELD_FIGHT && b.isPressed())) {
            if (getOrientation() == orientation) move(speed);
            else orientate(orientation);
        }
    }

    @Override
    public void draw(Canvas canvas) {

        //This whole takingDamage contraption simply makes the Player flash when they've lost some hp
        //for some time
        if (takingDamage % 4 == 0 && takingDamage != 0 && isAlive) {
            currentAnimation.draw(canvas);
        } else {
            if (takingDamage <= 0) {
                takingDamage = TAKING_DAMAGE_RESET;
            }
        }
        if (takingDamage != TAKING_DAMAGE_RESET) {
            --takingDamage;
        }

        if (isAlive) {
            playerStatusGUI.draw(canvas);
        } else {
            deadSprite.draw(canvas);
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
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return (getOwnerArea().getKeyboard().get(Keyboard.E).isPressed()) ||
                (currentState == PlayerStates.SWORD_FIGHT && spaceIsDown);
    }

    /**
     * Generic interaction call
     *
     * @param other (Interactable). Not null
     */
    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public boolean takeCellSpace() {
        return isAlive;
    }

    @Override
    public boolean isCellInteractable() {
        return isAlive;
    }

    @Override
    public boolean isViewInteractable() {
        return isAlive;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    void loseHp(float deltaHp) {
        if (takingDamage == TAKING_DAMAGE_RESET) {
            --takingDamage;
            if (isProtected) {
                hp -= deltaHp / 2;
            } else {
                hp -= deltaHp;
            }
        }
    }

    private void addHp(float deltaHp) {
        if (hp + deltaHp <= MAX_HP) {
            hp += deltaHp;
        } else {
            hp = MAX_HP;
        }
    }

    /**
     * Takes care of all things inventory related and switches player states accordingly
     */
    private void useItem() {
        keyboard = getOwnerArea().getKeyboard();

        if (!usingItem && keyboard.get(Keyboard.TAB).isPressed()) {
            currentItem = inventory.switchItem();
        }

        //Allowing the free placing of bombs in RouteChateau as long as they can physically be spawned
        canPlace = getOwnerArea().canEnterAreaCells(testBomb, getFieldOfViewCells());

        if (MODE && getOwnerArea().getTitle().equals("zelda/RouteChateau")) {

            if (keyboard.get(Keyboard.B).isPressed() && canPlace) {
                getOwnerArea().registerActor(new Bomb(getOwnerArea(), getFieldOfViewCells().get(0),
                        true));
            }

            if (keyboard.get(Keyboard.S).isPressed()) {
                getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), new DiscreteCoordinates(8, 10)));
            }

            if (keyboard.get(Keyboard.L).isPressed()) {
                getOwnerArea().registerActor(new LogMonster(getOwnerArea(), new DiscreteCoordinates(9, 9)));
            }
        }

        spaceIsPressed = (keyboard.get(Keyboard.SPACE).isPressed());
        spaceIsDown = (keyboard.get(Keyboard.SPACE).isDown());
        spaceIsReleased = (keyboard.get(Keyboard.SPACE).isReleased());

        switch (currentItem) {

            //Continuously slashes but ends after space is no longer down and animation has been played
            case Sword:
                if (spaceIsDown) {
                    currentState = PlayerStates.SWORD_FIGHT;
                    usingItem = true;
                } else {
                    if (currentAnimation.isCompleted()) {
                        currentAnimation.reset();
                        usingItem = false;
                        currentState = PlayerStates.DEFAULT;
                    }
                }
                break;

            //Allows maintaining the bow steady until space is released
            case Bow:
                if (spaceIsDown) {
                    currentState = PlayerStates.BOW_FIGHT;
                    usingItem = true;
                } else {
                    if (usingItem) {
                        if (!animationHold && possess(ARPGItem.Arrow) && getOwnerArea().canEnterAreaCells(testProjectile, getFieldOfViewCells())) {
                            getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0)));
                            inventory.deleteItem(ARPGItem.Arrow, 1);
                        }

                        animationHold = true;
                        if (currentAnimation.isCompleted()) {
                            currentState = PlayerStates.DEFAULT;
                            currentAnimation.reset();
                            holdBow = false;
                            usingItem = false;
                            animationHold = false;
                        }
                    }
                }
                break;

            case Staff:
                if (spaceIsPressed) {
                    currentState = PlayerStates.STAFF_FIGHT;
                    usingItem = true;
                } else if (usingItem) {
                    if (!animationHold && getOwnerArea().canEnterAreaCells(testProjectile, getFieldOfViewCells())) {

                        getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(), getOrientation(),
                                getFieldOfViewCells().get(0)));
                    }
                    animationHold = true;

                    if (currentAnimation.isCompleted()) {
                        currentState = PlayerStates.DEFAULT;
                        currentAnimation.reset();
                        usingItem = false;
                        animationHold = false;
                    }
                }
                break;

            case Bomb:
                if (spaceIsDown) {
                    currentState = PlayerStates.BOMB_MOVING;
                } else {
                    resetAnimations(playerAnimations[PlayerStates.BOMB_MOVING.ordinal()]);
                    if (spaceIsReleased && inventory.contains(ARPGItem.Bomb) && canPlace) {
                        getOwnerArea().registerActor(new Bomb(getOwnerArea(), getFieldOfViewCells().get(0), true));
                        inventory.deleteItem(ARPGItem.Bomb, 1);
                        currentState = PlayerStates.DEFAULT;
                    }
                }
                break;

            case Bicycle:
                currentState = PlayerStates.BICYCLE_MOVE;
                break;

            case Cherry:
                if (currentState != PlayerStates.BICYCLE_MOVE) {
                    currentState = PlayerStates.DEFAULT;
                }
                if (spaceIsPressed && inventory.contains(ARPGItem.Cherry) && hp < MAX_HP) {
                    addHp(Cherry.REGEN);
                    inventory.deleteItem(ARPGItem.Cherry, 1);
                }
                break;

            case Shield:
                isProtected = spaceIsDown && inventory.contains(ARPGItem.Shield);
                if (isProtected) {
                    currentState = PlayerStates.SHIELD_FIGHT;
                } else {
                    if (currentState != PlayerStates.BICYCLE_MOVE) {
                        currentState = PlayerStates.DEFAULT;
                    }
                }
                break;

            default:
                if (currentState != PlayerStates.BICYCLE_MOVE) {
                    currentState = PlayerStates.DEFAULT;
                }
                break;
        }

        if (!inventory.contains(currentItem)) {
            currentItem = inventory.switchItem();
        }
    }

    /**
     * Utilitary for resetting all 4 animations of some state
     *
     * @param stateAnimations (Animation[])
     */
    private static void resetAnimations(Animation[] stateAnimations) {
        for (int i = 0; i < 4; ++i) {
            stateAnimations[i].reset();
        }
    }

    @Override
    public boolean possess(RPG.InventoryItem item) {
        return inventory.contains(item);
    }

    public void setIsNotReading() {
        isReading = false;
    }

    public boolean getIsReading() {
        return isReading;
    }

    private class ARPGPlayerHandlerARPG implements ARPGAggressiveVisitor {

        /**
         * Flips the "passing door" switch in Player, later reset
         *
         * @param door (Door), not null
         */
        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(CaveDoor caveDoor) {
            if (caveDoor.getIsOpen()) {
                setIsPassingADoor(caveDoor);
            }
        }

        /**
         * Sets the grass to its cut state, engaging slicing animation
         *
         * @param grass (Grass), not null
         */
        @Override
        public void interactWith(Grass grass) {
            if (currentState == PlayerStates.SWORD_FIGHT) {
                grass.setIsCut();
            }
        }

        @Override
        public void interactWith(Jar jar) {
            if (getFieldOfViewCells().equals(jar.getCurrentCells())) {
                jar.setIsDestroyed();
            }
        }

        /**
         * Generous entity interaction...
         *
         * @param assistant (Assistant), not null
         */
        @Override
        public void interactWith(Assistant assistant) {
            assistant.setDialogs();
            isReading = true;
            if (numberOfInteractions < 1) {
                inventory.addItem(ARPGItem.Bomb, 10);
                inventory.addMoney(169);
                ++numberOfInteractions;
            }

        }

        @Override
        public void interactWith(Coin coin) {
            coin.setIsCollected();
            if (coin.isHasBeenCollected()) {
                inventory.addMoney(Coin.VALUE);
            }
        }

        @Override
        public void interactWith(Heart heart) {
            heart.setIsCollected();
            if (heart.isHasBeenCollected() && isAlive && hp < MAX_HP) {
                addHp(Heart.VALUE);
            }
        }

        @Override
        public void interactWith(CastleKey castleKey) {
            castleKey.setIsCollected();
            if (castleKey.isHasBeenCollected() && isAlive) {
                inventory.addItem(ARPGItem.CastleKey, 1);
            }
        }

        //NOTE:
        // TWO cherries are added purposefully (two cherries in the sprite...)
        @Override
        public void interactWith(Cherry cherry) {
            cherry.setIsCollected();
            if (cherry.isHasBeenCollected() && isAlive) {
                inventory.addItem(ARPGItem.Cherry, 2);
            }
        }

        /**
         * Sets the cherry tree to the state where it gives cherries to the player
         *
         * @param cherryTree (CherryTree), not null
         */
        @Override
        public void interactWith(CherryTree cherryTree) {

            if (numberOfInteractions < MAX_INTERACTIONS) {
                inventory.addItem(ARPGItem.Cherry, 2);
                ++numberOfInteractions;
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the CastleDoor (open with Key and close when we enter)
         * The first interaction must be a view interaction, as CastleDoor's cellInteraction is set off and its takeCellSpace
         * is set to true. After this first interaction, if the player had a castlekey, the door will open up.
         *
         * @param castleDoor (CastleDoor), not null
         */
        @Override
        public void interactWith(CastleDoor castleDoor) {

            if (inventory.contains(ARPGItem.CastleKey)) {
                castleDoor.openDoor();
            }

            if (castleDoor.isOpen()) {
                ArrayList<DiscreteCoordinates> doorCoords = (ArrayList<DiscreteCoordinates>) castleDoor.getCurrentCells();
                DiscreteCoordinates playerCoords = getCurrentCells().get(0);

                for (DiscreteCoordinates doorCoord : doorCoords) {
                    if (playerCoords.equals(doorCoord)) {
                        setIsPassingADoor(castleDoor);
                        castleDoor.closeDoor();
                        return;
                    }
                }

            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the Bicycle
         *
         * @param bicycle (Bicycle), not null
         */
        @Override
        public void interactWith(Bicycle bicycle) {
            bicycle.setIsCollected();
            if (bicycle.isHasBeenCollected() && isAlive) {
                inventory.addItem(ARPGItem.Bicycle, 1);
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the pressure plate
         *
         * @param press (PressurePlate), not null
         */
        @Override
        public void interactWith(PressurePlate press) {
            press.setOn();
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the Mew
         *
         * @param mew (Mew), not null
         */
        @Override
        public void interactWith(Mew mew) {
            mew.setIsCollected();
            if (mew.isHasBeenCollected() && isAlive) {
                inventory.addItem(ARPGItem.Mew, 1);
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the King
         *
         * @param king (King), not null
         */
        @Override
        public void interactWith(King king) {
            king.setDialogs();
            isReading = true;
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the NPCCenter
         *
         * @param doctor (NPCCenter), not null
         */
        @Override
        public void interactWith(NPCCenter doctor) {
            if (hp == MAX_HP) {
                doctor.setDialogs();
                isReading = true;
            }
            if (hp != MAX_HP) {
                hp = MAX_HP;
                inventory.addItem(ARPGItem.Cherry, 2);
                doctor.setDialogs();
                doctor.setCanHeal();
            }
        }

        @Override
        public void interactWith(Chest chest) {
            if (chest.getIsOpenable()) {
                chest.openChest();
                inventory.addItem(ARPGItem.Shield, 1);
            }
        }

        /**
         * Attacks all vulnerable actors
         *
         * @param enemy (VulnerableActor)
         */
        @Override
        public void interactWith(VulnerableActor enemy) {
            if (currentState == PlayerStates.SWORD_FIGHT) {
                enemy.loseHp(SWORD_DAMAGE, VulnerableActor.Vulnerabilities.PHYSICAL);
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the ShopAssistant
         *
         * @param seller (ShopAssistant), not null
         */
        @Override
        public void interactWith(ShopAssistant seller) {
            seller.setDialogs();
            isReading = true;

            if (inventory.getMoney() >= 100 && (keyboard.get(Keyboard.E).isPressed()) && canBuy) {
                dialogSeller = seller.getDialog_Seller();
                inventory.addItem(ARPGItem.Bomb, 10);
                inventory.delMoney(ARPGItem.Bomb.getPrice() * 10);
                canBuy = false;
                getOwnerArea().unregisterActor(seller);
                //dialogSeller.resetDialog("Thank you for the trade ! See you soon...");
            } else if (inventory.getMoney() < 100 && (keyboard.get(Keyboard.E).isPressed()) && !canBuy) {
                dialogSeller = seller.getDialog_Seller();
                dialogSeller.resetDialog("Get out of my shop ! You're not rich enough to purchase this item.");
                canBuy = false;
            }

            if (!canBuy) {
                canBuy = true;
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the torch
         *
         * @param torch (Torch), not null
         */
        @Override
        public void interactWith(Torch torch) {
            if (torch.getSignal() == Logic.FALSE) {
                torch.setOn();
            } else {
                torch.setOff();
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the NPCQuest
         *
         * @param quest (NPCQuest), not null
         */
        @Override
        public void interactWith(NPCQuest quest) {
            quest.setDialogs();
            isReading = true;
            if (inventory.contains(ARPGItem.Mew) && (keyboard.get(Keyboard.E).isPressed())) {
                quest.hasMew();
                inventory.deleteItem(ARPGItem.Mew, 1);
                inventory.addMoney(500);
            }
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the sign
         *
         * @param sign (ARPGSign), not null
         */
        @Override
        public void interactWith(ARPGSign sign) {
            sign.setDialogs();
            isReading = true;
        }

        /**
         * Precise the interaction between the ARPGPlayer and the specific case of the computer
         *
         * @param computer (Computer), not null
         */
        @Override
        public void interactWith(Computer computer) {
            setIsPassingADoor(computer);
        }

        /**
         * Player - policeman interaction
         *
         * @param policeman (Policeman)
         */
        @Override
        public void interactWith(Policeman policeman) {
            policeman.setDialogs();
            isReading = true;
        }

        /**
         * Collect temple staff
         *
         * @param staff (Staff)
         */
        @Override
        public void interactWith(Staff staff) {
            staff.setIsCollected();
            if (staff.isHasBeenCollected() && isAlive) {
                inventory.addItem(ARPGItem.Staff, 1);
            }
        }
    }
}
