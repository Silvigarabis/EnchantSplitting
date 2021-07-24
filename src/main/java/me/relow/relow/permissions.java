package me.relow.relow;

public class permissions {
    private String name;
    private int num;
    private double money1;
    private int points1;
    private int level1;

    private double money2;
    private int points2;
    private int level2;

    private int rsuccess;
    private int wsuccess;

    public permissions(String name, int num, double money1, int points1, int level1, double money2, int points2, int level2, int rsuccess, int wsuccess) {
        this.name = name;
        this.num = num;
        this.money1 = money1;
        this.points1 = points1;
        this.level1 = level1;
        this.money2 = money2;
        this.points2 = points2;
        this.level2 = level2;
        this.rsuccess = rsuccess;
        this.wsuccess = wsuccess;
    }

    public permissions() {
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public double getMoney1() {
        return money1;
    }

    public int getPoints1() {
        return points1;
    }

    public int getLevel1() {
        return level1;
    }

    public double getMoney2() {
        return money2;
    }

    public int getPoints2() {
        return points2;
    }

    public int getLevel2() {
        return level2;
    }

    public int getRsuccess() {
        return rsuccess;
    }

    public int getWsuccess() {
        return wsuccess;
    }
}
