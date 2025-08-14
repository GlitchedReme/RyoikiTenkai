package sts.fps.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Utils {
    public static Vector2 lerp(Vector2 a, Vector2 b, float w) {
        return new Vector2(
                MathUtils.lerp(a.x, b.x, w),
                MathUtils.lerp(a.y, b.y, w)
        );
    }

    public static Vector3 lerp(Vector3 a, Vector3 b, float w) {
        return new Vector3(
                MathUtils.lerp(a.x, b.x, w),
                MathUtils.lerp(a.y, b.y, w),
                MathUtils.lerp(a.z, b.z, w)
        );
    }
}
