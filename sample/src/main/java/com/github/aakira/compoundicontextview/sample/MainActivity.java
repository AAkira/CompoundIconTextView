package com.github.aakira.compoundicontextview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.aakira.compoundicontextview.CompoundIconTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CompoundIconTextView tv1 = (CompoundIconTextView) findViewById(R.id.compoundIconTextView1);
        CompoundIconTextView tv2 = (CompoundIconTextView) findViewById(R.id.compoundIconTextView2);

        // set icon
        tv1.setVectorDrawableTop(R.drawable.ic_android_black_24dp);
        tv1.setIconColorResource(R.color.colorPrimary);
        tv1.setIconSizeResource(R.dimen.icon_size, R.dimen.icon_size);

        // clear icon
        tv2.setVectorDrawableRight(CompoundIconTextView.UNDEFINED_RESOURCE);
    }
}