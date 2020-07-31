package net.draycia.uranium.storage;

public class GameStats {

    private int timesWon;
    private double recordTime;

    public GameStats(int timesWon, double recordTime) {
        this.timesWon = timesWon;
        this.recordTime = recordTime;
    }

    public int getTimesWon() {
        return timesWon;
    }

    public double getRecordTime() {
        return recordTime;
    }

    public void setTimesWon(int timesWon) {
        this.timesWon = timesWon;
    }

    public void setRecordTime(double recordTime) {
        this.recordTime = recordTime;
    }

}
