package com.switchboard.domain.service;

import java.nio.charset.StandardCharsets;

public final class RolloutHasher {

    private static final int SEED = 0;

    private RolloutHasher() {}

    public static int bucket(String flagKey, String userId) {
        String input = flagKey + ":" + userId;
        int hash = murmurhash3(input.getBytes(StandardCharsets.UTF_8), SEED);

        return Math.abs(hash % 100);
    }

    public static boolean isInRollout(String flagKey, String userId, int percentage) {
        if (percentage <= 0) {
            return false;
        }

        if (percentage >= 100) {
            return true;
        }

        return bucket(flagKey, userId) < percentage;
    }

    static int murmurhash3(byte[] data, int seed) {
        int h = seed;
        int length = data.length;
        int roundedEnd = length & ~3;

        for (int i = 0; i < roundedEnd; i += 4) {
            int k = (data[i] & 0xff)
                | ((data[i + 1] & 0xff) << 8)
                | ((data[i + 2] & 0xff) << 16)
                | ((data[i + 3] & 0xff) << 24);

            k *= 0xcc9e2d51;
            k = Integer.rotateLeft(k, 15);
            k *= 0x1b873593;

            h ^= k;
            h = Integer.rotateLeft(h, 13);
            h = h * 5 + 0xe6546b64;
        }

        int k = 0;
        switch (length & 3) {
            case 3:
                k ^= (data[roundedEnd + 2] & 0xff) << 16;
            case 2:
                k ^= (data[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k ^= (data[roundedEnd] & 0xff);
                k *= 0xcc9e2d51;
                k = Integer.rotateLeft(k, 15);
                k *= 0x1b873593;
                h ^= k;
        }

        h ^= length;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;

        return h;
    }
}
