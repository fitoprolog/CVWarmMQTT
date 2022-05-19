package com.test.cvmqtt;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.test.cvmqtt.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements MqttCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    String topic        = "cv";
    int qos             = 2;
    String broker       = "tcp://192.168.68.113:1883";
    String clientId     = "thisisas";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient mqttClient;
    ImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preview =  (ImageView) findViewById(R.id.currentImageView);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            mqttClient.setCallback(this);
            mqttClient.connect(connOpts);
            mqttClient.subscribe("cv");


        }catch (MqttException me) {
            Log.e("MQTT Error",me.toString());
        }

    }
    public void connectionLost(Throwable arg0) {
    }
    //Called when a outgoing publish is complete
    public void deliveryComplete(IMqttDeliveryToken arg0) {
    }
    public void messageArrived(String topic, MqttMessage message){
       byte []payload  = message.getPayload();
       Log.i("MQTTMESSAGE","Size: "+payload.length);
       Bitmap bmp = BitmapFactory.decodeByteArray(payload,0,payload.length);
       if (bmp ==null)
           Log.i("MQTTMESSAGE","Is null");
       else
            Log.i("MQTTMESSAGE","Not null");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preview.setImageBitmap(bmp);
            }
        });
       //preview.setImageBitmap(bmp);
       Log.i("MQTTMESSAGE","Should update");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}