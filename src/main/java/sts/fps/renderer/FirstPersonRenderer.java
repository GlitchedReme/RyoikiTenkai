package sts.fps.renderer;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import sts.fps.utils.CameraController;
import sts.fps.utils.Skybox;
import sts.fps.utils.Utils;

import static sts.fps.patch.CharacterPatch.AbstractMonsterFieldPatch.*;

@SuppressWarnings("unused")
public class FirstPersonRenderer {
    public static boolean moveMode = true;

    public static PerspectiveCamera camera;
    public static CameraController controller;

    public static Skybox skybox = new Skybox();

    public static Vector3 targetHpBarPosition = new Vector3();
    public static Vector3 currentHpBarPosition = new Vector3();

    public static Vector3 targetScreenPosition = new Vector3();
    public static Vector3 currentScreenPosition = new Vector3();

    public static Vector3 targetEndTurnPosition = new Vector3();
    public static Vector3 currentEndTurnPosition = new Vector3();

    public static Vector2 targetCameraOffset = new Vector2();
    public static Vector2 currentCameraOffset = new Vector2();

    protected static final Vector3 tmp = new Vector3();
    public static float parallax;

    public static Matrix4 tmpMat = new Matrix4();
    public static Vector2 boundingBoxMax = new Vector2();
    public static Vector2 boundingBoxMin = new Vector2();

    private static final float ROTATE_SPEED = 0.025f;

    public static Rectangle screenRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    static {
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 0f);
        camera.lookAt(0f, 0f, 1f);
        camera.near = 0.1f;
        camera.far = 5000.0f;

