package com.joshuaavalon.saltypw;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.joshuaavalon.saltypw.config.Config;
import com.joshuaavalon.saltypw.hash.HashFactory;

import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends AppCompatActivity {
    private TextInputEditText hashCharsetEditText;
    private TextInputEditText hashLengthEditText;
    private TextInputLayout hashCharsetLayout;
    private TextInputLayout hashLengthLayout;
    private Spinner hashSpinner;
    private boolean changed;
    private BiMap<String, Integer> hashTypeMap;
    private List<String> spinnerItems;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        config = Config.getInstance(this);
        hashTypeMap = HashBiMap.create();
        hashTypeMap.put("SHA256", HashFactory.SHA256);
        hashTypeMap.put("SHA512", HashFactory.SHA512);
        hashTypeMap.put("MD5", HashFactory.MD5);
        spinnerItems = new ArrayList<>(hashTypeMap.keySet());
        hashSpinner = (Spinner) findViewById(R.id.hashSpinner);
        hashSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems));

        hashCharsetLayout = (TextInputLayout) findViewById(R.id.charsetLayout);
        hashCharsetEditText = (TextInputEditText) findViewById(R.id.charsetEditText);

        hashLengthLayout = (TextInputLayout) findViewById(R.id.hashLengthLayout);
        hashLengthEditText = (TextInputEditText) findViewById(R.id.hashLengthEditText);

        final Button applyButton = (Button)  findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryApplyConfig();
            }
        });

        final Button resetButton = (Button)  findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRestoreDefault();
            }
        });
        initialToolBar();
        initialValue();

        hashCharsetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no-ops
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no-ops
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed = true;
                if (!TextUtils.isEmpty(editable))
                    hashCharsetLayout.setError(null);
                else
                    hashCharsetLayout.setError(getString(R.string.error_charset));
            }
        });
        hashLengthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no-ops
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no-ops
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changed = true;
                if (!TextUtils.isEmpty(editable))
                    hashLengthLayout.setError(null);
                else
                    hashLengthLayout.setError(getString(R.string.error_charset));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                tryBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_setting);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initialValue() {
        final String hashType = hashTypeMap.inverse().get(config.getHashType());
        hashSpinner.setSelection(spinnerItems.indexOf(hashType));
        hashCharsetEditText.setText(config.getCharset());
        hashLengthEditText.setText(String.valueOf(config.getLength()));
    }

    private void tryRestoreDefault() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_restore)
                .setTitle(R.string.title_restore)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        restoreDefault();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void restoreDefault() {
        changed = false;
        config.reset();
    }

    private void tryApplyConfig() {
        if (TextUtils.isEmpty(hashLengthEditText.getText()) ||
                TextUtils.isEmpty(hashCharsetEditText.getText())) return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_apply)
                .setTitle(R.string.title_apply)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        applyConfig();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void applyConfig() {
        changed = false;
        final int hashType = hashTypeMap.get(hashSpinner.getSelectedItem().toString());
        final int length = Integer.valueOf(hashLengthEditText.getText().toString());
        final String charset = hashCharsetEditText.getText().toString();
        config.setAll(hashType, length, charset);
    }

    private void tryBack() {
        if (!changed)
            finish();
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.message_leave)
                    .setTitle(R.string.title_leave)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show();
        }
    }
}
