package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;


/**
 * Created by Γιώργος on 6/6/2018.
 */

public class EditSensorViewModel extends AndroidViewModel {

    private final LiveData<SensorEntity> mObservableSensor;
    public ObservableField<SensorEntity> sensor = new ObservableField<>();
    private static DataRepository nRepository;
    public static final String TAGE = "update_sensor_on_db";

    public EditSensorViewModel(@NonNull Application application, DataRepository repository,
                               final int sensorId) {
        super(application);

        mObservableSensor = repository.loadSensor(sensorId);

        nRepository = repository;
    }

    /**
     * Expose the LiveData Sensor query so the UI can observe it.
     */
    public LiveData<SensorEntity> getObservableSensor() {
        return mObservableSensor;
    }

    public void setSensor(SensorEntity sensor) {
        this.sensor.set(sensor);
    }


    /**
     * A creator is used to inject the sensor ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the sensor ID can be passed in a public method.
     */

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mSensorId;
        private final DataRepository mRepository;


        public Factory(@NonNull Application application, int sensorId) {
            mApplication = application;
            mSensorId = sensorId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new EditSensorViewModel(mApplication, mRepository, mSensorId);
        }
    }

    public static void editSensorTask(SensorEntity sensor, View v) {
        EditSensorTask task = new EditSensorTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                task.execute(sensor);
                startNewFrag(v);
            }
        });
    }

    private static class EditSensorTask extends AsyncTask<SensorEntity, Void, Void> {

        @Override
        protected Void doInBackground(SensorEntity... sensorEntities) {
            nRepository.updateSensor(sensorEntities[0]);
            return null;
        }
    }

    private static void startNewFrag(View v) {
        Context context = v.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SensorListFragment(), null).commit();
        }
    }
}
