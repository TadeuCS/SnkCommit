package com.tcs.main;

import com.tcs.util.ScreenUtils;
import com.tcs.util.SessionUtils;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SessionUtils.stage = stage;
        SessionUtils.screenUtils = new ScreenUtils();
        SessionUtils.screenUtils.toHome();
    }
}
