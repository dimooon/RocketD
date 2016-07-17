package dimooon.com.rocketd.session;

import dimooon.com.rocketd.session.data.RocketEntity;

/**
 * Created by dimooon on 12.07.16.
 */
public interface SessionRequestListener<T extends RocketEntity>{
    void onSuccess(T response);
    void onSomethingWentWrong(int resourceId);
}