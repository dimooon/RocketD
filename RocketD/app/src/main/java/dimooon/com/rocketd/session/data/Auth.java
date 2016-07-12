package dimooon.com.rocketd.session.data;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * Created by dimooon on 11.07.16.
 */
public class Auth extends RocketEntity{

    private static final String CONTRACT_KEY = "KEY";

    @Override
    protected ContentHandler getResponsibleParser() {
        return new AuthResponseParser();
    }

    private class AuthResponseParser extends RocketResponseParser{
        private String tag;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            this.tag = TextUtils.isEmpty(localName) ? qName : localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String chars = new String(ch, start, length);
            chars = chars.trim();

            if(TextUtils.isEmpty(chars)){
                return;
            }

            valueMap.put(tag,chars);
        }
    }

    public String getAuthKey(){
        return this.valueMap.get(CONTRACT_KEY);
    }
}
