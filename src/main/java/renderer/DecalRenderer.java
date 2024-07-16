package renderer;

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
import com.badlogic.gdx.math.Rectangle;
import utils.ObjectQueue;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DecalRenderer {
    public static ObjectQueue<Decal> decals = new ObjectQueue<>();
    public static ObjectQueue<TextureRegion> regions = new ObjectQueue<>();
    public static ObjectQueue<FrameBuffer> frameBuffers = new ObjectQueue<>();
//    public static HashMap<Integer, DecalBatch> decalBatches = new HashMap<>();
    public static DecalBatch decalBatch;
    public static CameraGroupStrategy strategy;

    public static FrameBuffer currentFrameBuffer;
    public static TextureRegion whiteTex;

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

    public static Texture getScreenTexture(SpriteBatch sb, Runnable drawFunc) {
        FrameBuffer fbo = frameBuffers.get(() -> new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));

        sb.flush();

        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawFunc.run();
        sb.flush();
        fbo.end();

        return fbo.getColorBufferTexture();
    }

    public static void drawManyTimes(SpriteBatch sb, Rectangle rect, Runnable drawFunc, int times, BiConsumer<Decal, Integer> callback) {
        FrameBuffer fbo = frameBuffers.get(() -> new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));

        fbo.begin();

        sb.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawFunc.run();
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

//        sb.begin();
    }

    public static void draw(SpriteBatch sb, float scale, Rectangle rect, Runnable drawFunc, Consumer<Decal> callback) {
        FrameBuffer fbo = frameBuffers.get(() -> new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));
        fbo.begin();
        sb.getProjectionMatrix().scl(scale);
        sb.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
//        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawFunc.run();
        sb.end();
        sb.getProjectionMatrix().scl(1f / scale);
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

        decalBatch.add(decal);
    }

    public static void draw(SpriteBatch sb, Rectangle rect, Runnable drawFunc, Consumer<Decal> callback) {
        FrameBuffer fbo = frameBuffers.get(() -> new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));
        fbo.begin();
        sb.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
//        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawFunc.run();
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

        decalBatch.add(decal);
    }

    public static void flush() {
//        for (DecalBatch db : decalBatches.values()) {
//            db.flush();
//        }
        decalBatch.flush();
    }
}
