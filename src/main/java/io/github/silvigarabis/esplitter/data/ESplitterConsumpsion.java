package io.github.silvigarabis.esplitter.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ESplitterConsumpsion {
    private int experiencePoint;
    private int experienceLevel;

    private Material item1;
    private int item1Amount;
    private Material item2;
    private int item2Amount;

    private Material backItem1;
    private int backItem1Amount;
    private Material backItem2;
    private int backItem2Amount;

    public int getExperiencePoint() {
        return experiencePoint;
    }

    public void setExperiencePoint(int experiencePoint) {
        this.experiencePoint = experiencePoint;
    }

    public int changeEXperiencePoint(int experiencePoint) {
        return this.experiencePoint += experiencePoint;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public int changeEXperienceLevel(int experienceLevel) {
        return this.experienceLevel += experienceLevel;
    }

    public Material getItem1() {
        return item1;
    }

    public void setItem1(Material item1) {
        this.item1 = item1;
    }

    public int getItem1Amount() {
        return item1Amount;
    }

    public void setItem1Amount(int item1Amount) {
        this.item1Amount = item1Amount;
    }

    public int changeItem1Amount(int item1Amount) {
        return this.item1Amount += item1Amount;
    }

    public Material getItem2() {
        return item2;
    }

    public void setItem2(Material item2) {
        this.item2 = item2;
    }

    public int getItem2Amount() {
        return item2Amount;
    }

    public void setItem2Amount(int item2Amount) {
        this.item2Amount = item2Amount;
    }

    public int changeItem2Amount(int item2Amount) {
        return this.item2Amount += item2Amount;
    }

    public Material getBackItem1() {
        return backItem1;
    }

    public void setBackItem1(Material backItem1) {
        this.backItem1 = backItem1;
    }

    public int getBackItem1Amount() {
        return backItem1Amount;
    }

    public void setBackItem1Amount(int backItem1Amount) {
        this.backItem1Amount = backItem1Amount;
    }

    public int changeBackItem1Amount(int backItem1Amount) {
        return this.backItem1Amount += backItem1Amount;
    }

    public Material getBackItem2() {
        return backItem2;
    }

    public void setBackItem2(Material backItem2) {
        this.backItem2 = backItem2;
    }

    public int getBackItem2Amount() {
        return backItem2Amount;
    }

    public void setBackItem2Amount(int backItem2Amount) {
        this.backItem2Amount = backItem2Amount;
    }

    public int changeBackItem2Amount(int amount) {
        return this.backItem2Amount += amount;
    }

    public ESplitterConsumpsion() {

    }

    public ESplitterConsumpsion(int experiencePoint, int experienceLevel, Material item1, int item1Amount,
            Material item2, int item2Amount, Material backItem1, int backItem1Amount, Material backItem2,
            int backItem2Amount) {
        this.experiencePoint = experiencePoint;
        this.experienceLevel = experienceLevel;
        this.item1 = item1;
        this.item2 = item2;
        this.item1Amount = item1Amount;
        this.item2Amount = item2Amount;
        this.backItem1 = backItem1;
        this.backItem2 = backItem2;
        this.backItem1Amount = backItem1Amount;
        this.backItem2Amount = backItem2Amount;
    }

    public ESplitterConsumpsion clone() {
        return new ESplitterConsumpsion(this.experiencePoint, this.experienceLevel, this.item1, this.item1Amount,
                this.item2, this.item2Amount, this.backItem1, this.backItem1Amount, this.backItem2,
                this.backItem2Amount);
    }

    public static boolean removeConsumpsion(Player player, ESplitterConsumpsion consumpsion) {
        // 所有操作都要在这时候依次同步完成以避免错误
        LinkedList<Runnable> instantOpList = new LinkedList<>();
        try {
            for (var opGenerator : opGenerators) {
                try {
                    instantOpList.addAll(opGenerator.apply(player, consumpsion));
                } catch (NoOpException ex) {
                    continue;
                }
            }
        } catch (OpCantExecuteException ex) {
            return false;
        }
        instantOpList.forEach(op -> op.run());
        return true;
    }

    public static class NoOpException extends RuntimeException {
    }
    public static class OpCantExecuteException extends RuntimeException {
    }

    private static LinkedList<BiFunction<Player, ESplitterConsumpsion, LinkedList<Runnable>>> opGenerators = new LinkedList<>();
    static {
        opGenerators.add(ESplitterConsumpsion::genOpTakePlayerExp);
        opGenerators.add(ESplitterConsumpsion::genOpTakePlayerInv);
        opGenerators.add(ESplitterConsumpsion::genOpGivePlayerInv);
    }

    private static LinkedList<Runnable> genOpTakePlayerExp(Player player, ESplitterConsumpsion consumpsion){
        LinkedList<Runnable> opList = new LinkedList<>();
        if (consumpsion.getExperienceLevel() == 0 && consumpsion.getExperiencePoint() == 0) {
            throw new NoOpException();
        }

        final var playerCurrentExp = player.getTotalExperience();
        final var playerCurrentLevel = player.getLevel();
        final var expChange = consumpsion.getExperiencePoint();
        final var expLevelChange = consumpsion.getExperienceLevel();

        var expPoint = playerCurrentExp;
        var expLevel = playerCurrentLevel;

        if (expChange != 0) {
            expPoint -= expChange;
            if (expPoint < 0) {
                throw new OpCantExecuteException();
            }

            // 这样做是为了获取在扣除经验点后的经验等级数目
            player.setTotalExperience(expPoint);
            expLevel = player.getLevel();
            player.setTotalExperience(playerCurrentExp);

            final var expPointFinalized = expPoint;
            opList.add(() -> {
                player.setTotalExperience(expPointFinalized);
            });
        }

        if (expLevelChange != 0) {
            expLevel -= expLevelChange;
            if (expLevel < 0) {
                throw new OpCantExecuteException();
            }
            final var expLevelFinalized = expLevel;
            opList.add(() -> {
                player.setTotalExperience(expLevelFinalized);
            });
        }
        return opList;
    }

    private static LinkedList<Runnable> genOpTakePlayerInv(Player player, ESplitterConsumpsion consumpsion){
        LinkedList<Runnable> opList = new LinkedList<>();
        final var item1 = consumpsion.getItem1();
        final var item2 = consumpsion.getItem2();
        final int item1Amount = consumpsion.getItem1Amount();
        final int item2Amount = consumpsion.getItem2Amount();

        final List<Pair<Material, Integer>> itemsToRemove = new ArrayList<>();
        if (item1 != null) {
            itemsToRemove.add(Pair.of(item1, item1Amount));
        }
        if (item2 != null) {
            itemsToRemove.add(Pair.of(item2, item2Amount));
        }
        if (itemsToRemove.isEmpty()) {
            throw new NoOpException();
        }

        Map<Integer, ItemStack> inventoryModified = new HashMap<>();
        int[] removalProgress = new int[itemsToRemove.size()];

        // 遍历每个物品和数量
        for (int i = 0; i < itemsToRemove.size(); i++) {
            var entry = itemsToRemove.get(i);
            Material material = entry.getKey();
            int targetAmount = entry.getValue();

            @SuppressWarnings("unchecked")
            Map<Integer, ItemStack> storedItems = (Map<Integer, ItemStack>) player.getInventory().all(material);
            storedItems.putAll(inventoryModified);

            for (Map.Entry<Integer, ? extends ItemStack> slotEntry : storedItems.entrySet()) {
                if (removalProgress[i] == targetAmount) {
                    break;
                }

                int slot = slotEntry.getKey();
                ItemStack itemStack = slotEntry.getValue();
                int amount = itemStack.getAmount();

                if (itemStack.getType() != material) {
                    continue;
                }

                int removedAmount = Math.min(amount, targetAmount - removalProgress[i]);
                itemStack.setAmount(amount - removedAmount);
                removalProgress[i] += removedAmount;

                // 如果物品用完，清除
                if (itemStack.getAmount() == 0) {
                    inventoryModified.put(slot, null);
                }
            }

            // 如果目标数量未完成，返回失败
            if (removalProgress[i] != targetAmount) {
                throw new OpCantExecuteException();
            }
        }

        // 提交所有变更
        opList.add(() -> inventoryModified.forEach(player.getInventory()::setItem));

        return opList;
    }

    private static LinkedList<Runnable> genOpGivePlayerInv(Player player, ESplitterConsumpsion consumpsion) {
        LinkedList<Runnable> opList = new LinkedList<>();
        final var backItem1 = consumpsion.getBackItem1();
        final var backItem2 = consumpsion.getBackItem2();
        final int backItem1Amount = consumpsion.getBackItem1Amount();
        final int backItem2Amount = consumpsion.getBackItem2Amount();

        List<ItemStack> itemsToGive = new ArrayList<>();
        if (backItem1 != null) {
            itemsToGive.add(new ItemStack(backItem1, backItem1Amount));
        }
        if (backItem2 != null) {
            itemsToGive.add(new ItemStack(backItem2, backItem2Amount));
        }
        if (itemsToGive.isEmpty()) {
            throw new NoOpException();
        }
        var dropLocation = player.getLocation();
        if (itemsToGive.size() > 0) {
            opList.add(() -> itemsToGive.forEach(item -> dropLocation.getWorld().dropItem(dropLocation, item)));
        }

        return opList;
    }
}
