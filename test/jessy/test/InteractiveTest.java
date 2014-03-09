package jessy.test;

import java.util.Date;
import java.util.GregorianCalendar;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import jessy.attendance.AttendanceSheetController;
import jessy.attendance.SheetSidebar;
import jessy.createworker.CreateWorker;
import jessy.workers.SearchWorker;
import org.testng.annotations.Test;
import jessy.controls.DatePicker;

public class InteractiveTest extends TestBase {
    
    @Test
    public void testCreateWorkerForm() throws Exception {
        CreateWorker target = inject(CreateWorker.class);
        putOnStage(target, 300, 600);
    }
    
    @Test
    public void testSearchWorker() throws Exception {
        fillDatabaseWithTestData();
        SearchWorker target = inject(SearchWorker.class);
        target.showAll();
        putOnStage(target, 600, 600);
    }
    
    @Test
    public void testAttendancheSheet() throws Exception {
        fillDatabaseWithTestData();
        AttendanceSheetController target = inject(AttendanceSheetController.class);
        target.loadDate(new Date());
        target.addRowForWorker("foo", "Jingle Bells");
        putOnStage(target, 600, 600);
    }
    
    @Test
    public void testSheetSidebar() throws Exception {
        fillDatabaseWithTestData();
        SheetSidebar target = inject(SheetSidebar.class);
        target.load();
        putOnStage(target, 200, 600);
    }
    
    @Test
    public void testDatePicker() throws Exception {
        final AnchorPane parent = new AnchorPane();
        DatePicker target = new DatePicker();
        parent.getChildren().add(target);
        target.dateProperty().set(new GregorianCalendar(1988, 10, 18).getTime());
        putOnStage(parent, 300, 300);
    }
    
    private void putOnStage(Parent parent, int width, int height) {
        putOnStage(parent, width, height, null);
    }
    
    private void putOnStage(Parent parent, int width, int height, Runnable runnable) {
        TestApplication.TestCase.width = width;
        TestApplication.TestCase.height = height;
        TestApplication.TestCase.parent = parent;
        TestApplication.TestCase.onLaunch = runnable;
        Application.launch(TestApplication.class);
    }
}
