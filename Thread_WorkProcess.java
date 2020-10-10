package sample;

import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class Thread_WorkProcess extends Thread{
    private String directory;
    private int file_part_size;
    private String url;
    private int numberthread;
    private ProgressIndicator progIdct;

    public Thread_WorkProcess(String directory, int part_size, String url, int numberthread , ProgressIndicator progIdct){
        this.directory = directory;
        this.file_part_size = part_size;
        this.url = url;
        this.numberthread = numberthread;
        this.progIdct = progIdct;
        listBufferFile.bufferFile.add(this.directory+"/temp"+this.numberthread);//add where file part directory to static List in Main;
    }

    @Override
    public void run() {
        System.out.println("--Thread Progress--");
        int byteread_current;
        int byteread_before = 0;
        double buffer_progress = 0;
        long current_byte = numberthread*file_part_size; //start from byte
        long end_byte = ((numberthread+1)*file_part_size)-1; //to byte
        String file_path = this.directory+"/temp"+numberthread; //set String file buffer to directory?
        //listBufferFile.bufferFile.add(file_path); //add where file part directory to static List in Main;

        try{
            FileOutputStream op_s = new FileOutputStream(file_path); //file part where output to
            BufferedOutputStream bo_s = new BufferedOutputStream(op_s); //Buffer put in?

            URL url_ = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url_.openConnection();
            urlConnection.setRequestProperty("Range","Bytes="+current_byte+"-"+end_byte); //request range of byte
            urlConnection.connect();
            InputStream in_s = urlConnection.getInputStream(); //get input;
            byte[] byte_Arr = new byte[this.file_part_size]; //create byte array size = part_size

            for(;byteread_before < file_part_size;){
                //.read(byte[], int offset, int length)
                byteread_current = in_s.read(byte_Arr,byteread_before,byte_Arr.length-byteread_before); //read() methods which can read data into a byte array.
                if(byteread_current >= 0){
                    byteread_before += byteread_current;
                    int pgb = byteread_before;
                    double finalValue = Math.round( ((double)pgb/this.file_part_size) * 100.0 ) / 100.0;
                    if(finalValue > buffer_progress){ //just check
                        System.out.println("Thread"+(numberthread+1)+": "+finalValue);
                        buffer_progress = finalValue;
                    }
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                                progIdct.setProgress(finalValue);
                        }
                    });
                }
            }
            bo_s.write(byte_Arr,0 , byteread_before); //write file from byte in array
            bo_s.flush(); //clear buffer
            //close all connect;
            bo_s.close();
            op_s.close();
            in_s.close();

            ThreadProgress.progressP+=1; //Thread complete counter to show progress;
        } catch (Exception e){
            System.out.println("Error");
        }
    }
}
            //reference:
            /*do {
                //.read(byte[], int offset, int length)
                byteread_current = in_s.read(byte_Arr,byteread_before,byte_Arr.length-byteread_before); //read() methods which can read data into a byte array.
                if(byteread_current>=0){ //if byte
                    byteread_before += byteread_current;
                    int pgb = byteread_before;
                    progIdct.setProgress((double)pgb/file_part_size); //thread progress show on indicator
                }
            } while (byteread_before<file_part_size);*/