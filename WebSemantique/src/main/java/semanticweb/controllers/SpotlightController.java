package semanticweb.controllers;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URLEncoder;


/**
 * Created by rim on 08/11/2016.
 */
public class SportlightController {
    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json"); //header format accepted
        conn.connect();

        //formulating request
        byte[] inputBytes = "text= President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance".getBytes("UTF-8");
        OutputStream os = conn.getOutputStream();
        os.write(inputBytes);
        os.close();


        //result of request in json
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();

        //il reste à traiter le json pour récupérer juste la liste d'URI
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(getHTML("http://spotlight.sztaki.hu:2222/rest/annotate"));
    }

}
