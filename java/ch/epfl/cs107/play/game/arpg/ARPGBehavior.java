package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.EnvironmentEntity;
import ch.epfl.cs107.play.game.rpg.actor.FlyableEntity;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {

    public enum ARPGCellType{

        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true,true),
        WALKABLE(-1, true, true),;

        final int type;
        final boolean isWalkable;
        final boolean isFlyable;

        ARPGCellType(int cellType, boolean walkable, boolean flyable){
            type = cellType;
            isWalkable = walkable;
            isFlyable = flyable;
        }

        public static ARPGBehavior.ARPGCellType toType(int type){
            for(ARPGBehavior.ARPGCellType ict : ARPGBehavior.ARPGCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type

            return NULL;
        }
    }

    /**
     * Default ARPGBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ARPGBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ARPGBehavior.ARPGCellType color = ARPGBehavior.ARPGCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ARPGBehavior.ARPGCell(x,y,color));
            }
        }
    }

    /**
     * Cell adapted to the ARPG game
     */
    public class ARPGCell extends Cell { //AreaBehavior.Cell
        /// Type of the cell following the enum
        private final ARPGBehavior.ARPGCellType type;

        /**
         * Default ARPGCell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (CellType), not null
         */
        public  ARPGCell(int x, int y, ARPGBehavior.ARPGCellType type){
            super(x, y);
            this.type = type;
        }

        @Override

        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {

            if (entity instanceof EnvironmentEntity) {
                return true;
            }
            if (entity instanceof FlyableEntity) {
                if (((FlyableEntity) entity).canFly()) {
                    return type.isFlyable;
                }
            }
            return ((type.isWalkable)&&(!hasNonTraversableContent() || !entity.takeCellSpace()));
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ARPGInteractionVisitor) v).interactWith(this);
        }

    }
}