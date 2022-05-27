package com.maueski.beberagua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.PrimitiveIterator;

public class MainActivity extends AppCompatActivity {

    //VARIAVEIS
    private Button btnNotify;
    private EditText editMinutes;
    private TimePicker timePicker;

    private int hour;
    private int minute;
    private int interval;

    private boolean voltarTextoBotao = false;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btn_notify);
        editMinutes = findViewById(R.id.edit_txt_number_interval);
        timePicker = findViewById(R.id.time_picker);

        timePicker.setIs24HourView(true);
        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        voltarTextoBotao = preferences.getBoolean("voltarTextoBotao", false);

        if (voltarTextoBotao) {
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            btnNotify.setBackgroundColor(color);

            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

            editMinutes.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

    }

    public void notifyClick(View view) {
        String sInterval = editMinutes.getText().toString();

        //CONDICAO PARA USUARIO, SE NÃO ESCREVER NADA NO TEXTBOX IRÁ APARECER MENSAGEM DE ERRO.
        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        interval = Integer.parseInt(sInterval);

        //AO CLICAR NO BOTÃO IRÁ MUDAR A COR PARA VERMELHO E ALTERAR O TEXTO PARA 'PAUSAR'
        if (!voltarTextoBotao) {
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            btnNotify.setBackgroundColor(color);
            voltarTextoBotao = true;

        //AQUI SERÁ ARMAZENADO NO DB (banco de dados) INTERNO TODOS OS DADOS
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("voltarTextoBotao", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();

        } else {
            btnNotify.setText(R.string.notify);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            btnNotify.setBackgroundColor(color);
            voltarTextoBotao = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("voltarTextoBotao", false);
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }

        Log.d("Teste", "Hora:" + hour + "Minuto:" + minute + "Intervalo:" + interval);

    }

}