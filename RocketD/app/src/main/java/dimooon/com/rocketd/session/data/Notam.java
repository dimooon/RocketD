package dimooon.com.rocketd.session.data;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Created by dimooon on 12.07.16.
 */
public class Notam extends RocketEntity {

    private static final String TAG = Notam.class.getSimpleName();

    @Override
    protected ContentHandler getResponsibleParser() {
        return new NotamResponseParser();
    }

    private class NotamResponseParser extends RocketResponseParser{
        private String tag;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            this.tag = localName;
            Log.e(TAG,""+localName);
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

    public double getLat(){
        return Double.parseDouble(getLanLgn()[0])/1e2;
    }

    public double getLng(){
        return -Double.parseDouble(getLanLgn()[1])/1e3;
    }

    public String getDescription(){
        return this.valueMap.get("ItemE");
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Notam{");
        sb.append("valueMap=").append(valueMap);
        sb.append('}');
        return sb.toString();
    }

    private String[] getLanLgn(){
        String info = valueMap.get("ItemQ");

        int lastSegment = info.lastIndexOf("/");

        String geo = info.substring(lastSegment+1,info.length()-1);
        String[] lanLgn = geo.split("N");

        return lanLgn;
    }

}
