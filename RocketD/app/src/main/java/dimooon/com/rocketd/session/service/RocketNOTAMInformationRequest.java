package dimooon.com.rocketd.session.service;

import java.io.InputStream;

/**
 * Created by dimooon on 11.07.16.
 */
public class RocketNOTAMInformationRequest {

    private static final String TAG = RocketNOTAMInformationRequest.class.getSimpleName();

    public InputStream request(String body){
        return call(body);
    }

    private InputStream call(String body) {

        InputStream stream = null;

        return stream;
    }
}
