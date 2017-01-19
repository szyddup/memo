package com.xiaoi.app.testjni_two;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.loadLibrary("jniutil");
        TextView test= (TextView)findViewById(R.id.test);
        test.setText(new JniMeasurement().getString());
    }
}
