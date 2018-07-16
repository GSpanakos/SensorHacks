package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.ObservableField;
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
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;
import com.github.mikephil.charting.data.LineData;

import java.util.List;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorViewModel extends AndroidViewModel {

    private final LiveData<SensorEntity> mObservableSensor;
    public ObservableField<SensorEntity> sensor = new ObservableField<>();
    public static int mSensorId;
    public static String mSensorName;
    private static DataRepository nRepository;
    private static final String TAG = "delete_error_from_db";

    private SensorViewModel(@NonNull Application application, DataRepository repository,
                            final int sensorId, final String sensorName) {
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
        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int sensorId, String sensorName) {
            mApplication = application;
            mSensorId = sensorId;
            mSensorName = sensorName;
            mRepository = ((BasicApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SensorViewModel(mApplication, mRepository, mSensorId, mSensorName);
        }
    }


    public static void deleteSensorTask(int sensorId, View view) {
        DeleteSensorTask task = new DeleteSensorTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SensorEntity dsensor = nRepository.loadSensorSync(sensorId);

                //inspection not really needed unless there is addtobackstack in editsensorviewmodel.startnewfrag
                if (dsensor == null) {
                    Snackbar.make(view, "Already Deleted", Snackbar.LENGTH_LONG).setAction("DelAction", null).show();
                } else {
                    task.execute(dsensor);
                    startNewFrag(view);
                }
            }
        });
    }


    private static class DeleteSensorTask extends AsyncTask<SensorEntity, Void, Void> {

        @Override
        protected Void doInBackground(SensorEntity... sensorEntities) {
            nRepository.delete(sensorEntities[0]);
            return null;
        }
    }

    private static void startNewFrag(View view) {
        Context context = view.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SensorListFragment(), null).commit();
        }

    }
}


