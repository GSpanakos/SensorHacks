package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vanir.sensorhacks.AppExecutors;
import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;

import java.util.List;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<SensorEntity>> mObservableSensors;
    public static final String TAG = "add_sensor_to_db";
    public static Application mApplication;
    public static int flag = 1;

    public SensorListViewModel(Application application) {
        super(application);
        mApplication = application;

        mObservableSensors = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableSensors.setValue(null);

        LiveData<List<SensorEntity>> sensors = ((BasicApp) application).getRepository()
                .getSensors();

        // observe the changes of the sensors from the database and forward them
        mObservableSensors.addSource(sensors, mObservableSensors::setValue);
    }

    /**
     * Expose the LiveData Sensors query so the UI can observe it.
     */
    public LiveData<List<SensorEntity>> getSensors() {
        return mObservableSensors;
    }

    public static void insertSensorTask(SensorEntity sensor) {
        InsertSensorTask task = new InsertSensorTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<SensorEntity> allTheSensors = ((BasicApp) mApplication).getRepository().loadAllSensorsSync();
                for (int i = 0; i < allTheSensors.size(); i++) {
                    if (allTheSensors.get(i).getId() == sensor.getId()) {
                        new Exception("Eisai mpoufos vale allo id");
                        Log.d(TAG, "run: Eisai mpoufos vale allo id");
                        flag = 1;
                    } else {
                        flag = 0;
                    }
                }
                if (flag == 0) {
                    task.execute(sensor);
                }
            }
        });


    }

    private static class InsertSensorTask extends AsyncTask<SensorEntity, Void, Void> {

        @Override
        protected Void doInBackground(SensorEntity... sensorEntities) {
            ((BasicApp) mApplication).getRepository().insert(sensorEntities[0]);
            return null;
        }
    }
}
