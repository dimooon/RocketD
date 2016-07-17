package dimooon.com.rocketd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import dimooon.com.rocketd.session.data.ItemQParser;

import static junit.framework.Assert.assertTrue;

/**
 * Created by dimooon on 16.07.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ItemQPareserTest {

    @Test
    public void testItemQParse(){
        ItemQParser parser = new ItemQParser("LFRR/QPDXX/I/NBO /A /000/999/4926N00236W");
        double lat = parser.getLat();
        double lng = parser.getLng();

        assertTrue(lat > 0);
        assertTrue(lng < 0);
        assertTrue(lat == 49.43);
        assertTrue(lng == -2.6);
    }

    @Test
    public void testItemQWithElevationParse(){
        ItemQParser parser = new ItemQParser("LFRR/QPDXX/I/NBO /A /000/999/4926N00236WE500");
        double lat = parser.getLat();
        double lng = parser.getLng();

        assertTrue(lat > 0);
        assertTrue(lng < 0);
        assertTrue(lat == 49.43);
        assertTrue(lng == -2.6);
    }

    @Test
    public void testItemQEmtpyParse(){
        ItemQParser parser = new ItemQParser("/// / ///");
        double lat = parser.getLat();
        double lng = parser.getLng();

        assertTrue(lat == 0);
        assertTrue(lng == 0);
    }

    @Test
    public void testItemQParseProblem(){
        ItemQParser parser = new ItemQParser("UKLV/QICAS/I/NBO /A /000/999/4949N02358E");
        double lat = parser.getLat();
        double lng = parser.getLng();

        System.out.println(""+lat+", "+lng);

        assertTrue(lat > 0);
        assertTrue(lng > 0);
        assertTrue(lat == 49.82);
        assertTrue(lng == 23.97);
    }
}