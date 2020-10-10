package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {

    private String file_name;
    private int file_size;
    private CheckDownloader download = new CheckDownloader();
    private String path;
    private String url;
    @FXML
    private Text status_total;
    @FXML
    private ProgressBar progbar;
    @FXML
    private Text size_detail;
    @FXML
    private TextField filenamebar;
    @FXML
    private TextField urlbar;
    @FXML
    private VBox vboxid;
    @FXML
    private TextField dirpath;
    @FXML
    private Button downloadbutton;

    @FXML
    private void closeButtonEvent(ActionEvent event){
        System.exit(0);
    }
    @FXML
    private void dirButtonAction(ActionEvent event){ //Button choose directory
        final DirectoryChooser dirchooser = new DirectoryChooser();

        Stage stage = (Stage) vboxid.getScene().getWindow();

        File file = dirchooser.showDialog(null); //dialog file chooser

        if(file!=null){ //After choose
            dirpath.setText(file.getAbsolutePath());
            this.path = file.getAbsolutePath(); //save current path
        }

        clearIndicator();
    }

    @FXML
    private void urlInsertAction(ActionEvent event) throws IOException {
        clearDataBuffer();
        this.url = urlbar.getText();
        try {
            this.file_name = download.getFilename(url);
            filenamebar.setText(this.file_name);
            if (download.checkUrl(urlbar.getText())) {
                //downloadbutton.setDisable(false);
                this.file_size = download.getFilesize(url);
                if (file_size != -1) {
                    downloadbutton.setDisable(false);
                    size_detail.setText(String.format("%.2f", (double)file_size/1048576) + " MB");
                    System.out.println("File name: " + this.file_name);
                    System.out.println("File size: " + this.file_size);
                    System.out.println("------------------------------");

                } else {
                    downloadbutton.setDisable(true);
                    urlbar.setText(null);
                    filenamebar.setText(null);
                    size_detail.setText("0 MB");
                }
            } else {
                downloadbutton.setDisable(true);
                filenamebar.setText(null);
                size_detail.setText("0 MB");
            }
        } catch (Exception e){}
        clearIndicator();
    }

    @FXML
    private void downloadButtonAction(ActionEvent event) throws Exception {
        if(this.path==null){
            return;
        }
        System.out.println("Directory:" + this.path);
        System.out.println("------------------------------");
        StartTime.start = System.currentTimeMillis();
        ThreadProgress.progressP = 0;
        downloadbutton.setDisable(true);
        urlbar.setEditable(false);
        downloadExecutor(download.getFilesize(this.url));
    }

    private void downloadExecutor(int size){ //Thread
        int part = size/10;
        ExecutorService exe_pool = Executors.newFixedThreadPool(10); // Number of thread
        exe_pool.execute(new Thread_WorkProcess(path,part,url,0,t1_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,1,t2_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,2,t3_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,3,t4_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,4,t5_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,5,t6_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,6,t7_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,7,t8_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,8,t9_pgb));
        exe_pool.execute(new Thread_WorkProcess(path,part,url,9,t10_pgb));
        exe_pool.shutdown();

        Thread merge = new ThreadMergefile(path, file_name, progbar, status_total, urlbar, thread_time);
        merge.start();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.downloadbutton.setDisable(true);
    }
    @FXML
    private Text thread_time;
    @FXML
    private ProgressIndicator t1_pgb;
    @FXML
    private ProgressIndicator t2_pgb;
    @FXML
    private ProgressIndicator t3_pgb;
    @FXML
    private ProgressIndicator t4_pgb;
    @FXML
    private ProgressIndicator t5_pgb;
    @FXML
    private ProgressIndicator t6_pgb;
    @FXML
    private ProgressIndicator t7_pgb;
    @FXML
    private ProgressIndicator t8_pgb;
    @FXML
    private ProgressIndicator t9_pgb;
    @FXML
    private ProgressIndicator t10_pgb;

    private void clearIndicator(){
        this.progbar.setProgress(0);
        this.t1_pgb.setProgress(0);
        this.t2_pgb.setProgress(0);
        this.t3_pgb.setProgress(0);
        this.t4_pgb.setProgress(0);
        this.t5_pgb.setProgress(0);
        this.t6_pgb.setProgress(0);
        this.t7_pgb.setProgress(0);
        this.t8_pgb.setProgress(0);
        this.t9_pgb.setProgress(0);
        this.t10_pgb.setProgress(0);
        this.thread_time.setText("Total time: ");
    }

    private void clearDataBuffer(){
        StartTime.start = 0;
        this.status_total.setText("0%");
        this.file_name = null;
        int file_size = 0;
        this.download = new CheckDownloader();
        this.url = null;
        this.thread_time.setText("Total time: ");
        listBufferFile.bufferFile.clear();
    }

}
