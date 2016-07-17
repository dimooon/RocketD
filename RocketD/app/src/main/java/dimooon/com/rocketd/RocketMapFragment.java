package dimooon.com.rocketd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;

import dimooon.com.rocketd.session.Session;
import dimooon.com.rocketd.session.SessionRequestListener;
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.data.Notam;
import dimooon.com.rocketd.session.data.RocketEntity;

/**
 * Created by dimooon on 12.07.16.
 */
public class RocketMapFragment extends MapFragment {

    private static final String ICAO_ID = "icaoId";

    private ArrayList<Notam> notams = new ArrayList<>();
    private GoogleMap map;
    private String icao;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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

        if(TextUtils.isEmpty(icao)) {
            return;
        }

        notams.clear();
        map.clear();

        Session.getInstance().getNOTAMInformation(icao, getContext(), new SessionRequestListener<NOTAMInformation>() {
            @Override
            public void onSuccess(NOTAMInformation result) {

                ArrayList<Notam> newNotams = result.getNotamList();

                Log.e(RocketMapFragment.class.getSimpleName(),""+result);

                if(newNotams == null){
                    return;
                }

                notams.addAll(newNotams);

                LatLng location = null;

                for (Notam notm : notams){

                    location = new LatLng(notm.getLat(), notm.getLng());

                    Log.e(RocketMapFragment.class.getSimpleName(),"coords: "+notm.getLat()+" , "+notm.getLng());

                    MarkerOptions options = new MarkerOptions().position(location)
                            .title(notm.getDescription())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));

                    map.addMarker(options);
                }

                if(location!=null){
                    map.animateCamera(CameraUpdateFactory.newLatLng(location));
                }
            }

            @Override
            public void onSomethingWentWrong(int message) {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
        });
    }
}