package com.joshuaavalon.saltypw.hash;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.joshuaavalon.saltypw.config.Config;

import java.math.BigInteger;

public class HashFactory {
    public static final int SHA256 = 0;
    public static final int SHA512 = 1;
    public static final int MD5 = 2;
    public static final String DEFAULT_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_=!@#$%^&*()[]{}|;:,.<>/?`~ \\'\"+-";
    public static final int DEFAULT_LENGTH = 16;
    @NonNull
    private final Context context;

    public HashFactory(@NonNull final Context context) {
        this.context = context;
    }

    public static int getDefaultHashType() {
        return SHA256;
    }

    public IHash getHash(final int hashType) {
        HashFunction hashFunction;
        switch (hashType) {
            case MD5:
                hashFunction = Hashing.md5();
                break;
            case SHA256:
                hashFunction = Hashing.sha256();
                break;
            case SHA512:
                hashFunction = Hashing.sha512();
                break;
            default:
                hashFunction = Hashing.sha256();
        }
        return new Hash(hashFunction, getCharset(), getLength());
    }

    @NonNull
    private String getCharset() {
        return Config.getInstance(context).getCharset();
    }

    private int getLength() {
        return Config.getInstance(context).getLength();
    }

    private static class Hash implements IHash {
        @NonNull
        private final HashFunction hashFunction;
        @NonNull
        private final String charset;
        private final int length;

        public Hash(@NonNull final HashFunction hashFunction,
                    @NonNull final String charset,
                    final int length) {
            this.hashFunction = hashFunction;
            this.charset = charset;
            this.length = length;
        }

        @NonNull
        @Override
        public String hash(@NonNull final String password, @NonNull final String salt) {
            final String hash = hashFunction.newHasher()
                    .putString(password, Charsets.UTF_8)
                    .putString(salt, Charsets.UTF_8)
                    .hash().toString().substring(0, length);
            return baseConvert(new BigInteger(hash, 16));
        }

        private String baseConvert(@NonNull final BigInteger number) {
            BigInteger quotient;
            BigInteger remainder;
            BigInteger current = number;
            final StringBuilder result = new StringBuilder();
            final BigInteger base = BigInteger.valueOf(charset.length());
            do {
                remainder = current.remainder(base);
                quotient = current.divide(base);
                result.append(charset.charAt(remainder.intValue()));
                current = current.divide(base);
            } while (!BigInteger.ZERO.equals(quotient));
            return result.reverse().toString();
        }
    }
}
