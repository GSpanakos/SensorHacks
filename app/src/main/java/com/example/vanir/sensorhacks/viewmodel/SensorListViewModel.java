package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<SensorEntity>> mObservableSensors;
    private static final String TAG = "add_sensor_to_db";
    private static int flagId, flagName;
    private static DataRepository nRepository;

    public SensorListViewModel(Application application, DataRepository repository) {
        super(application);

        nRepository = repository;

        mObservableSensors = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableSensors.setValue(null);

        LiveData<List<SensorEntity>> sensors = repository
                .getSensors();

        // observe the changes of the sensors from the database and forward them
        mObservableSensors.addSource(sensors, mObservableSensors::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final DataRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BasicApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SensorListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData Sensors query so the UI can observe it.
     */
    public LiveData<List<SensorEntity>> getSensors() {
        return mObservableSensors;
    }

    public static void insertSensorTask(SensorEntity sensor, View view) {
        InsertSensorTask task = new InsertSensorTask();
        flagId = 0;
        flagName = 0;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Integer> sensorIds = nRepository.loadSensorIds();
                List<String> sensorNames = nRepository.loadSensorNames();
                if (sensorIds.size() != 0) {
                    for (int i = 0; i < sensorIds.size(); i++) {
                        if (sensorIds.get(i) == sensor.getId()) {
                            Log.d(TAG, "run: Eisai mpoufos vale allo id");
                            flagId = 1;
                            break;
                        }
                    }
                }
                if (sensorNames.size() != 0) {
                    for (int i = 0; i < sensorNames.size(); i++) {
                        if (Objects.equals(sensorNames.get(i), sensor.getName())) {
                            Log.d(TAG, "run: Eisai mpoufos vale allo name");
                            flagName = 1;
                            break;
                        }
                    }
                }
                if (flagId == 0 && flagName == 0) {
                    task.execute(sensor);
                    startNewFrag(view);
                } else if (flagId == 1 && flagName == 1) {
                    Snackbar.make(view, "The id and Name are used, try again", Snackbar.LENGTH_LONG).setAction("Id and Name in use", null).show();
                    Log.d(TAG, "run: no sensor added");
                } else if (flagId == 1) {
                    Snackbar.make(view, "The id is used, try again", Snackbar.LENGTH_LONG).setAction("Id in use", null).show();
                    Log.d(TAG, "run: no sensor added");
                } else {
                    Snackbar.make(view, "The Name is used, try again", Snackbar.LENGTH_LONG).setAction("Name in use", null).show();
                    Log.d(TAG, "run: no sensor added");
                }
            }
        });


    }

    private static class InsertSensorTask extends AsyncTask<SensorEntity, Void, Void> {

        @Override
        protected Void doInBackground(SensorEntity... sensorEntities) {
            nRepository.insert(sensorEntities[0]);
            return null;
        }


    }

    private static void startNewFrag(View view) {
        Context context = view.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(TAG).replace(R.id.fragment_container, new SensorListFragment(), null).commit();
        }
    }

}
