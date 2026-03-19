package com.switchboard.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolloutHasherTest {

    @Test
    void bucketIsDeterministic() {
        int first = RolloutHasher.bucket("flag-1", "user-1");
        int second = RolloutHasher.bucket("flag-1", "user-1");

        assertEquals(first, second);
    }

    @Test
    void bucketIsInRange() {
        for (int i = 0; i < 1000; i++) {
            int bucket = RolloutHasher.bucket("flag-" + i, "user-" + i);
            assertTrue(bucket >= 0 && bucket < 100,
                "bucket " + bucket + " out of range");
        }
    }

    @Test
    void differentUsersGetDifferentBuckets() {
        int bucket1 = RolloutHasher.bucket("flag-1", "user-1");
        int bucket2 = RolloutHasher.bucket("flag-1", "user-2");

        // not guaranteed but extremely likely with different inputs
        // this test verifies hashing produces variation, not that all values differ
        assertNotEquals(bucket1, bucket2);
    }

    @Test
    void differentFlagsGetDifferentBuckets() {
        int bucket1 = RolloutHasher.bucket("flag-a", "user-1");
        int bucket2 = RolloutHasher.bucket("flag-b", "user-1");

        assertNotEquals(bucket1, bucket2);
    }

    @Test
    void zeroPercentageNeverInRollout() {
        for (int i = 0; i < 100; i++) {
            assertFalse(RolloutHasher.isInRollout("flag-1", "user-" + i, 0));
        }
    }

    @Test
    void hundredPercentageAlwaysInRollout() {
        for (int i = 0; i < 100; i++) {
            assertTrue(RolloutHasher.isInRollout("flag-1", "user-" + i, 100));
        }
    }

    @Test
    void rolloutIsMonotonic() {
        // if a user is in the 30% rollout, they should also be in the 50% rollout
        for (int i = 0; i < 200; i++) {
            String userId = "user-" + i;
            boolean in30 = RolloutHasher.isInRollout("flag-1", userId, 30);
            boolean in50 = RolloutHasher.isInRollout("flag-1", userId, 50);

            if (in30) {
                assertTrue(in50,
                    userId + " was in 30% rollout but not in 50%");
            }
        }
    }

    @Test
    void distributionIsRoughlyUniform() {
        int count = 10000;
        int inRollout = 0;

        for (int i = 0; i < count; i++) {
            if (RolloutHasher.isInRollout("flag-1", "user-" + i, 50)) {
                inRollout++;
            }
        }

        double ratio = (double) inRollout / count;
        assertTrue(ratio > 0.45 && ratio < 0.55,
            "expected ~50% but got " + (ratio * 100) + "%");
    }
}
