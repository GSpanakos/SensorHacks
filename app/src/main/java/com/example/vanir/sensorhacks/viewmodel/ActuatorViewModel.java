package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.model.Actuator;

import java.util.List;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorViewModel extends AndroidViewModel {

    private final LiveData<ActuatorEntity> mObservableActuator;
    public ObservableField<ActuatorEntity> actuator = new ObservableField<>();
    public final int mActuatorId;
    public static DataRepository nRepository;
    public static int nActuatorId;
    private static int flag;
    private static final String TAG = "delete_error_from_db";

    public ActuatorViewModel(@NonNull Application application, DataRepository repository,
                             final int actuatorId) {
        super(application);
        mActuatorId = actuatorId;
        mObservableActuator = repository.loadActuator(mActuatorId);

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
     * actually necessary in this case, as the actuator ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        public final int mActuatorId;
        public final DataRepository mRepository;

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
        flag = 0;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<Integer> actuatorIds = nRepository.loadActuatorIds();
                if (actuatorIds.size() != 0) {
                    for (int i = 0; i < actuatorIds.size(); i++) {
                        if (actuatorIds.get(i) == actuatorId) {
                            Log.d(TAG, "run: diagrafh actuator me id: " + actuatorIds.get(i));
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 1) {
                    ActuatorEntity dactuator = nRepository.loadActuatorSync(nActuatorId);
                    task.execute(dactuator);
                } else {
                    Log.d(TAG, "run: actuator is already deleted");
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
}
