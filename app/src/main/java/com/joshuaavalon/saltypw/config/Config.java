package com.joshuaavalon.saltypw.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.joshuaavalon.saltypw.hash.HashFactory;

public class Config {
    private static final String HASH_TYPE_KEY = "HashType";
    private static final String LENGTH_KEY = "Length";
    private static final String CHARSET_KEY = "Charset";
    @Nullable
    private static Config instance;
    @NonNull
    private final SharedPreferences sharedPreferences;

    private Config(@NonNull final Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Config getInstance(@NonNull final Context context) {
        if (instance == null)
            instance = new Config(context.getApplicationContext());
        return instance;
    }

    public int getHashType() {
        return sharedPreferences.getInt(HASH_TYPE_KEY, HashFactory.getDefaultHashType());
    }

    public void reset() {
        setAll(HashFactory.getDefaultHashType(),
                HashFactory.DEFAULT_LENGTH,
                HashFactory.DEFAULT_CHARSET);
    }

    public int getLength() {
        return sharedPreferences.getInt(LENGTH_KEY, HashFactory.DEFAULT_LENGTH);
    }

    @NonNull
    public String getCharset() {
        return sharedPreferences.getString(CHARSET_KEY, HashFactory.DEFAULT_CHARSET);
    }

    public void setAll(final int hashType,
                       final int length,
                       @NonNull final String charset) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HASH_TYPE_KEY, hashType);
        editor.putInt(LENGTH_KEY, length);
        editor.putString(CHARSET_KEY, charset);
        editor.apply();
    }
}
