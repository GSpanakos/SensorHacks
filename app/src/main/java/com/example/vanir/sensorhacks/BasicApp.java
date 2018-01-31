package com.example.vanir.sensorhacks;

/**
 * Created by Γιώργος on 31/1/2018.
 */

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}

