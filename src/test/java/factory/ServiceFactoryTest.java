package factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFactoryTest {

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = ServiceFactory.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    void testGetInstanceReturnsSameInstance() {
        ServiceFactory factory1 = ServiceFactory.getInstance();
        ServiceFactory factory2 = ServiceFactory.getInstance();

        assertSame(factory1, factory2);
    }

    @Test
    void testGetFlashcardService() {
        ServiceFactory factory = ServiceFactory.getInstance();
        FlashcardService service = factory.getFlashcardService();

        assertNotNull(service);
        assertSame(service, factory.getFlashcardService());
    }

    @Test
    void testGetFlashcardSetService() {
        ServiceFactory factory = ServiceFactory.getInstance();
        FlashcardSetService service = factory.getFlashcardSetService();

        assertNotNull(service);
        assertSame(service, factory.getFlashcardSetService());
    }

    @Test
    void testGetUserService() {
        ServiceFactory factory = ServiceFactory.getInstance();
        UserService service = factory.getUserService();

        assertNotNull(service);
        assertSame(service, factory.getUserService());
    }

    @Test
    void testGetStatisticsService() {
        ServiceFactory factory = ServiceFactory.getInstance();
        StatisticsService service = factory.getStatisticsService();

        assertNotNull(service);
        assertSame(service, factory.getStatisticsService());
    }
}
