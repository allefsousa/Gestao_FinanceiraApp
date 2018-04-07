package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Application;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Created by allef on 22/03/2018.
 */

public class myApplication extends Application {
    @Override
    public void onCreate() {
        setTheme(R.style.SplashTheme);
        super.onCreate();
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
        }

}
