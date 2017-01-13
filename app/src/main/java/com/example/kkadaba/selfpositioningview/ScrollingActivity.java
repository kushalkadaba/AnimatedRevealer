package com.example.kkadaba.selfpositioningview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
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
    private int prevCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.include_content, BlankFragment.newInstance(null, null),"BlankFragment1")
                .commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.include_content, SecondFragment.newInstance(null, null))
                        .addToBackStack(null)
                        .commit();
                view.setTag(true);
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                prevCount = getSupportFragmentManager().getBackStackEntryCount();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public int getPrevCount() {
        return prevCount;
    }
}
