package ch.epfl.cs107.play.game.rpg.actor;

public interface FlyableEntity {

    default boolean canFly() {
        return true;
    }
}
