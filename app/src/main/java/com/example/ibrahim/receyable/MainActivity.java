package com.example.ibrahim.receyable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private ImageView recyaleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Btns
        recyaleBtn = findViewById(R.id.recyleBtn);


        //Event Listeners
        recyaleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // do stuff
                Log.i("HELLOFRIEND", "DONKEY");
            }

        });
    }


}
