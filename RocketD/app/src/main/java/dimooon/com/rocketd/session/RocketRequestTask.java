package dimooon.com.rocketd.session;

import android.os.AsyncTask;

import dimooon.com.rocketd.R;
import dimooon.com.rocketd.session.data.RocketEntity;
import dimooon.com.rocketd.session.service.RocketRequest;

/**
 * Created by dimooon on 16.07.16.
 */
class RocketRequestTask<T extends RocketEntity> extends AsyncTask<RocketRequest, Void, T> {

    private SessionRequestListener listener;

    public RocketRequestTask(SessionRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected T doInBackground(RocketRequest... params) {
        RocketRequest<T> request = params[0];

        if (request == null) {
            return null;
        }

        return request.execute();
    }

    @Override
    protected void onPostExecute(T response) {
        super.onPostExecute(response);
        if (response == null) {
            listener.onSomethingWentWrong(R.string.error_message_notam_is_null);
        } else {
            listener.onSuccess(response);
        }
    }
}
