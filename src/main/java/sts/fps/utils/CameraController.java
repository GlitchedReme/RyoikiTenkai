package sts.fps.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import sts.fps.renderer.FirstPersonRenderer;

public class CameraController extends InputAdapter {
    private static final float MOVE_SPEED = 500.0f;

    private final IntIntMap keyMap = new IntIntMap();
    private final PerspectiveCamera camera;

    protected final Vector3 tmp = new Vector3();

    public CameraController(PerspectiveCamera camera) {
        this.camera = camera;
    }

    public void update() {
        if (!FirstPersonRenderer.moveMode) return;

        float deltaTime = Gdx.graphics.getDeltaTime();
        Vector3 dir = camera.direction.cpy();
        dir.y = 0f;
        if (keyMap.containsKey(Input.Keys.S)) {
            tmp.set(dir).nor().scl(deltaTime * MOVE_SPEED);
            camera.position.add(tmp);
        }

        if (keyMap.containsKey(Input.Keys.X)) {
            tmp.set(dir).nor().scl(-deltaTime * MOVE_SPEED);
            camera.position.add(tmp);
        }

        if (keyMap.containsKey(Input.Keys.Z)) {
            tmp.set(dir).crs(camera.up).nor().scl(-deltaTime * MOVE_SPEED);
            camera.position.add(tmp);
        }

        if (keyMap.containsKey(Input.Keys.C)) {
            tmp.set(dir).crs(camera.up).nor().scl(deltaTime * MOVE_SPEED);
            camera.position.add(tmp);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keyMap.put(keycode, 1);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyMap.remove(keycode, 0);
        return false;
    }
}
