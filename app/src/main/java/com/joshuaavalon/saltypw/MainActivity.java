package com.joshuaavalon.saltypw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.joshuaavalon.saltypw.config.Config;
import com.joshuaavalon.saltypw.hash.HashFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private TextInputEditText basePasswordEditText;
    private TextInputEditText saltEditText;
    private HashFactory hashFactory;
    private TextView displayTextView;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hashFactory = new HashFactory(this);
        basePasswordEditText = (TextInputEditText) findViewById(R.id.basePasswordEditText);
        basePasswordEditText.addTextChangedListener(this);
        saltEditText = (TextInputEditText) findViewById(R.id.saltEditText);
        saltEditText.addTextChangedListener(this);
        displayTextView = (TextView) findViewById(R.id.displayTextView);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        final FloatingActionButton copyButton = (FloatingActionButton) findViewById(R.id.copyButton);
        copyButton.setOnClickListener(this);
        initialToolBar();
    }

    @Override
    public void onClick(View view) {
        final String password = displayTextView.getText().toString();
        hideKeyboard();
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(coordinatorLayout, R.string.copy_fail_message, Snackbar.LENGTH_LONG).show();
        } else {
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            final ClipData clip = ClipData.newPlainText(getString(R.string.clipboard_password_display), password);
            clipboard.setPrimaryClip(clip);
            Snackbar.make(coordinatorLayout, R.string.copy_message, Snackbar.LENGTH_LONG).show();
        }
    }

    @NonNull
    public String getPassword(@NonNull final String password, @NonNull final String salt) {
        int hashType = Config.getInstance(this).getHashType();
        return hashFactory.getHash(hashType).hash(password, salt);
    }

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
        final String basePassword = basePasswordEditText.getText().toString();
        final String salt = saltEditText.getText().toString();
        if (TextUtils.isEmpty(basePassword) || TextUtils.isEmpty(salt))
            displayTextView.setText("");
        else
            displayTextView.setText(getPassword(basePassword, salt));
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                final Intent intent = new Intent(this, ConfigActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_info:
                showInfoDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void showInfoDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setMessage(R.string.message_info)
                .setTitle(R.string.title_info)
                .setIcon(R.drawable.ic_info_outline_black_24dp)
                .create();
        dialog.show();
        final TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setMovementMethod(LinkMovementMethod.getInstance());
            Linkify.addLinks(messageView, Linkify.ALL);
        }
    }
}
