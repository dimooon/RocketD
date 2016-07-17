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

        session.signIn(RuntimeEnvironment.application,new SessionRequestListener<Auth>() {

            @Override
            public void onSuccess(Auth response) {
                assertNotNull(response);
                assertFalse(TextUtils.isEmpty(response.getAuthKey()));
                assertTrue(session.isSigned(RuntimeEnvironment.application));
            }

            @Override
            public void onSomethingWentWrong(int message) {
                Assert.fail();
            }
        });

    }

    @Test
    public void testSessionGetNOTAMInformation(){

        session.signIn(RuntimeEnvironment.application, new SessionRequestListener() {
            @Override
            public void onSuccess(RocketEntity response) {
                assertTrue(session.isSigned(RuntimeEnvironment.application));
            }

            @Override
            public void onSomethingWentWrong(int resourceId) {
                Assert.fail();
            }
        });

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
            }

            @Override
            public void onSomethingWentWrong(int message) {
                Assert.fail();
            }
        });

    }

    @Test
    public void testSessionDestroy(){
        session.signIn(RuntimeEnvironment.application,new SessionRequestListener() {
            @Override
            public void onSuccess(RocketEntity response) {
                assertTrue(session.isSigned(RuntimeEnvironment.application));

                boolean result =  session.destroy();
                assertTrue(result);

                assertFalse(session.isSigned(RuntimeEnvironment.application));
            }

            @Override
            public void onSomethingWentWrong(int message) {

            }
        });
    }
}