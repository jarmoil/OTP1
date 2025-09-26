package controllers.sets;

public class TeacherSetsController extends BaseSetsController {
    @Override
    protected String getUserRole() {
        return "teacher";
    }

    @Override
    protected String getViewResource() {
        return "/views/teacherSets.fxml";
    }
}