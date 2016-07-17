package dimooon.com.rocketd.session.service;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dimooon.com.rocketd.session.data.RocketEntity;

/**
 * Created by dimooon on 12.07.16.
 */
public abstract class RocketRequest<T extends RocketEntity> {

    private static final String RESPONSE_TAG = "<response>";
    private static final String RESPONSE_END_TAG = "</response>";

    public abstract T execute();

    protected InputStream getResponseStreamFromEnvelope(InputStream stream) throws IOException {

        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null)
        {
            sb.append(line + '\n');
        }

        String responseXML = StringEscapeUtils.unescapeXml(sb.toString());

        int responseStartIndex = responseXML.indexOf(RESPONSE_TAG) + RESPONSE_TAG.length();
        int responseEndIndex = responseXML.indexOf(RESPONSE_END_TAG);

        responseXML = responseXML.substring(responseStartIndex, responseEndIndex);

        return new ByteArrayInputStream(responseXML.getBytes());
    }

}
