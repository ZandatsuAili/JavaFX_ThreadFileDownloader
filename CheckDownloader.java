package sample;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;

public class CheckDownloader {

    public boolean checkUrl(String input_url) throws IOException {
        URLConnection url_conn = null;
        if(input_url==null||input_url==""){
            return false;
        }
        try /*check url can connect?*/{
            URL url = new URL(input_url);
            url_conn = url.openConnection();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if(url_conn!=null)
                url_conn.getInputStream().close(); //close inputStream
        }
    }

    public String getFilename(String input_url){
        try{
            if(Paths.get(new URI(input_url).getPath()).getFileName().toString().equals(input_url))
                return "";
            else
                return Paths.get(new URI(input_url).getPath()).getFileName().toString();
        }
        catch (Exception e){
            return "";
        }
    }

    public int getFilesize(String input_url) throws IOException {
        URLConnection url_conn = null;
        try{
            URL url = new URL(input_url);
            url_conn = url.openConnection();
            return url_conn.getContentLength();
        } catch (Exception e) {
            return -1;
        } finally {
            if(url_conn!=null)
                url_conn.getInputStream().close(); //close inputStream
        }
    }
}
