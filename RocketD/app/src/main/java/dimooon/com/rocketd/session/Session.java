package dimooon.com.rocketd.session;

import android.util.Log;

import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.service.RocketAuthRequest;
import dimooon.com.rocketd.session.service.RocketNOTAMInformationRequest;

/**
 * Created by dimooon on 11.07.16.
 */
public class Session {

    private static final String TAG = Session.class.getSimpleName();
    private Auth auth;

    public void signIn(){

        final StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\n")
                .append("<AUTH>").append("\n")
                .append("<USR>dimooon.naumenko@gmail.com</USR>").append("\n")
                .append("<PASSWD>ee13b152e65b89d924d775a98bca300a</PASSWD>").append("\n")
                .append("<DEVICEID>e138231a68ad82f054e3d756c6634ba1</DEVICEID>").append("\n")
                .append("<PCATEGORY>RocketRoute</PCATEGORY>").append("\n")
                .append("<APPMD5>cfPKVvTfC9TU2Hvv2qyQ</APPMD5>").append("\n")
                .append("</AUTH>");

        new Thread(new Runnable() {
            @Override
            public void run() {
                auth = new Auth();
                auth.parse(new RocketAuthRequest().request(builder.toString()));
                Log.e(TAG,"toString: "+auth.toString());
            }
        }).start();
    }

    public void getNOTAMInformation(String icao){

    }
}
