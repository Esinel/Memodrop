package eu.execom.todolistgrouptwo;


import android.app.Application;

import com.facebook.stetho.Stetho;

import org.androidannotations.annotations.EApplication;

@EApplication
public class TodoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
