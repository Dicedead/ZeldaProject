package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.rpg.Inventory;
import ch.epfl.cs107.play.game.rpg.RPG.InventoryItem;

public class ARPGInventory extends Inventory {
    private int currentMoney = 100;
    private float fortune = currentMoney;
    private static final int MAX_MONEY = 999;
    private int itemPrice;

    private int indexMap = 0;

    protected void addMoney(int money) {
        if (money > 0 && currentMoney + money < MAX_MONEY) {
            currentMoney += money;
        } else {
            currentMoney = MAX_MONEY;
        }
    }

    protected void delMoney(int money) {
        if (money > 0 && currentMoney - money > 0) {
            currentMoney -= money;
        } else {
            currentMoney = 0;
        }
    }

    public int getMoney() {
        return currentMoney;
    }

    @Override
    protected boolean addItem(InventoryItem toBeAddedItem, int numberToAdd) {
        if (inventoryWeight + toBeAddedItem.getWeight() * numberToAdd <= MAX_WEIGHT) {
            itemPrice = toBeAddedItem.getPrice();
            fortune += itemPrice * numberToAdd;
        }
        return super.addItem(toBeAddedItem, numberToAdd);
    }

    @Override
    protected boolean deleteItem(InventoryItem toBeDeletedItem, int numberToDelete) {
        itemPrice = toBeDeletedItem.getPrice();
        fortune -= itemPrice * numberToDelete;
        return super.deleteItem(toBeDeletedItem, numberToDelete);
    }

    public float getFortune() {
        return fortune;
    }

    /**
     * Method switching the index of the item, modifying the current item used and returning the new selected Item
     *
     * @return itemsArray[indexMap] (InventoryItem)
     */
    protected ARPGItem switchItem() {

        //Using the double colon operator here to create an InventoryItem array via implicit constructor call
        //(suggested by Intellij) -other usage: DarkLord.createOrientationArrayExcluding()...
        InventoryItem[] itemsArray = items.keySet().toArray(InventoryItem[]::new);

        ++indexMap;
        if (items.size() != 0) {
            indexMap = indexMap % items.size();
            return (ARPGItem) itemsArray[indexMap];
        }
        else {
            addItem(ARPGItem.Stick,1);
            return ARPGItem.Stick;
        }
    }
}