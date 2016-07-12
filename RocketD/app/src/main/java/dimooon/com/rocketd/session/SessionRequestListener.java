package dimooon.com.rocketd.session;

import java.io.InputStream;

/**
 * Created by dimooon on 12.07.16.
 */
public interface SessionRequestListener {
    void onSuccess(InputStream result);
}
