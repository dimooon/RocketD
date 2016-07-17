package dimooon.com.rocketd.session.data;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dimooon on 11.07.16.
 */
public class NOTAMInformation extends RocketEntity{

    private static final String CONTRACT_NOTAM = "NOTAM";
    private static final String CONTRACT_NOTAMSET = "NOTAMSET";
    private ArrayList<Notam> notams = new ArrayList<>();

    @Override
    protected ContentHandler getResponsibleParser() {
        return new NOTAMInformationParser();
    }

    private class NOTAMInformationParser extends RocketResponseParser{
        private String tag;
        private Notam notam;

        Pattern p = Pattern.compile("Item");

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            this.tag = TextUtils.isEmpty(localName) ? qName : localName;

            if(CONTRACT_NOTAM.equals(this.tag)){
                notam = new Notam();
            }
            if(CONTRACT_NOTAMSET.equals(this.tag)){
                valueMap.put(this.tag,atts.getValue(0));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            String tag = TextUtils.isEmpty(localName) ? qName : localName;

            if(CONTRACT_NOTAM.equals(tag)){
                notams.add(notam);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String chars = new String(ch, start, length);
            chars = chars.trim();

            if(TextUtils.isEmpty(chars)){
                return;
            }

            Matcher m = p.matcher(this.tag);

            if (m.find()){
                this.notam.put(this.tag,chars);
                return;
            }

            valueMap.put(tag,chars);
        }
    }

    public ArrayList<Notam> getNotamList(){
        return this.notams;
    }

    public String getNotamSetName(){
        return this.valueMap.get(CONTRACT_NOTAMSET);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(getNotamList());
        return sb.toString();
    }
}
