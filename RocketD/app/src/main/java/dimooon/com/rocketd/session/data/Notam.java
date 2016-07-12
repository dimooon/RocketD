package dimooon.com.rocketd.session.data;

import org.xml.sax.ContentHandler;

import java.io.File;

/**
 * Created by dimooon on 12.07.16.
 */
public class Notam extends RocketEntity {

    public static final String GEO_N_MARKER = "N";
    public static final String CONTRACT_GEO_DATA_ITEM_Q = "ItemQ";
    public static final String CONTRACT_DESCRIPTION_ITEM_E = "ItemE";

    public static final int LAT = 0;
    public static final int LNG = 1;

    public static final double REALLY_MAGIC_MULTIPLIER_LAT = 1e2;
    public static final double REALLY_MAGIC_MULTIPLIER_LNG = 1e3;


    @Override
    protected ContentHandler getResponsibleParser() {
        return new RocketResponseParser();
    }

    public double getLat(){
        return Double.parseDouble(getLanLgn()[LAT])/ REALLY_MAGIC_MULTIPLIER_LAT;
    }

    public double getLng(){
        return -Double.parseDouble(getLanLgn()[LNG])/REALLY_MAGIC_MULTIPLIER_LNG;
    }

    public String getDescription(){
        return this.valueMap.get(CONTRACT_DESCRIPTION_ITEM_E);
    }

    private String[] getLanLgn(){
        String info = valueMap.get(CONTRACT_GEO_DATA_ITEM_Q);
        int lastSegment = info.lastIndexOf(File.separator);
        String geo = info.substring(lastSegment+1,info.length()-1);
        String[] lanLgn = geo.split(GEO_N_MARKER);

        return lanLgn;
    }
}