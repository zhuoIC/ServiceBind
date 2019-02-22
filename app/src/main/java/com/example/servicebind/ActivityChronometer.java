package com.example.servicebind;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityChronometer extends AppCompatActivity {
    private Button btnStart, btnEnd, btnShowTime;
    private TextView txvTime;
    /**
     * Nos declaramos un objeto de la clase SErviceConnection que será la encargada de gestionar
     * la conexión entre Activity-Service
     */
    private  ServiceChronometer serviceChronometer;
    private boolean isServiceBind = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceChronometer = ((ServiceChronometer.MyBinder) service).getService();
            setServiceBindTrue();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceChronometer = null;
            setServiceBindFalse();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isServiceBind){
                    Intent intent = new Intent(ActivityChronometer.this, ServiceChronometer.class);
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    setServiceBindTrue();
                }

            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceBind){
                    unbindService(serviceConnection);
                    setServiceBindFalse();
                }

            }
        });
        btnShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    txvTime.setText(serviceChronometer.getTime());
            }
        });
    }

    private void initialize() {
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        btnShowTime = findViewById(R.id.btnShowTime);
        txvTime = findViewById(R.id.txvTime);
    }

    private void setServiceBindTrue(){
        isServiceBind = true;
        btnStart.setEnabled(false);
        btnEnd.setEnabled(true);
        btnShowTime.setEnabled(true);
    }

    private void setServiceBindFalse(){
        isServiceBind = false;
        btnStart.setEnabled(true);
        btnEnd.setEnabled(false);
        btnShowTime.setEnabled(false);
    }

    /**
     * Si no se ha iniciado un servicio con StartService y la Activity llama al método BindService
     * inicia el servicio y se vincula.
     * Varios clientes pueden conectarse a un servicio (una vez).
     * Pero el sistema siemplre devolverá la misma instancia del objeto IBinder
     * sin llamar al método onBind() de nuevo
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ServiceChronometer.class);
        startService(intent);
        Log.d("onStart()", "Arranca");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, ServiceChronometer.class);
        stopService(intent);
    }
}
