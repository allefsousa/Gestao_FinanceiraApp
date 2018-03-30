package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by allef on 22/03/2018.
 */

public class myApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
        }

}
