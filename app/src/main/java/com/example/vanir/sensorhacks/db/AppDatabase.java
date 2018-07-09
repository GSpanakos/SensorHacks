package com.example.vanir.sensorhacks.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.vanir.sensorhacks.AppExecutors;
import com.example.vanir.sensorhacks.model.Actuator;

import java.util.List;

/**
 * Created by Γιώργος on 16/1/2018.
 */

@Database(entities = {SensorEntity.class, ActuatorEntity.class}, version = 4, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract SensorDAO sensorDAO();

    public abstract ActuatorDAO actuatorDAO();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */

    private static AppDatabase buildDatabase(final Context appContext, final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    // Add a delay to simulate a long-running operation
                    addDelay();
                    // Generate the data for pre-population
                    AppDatabase database = AppDatabase.getInstance(appContext, executors);
                    List<SensorEntity> sensors = DataGenerator.generateSensors();
                    List<ActuatorEntity> actuators = DataGenerator.generateActuators();

                    insertData(database, sensors, actuators);
                    // notify that the database was created and it's ready to be used
                    database.setDatabaseCreated();
                });
            }
        }).addMigrations(MIGRATION_2_3, MIGRATION_3_4).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<SensorEntity> sensors, final List<ActuatorEntity> actuators) {
        database.runInTransaction(() -> database.sensorDAO().insertAll(sensors));
        database.runInTransaction(() -> {
            database.actuatorDAO().insertAll(actuators);
        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'actuators' ('id' INTEGER NOT NULL, 'name' TEXT, 'type' TEXT, 'status' INTEGER, PRIMARY KEY('id'))");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE new_sensors (id INTEGER NOT NULL, name TEXT NOT NULL, type TEXT, status INTEGER, value REAL NOT NULL, PRIMARY KEY(id, name))");
            database.execSQL("INSERT INTO new_sensors (id, name, type, status, value) SELECT id, name, type, status, value FROM sensors");
            database.execSQL("DROP TABLE sensors");
            database.execSQL("ALTER TABLE new_sensors RENAME TO sensors");
        }
    };
}
