package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.math.RandomGenerator;

public interface CollectableDropperEntity {

    default Droppables dropItem() {

        double randomGen = RandomGenerator.getInstance().nextDouble();
        if (randomGen > 0.7) {
            if (randomGen > 0.9) {
                return Droppables.HEART;
            }
            else {
                return Droppables.COIN;
            }
        } else {
            return Droppables.VOID;
        }
    }

    enum Droppables {
        HEART,
        COIN,
        CHERRY,
        CASTLEKEY,
        VOID,;
    }
}
