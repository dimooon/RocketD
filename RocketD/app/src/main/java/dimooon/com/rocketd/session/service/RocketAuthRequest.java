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

import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.data.RocketEntity;

/**
 * Created by dimooon on 11.07.16.
 */
public class RocketAuthRequest extends RocketRequest {

    private static final String BODY_KEY = "req";
    private static final String AUTH_URL = "https://mobiledev.rocketroute.com/remote/auth";

    private final StringBuilder builder = new StringBuilder();

    public RocketAuthRequest() {

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\n")
                .append("<AUTH>").append("\n")
                .append("<USR>dimooon.naumenko@gmail.com</USR>").append("\n")
                .append("<PASSWD>ee13b152e65b89d924d775a98bca300a</PASSWD>").append("\n")
                .append("<DEVICEID>e138231a68ad82f054e3d756c6634ba1</DEVICEID>").append("\n")
                .append("<PCATEGORY>RocketRoute</PCATEGORY>").append("\n")
                .append("<APPMD5>cfPKVvTfC9TU2Hvv2qyQ</APPMD5>").append("\n")
                .append("</AUTH>");

    }

    public RocketEntity execute(){

        InputStream stream = null;
        HttpPost httppost ;
        List<NameValuePair> dataToPost = new ArrayList<NameValuePair>();
        HttpClient httpclient;
        Auth auth = null;
        try {
            httppost = new HttpPost(AUTH_URL);

            dataToPost.add(new BasicNameValuePair(BODY_KEY, builder.toString()));
            httppost.setEntity(new UrlEncodedFormEntity(dataToPost));

            httpclient = new DefaultHttpClient();

            BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
            stream = httpResponse.getEntity().getContent();

            auth = new Auth();
            auth.parse(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;
    }

}