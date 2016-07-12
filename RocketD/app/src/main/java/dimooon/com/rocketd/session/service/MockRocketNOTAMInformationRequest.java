package dimooon.com.rocketd.session.service;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import dimooon.com.rocketd.R;

/**
 * Created by dimooon on 11.07.16.
 */
public class MockRocketNOTAMInformationRequest implements RocketRequest{

    private static final String TAG = MockRocketNOTAMInformationRequest.class.getSimpleName();
    private Context context;

    public MockRocketNOTAMInformationRequest(Context context) {
        this.context = context;
    }

    public InputStream request(String body){
        return call(body);
    }

    private InputStream call(String body) {

        InputStream object = context.getResources().openRawResource(R.raw.notam_response);

        return object;
    }
}
