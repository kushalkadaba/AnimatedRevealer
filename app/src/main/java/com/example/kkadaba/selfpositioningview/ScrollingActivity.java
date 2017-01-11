package com.example.kkadaba.selfpositioningview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import static com.example.kkadaba.selfpositioningview.AnimatedRevealer.HORIZONTAL;
import static com.example.kkadaba.selfpositioningview.AnimatedRevealer.VERTICAL;

public class ScrollingActivity extends AppCompatActivity {
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout container = (FrameLayout) findViewById(R.id.include_content);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.include_content, BlankFragment.newInstance(null, null),"BlankFragment1")
                .commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.include_content, SecondFragment.newInstance(null, null))
                        .commit();
                view.setTag(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fab.getTag() != null && (boolean)fab.getTag()) {

            init();
            fab.setTag(false);
        } else {
            super.onBackPressed();
        }
    }
}
