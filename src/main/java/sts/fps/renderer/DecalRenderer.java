package sts.fps.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.*;
import sts.fps.utils.ObjectQueue;
import sts.fps.utils.Utils;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DecalRenderer {
    public static ObjectQueue<Decal> decals = new ObjectQueue<>();
    public static ObjectQueue<TextureRegion> regions = new ObjectQueue<>();
    public static ObjectQueue<FrameBuffer> frameBuffers = new ObjectQueue<>();
    public static HashMap<String, Decal> decalCaches = new HashMap<>();

    public static DecalBatch decalBatch;
    public static CameraGroupStrategy strategy;
    public static TextureRegion whiteTex;

    public static FrameBuffer currentFrameBuffer;
    public static Plane tmpPlane = new Plane();
    public static Vector3 tmp = new Vector3();

    public static final Color COLOR = Color.WHITE.cpy();

    static {
        strategy = new CameraGroupStrategy(FirstPersonRenderer.camera);
        decalBatch = new DecalBatch(strategy);

        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE.cpy());
        pixmap.fill();
        whiteTex = new TextureRegion(new Texture(pixmap));
    }

    public static void reset() {
        decals.reset();
        regions.reset();
        frameBuffers.reset();
    }

    public static void projectInput(String id, Vector2 b) {
        if (!decalCaches.containsKey(id)) return;

        Decal decal = decalCaches.get(id);

        Vector3 position = decal.getPosition();
        Vector3 normal = new Vector3(0, 0, 1).mul(decal.getRotation()).add(position);

        Vector3 a = Utils.cameraOnPlane(normal, position, FirstPersonRenderer.camera.direction);
        if (a == null) {
            return;
        }


    }

    public static void begin(SpriteBatch sb) {
        FrameBuffer fbo = frameBuffers.get(() -> new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));
        fbo.begin();
        sb.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentFrameBuffer = fbo;
    }

    public static Texture getScreenTexture(SpriteBatch sb) {
        FrameBuffer fbo = currentFrameBuffer;
        sb.end();
        fbo.end();
        return fbo.getColorBufferTexture();
    }

    public static void drawManyTimes(SpriteBatch sb, Rectangle rect, int times, BiConsumer<Decal, Integer> callback) {
        FrameBuffer fbo = currentFrameBuffer;
        sb.end();
        fbo.end();

        TextureRegion region = regions.get(TextureRegion::new);
        region.setTexture(fbo.getColorBufferTexture());
        region.setRegion((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        region.flip(false, true);

        for (int i = 0; i < times; i++) {
            Decal decal = decals.get(Decal::new);
            decal.setTextureRegion(region);
            decal.setDimensions(region.getRegionWidth(), region.getRegionHeight());
            decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            decal.setColor(COLOR);
            callback.accept(decal, i);

            decalBatch.add(decal);
        }
    }

    public static void draw(SpriteBatch sb, String id, Rectangle rect, Consumer<Decal> callback) {
        FrameBuffer fbo = currentFrameBuffer;
        sb.end();
        fbo.end();

        TextureRegion region = regions.get(TextureRegion::new);
        region.setTexture(fbo.getColorBufferTexture());
        region.setRegion((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        region.flip(false, true);

        Decal decal = decals.get(Decal::new);
        decal.setTextureRegion(region);
        decal.setDimensions(region.getRegionWidth(), region.getRegionHeight());
        decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        decal.setColor(COLOR);
        callback.accept(decal);
        decalCaches.put(id, decal);

        decalBatch.add(decal);
    }

    public static void flush() {
        decalBatch.flush();
    }
}
