package dimooon.com.rocketd;

import android.text.TextUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dimooon.com.rocketd.session.Session;
import dimooon.com.rocketd.session.SessionRequestListener;
import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.data.RocketEntity;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by dimooon on 16.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SessionTest {

    private Session session;

    @Before
    public void setUpPreconditions(){
        session = Session.getInstance();
    }

    @After
    public void clean(){
        session.destroy();
        session = null;
    }

    @Test
    public void testSessionInstance(){
        assertNotNull(session);
    }

    @Test
    public void testSessionIsLoggedIn() throws InterruptedException {

        assertFalse(session.getCurrentStatus() == Session.Status.LOGGED_IN);

        session.signIn(new SessionRequestListener<Auth>() {

            @Override
            public void onSuccess(Auth response) {
                assertNotNull(response);
                assertFalse(TextUtils.isEmpty(response.getAuthKey()));
                assertTrue(session.isSigned());
            }

            @Override
            public void onSomethingWentWrong(String message) {
                Assert.fail();
            }
        });

    }

    @Test
    public void testSessionGetNOTAMInformation(){

        session.getNOTAMInformation("EGKA", RuntimeEnvironment.application,
                new SessionRequestListener<NOTAMInformation>() {
            @Override
            public void onSuccess(NOTAMInformation response) {
                assertNotNull(response);
                assertFalse(TextUtils
                        .isEmpty(response.getNotamList().get(0).getDescription()));
                assertFalse(TextUtils
                        .isEmpty(String.valueOf(response.getNotamList().get(0).getLat())));
                assertFalse(TextUtils.
                        isEmpty(String.valueOf(response.getNotamList().get(0).getLng())));

                assertTrue(session.isSigned());
            }

            @Override
            public void onSomethingWentWrong(String message) {
                Assert.fail();
            }
        });

    }

    @Test
    public void testSessionDestroy(){
        session.signIn(new SessionRequestListener() {
            @Override
            public void onSuccess(RocketEntity response) {
                assertTrue(session.isSigned());

                boolean result =  session.destroy();
                assertTrue(result);

                assertFalse(session.isSigned());
            }

            @Override
            public void onSomethingWentWrong(String message) {

            }
        });
    }
}