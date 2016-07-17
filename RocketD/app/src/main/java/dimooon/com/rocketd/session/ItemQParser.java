package dimooon.com.rocketd.session;

import java.io.File;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dimooon on 17.07.16.
 */
public class ItemQParser {

    private static final int LAT = 0;
    private static final int LNG = 1;
    private static final int TWO_COORDINATES = 2;

    private double lat;
    private double lng;

    public ItemQParser(String qItem) {

        String[] segments = qItem.split(File.separator);
        String geo = segments[segments.length-1];

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(geo);

        double[] result = new double[TWO_COORDINATES];
        int group = 0;

        int latSign = 1;
        int lngSign = 1;

        if(geo.contains("W")){
            lngSign = -1;
        }

        int majorLength = 2;

        while (m.find()&&group<TWO_COORDINATES) {

            int size = m.group().length();

            if(size > 4){
                majorLength = 3;
            }

            String stringValue = m.group();

            double major = (Double.parseDouble(stringValue.substring(0,majorLength)));
            double minor = (Double.parseDouble(stringValue.substring(majorLength,size)));

            result[group++] = getDecimal(major,minor);
        }

        lat = result[LAT]*latSign;
        lng = result[LNG]*lngSign;
    }
    private double getDecimal(double degrees, double minutes){

        double decimalValue = degrees + minutes/60;

        String s = new DecimalFormat(".##").format(decimalValue);

        return Double.parseDouble(s);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}