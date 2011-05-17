package com.roiding.rweibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.roiding.rweibo.provider.DataProvider;

public class AbstractActivity extends Activity {
    DataProvider provider;
    Exception error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        provider = DataProvider.getInstance(this);
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case Constants.DIALOG_ERROR:
                if (isFinishing()) {
                    error = null;
                    return null;
                } else {
                    Dialog dialog = new AlertDialog.Builder(AbstractActivity.this).setMessage(error.getMessage())
                            .create();
                    error = null;
                    return dialog;
                }
        }

        return null;
    }
}
