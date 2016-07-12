package dimooon.com.rocketd.session.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimooon on 11.07.16.
 */
public class RocketAuthRequest {

    public InputStream request(String body){
        return callSOAPServer(body);
    }

    private InputStream callSOAPServer(String body) {

        InputStream stream = null;
        try {
            HttpPost httppost = new HttpPost("https://mobiledev.rocketroute.com/remote/auth");
            //StringEntity se = new StringEntity(builder.toString(), HTTP.UTF_8);

            List<NameValuePair> dataToPost = new ArrayList<NameValuePair>();

            dataToPost.add(new BasicNameValuePair("req", body));

            httppost.setEntity(new UrlEncodedFormEntity(dataToPost));

            HttpClient httpclient = new DefaultHttpClient();

            BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
            stream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