        controller = new CameraController(camera);
        InputProcessor prevInput = Gdx.input.getInputProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controller);
        multiplexer.addProcessor(prevInput);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public static Vector3 screenPositionToWorld(float dist, Vector2 offset) {
        Vector3 direction = camera.direction.cpy().nor();
        Vector3 frontPoint = camera.position.cpy().add(direction.cpy().scl(dist));
        Vector3 rightVector = direction.crs(camera.up).nor();
        Vector3 upVector = camera.up.cpy().nor();
        frontPoint.add(rightVector.scl(offset.x)).add(upVector.scl(offset.y));
        return frontPoint;
    }

    public static void calculateBoundingBox(Decal decal) {
        Vector3[] corners = new Vector3[4];
        corners[0] = new Vector3(-decal.getWidth() / 2, -decal.getHeight() / 2, 0);
        corners[1] = new Vector3(-decal.getWidth() / 2, decal.getHeight() / 2, 0);
        corners[2] = new Vector3(decal.getWidth() / 2, -decal.getHeight() / 2, 0);
        corners[3] = new Vector3(decal.getWidth() / 2, decal.getHeight() / 2, 0);

        tmp.set(decal.getScaleX(), decal.getScaleY(), 0f);
        Matrix4 transform = tmpMat.set(decal.getPosition(), decal.getRotation(), tmp);
        for (Vector3 corner : corners) {
            corner.mul(transform);
        }

        boundingBoxMin.set(Float.MAX_VALUE, Float.MAX_VALUE);
        boundingBoxMax.set(Float.MIN_VALUE, Float.MIN_VALUE);

        for (Vector3 corner : corners) {
            Vector3 projected = camera.project(corner);
            if (projected.x < boundingBoxMin.x) boundingBoxMin.x = projected.x;
            if (projected.y < boundingBoxMin.y) boundingBoxMin.y = projected.y;
            if (projected.x > boundingBoxMax.x) boundingBoxMax.x = projected.x;
            if (projected.y > boundingBoxMax.y) boundingBoxMax.y = projected.y;
        }
    }

    public static void update() {

        targetHpBarPosition.set(screenPositionToWorld(100f, new Vector2(-40f, -15f)));
        targetEndTurnPosition.set(screenPositionToWorld(100f, new Vector2(40f, 15f)));
        targetScreenPosition.set(screenPositionToWorld(5f, Vector2.Zero));

        float dt = Gdx.graphics.getDeltaTime() * 30f;

        float offsetX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2f;
        float offsetY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2f;

        parallax = offsetY / Gdx.graphics.getHeight() / 2f;

        targetCameraOffset.set(-offsetX * ROTATE_SPEED, -offsetY * ROTATE_SPEED);

        camera.direction.set(0, 0, 1);
        camera.direction.rotate(camera.up, currentCameraOffset.x);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, currentCameraOffset.y);

        currentHpBarPosition.set(Utils.lerp(currentHpBarPosition, targetHpBarPosition, dt));
        currentScreenPosition.set(Utils.lerp(currentScreenPosition, targetScreenPosition, dt));
        currentCameraOffset.set(Utils.lerp(currentCameraOffset, targetCameraOffset, dt));
    }

    public static void beforeRenderCombatBg(SpriteBatch sb) {
        sb.end();
        DecalRenderer.begin(sb);
    }

    public static void afterRenderCombatBg(SpriteBatch sb) {
        Texture tex = DecalRenderer.getScreenTexture(sb);
        skybox.setTexture(tex);
        skybox.render(camera);
        sb.begin();
    }

    public static void beforeRenderEndTurnButton(SpriteBatch sb) {
        sb.end();
        DecalRenderer.begin(sb);
    }

    public static void afterRenderEndTurnButton(SpriteBatch sb) {
        EndTurnButton btn = AbstractDungeon.overlayMenu.endTurnButton;
        float x = ReflectionHacks.getPrivate(btn, EndTurnButton.class, "current_x");
        float y = ReflectionHacks.getPrivate(btn, EndTurnButton.class, "current_y");

        DecalRenderer.draw(sb,
                "end_turn",
                new Rectangle(
                        x - 136,
                        y - 136,
                        272,
                        272
                ),
                d -> {
                    d.setPosition(currentEndTurnPosition);
                    d.lookAt(camera.position, camera.up);
                }
        );
        sb.begin();
    }

    public static void beforeRenderPlayerHealth(SpriteBatch sb, AbstractPlayer player) {
        sb.end();
        DecalRenderer.begin(sb);
    }

    public static void afterRenderPlayerHealth(SpriteBatch sb, AbstractPlayer player) {
        float offset = ReflectionHacks.getPrivate(player, AbstractCreature.class, "hbYOffset");
        DecalRenderer.draw(sb,
                "player_hp",
                new Rectangle(
                        player.hb.cX - player.hb.width / 2f - 20f * Settings.scale,
                        player.hb.cY - player.hb.height / 2f + offset - 32f * Settings.scale,
                        player.hb.width + 40f * Settings.scale,
                        28.0F * Settings.scale
                ),
                d -> {
                    d.setPosition(currentHpBarPosition);
                    d.setScale(0.15f);
                    d.lookAt(camera.position, camera.up);
                }
        );
        sb.begin();
    }

    static float oriRot;
    static float oriX;
    static float oriY;

    public static void beforeRenderHandCard(SpriteBatch sb, AbstractCard c) {
        sb.end();
        oriRot = c.angle;
        oriX = c.current_x;
        oriY = c.current_y;
        c.current_x = Settings.WIDTH / 2f;
        c.current_y = Settings.HEIGHT / 2f;
        c.angle = 0f;

        DecalRenderer.begin(sb);
    }

    public static void afterRenderHandCard(SpriteBatch sb, AbstractCard c) {
        int index = AbstractDungeon.player.hand.group.indexOf(c);

        float w = (AbstractCard.IMG_WIDTH + 60f) * c.drawScale * Settings.scale;
        float h = (AbstractCard.IMG_HEIGHT + 60f) * c.drawScale * Settings.scale;

        DecalRenderer.draw(sb,
                String.format("card:%s", c.uuid.toString()),
                new Rectangle(
                        c.current_x - w / 2f,
                        c.current_y - h / 2f,
                        w,
                        h
                ),
                d -> {
                    d.setPosition(screenPositionToWorld(300f - index * 0.01f, new Vector2(oriX - Settings.WIDTH / 2f, oriY - Settings.HEIGHT / 2f).scl(0.2f)));
                    d.setScale(0.3f);
                    d.lookAt(camera.position, camera.up);
                    d.rotateZ(oriRot);

                    calculateBoundingBox(d);

                    Vector2 diff = boundingBoxMax.cpy().sub(boundingBoxMin);
                    Vector2 center = boundingBoxMax.cpy().add(boundingBoxMin).scl(0.5f);
                    c.hb.resize(diff.x, diff.y);
                    c.hb.move(center.x, center.y);
                }
        );
        c.current_x = oriX;
        c.current_y = oriY;
        c.angle = oriRot;
        sb.begin();
    }

    public static void updateEnemyHitbox(AbstractMonster m) {
        Vector2 min = hbMin.get(m).cpy();
        Vector2 max = hbMax.get(m).cpy();

        hbMin.get(m).set(m.hb.x, m.hb.y);
        hbMax.get(m).set(m.hb.x + m.hb.width, m.hb.y + m.hb.height);

        m.hb.x = min.x;
        m.hb.y = min.y;
        m.hb.cX = (min.x + max.x) / 2;
        m.hb.cY = (min.y + max.y) / 2;
        m.hb.width = max.x - min.x;
        m.hb.height = max.y - min.y;
    }

    public static void beforeRenderEnemy(SpriteBatch sb, AbstractMonster m) {
        sb.end();
        DecalRenderer.begin(sb);
    }

    public static void afterRenderEnemy(SpriteBatch sb, AbstractMonster m) {
        int index = AbstractDungeon.getMonsters().monsters.indexOf(m);
        String id = uuid.get(m).toString();

        DecalRenderer.draw(sb,
                String.format("enemy:%s", id),
                new Rectangle(
                        m.hb.cX - m.hb.width / 2f - 60f * Settings.scale,
                        m.hb.cY - m.hb.height / 2f - 72f * Settings.scale,
                        m.hb.width + 120f * Settings.scale,
                        m.hb.height + 160f * Settings.scale
                ),
                d -> {
                    d.setPosition(m.drawX - Settings.WIDTH * 0.75F, m.drawY - AbstractDungeon.floorY, 1000f);
                    d.lookAt(camera.position, camera.up);

                    calculateBoundingBox(d);

                    hbMin.get(m).set(boundingBoxMin);
                    hbMax.get(m).set(boundingBoxMax);
                }
        );
        sb.begin();
    }

    public static void beforeRenderScreen(SpriteBatch sb) {
        sb.end();
        DecalRenderer.begin(sb);
    }

    public static void afterRenderScreen(SpriteBatch sb) {
        DecalRenderer.draw(sb,
                "screen",
                screenRect,
                d -> {
                    d.setPosition(currentScreenPosition);
                    d.setScale(1f / 300f);
                    d.lookAt(camera.position, camera.up);
                    d.rotateX(-parallax * 80f - 20f);
                }
        );
        sb.begin();
    }

    public static void render3D(SpriteBatch sb) {
        if (AbstractDungeon.getMonsters() != null) {
            StringBuilder stb = new StringBuilder();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                Vector2 min = hbMin.get(m);
                Vector2 max = hbMax.get(m);

                stb.append(String.format("hb [x:%.1f y:%.1f cx:%.1f, cy:%.1f w:%.1f h:%.1f]\n",
                        m.hb.x, m.hb.y, m.hb.cX, m.hb.cY, m.hb.width, m.hb.height));

                stb.append(String.format("bound [min:%s max:%s]\n", min.toString(), max.toString()));
            }
            stb.append(String.format("mouse [x:%d y:%d]\n", InputHelper.mX, InputHelper.mY));
            FontHelper.renderFont(sb, FontHelper.blockInfoFont, stb.toString(), 50f, Gdx.graphics.getHeight() - 80f, Color.WHITE);
        }

        sb.end();
        controller.update();
        camera.update();
        DecalRenderer.flush();

        sb.begin();
    }
}
