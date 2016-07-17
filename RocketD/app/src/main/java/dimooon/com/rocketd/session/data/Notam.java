package dimooon.com.rocketd.session.data;

import android.text.TextUtils;

import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;

import java.io.File;

import dimooon.com.rocketd.session.ItemQParser;

/**
 * Created by dimooon on 12.07.16.
 */
public class Notam extends RocketEntity {

    private static final String CONTRACT_GEO_DATA_ITEM_Q = "ItemQ";
    private static final String CONTRACT_DESCRIPTION_ITEM_E = "ItemE";

    @Override
    protected ContentHandler getResponsibleParser() {
        return new RocketResponseParser();
    }

    public double getLat(){
        return new ItemQParser(valueMap.get(CONTRACT_GEO_DATA_ITEM_Q)).getLat();
    }

    public double getLng(){
        return new ItemQParser(valueMap.get(CONTRACT_GEO_DATA_ITEM_Q)).getLng();
    }

    public String getDescription(){
        return this.valueMap.get(CONTRACT_DESCRIPTION_ITEM_E);
    }

}