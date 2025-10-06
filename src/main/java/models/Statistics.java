package models;

public class Statistics {
    private int stats_id;
    private int user_id;
    private int sets_id;
    private int stats_correct_percentage;

    public Statistics(int stats_id, int user_id, int sets_id, int stats_correct_percentage) {
        this.stats_id = stats_id;
        this.user_id = user_id;
        this.sets_id = sets_id;
        this.stats_correct_percentage = stats_correct_percentage;
    }

    // Getters
    public int getStats_id() { return stats_id; }
    public int getUser_id() { return user_id; }
    public int getSets_id() { return sets_id; }
    public int getStats_correct_percentage() { return stats_correct_percentage; }

    // Setters
    public void setStats_id(int stats_id) { this.stats_id = stats_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setSets_id(int sets_id) { this.sets_id = sets_id; }
    public void setStats_correct_percentage(int stats_correct_percentage) { this.stats_correct_percentage = stats_correct_percentage; }

    // This method was used for debugging purposes, can be removed later if not needed
    @Override
    public String toString() {
        return "Statistics{" +
                "stats_id=" + stats_id +
                ", user_id=" + user_id +
                ", sets_id=" + sets_id +
                ", stats_correct_percentage=" + stats_correct_percentage +
                '}';
    }

}
