package sts.ryoikitenkai.vfx.red;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FireFlyEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float aX;
    private float waveDst;
    private float baseAlpha;
    private float trailTimer = 0.0F;
    private TextureAtlas.AtlasRegion img;
    private Color setColor;
    private ArrayList<Vector2> prevPositions = new ArrayList<>();

    public FireFlyEffect(Color setColor, float x, float y) {
        this.renderBehind = MathUtils.randomBoolean();
        this.startingDuration = MathUtils.random(6.0F, 14.0F);
        this.duration = this.startingDuration;
        this.setColor = setColor;
        this.img = ImageMaster.STRIKE_BLUR;
        this.x = x + MathUtils.random(-200.0F, 200.0F) - (float) this.img.packedWidth / 2.0F;
        this.y = y + MathUtils.random(-200.0F, 200.0F) - (float) this.img.packedHeight / 2.0F;
        this.vX = MathUtils.random(-36.0F, 36.0F) * Settings.scale;
        this.aX = MathUtils.random(-5.0F, 5.0F) * Settings.scale;
        this.waveDst = this.vX * MathUtils.random(0.03F, 0.07F);
        this.scale = Settings.scale * Math.abs(this.vX) / 24.0F;
        if (MathUtils.randomBoolean()) {
            this.vX = -this.vX;
        }

        this.vY = MathUtils.random(-24.0F, 24.0F) * Settings.scale;
        this.color = setColor.cpy();
        this.baseAlpha = 0.25F;
        this.color.a = 0.0F;
    }

    public void update() {
        this.trailTimer -= Gdx.graphics.getDeltaTime();
        if (this.trailTimer < 0.0F) {
            this.trailTimer = 0.04F;
            this.prevPositions.add(new Vector2(this.x, this.y));
            if (this.prevPositions.size() > 30) {
                this.prevPositions.remove(0);
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.vX += this.aX * Gdx.graphics.getDeltaTime();
        if (!this.prevPositions.isEmpty()
                && (((Vector2) this.prevPositions.get(0)).x < 0.0F || ((Vector2) this.prevPositions.get(0)).x > (float) Settings.WIDTH)) {
            this.isDone = true;
        }

        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.y += MathUtils.sin(this.duration * this.waveDst) * this.waveDst / 4.0F * Gdx.graphics.getDeltaTime() * 60.0F;
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration > this.startingDuration / 2.0F) {
            float tmp = this.duration - this.startingDuration / 2.0F;
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.startingDuration / 2.0F - tmp) * this.baseAlpha;
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / (this.startingDuration / 2.0F)) * this.baseAlpha;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        this.setColor.a = this.color.a;

        for (int i = this.prevPositions.size() - 1; i > 0; --i) {
            Color var10000 = this.setColor;
            var10000.a *= 0.95F;
            sb.setColor(this.setColor);
            float var10004 = (float) this.img.packedWidth / 2.0F;
            float var10005 = (float) this.img.packedHeight / 2.0F;
            float var10006 = (float) this.img.packedWidth;
            sb.draw(this.img, ((Vector2) this.prevPositions.get(i)).x, ((Vector2) this.prevPositions.get(i)).y, var10004, var10005, var10006,
                    (float) this.img.packedHeight, this.scale * (float) (i + 5) / (float) this.prevPositions.size(),
                    this.scale * (float) (i + 5) / (float) this.prevPositions.size(), this.rotation);
        }

        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float) this.img.packedWidth / 2.0F, (float) this.img.packedHeight / 2.0F, (float) this.img.packedWidth,
                (float) this.img.packedHeight, this.scale * MathUtils.random(2.5F, 3.0F), this.scale * MathUtils.random(2.5F, 3.0F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
