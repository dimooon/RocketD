package dimooon.com.rocketd.session;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dimooon.com.rocketd.R;
import dimooon.com.rocketd.session.data.Auth;
import dimooon.com.rocketd.session.data.NOTAMInformation;
import dimooon.com.rocketd.session.data.RocketEntity;
import dimooon.com.rocketd.session.service.RocketAuthRequest;
import dimooon.com.rocketd.session.service.RocketNOTAMInformationRequest;

/**
 * Created by dimooon on 11.07.16.
 */
public class Session implements SessionRequestListener<RocketEntity>{

    public enum Status {NOT_INITIALIZED,LOGGED_IN,CONNECTION_ISSUE}

    private static Session instance;
    private static Status status = Status.NOT_INITIALIZED;
    private static SessionRequestListener outboundListener;

    private static Auth cachedAuth = null;
    private static NOTAMInformation cachedNotamInformation = null;

    private Session() {
        super();
    }

    public static Session getInstance(){
        return instance = instance == null ? new Session() : instance;
    }

    public Status getCurrentStatus() {
        return status;
    }

    public boolean isSigned(Context context) {
        return isNetworkAvailable(context)&&
                (Session.Status.LOGGED_IN == getCurrentStatus());
    }

    public void signIn(Context context, final SessionRequestListener listener){
        outboundListener = listener;

        if(isAppearedInternetIssue(context, outboundListener)){
            return;
        }

        if(cachedAuth!=null){
            outboundListener.onSuccess(cachedAuth);
        }else{
            new RocketRequestTask<Auth>(this).execute(new RocketAuthRequest());
        }
    }

    public void getNOTAMInformation(String icao, final Context context,final SessionRequestListener listener){
        outboundListener = listener;

        if(isAppearedInternetIssue(context, outboundListener)){
            return;
        }

        if(cachedNotamInformation!=null&&cachedNotamInformation.getNotamSetName().equalsIgnoreCase(icao)){
            listener.onSuccess(cachedNotamInformation);
        }else{
            new RocketRequestTask<NOTAMInformation>(this).execute(new RocketNOTAMInformationRequest(icao));
        }

    }

    public boolean destroy() {
        status = Status.NOT_INITIALIZED;
        outboundListener = null;
        cachedAuth = null;
        cachedNotamInformation = null;
        return true;
    }

    @Override
    public void onSuccess(RocketEntity response) {

        if(Auth.class.getSimpleName().equals(response.getClass().getSimpleName())){
            status = Status.LOGGED_IN;
            cachedAuth = (Auth) response;
        }else if(NOTAMInformation.class.getSimpleName().equals(response.getClass().getSimpleName())){
            cachedNotamInformation = (NOTAMInformation) response;

            if(cachedNotamInformation==null){
                onSomethingWentWrong(R.string.error_message_notam_is_null);
                return;
            }else if(cachedNotamInformation.getNotamList() == null){
                onSomethingWentWrong(R.string.error_message_notam_not_assigned);
                return;
            }else if(cachedNotamInformation.getNotamList().size() == 0){
                onSomethingWentWrong(R.string.error_message_notam_not_assigned);
                return;
            }
        }

        if(outboundListener!=null){
            outboundListener.onSuccess(response);
        }
    }

    @Override
    public void onSomethingWentWrong(int resourceId) {
        if (outboundListener!=null){
            outboundListener.onSomethingWentWrong(resourceId);
        }
    }

    private boolean isAppearedInternetIssue(Context context, SessionRequestListener outboundListener){
        if(!isNetworkAvailable(context)){
            outboundListener.onSomethingWentWrong(R.string.error_message_no_internet);
            destroy();
            return true;
        }
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}