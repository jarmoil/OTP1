package factory;

import dao.*;
import services.*;
import utils.validation.FlashcardValidator;
import utils.validation.Validator;
import utils.validation.FlashcardData;

// Singleton ServiceFactory to provide service instances for controllers to improve dependency management
// Better for testing and looks cleaner
public class ServiceFactory {
    private static ServiceFactory instance;

    private final IFlashcardDao flashcardDao;
    private final IFlashcardSetDao flashcardSetDao;
    private final IUserDao userDao;
    private final IStatisticsDao statisticsDao;

    private final FlashcardService flashcardService;
    private final FlashcardSetService flashcardSetService;
    private final UserService userService;
    private final StatisticsService statisticsService;

    private ServiceFactory() {
        this.flashcardDao = new FlashcardDao();
        this.flashcardSetDao = new FlashcardSetDao();
        this.userDao = new UserDao();
        this.statisticsDao = new StatisticsDao();

        Validator<FlashcardData> validator = new FlashcardValidator();
        this.flashcardService = new FlashcardService(flashcardDao, validator);
        this.flashcardSetService = new FlashcardSetService(flashcardSetDao);
        this.userService = new UserService(userDao);
        this.statisticsService = new StatisticsService(statisticsDao);
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public FlashcardService getFlashcardService() { return flashcardService; }
    public FlashcardSetService getFlashcardSetService() { return flashcardSetService; }
    public UserService getUserService() { return userService; }
    public StatisticsService getStatisticsService() { return statisticsService; }
}
