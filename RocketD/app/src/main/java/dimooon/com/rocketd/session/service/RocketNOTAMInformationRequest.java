package dimooon.com.rocketd.session.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.data.RocketEntity;

/**
 * Created by dimooon on 11.07.16.
 */
public class RocketNOTAMInformationRequest extends RocketRequest {

    private String body;

    public RocketNOTAMInformationRequest(String icao) {

        body =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<REQWX>\n" +
                "<USR>dimooon.naumenko@gmail.com</USR>\n" +
                "<PASSWD>ee13b152e65b89d924d775a98bca300a</PASSWD>\n" +
                "<ICAO>"+icao+"</ICAO>\n" +
                "</REQWX>";
    }

    public RocketEntity execute(){
        try {
            return request();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private RocketEntity request(){

        NOTAMInformation information = null;
        String url="https://apidev.rocketroute.com/notam/v1/";
        String soapAction="urn:xmethods-notam#getNotam";

        HttpURLConnection connection = null;
        URL serverAddress = null;
        InputStream stream = null;
        OutputStreamWriter writer = null;

        String data =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<request>\n" +
                "<![CDATA["+body+"]]>\n" +
                "</request>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";

        try {
            serverAddress = new URL(url);

            connection = null;
            connection = (HttpURLConnection)serverAddress.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty( "Content-Length", Integer.toString( data.length() ) );
            connection.setRequestProperty("SOAPAction", soapAction);

            writer = new OutputStreamWriter(connection.getOutputStream());

            writer.write(data);
            writer.flush();

            stream = getResponseStreamFromEnvelope(connection.getInputStream());

            information = new NOTAMInformation();
            information.parse(stream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        finally
        {
            connection.disconnect();
            try {
                writer.close();
            } catch (IOException e) {}
            try {
                stream.close();
            } catch (IOException e) {}

        }
        return information;
    }

}
