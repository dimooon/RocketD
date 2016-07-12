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
public class RocketAuthRequest implements RocketRequest{

    private static final String BODY_KEY = "req";
    private static final String AUTH_URL = "https://mobiledev.rocketroute.com/remote/auth";

    public InputStream request(String body){

        InputStream stream = null;
        HttpPost httppost ;
        List<NameValuePair> dataToPost = new ArrayList<NameValuePair>();
        HttpClient httpclient;

        try {
            httppost = new HttpPost(AUTH_URL);

            dataToPost.add(new BasicNameValuePair(BODY_KEY, body));
            httppost.setEntity(new UrlEncodedFormEntity(dataToPost));

            httpclient = new DefaultHttpClient();

            BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
            stream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

}