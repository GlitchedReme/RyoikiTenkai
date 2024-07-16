package renderer;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import patch.DungeonPatch;
import utils.CameraController;
import utils.MathTool;
import utils.Skybox;

import java.util.ArrayList;


public class FirstPersonRenderer {
    public static boolean moveMode = true;

    public static PerspectiveCamera camera;
    public static CameraController controller;

    public static Skybox skybox = new Skybox();

    public static Vector3 targetHpBarPosition = new Vector3();
    public static Vector3 currentHpBarPosition = new Vector3();

    public static Vector3 targetScreenPosition = new Vector3();
    public static Vector3 currentScreenPosition = new Vector3();

    protected static final Vector3 tmp = new Vector3();
    public static float parallax;

    private static final float ROTATE_SPEED = 0.025f;

    public static Rectangle screenRect = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    static {
        camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 0f);
        camera.lookAt(0f, 0f, 1f);
        camera.near = 0.1f;
        camera.far = 5000.0f;

        controller = new CameraController(camera);
        Gdx.input.setInputProcessor(controller);
    }

    public static Vector3 screenPositionToWorld(float dist, Vector2 offset) {
        Vector3 direction = camera.direction.cpy().nor();
        Vector3 frontPoint = camera.position.cpy().add(direction.scl(dist));
        Vector3 rightVector = direction.cpy().crs(camera.up).nor();
        Vector3 upVector = camera.up.cpy().nor();
        frontPoint.add(rightVector.scl(offset.x)).add(upVector.scl(offset.y));
        return frontPoint;
    }

    public static void update() {
        targetHpBarPosition = screenPositionToWorld(100f, new Vector2(20f, -20f));
        targetScreenPosition = screenPositionToWorld(5f, Vector2.Zero);

        float dt = Gdx.graphics.getDeltaTime();
        currentHpBarPosition = MathTool.lerp(currentHpBarPosition, targetHpBarPosition, dt * 20f);
        currentScreenPosition = MathTool.lerp(currentScreenPosition, targetScreenPosition, dt * 20f);

        float offsetX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2f;
        float offsetY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2f;

        parallax = offsetY / Gdx.graphics.getHeight() / 2f;

        camera.direction.set(0, 0, 1);

        float deltaX = -offsetX * ROTATE_SPEED;
        float deltaY = -offsetY * ROTATE_SPEED;
        camera.direction.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);
    }

    public static void renderRoomBg(SpriteBatch sb) {
        switch (AbstractDungeon.rs) {
            case NORMAL:
                Texture tex = DecalRenderer.getScreenTexture(sb, () -> {
                    AbstractDungeon.scene.renderCombatRoomBg(sb);
                });
                skybox.setTexture(tex);
                sb.end();
                skybox.render(camera);
                sb.begin();
                break;
            case CAMPFIRE:
                AbstractDungeon.scene.renderCampfireRoom(sb);
                break;
            case EVENT:
                AbstractDungeon.scene.renderEventRoom(sb);
                break;
        }
    }

    public static void renderRoomFg(SpriteBatch sb) {
//        sb.end();
//        DecalRenderer.drawManyTimes(sb, null, () -> AbstractDungeon.scene.renderCombatRoomFg(sb), 12, (d, i) -> {
//            d.setPosition(0f, 400f, i * 160f);
//        });
//        sb.begin();
    }

    public static void renderPlayerHpBar(AbstractPlayer player, SpriteBatch sb) {
        if (!Settings.hideCombatElements) {
            sb.end();
//            player.hb.width = Settings.WIDTH * 0.5f;
            float offset = ReflectionHacks.getPrivate(player, AbstractCreature.class, "hbYOffset");
            Rectangle rect = new Rectangle(
                    player.hb.cX - player.hb.width / 2f - 20f * Settings.scale,
                    player.hb.cY - player.hb.height / 2f + offset - 32f * Settings.scale,
                    player.hb.width + 40f * Settings.scale,
                    28.0F * Settings.scale
            );
//            Rectangle rect = new Rectangle(200, 200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            DecalRenderer.draw(sb, rect, () -> player.renderHealth(sb), d -> {
                d.setPosition(currentHpBarPosition);
                d.setScale(0.15f);
                d.lookAt(camera.position, camera.up);
            });
            sb.begin();
        }
    }

    public static void renderHand(ArrayList<AbstractCard> queued, ArrayList<AbstractCard> hand, SpriteBatch sb) {
        sb.end();
        for (AbstractCard c : hand) {
            float oriRot = c.angle;
            float oriX = c.current_x;
            float oriY = c.current_y;
            c.current_x = Settings.WIDTH / 2f;
            c.current_y = Settings.HEIGHT / 2f;
            c.angle = 0f;

            float w = (AbstractCard.IMG_WIDTH + 60f) * c.drawScale * Settings.scale;
            float h = (AbstractCard.IMG_HEIGHT + 60f) * c.drawScale * Settings.scale;
            Rectangle rect = new Rectangle(
                    c.current_x - w / 2f,
                    c.current_y - h / 2f,
                    w, h
            );
            DecalRenderer.draw(sb, rect, () -> c.render(sb), d -> {
                d.setPosition(screenPositionToWorld(300f, new Vector2(oriX - Settings.WIDTH / 2f, oriY - Settings.HEIGHT / 2f).scl(0.2f)));
                d.setScale(0.3f);
                d.rotateZ(oriRot);
                d.lookAt(camera.position, camera.up);
            });
            c.angle = oriRot;
            c.current_x = oriX;
            c.current_y = oriY;
        }

        for (AbstractCard c : queued) {
            float oriRot = c.angle;
            float oriX = c.current_x;
            float oriY = c.current_y;
            c.current_x = Settings.WIDTH / 2f;
            c.current_y = Settings.HEIGHT / 2f;
            c.angle = 0f;

            float w = (AbstractCard.IMG_WIDTH + 60f) * c.drawScale * Settings.scale;
            float h = (AbstractCard.IMG_HEIGHT + 60f) * c.drawScale * Settings.scale;
            Rectangle rect = new Rectangle(
                    c.current_x - w / 2f,
                    c.current_y - h / 2f,
                    w, h
            );
            DecalRenderer.draw(sb, rect, () -> c.render(sb), d -> {
                d.setPosition(screenPositionToWorld(300f, new Vector2(oriX - Settings.WIDTH / 2f, oriY - Settings.HEIGHT / 2f).scl(0.2f)));
                d.setScale(0.3f);
                d.rotateZ(oriRot);
                d.lookAt(camera.position, camera.up);
            });
            c.angle = oriRot;
            c.current_x = oriX;
            c.current_y = oriY;
        }
        sb.begin();
    }

    public static void renderEnemies(MonsterGroup group, SpriteBatch sb) {
        sb.end();
        for (AbstractMonster m : group.monsters) {
            Rectangle rect = new Rectangle(
                    m.hb.cX - m.hb.width / 2f - 80f * Settings.scale,
                    m.hb.cY - m.hb.height / 2f - 92f * Settings.scale,
                    m.hb.width + 160f * Settings.scale,
                    m.hb.height + 200f * Settings.scale
            );
            DecalRenderer.draw(sb, rect, () -> m.render(sb), d -> {
                d.setPosition(m.drawX - Settings.WIDTH * 0.75F, m.drawY - AbstractDungeon.floorY, 500f);
                d.lookAt(camera.position, camera.up);
            });
        }
        sb.begin();
    }

    public static void renderScreen(SpriteBatch sb) {
        sb.end();

        DecalRenderer.draw(sb, screenRect, () -> DungeonPatch.foldFuckingSwitch(sb), d -> {
            d.setPosition(currentScreenPosition);
            d.setScale(1f / 300f);
            d.lookAt(camera.position, camera.up);
            d.rotateX(-parallax * 120f + 20f);
        });
        sb.begin();
    }

    public static void render3D(SpriteBatch sb) {
        String msg = String.format(
                "camera:\ndir:%s\npos:%s",
                camera.direction.toString(),
                camera.position.toString()
        );
        FontHelper.renderFont(sb, AbstractDungeon.player.getEnergyNumFont(), msg, 50f, Gdx.graphics.getHeight() - 50f, Color.WHITE);

        sb.end();
        controller.update();
        camera.update();
        DecalRenderer.flush();
        sb.begin();
    }
}
