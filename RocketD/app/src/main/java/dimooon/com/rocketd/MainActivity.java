package dimooon.com.rocketd;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });
        Session.signIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("onQueryTextChange", newText);

                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                ArrayList<Notam> notams = Session.getNOTAMInformation(query,getApplicationContext());
                Notam last = null;
                for (Notam notm : notams){
                    LatLng sydney = new LatLng(notm.getLat(), notm.getLng());
                    MarkerOptions options = new MarkerOptions().position(sydney)
                            .title(notm.getDescription())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));

                    map.addMarker(options);
                }
                if(last!=null){
                    map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(last.getLat(),last.getLng())));
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}