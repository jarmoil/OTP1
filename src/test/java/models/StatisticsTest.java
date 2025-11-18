package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {
    @Test
    void testStatisticsGettersAndSetters() {
        Statistics statistics = new Statistics(1, 100, 5, 85);

        assertEquals(1, statistics.getStatsId());
        assertEquals(100, statistics.getUserId());
        assertEquals(5, statistics.getSetsId());
        assertEquals(85, statistics.getStatsCorrectPercentage());

        statistics.setStatsId(2);
        statistics.setUserId(200);
        statistics.setSetsId(10);
        statistics.setStatsCorrectPercentage(95);

        assertEquals(2, statistics.getStatsId());
        assertEquals(200, statistics.getUserId());
        assertEquals(10, statistics.getSetsId());
        assertEquals(95, statistics.getStatsCorrectPercentage());
    }
}
