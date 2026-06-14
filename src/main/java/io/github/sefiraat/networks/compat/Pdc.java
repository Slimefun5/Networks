package io.github.sefiraat.networks.compat;

import java.util.Optional;

import io.github.thebusybiscuit.slimefun5.libraries.dough.data.persistent.VersionedPdc;

/**
 * Load-safe PDC facade. dough's {@code PersistentDataAPI} class cannot load on MC 1.8 (its 1.14+
 * {@code PersistentDataType} references break class verification). {@code VersionedPdc} is fully
 * reflective (Object/String signatures) and loads on every version, persisting via PDC on 1.14+ and
 * item-NBT below. Mirrors the PersistentDataAPI String-keyed calls used by this addon.
 */
public final class Pdc {

    private Pdc() {}

    public static void setString(Object h, String k, String v) { VersionedPdc.setString(h, k, v); }
    public static String getString(Object h, String k) { return VersionedPdc.getString(h, k); }
    public static String getString(Object h, String k, String def) { String v = VersionedPdc.getString(h, k); return v != null ? v : def; }
    public static Optional<String> getOptionalString(Object h, String k) { return Optional.ofNullable(VersionedPdc.getString(h, k)); }
    public static boolean hasString(Object h, String k) { return VersionedPdc.has(h, k, "STRING"); }
    public static void setInt(Object h, String k, int v) { VersionedPdc.setInt(h, k, v); }
    public static int getInt(Object h, String k) { return VersionedPdc.getInt(h, k, -1); }
    public static int getInt(Object h, String k, int def) { return VersionedPdc.getInt(h, k, def); }
    public static boolean hasInt(Object h, String k) { return VersionedPdc.has(h, k, "INTEGER"); }
    public static void setLong(Object h, String k, long v) { VersionedPdc.setLong(h, k, v); }
    public static long getLong(Object h, String k) { return VersionedPdc.getLong(h, k, -1L); }
    public static long getLong(Object h, String k, long def) { return VersionedPdc.getLong(h, k, def); }
    public static void setDouble(Object h, String k, double v) { VersionedPdc.setDouble(h, k, v); }
    public static double getDouble(Object h, String k) { return VersionedPdc.getDouble(h, k, -1D); }
    public static double getDouble(Object h, String k, double def) { return VersionedPdc.getDouble(h, k, def); }
    public static void setBoolean(Object h, String k, boolean v) { VersionedPdc.setByte(h, k, (byte) (v ? 1 : 0)); }
    public static boolean getBoolean(Object h, String k) { return VersionedPdc.getByte(h, k, (byte) 0) != 0; }
    public static void remove(Object h, String k) { VersionedPdc.remove(h, k); }
}
