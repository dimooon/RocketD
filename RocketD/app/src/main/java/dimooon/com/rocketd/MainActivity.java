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
import dimooon.com.rocketd.session.data.RocketEntity;

public class MainActivity extends AppCompatActivity {

    public static final String SEARCH_QUERY = "searchQuery";
    public static final int ICAO_LENGTH = 4;

    private SearchView searchView = null;
    private RocketMapFragment mapFragment = null;
    private String currentICAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mapFragment = (RocketMapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        if(savedInstanceState!=null){
            updateToolbarTitle(Session.getInstance().isSigned());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        authenticate();
    }

    private void authenticate(){
        if(!Session.getInstance().isSigned()){

            Session.getInstance().signIn(new SessionRequestListener<Auth>() {

                @Override
                public void onSuccess(Auth result) {
                    updateToolbarTitle(true);
                }

                @Override
                public void onSomethingWentWrong(String message) {

                }
            });
        }
    }

    private void updateToolbarTitle(boolean loggedIn){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(loggedIn ? R.string.signed_in_message : R.string.app_name);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(currentICAO!=null){
            outState.putString(SEARCH_QUERY, currentICAO);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentICAO = savedInstanceState.getString(SEARCH_QUERY);
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

                if(!Session.getInstance().isSigned()){
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