package dimooon.com.rocketd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import dimooon.com.rocketd.session.Session;
import dimooon.com.rocketd.session.data.Notam;

/**
 * Created by dimooon on 12.07.16.
 */
public class RocketMapFragment extends MapFragment {

    public static final String ICAO_ID = "icaoId";
    private ArrayList<Notam> notams = new ArrayList<>();
    private GoogleMap map;
    private String icao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if(!TextUtils.isEmpty(icao)){
                    populate();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        if (bundle != null) {
            icao = bundle.getString(ICAO_ID,"");
        }

        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if(!TextUtils.isEmpty(icao)){
            bundle.putString(ICAO_ID,icao);
        }
    }

    public void setICAO(String icao){
        this.icao = icao;
    }


    public void populate(){

        Session.signIn();

        this.notams.clear();
        this.notams.addAll(Session.getNOTAMInformation(icao, getContext()));

        LatLng location = null;

        for (Notam notm : notams){

            location = new LatLng(notm.getLat(), notm.getLng());

            MarkerOptions options = new MarkerOptions().position(location)
                    .title(notm.getDescription())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));

            map.addMarker(options);
        }
        if(location!=null){
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }
}