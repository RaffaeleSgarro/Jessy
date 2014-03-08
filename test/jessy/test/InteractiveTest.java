package jessy.test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jessy.createworker.CreateWorker;
import org.testng.annotations.Test;

public class InteractiveTest extends TestBase {
    
    @Test
    public void testCreateWorkerForm() throws Exception {
        CreateWorker target = inject(CreateWorker.class);
        putOnStage(target, 300, 600);
    }
    
    private void putOnStage(Parent parent, int width, int height) {
        TestApplication.TestCase.width = width;
        TestApplication.TestCase.height = height;
        TestApplication.TestCase.parent = parent;
        Application.launch(TestApplication.class);
    }
}
