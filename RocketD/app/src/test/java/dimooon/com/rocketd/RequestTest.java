package dimooon.com.rocketd;

import android.text.TextUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;

import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.service.MockRocketNOTAMInformationRequest;
import dimooon.com.rocketd.session.service.RocketAuthRequest;
import dimooon.com.rocketd.session.service.RocketRequest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dimooon on 12.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RequestTest {

    @Test
    public void testMockRocketNOTAMInformationRequest(){

        RocketRequest mockRocketNOTAMInformationRequest =
                new MockRocketNOTAMInformationRequest(RuntimeEnvironment.application);

        InputStream stream =mockRocketNOTAMInformationRequest.request(null);
        assertNotNull(stream);

    }

    @Test
    public void testRocketAuthRequestWithoutBody(){

        RocketRequest  rocketAuthRequest = new RocketAuthRequest();

        InputStream stream = rocketAuthRequest.request(null);

        assertNotNull(stream);
        Auth auth = new Auth();
        auth.parse(stream);

        assertTrue(auth!=null);
        assertTrue(TextUtils.isEmpty(auth.getAuthKey()));
    }

    @Test
    public void testRocketAuthRequestWithBody(){

        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\n")
                .append("<AUTH>").append("\n")
                .append("<USR>dimooon.naumenko@gmail.com</USR>").append("\n")
                .append("<PASSWD>ee13b152e65b89d924d775a98bca300a</PASSWD>").append("\n")
                .append("<DEVICEID>e138231a68ad82f054e3d756c6634ba1</DEVICEID>").append("\n")
                .append("<PCATEGORY>RocketRoute</PCATEGORY>").append("\n")
                .append("<APPMD5>cfPKVvTfC9TU2Hvv2qyQ</APPMD5>").append("\n")
                .append("</AUTH>");

        RocketRequest  rocketAuthRequest = new RocketAuthRequest();

        InputStream stream = rocketAuthRequest.request(builder.toString());
        assertNotNull(stream);
        Auth auth = new Auth();
        auth.parse(stream);

        assertTrue(auth!=null);
        assertFalse(TextUtils.isEmpty(auth.getAuthKey()));
    }

}
