package com.example.kkadaba.selfpositioningview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.kkadaba.selfpositioningview.AnimatedRevealer.HORIZONTAL;
import static com.example.kkadaba.selfpositioningview.AnimatedRevealer.VERTICAL;

public class ScrollingActivity extends AppCompatActivity {

    AnimatedRevealer layout;
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

        layout = (AnimatedRevealer) findViewById(R.id.revealView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.start();
                view.setTag(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fab.getTag() != null && (boolean)fab.getTag()) {
            layout.stop();
            init();
            fab.setTag(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null && layout != null) {
            MenuItem item = menu.findItem(R.id.orientation);
            if (item != null) {
                item.setTitle(layout.getOrientation() == HORIZONTAL ? R.string.horizontal : R.string.vertical);
            }
            MenuItem item1 = menu.findItem(R.id.reveal);
            if (item1 != null) {
                item1.setTitle(layout.isReveal()? "Move" : "Reveal");
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.orientation) {
            layout.setOrientation(layout.getOrientation() == HORIZONTAL ? VERTICAL : HORIZONTAL);
            return true;
        } else if (id == R.id.reveal) {
            layout.setReveal(!layout.isReveal());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
