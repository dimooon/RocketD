package dimooon.com.rocketd;

import android.text.TextUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.service.RocketAuthRequest;
import dimooon.com.rocketd.session.service.RocketNOTAMInformationRequest;
import dimooon.com.rocketd.session.service.RocketRequest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dimooon on 12.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RequestTest {

    @Test
    public void testRocketAuthRequest(){

        RocketRequest<Auth> rocketAuthRequest = new RocketAuthRequest();

        Auth response = rocketAuthRequest.execute();
        assertNotNull(response);
        assertFalse(TextUtils.isEmpty(response.getAuthKey()));
    }

    @Test
    public void testGetNOTAMInformation() throws IOException {

        final String body = "EGKA";

        RocketRequest<NOTAMInformation> request = new RocketNOTAMInformationRequest(RuntimeEnvironment.application,body);
        NOTAMInformation information = request.execute();

        assertTrue(information!=null);
        assertNotNull(information.getNotamList());
        assertTrue(information.getNotamList().size()>0);

        assertNotNull(information.getNotamList().get(0).getLat());
        assertNotNull(information.getNotamList().get(0).getLng());
        assertNotNull(information.getNotamList().get(0).getDescription());
    }
}
