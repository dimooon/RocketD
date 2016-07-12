package dimooon.com.rocketd.session.data;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by dimooon on 11.07.16.
 */
public abstract class RocketEntity {

    protected abstract ContentHandler getResponsibleParser();

    public void parse(InputStream is){
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        try {
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(getResponsibleParser());

            xr.parse(new InputSource(is));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public class RocketResponseParser implements ContentHandler {

        @Override
        public void setDocumentLocator(Locator locator) {}

        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {}

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {}

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void processingInstruction(String target, String data) throws SAXException {}

        @Override
        public void skippedEntity(String name) throws SAXException {}
    }

}
