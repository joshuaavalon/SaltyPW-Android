package com.joshuaavalon.saltypw.hash;

import android.support.annotation.NonNull;

public interface IHash {
    @NonNull
    String hash(@NonNull String password, @NonNull String salt);
}
