package br.com.felipedeveloper.gestaofinanceira.View;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;

import br.com.felipedeveloper.gestaofinanceira.R;

/**
 * Created by allef on 22/03/2018.
 */

public class myApplication extends Application {
    @Override
    public void onCreate() {
        setTheme(R.style.SplashTheme);
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

}
