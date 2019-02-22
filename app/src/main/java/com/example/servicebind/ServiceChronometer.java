package com.example.servicebind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import java.util.concurrent.TimeUnit;

public class ServiceChronometer extends Service {

    private Chronometer chronometer;
    private final MyBinder myBinder = new MyBinder();

    /**
     * Método que devuelve el intervalo de tiempo del cronómetro
     * @return
     */
    public String getTime() {
        long millis = SystemClock.elapsedRealtime() - chronometer.getBase();
        String hhmmss = String.format("%02d:%02d:%02d",  TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return hhmmss;
    }

    /**
     * Clase interna que implementa la interfaz y se define un método que devuelva la instancia
     * del servicio
     */
    class MyBinder extends Binder {
        ServiceChronometer getService(){
            return ServiceChronometer.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        chronometer.start();
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("ServiceChronometer", "Desvinculado");
        super.onUnbind(intent);
        chronometer.stop();
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
    }
}
