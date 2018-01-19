package com.bzw.common.utils;

import java.util.UUID;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
public final class UuidUtil {
    public static String newUuidString() {
        UUID uuid = UUID.randomUUID();
        return Long.toHexString(uuid.getMostSignificantBits())
                + Long.toHexString(uuid.getLeastSignificantBits());
    }
}
