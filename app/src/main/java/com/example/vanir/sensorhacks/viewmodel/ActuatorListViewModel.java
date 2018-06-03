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
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.model.Actuator;
import com.example.vanir.sensorhacks.ui.frags.ActuatorListFragment;

import java.util.List;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorListViewModel extends AndroidViewModel {
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ActuatorEntity>> mObservableActuators;
    private static final String TAG = "add_actuator_to_db";
    private static int flag;
    private static DataRepository nRepository;

    public ActuatorListViewModel(Application application, DataRepository repository) {
        super(application);

        nRepository = repository;

        mObservableActuators = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableActuators.setValue(null);

        LiveData<List<ActuatorEntity>> actuators = repository
                .getActuators();

        // observe the changes of the actuators from the database and forward them
        mObservableActuators.addSource(actuators, mObservableActuators::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        public final DataRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ActuatorListViewModel(mApplication, mRepository);
        }
    }

    /**
     * Expose the LiveData Actuators query so the UI can observe it.
     */
    public LiveData<List<ActuatorEntity>> getActuators() {
        return mObservableActuators;
    }

    public static void insertActuatorTask(ActuatorEntity actuator, View view) {
        ActuatorListViewModel.InsertActuatorTask task = new ActuatorListViewModel.InsertActuatorTask();
        flag = 0;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Integer> actuatorIds = nRepository.loadActuatorIds();
                if (actuatorIds.size() != 0) {
                    for (int i = 0; i < actuatorIds.size(); i++) {
                        if (actuatorIds.get(i) == actuator.getId()) {
                            Log.d(TAG, "run: Eisai mpoufos vale allo id");
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 0) {
                    task.execute(actuator);
                    startNewFrag(view);
                } else {
                    Snackbar.make(view, "The id is used, try again", Snackbar.LENGTH_LONG).setAction("Id in use", null).show();
                    Log.d(TAG, "run: no actuator added");
                }
            }
        });


    }

    private static class InsertActuatorTask extends AsyncTask<ActuatorEntity, Void, Void> {

        @Override
        protected Void doInBackground(ActuatorEntity... actuatorEntities) {
            nRepository.insert(actuatorEntities[0]);
            return null;
        }


    }

    public static void startNewFrag(View view) {
        Context context = view.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(TAG).replace(R.id.fragment_actuator_container, new ActuatorListFragment(), null).commit();
        }
    }
}
