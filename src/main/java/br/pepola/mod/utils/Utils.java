package br.pepola.mod.utils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.Objects;

public class Utils {

    private Utils() {}

    public static boolean isValid(@Nullable Ref<EntityStore> ref) {
        return !Objects.isNull(ref) || ref.isValid();
    }

    public static byte clampByte(int value) {

        if (value < 0) {
            value = 0;
        }

        if (value > 127) {
            value = 127;
        }

        return (byte) value;
    }
}
