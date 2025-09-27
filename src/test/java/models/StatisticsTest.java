package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {
    @Test
    void testStatisticsGettersAndSetters() {
        Statistics statistics = new Statistics(1, 100, 5, 85);

        assertEquals(1, statistics.getStats_id());
        assertEquals(100, statistics.getUser_id());
        assertEquals(5, statistics.getSets_id());
        assertEquals(85, statistics.getStats_correct_percentage());

        statistics.setStats_id(2);
        statistics.setUser_id(200);
        statistics.setSets_id(10);
        statistics.setStats_correct_percentage(95);

        assertEquals(2, statistics.getStats_id());
        assertEquals(200, statistics.getUser_id());
        assertEquals(10, statistics.getSets_id());
        assertEquals(95, statistics.getStats_correct_percentage());
    }
}
