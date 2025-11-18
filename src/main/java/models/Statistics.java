package models;

public class Statistics {
    private int statsId;
    private int userId;
    private int setsId;
    private int statsCorrectPercentage;

    public Statistics(int statsId, int userId, int setsId, int statsCorrectPercentage) {
        this.statsId = statsId;
        this.userId = userId;
        this.setsId = setsId;
        this.statsCorrectPercentage = statsCorrectPercentage;
    }

    // Getters
    public int getStatsId() { return statsId; }
    public int getUserId() { return userId; }
    public int getSetsId() { return setsId; }
    public int getStatsCorrectPercentage() { return statsCorrectPercentage; }

    // Setters
    public void setStatsId(int statsId) { this.statsId = statsId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSetsId(int setsId) { this.setsId = setsId; }
    public void setStatsCorrectPercentage(int statsCorrectPercentage) { this.statsCorrectPercentage = statsCorrectPercentage; }

    // This method was used for debugging purposes, can be removed later if not needed
    @Override
    public String toString() {
        return "Statistics{" +
                "stats_id=" + statsId +
                ", user_id=" + userId +
                ", sets_id=" + setsId +
                ", stats_correct_percentage=" + statsCorrectPercentage +
                '}';
    }

}
