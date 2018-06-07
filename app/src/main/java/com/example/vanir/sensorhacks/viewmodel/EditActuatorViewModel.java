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
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.ui.frags.ActuatorListFragment;

/**
 * Created by Γιώργος on 7/6/2018.
 */

public class EditActuatorViewModel extends AndroidViewModel {

    private static DataRepository nRepository;
    private static int nActuatorId;
    private LiveData<ActuatorEntity> mObservableActuator;
    private ObservableField<ActuatorEntity> actuator = new ObservableField<>();
    public static final String TAGE = "update_actuator_on_db";

    public EditActuatorViewModel(@NonNull Application application, DataRepository repository,
                                 final int actuatorId) {
        super(application);

        mObservableActuator = repository.loadActuator(actuatorId);

        nRepository = repository;
        nActuatorId = actuatorId;
    }

    /**
     * Expose the LiveData Actuator query so the UI can observe it.
     */

    public LiveData<ActuatorEntity> getObservableActuator() {
        return mObservableActuator;
    }

    public void setActuator(ActuatorEntity actuator) {
        this.actuator.set(actuator);
    }

    /**
     * A creator is used to inject the actuator ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the sensor ID can be passed in a public method.
     */

    public static class Factory extends ViewModelProvider.NewInstanceFactory {


        private final Application mApplication;
        private final int mActuatorId;
        private final DataRepository mRepository;

        public Factory(Application application, int actuatorId) {
            mApplication = application;
            mActuatorId = actuatorId;
            mRepository = ((BasicApp) application).getRepository();

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new EditActuatorViewModel(mApplication, mRepository, mActuatorId);
        }
    }

    public static void editActuatorTask(ActuatorEntity actuator, View v) {
        EditActuatorTask task = new EditActuatorTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                task.execute(actuator);
                startNewFrag(v);
            }
        });
    }


    private static class EditActuatorTask extends AsyncTask<ActuatorEntity, Void, Void> {

        @Override
        protected Void doInBackground(ActuatorEntity... actuatorEntities) {
            nRepository.updateActuator(actuatorEntities[0]);
            return null;
        }
    }

    private static void startNewFrag(View v) {
        Context context = v.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(TAGE).replace(R.id.fragment_actuator_container, new ActuatorListFragment(), null).commit();
        }
    }
}
