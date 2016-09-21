package com.chaosjohn.fir_im_update.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chaosjohn.fir_im_update.R;
import com.chaosjohn.fir_im_update.config.DownloadKey;
import com.chaosjohn.fir_im_update.module.Download;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/16 19:25
 */
public class UpdateDialog extends Activity {

    private TextView yes, no;
    private TextView tv_changelog;

    private Context context = DownloadKey.FROMACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);

        yes = (TextView) findViewById(R.id.updatedialog_yes);
        no = (TextView) findViewById(R.id.updatedialog_no);
        tv_changelog = (TextView) findViewById(R.id.updatedialog_text_changelog);

        if (getIntent().getBooleanExtra("hideCancel", false)) {
            no.setVisibility(View.GONE);
        }

        String changelog = getString(R.string.update_changelog) + "：\n";
        if (null != DownloadKey.changelogInfo) {
            switch (getString(R.string.lang)) {
                case "en": {
                    changelog += DownloadKey.changelogInfo.en;
                    break;
                }
                case "cn": {
                    changelog += DownloadKey.changelogInfo.cn;
                    break;
                }
            }
        } else {
            changelog += DownloadKey.changeLog;
        }
        tv_changelog.setText(changelog);

        yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(UpdateDialog.this, context.getClass());
                setResult(2, intent);
                DownloadKey.TOShowDownloadView = 2;
                finish();
            }
        });

        no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(UpdateDialog.this, context.getClass());
                setResult(1, intent);
                DownloadKey.TOShowDownloadView = 1;
                if (DownloadKey.ISManual) {
                    DownloadKey.LoadManual = false;
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                /*&& event.getRepeatCount() == 0*/) {
            //do something...
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
