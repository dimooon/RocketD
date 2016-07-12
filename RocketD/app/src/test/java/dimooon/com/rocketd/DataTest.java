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
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.data.Notam;

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
        assertNotNull(auth);
        assertTrue(auth.getAuthKey()!=null);
        assertFalse("".equals(auth.getAuthKey()));
        assertTrue("239b50adfdc9ded6fc840e136dc4d5ea".equals(auth.getAuthKey()));

    }

    @Test
    public void testNOTAMInformation(){

        InputSource authResponse = new InputSource(RuntimeEnvironment.application.getResources().openRawResource(R.raw.notam_response));

        NOTAMInformation notamInformation = new NOTAMInformation();
        notamInformation.parse(authResponse.getByteStream());
        assertNotNull(notamInformation);
        assertTrue(notamInformation.getNotamList()!=null);
        assertTrue(notamInformation.getNotamList().size() > 0);
        assertTrue(notamInformation.getNotamList().size() == 3);

    }

    @Test
    public void testNOTAM(){
        InputSource authResponse = new InputSource(RuntimeEnvironment.application.getResources().openRawResource(R.raw.notam_response));

        NOTAMInformation notamInformation = new NOTAMInformation();
        notamInformation.parse(authResponse.getByteStream());
        assertNotNull(notamInformation);
        assertTrue(notamInformation.getNotamList()!=null);

        Notam notam = notamInformation.getNotamList().get(0);
        assertNotNull(notam);
        assertTrue(("CHANGE TO RWY 07/25 DESIGNATORS TO RWY 06/24. AMEND ALL " +
                "REFERENCES OF RWY 07 TO RWY 06 AND OF RWY 25 TO RWY 24. AIP " +
                "AD 2.EGKA SECTIONS AD 2.9, 2.10, 2.12, 2.13, 2.20, 2.21 AND " +
                "CHARTS AD 2-EGKA-2-1, 4-1,8-1, 8-2, 8-3, 8-4 AND 8-5 REFER.")
                .equals(notam.getDescription()));

        assertTrue(50.50 == notam.getLat());
        assertTrue(-00.018 == notam.getLng());
    }

}