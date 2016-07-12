package dimooon.com.rocketd;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.InputStream;

import dimooon.com.rocketd.session.Session;
import dimooon.com.rocketd.session.SessionRequestListener;
import dimooon.com.rocketd.session.data.Auth;

public class MainActivity extends AppCompatActivity {

    public static final String SEARCH_QUERY = "searchQuery";
    public static final String SAVED_API_KEY = "SAVED_API_KEY";
    public static final int ICAO_LENGTH = 4;

    private SearchView searchView = null;
    private RocketMapFragment mapFragment = null;
    private String currentICAO = null;
    private String apiKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mapFragment = (RocketMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        restoreTitle(savedInstanceState);
        authenticate();
    }

    private void restoreTitle(Bundle savedInstanceState){
        if(savedInstanceState!=null) {
            if(!TextUtils.isEmpty(this.apiKey)){
                updateToolbarTitle(getString(R.string.signed_in_message));
            }
        }
    }

    private void authenticate(){
        if(TextUtils.isEmpty(apiKey)){

            Session.signIn(new SessionRequestListener() {
                @Override
                public void onSuccess(InputStream result) {

                    Auth auth;
                    auth = new Auth();
                    auth.parse(result);

                    apiKey = auth.getAuthKey();

                    if(TextUtils.isEmpty(apiKey)){
                        return;
                    }
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            updateToolbarTitle(getString(R.string.signed_in_message));
                        }
                    });
                }
            });
        }
    }

    private void updateToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentICAO!=null){
            outState.putString(SEARCH_QUERY, currentICAO);
        }
        if(!TextUtils.isEmpty(this.apiKey)){
            outState.putString(SAVED_API_KEY,this.apiKey);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentICAO = savedInstanceState.getString(SEARCH_QUERY);
        this.apiKey = savedInstanceState.getString(SAVED_API_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if(!TextUtils.isEmpty(currentICAO)){
                searchView.setQuery(currentICAO,false);
                searchView.setIconified(false);
            }
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                currentICAO = newText;
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                boolean enableSearch = TextUtils.isEmpty(apiKey);

                if(enableSearch){
                    Toast.makeText(getApplicationContext(),getString(R.string.not_authorized_error),Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(query.length()!= ICAO_LENGTH){
                    Toast.makeText(getApplicationContext(),getString(R.string.query_error),Toast.LENGTH_SHORT).show();
                    return true;
                }

                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                if(mapFragment!=null){
                    mapFragment.setICAO(query);
                    mapFragment.populate();
                }

                currentICAO = null;

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }
}