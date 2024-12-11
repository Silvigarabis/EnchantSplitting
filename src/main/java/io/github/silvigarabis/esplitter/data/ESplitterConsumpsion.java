package io.github.silvigarabis.esplitter.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        // 处理经验
        if (consumpsion.getExperienceLevel() != 0 || consumpsion.getExperiencePoint() != 0) {
            final var playerCurrentExp = player.getTotalExperience();
            final var playerCurrentLevel = player.getLevel();
            final var expChange = consumpsion.getExperiencePoint();
            final var expLevelChange = consumpsion.getExperienceLevel();

            var expPoint = playerCurrentExp;
            var expLevel = playerCurrentLevel;

            if (expChange != 0) {
                expPoint -= expChange;
                if (expPoint < 0) {
                    return false;
                }

                // 这样做是为了获取在扣除经验点后的经验等级数目
                player.setTotalExperience(expPoint);
                expLevel = player.getLevel();
                player.setTotalExperience(playerCurrentExp);

                final var expPointFinalized = expPoint;
                instantOpList.add(() -> {
                    player.setTotalExperience(expPointFinalized);
                });
            }

            if (expLevelChange != 0) {
                expLevel -= expLevelChange;
                if (expLevel < 0) {
                    return false;
                }
                final var expLevelFinalized = expLevel;
                instantOpList.add(() -> {
                    player.setTotalExperience(expLevelFinalized);
                });
            }

        }

        final var item1 = consumpsion.getItem1();
        final var item2 = consumpsion.getItem2();
        final int item1Amount = consumpsion.getItem1Amount();
        final int item2Amount = consumpsion.getItem2Amount();

        //BUG: 两个物品相同会报错

        // 总之是移除物品
        if (item1 != null) {
            var storedItem1 = player.getInventory().all(item1);
            int item1RemoveCounter = 0;
            for (Map.Entry<Integer, ? extends ItemStack> entry : storedItem1.entrySet()) {
                if (item1RemoveCounter == item1Amount) {
                    break;
                }

                int slot = entry.getKey();
                ItemStack itemStack = entry.getValue();
                int amount = itemStack.getAmount();

                int removedAmount = Math.min(amount, item1Amount - item1RemoveCounter);
                amount -= removedAmount;
                item1RemoveCounter += removedAmount;

                if (amount == 0) {
                    entry.setValue(null);
                } else {
                    itemStack.setAmount(amount);
                }
            }
            if (item1RemoveCounter != item1Amount) {
                return false;
            }
            instantOpList.add(() -> {
                for (Map.Entry<Integer, ? extends ItemStack> entry : storedItem1.entrySet()) {
                    int slot = entry.getKey();
                    ItemStack itemStack = entry.getValue();
                    player.getInventory().setItem(slot, itemStack);
                }
            });
        }

        if (item2 != null) {
            var storedItem2 = player.getInventory().all(item2);
            int item2RemoveCounter = 0;
            for (Map.Entry<Integer, ? extends ItemStack> entry : storedItem2.entrySet()) {
                if (item2RemoveCounter == item2Amount) {
                    break;
                }

                int slot = entry.getKey();
                ItemStack itemStack = entry.getValue();
                int amount = itemStack.getAmount();

                int removedAmount = Math.min(amount, item2Amount - item2RemoveCounter);
                amount -= removedAmount;
                item2RemoveCounter += removedAmount;

                if (amount == 0) {
                    entry.setValue(null);
                } else {
                    itemStack.setAmount(amount);
                }
            }
            if (item2RemoveCounter != item2Amount) {
                return false;
            }
            instantOpList.add(() -> {
                for (Map.Entry<Integer, ? extends ItemStack> entry : storedItem2.entrySet()) {
                    int slot = entry.getKey();
                    ItemStack itemStack = entry.getValue();
                    player.getInventory().setItem(slot, itemStack);
                }
            });
        }

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
        var dropLocation = player.getLocation();
        if (itemsToGive.size() > 0) {
            instantOpList.add(() -> {
                for (var item : itemsToGive) {
                    dropLocation.getWorld().dropItem(dropLocation, item);
                }
            });
        }

        instantOpList.forEach(op -> op.run());
        return true;
    }
}
