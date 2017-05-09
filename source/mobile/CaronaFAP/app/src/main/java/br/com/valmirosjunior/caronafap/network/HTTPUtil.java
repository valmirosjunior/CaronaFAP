package br.com.valmirosjunior.caronafap.network;

/**
 * Created by junior on 22/04/17.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by Valmir on 16/06/2016.
 */

public class HTTPUtil {
    public static String request(String endereco, String data){
        try {

            URL url = new URL(endereco);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            Scanner scanner = new Scanner(is);
            String conteudo = scanner. useDelimiter("\\A"). next();
            scanner.close();
            return conteudo;
        } catch (Exception e) {
            return null;
        }


    }


    public static String requestPost(String urlRequest, String data){
        HttpURLConnection httpURLConnection;
        try {
            URL url=new URL(urlRequest);
            httpURLConnection=(HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setChunkedStreamingMode(0);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();


            Scanner scanner=new Scanner(httpURLConnection.getInputStream()).useDelimiter("\\Z");
            String result=scanner.next();
            httpURLConnection.disconnect();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "erro na requisiçao";
        }

    }

    public static String doGet(String urlString){
        try {
            URL url=new URL(urlString);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            InputStream inputStream =httpURLConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            String result=scanner.nextLine();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

}
