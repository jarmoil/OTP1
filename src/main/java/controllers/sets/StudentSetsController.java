package controllers.sets;

public class StudentSetsController extends BaseSetsController {
    @Override
    protected String getUserRole() {
        return "student";
    }

    @Override
    protected String getViewResource() {
        return "/views/studentSets.fxml";
    }
}