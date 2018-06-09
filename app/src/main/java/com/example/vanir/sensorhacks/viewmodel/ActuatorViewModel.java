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
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.model.Actuator;
import com.example.vanir.sensorhacks.ui.frags.ActuatorListFragment;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;

import java.util.List;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorViewModel extends AndroidViewModel {

    private final LiveData<ActuatorEntity> mObservableActuator;
    public ObservableField<ActuatorEntity> actuator = new ObservableField<>();
    public static DataRepository nRepository;
    private static final String TAG = "delete_error_from_db";

    public ActuatorViewModel(@NonNull Application application, DataRepository repository,
                             final int actuatorId) {
        super(application);
        mObservableActuator = repository.loadActuator(actuatorId);
        nRepository = repository;

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
     * actually necessary in this case, as the actuator ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private int mActuatorId;
        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int actuatorId) {
            mApplication = application;
            mActuatorId = actuatorId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ActuatorViewModel(mApplication, mRepository, mActuatorId);
        }
    }

    public static void deleteActuatorTask(int actuatorId, View view) {
        ActuatorViewModel.DeleteActuatorTask task = new ActuatorViewModel.DeleteActuatorTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ActuatorEntity dactuator = nRepository.loadActuatorSync(actuatorId);
                //inspection not really needed unless there is addtobackstack in editactuatorviewmodel.startnewfrag
                if (dactuator == null) {
                    Snackbar.make(view, "Already Deleted", Snackbar.LENGTH_LONG).setAction("DelAction", null).show();
                } else {
                    task.execute(dactuator);
                    startNewFrag(view);
                }
            }
        });
    }

    private static class DeleteActuatorTask extends AsyncTask<ActuatorEntity, Void, Void> {

        @Override
        protected Void doInBackground(ActuatorEntity... actuatorEntities) {
            nRepository.delete(actuatorEntities[0]);
            return null;
        }
    }

    private static void startNewFrag(View view) {
        Context context = view.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actuator_container, new ActuatorListFragment(), null).commit();
        }

    }
}
