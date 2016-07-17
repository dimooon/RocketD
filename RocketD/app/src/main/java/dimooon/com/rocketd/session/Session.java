package dimooon.com.rocketd.session;

import android.content.Context;

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

    public boolean isSigned() {
        return Session.Status.LOGGED_IN == getCurrentStatus();
    }

    public void signIn(final SessionRequestListener listener){
        this.outboundListener = listener;

        if(cachedAuth!=null){
            outboundListener.onSuccess(cachedAuth);
        }else{
            new RocketRequestTask<Auth>(this).execute(new RocketAuthRequest());
        }
    }

    public void getNOTAMInformation(String icao, final Context context,final SessionRequestListener listener){
        this.outboundListener = listener;
        if(cachedNotamInformation!=null&&cachedNotamInformation.getNotamSetName().equalsIgnoreCase(icao)){
            listener.onSuccess(cachedNotamInformation);
        }else{
            new RocketRequestTask<NOTAMInformation>(this).execute(new RocketNOTAMInformationRequest(context,icao));
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
            cachedAuth = (Auth) response;
        }else if(NOTAMInformation.class.getSimpleName().equals(response.getClass().getSimpleName())){
            cachedNotamInformation = (NOTAMInformation) response;
        }
        if(outboundListener!=null){
            status = Status.LOGGED_IN;
            outboundListener.onSuccess(response);
        }
    }

    @Override
    public void onSomethingWentWrong(String message) {
        if (outboundListener!=null){
            outboundListener.onSomethingWentWrong(message);
        }
    }
}