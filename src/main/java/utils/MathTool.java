package utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class MathTool {
    public static Vector3 lerp(Vector3 a, Vector3 b, float w) {
        return new Vector3(
                MathUtils.lerp(a.x, b.x, w),
                MathUtils.lerp(a.y, b.y, w),
                MathUtils.lerp(a.z, b.z, w)
        );
    }
}
