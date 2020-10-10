package sample;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.text.DecimalFormat;
import java.util.Collections;


public class ThreadMergefile extends Thread {

    private String file_path;
    private String file_name;
    private ProgressBar progbar;
    private Text text_status;
    private TextField urlbar;
    private Text total_time;

    public ThreadMergefile(String file_path, String file_name, ProgressBar progbar, Text text_status, TextField urlbar, Text total_time) /*Constructor*/{
        this.file_path = file_path;
        this.file_name = file_name;
        this.progbar = progbar;
        this.text_status = text_status;
        this.urlbar = urlbar;
        this.total_time = total_time;
    }

    @Override
    public void run() {
        try{
            while(ThreadProgress.progressP<10){ //check status Thread
                Thread.sleep(10); //sleep while all part not done 0.01 sec/loop
            }
            System.out.println("------------------------------");
            System.out.println("--Merge Progress--");
            int i = 0;
            Collections.sort(listBufferFile.bufferFile); //sort name before merge file
            try(OutputStream out = Files.newOutputStream(Paths.get(file_path+"/"+file_name), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) /*Create new file if does not exists and Open access to write in that file*/ {
                for (String inFileName : listBufferFile.bufferFile) {
                    Files.copy(Paths.get(inFileName), out); //copy data from part file to out.
                    Thread.sleep(500);
                    i++; //plus progress
                    System.out.println("Copy file number " + i + ": " + inFileName); // check What path is pass?
                    new File(inFileName).delete(); //delete file part if file used.

                    progbar.setProgress((double)(i*10)/100); //merge file statusbar
                    text_status.setText((i*10)+"%"); //text statusbar
                }
            }
            Desktop.getDesktop().open(new File(file_path)); //after success open explorer
        } catch (Exception e) {}
        total_time.setText("Total time: "+(new DecimalFormat("#.##").format(((double)(System.currentTimeMillis()-StartTime.start)/1000)/60))+" minutes."); //text total time status
        urlbar.setEditable(true); //after current file downloaded, you can insert new url to download next file.
        listBufferFile.bufferFile.clear(); //clear list for next download
        System.out.println("Download Successfully!");
        System.out.println("------------------------------");
    }
}

