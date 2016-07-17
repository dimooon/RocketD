package dimooon.com.rocketd.session.data;

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
    public static final int POSITIVE_SIGN = 1;
    public static final int NEGATIVE_SIGN = -1;
    public static final int NOTAM_LAT_DEGREE_LENGTH = 2;
    public static final int NOTAM_LAT_SIZE = 4;
    public static final int NOTAM_LNG_GEGREE_LENGTH = 3;
    public static final int SEC_IN_MIN = 60;
    public static final String RESULT_DIGIT_PATTERN = ".##";

    private double lat;
    private double lng;

    public ItemQParser(String qItem) {

        String[] segments = qItem.split(File.separator);
        String geo = segments[segments.length-1];

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(geo);

        double[] result = new double[TWO_COORDINATES];
        int group = 0;

        int latSign = POSITIVE_SIGN;
        int lngSign = POSITIVE_SIGN;

        if(geo.contains("W")){
            lngSign = NEGATIVE_SIGN;
        }

        int majorLength = NOTAM_LAT_DEGREE_LENGTH;

        while (m.find()&&group<TWO_COORDINATES) {

            int size = m.group().length();

            if(size > NOTAM_LAT_SIZE){
                majorLength = NOTAM_LNG_GEGREE_LENGTH;
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

        double decimalValue = degrees + minutes/ SEC_IN_MIN;

        String s = new DecimalFormat(RESULT_DIGIT_PATTERN).format(decimalValue);

        return Double.parseDouble(s);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}