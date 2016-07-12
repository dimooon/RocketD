package dimooon.com.rocketd;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import dimooon.com.rocketd.session.Session;
import dimooon.com.rocketd.session.data.Auth;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dimooon on 12.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DataTest {

    @Test
    public void testAuth() throws Exception {

        InputSource authResponse = new InputSource(RuntimeEnvironment.application.getResources().openRawResource(R.raw.auth_response));

        Auth auth = new Auth();
        auth.parse(authResponse.getByteStream());
        System.out.println(""+(DataTest.class.getSimpleName()+"auth:"+auth));
        assertNotNull(auth);
        assertTrue(auth.getAuthKey()!=null);
        assertFalse("".equals(auth.getAuthKey()));

    }

}