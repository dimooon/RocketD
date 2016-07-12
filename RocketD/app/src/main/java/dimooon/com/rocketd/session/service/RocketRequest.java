package dimooon.com.rocketd.session.service;

import java.io.InputStream;

/**
 * Created by dimooon on 12.07.16.
 */
public interface RocketRequest {

    InputStream request(String body);

}
