package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application { //Link demo download: http://www.mediafire.com/file/e08fzofg3wh1we5/%255BHell-FS_v2%255D_Nakanohito_Genome_%255BJikkyouchuu%255D_-_04_%255B1080p%255D.mp4/file

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXform.fxml"));
        primaryStage.setTitle("ThreadFileDownloader");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("dl.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
class StartTime{
    public static long start;
}
class ThreadProgress{
    public static int progressP = 0; //Total number of thread(working done/not)
}

class listBufferFile{
    public static List<String> bufferFile = new ArrayList<>(); //list file part directory
}
