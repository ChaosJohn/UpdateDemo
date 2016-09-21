package com.chaosjohn.fir_im_update.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chaosjohn.fir_im_update.FirImUpdater;

/**
 * 手动更新界面示例
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/8/28 21:52
 */
public class ManualUpdateActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualupdate);

        initToolbar();
    }

    public void checkUpdate(View view) {
        //手动更新onClick(),启动更新检测
        FirImUpdater.manualStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirImUpdater.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirImUpdater.onStop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        getSupportActionBar().setTitle(getString(R.string.about));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
