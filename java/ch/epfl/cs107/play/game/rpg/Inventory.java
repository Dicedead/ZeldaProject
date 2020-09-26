package ch.epfl.cs107.play.game.rpg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.epfl.cs107.play.game.rpg.RPG.InventoryItem;

/**
 * Creating a new class inventory where we are going to manage the inventory
 */
abstract public class Inventory {

    protected static final float MAX_WEIGHT = 45.f;
    protected Map<InventoryItem, Integer> items = new HashMap<>();
    protected float inventoryWeight = 0.f;


    protected boolean addItem(InventoryItem toBeAddedItem, int numberToAdd) {

        Integer quantityOfItem = 0;
        float itemsWeight = 0f;
        for (Entry<InventoryItem, Integer> item : items.entrySet()) {
            if (item.getKey().equals(toBeAddedItem)) {
                quantityOfItem = item.getValue();
            }
            itemsWeight = item.getKey().getWeight() * item.getValue();
            inventoryWeight += itemsWeight;
        }

        if (inventoryWeight <= MAX_WEIGHT) {
            items.put(toBeAddedItem, quantityOfItem + numberToAdd);
            return true;
        } else {
            inventoryWeight -= itemsWeight;
            if (numberToAdd - 1 > 0) {
                addItem(toBeAddedItem, numberToAdd - 1);
            }
            return false;
        }
    }

    protected boolean deleteItem(InventoryItem toBeDeletedItem, int numberToDelete) {

        Integer quantityOfItem = 0;

        for (Entry<InventoryItem, Integer> item : items.entrySet()) {

            if (item.getKey().equals(toBeDeletedItem)) {
                quantityOfItem = item.getValue();
            }
            inventoryWeight -= item.getKey().getWeight() * item.getValue();
        }
        if (inventoryWeight >= 0 && quantityOfItem > 0) {

            if (quantityOfItem - numberToDelete == 0) {
                items.remove(toBeDeletedItem);
            } else {
                items.put(toBeDeletedItem, quantityOfItem - numberToDelete);
            }

            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfItems(InventoryItem itemName) {

        int quantityOfItem;

        quantityOfItem = items.get(itemName);
        return quantityOfItem;
    }

    public boolean contains(InventoryItem itemNames) {
        for (Entry<InventoryItem, Integer> item : items.entrySet()) {
            if (item.getKey().equals(itemNames) && item.getValue() != 0) {
                return true;
            }
        }
        return false;
    }

    public interface Holder {
        boolean possess(InventoryItem item);
    }
}