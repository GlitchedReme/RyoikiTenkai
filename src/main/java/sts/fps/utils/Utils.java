package sts.fps.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sts.fps.renderer.FirstPersonRenderer;

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

    public static Vector3 cameraOnPlane(Vector3 normal, Vector3 position, Vector3 ray) {
        float denom = normal.dot(ray);
        if (Math.abs(denom) < 1e-6) {
            return null;
        }
        float t = normal.dot(position) / denom;
        if (t < 0) {
            return null;
        }

        return ray.scl(t);
    }

    public static String wrapFPSInstrument(String method) {
        String clz = FirstPersonRenderer.class.getName();
        return String.format("{%s.before%s;$_=$proceed($$);%s.after%s;}", clz, method, clz, method);
    }
}
