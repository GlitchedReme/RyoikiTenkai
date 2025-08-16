package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CaltropsEffect extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img;

    private float x;
    private float y;

    private float vX;
    private float vY;

    private float floor;

    private static final float GRAVITY = 12000.0F * Settings.scale;

    public CaltropsEffect(float x, float y) {
        this.img = ImageMaster.TINY_STAR;
        this.duration = MathUtils.random(1.0F, 1.5F);
        this.x = x - (this.img.packedWidth / 2);
        this.y = y - (this.img.packedHeight / 2);
        float value = MathUtils.random(0.8F, 1.0F);
        this.color = new Color(value, value, value, 0.0F);
        this.color.a = 0.0F;
        this.rotation = MathUtils.random(0.0F, 360.0F);
        this.scale = MathUtils.random(0.5F, 2.0F) * Settings.scale;
        this.vX = MathUtils.random(1200F, 1500.0F) * Settings.scale;
        this.vY = MathUtils.random(1500.0F, 2500.0F) * Settings.scale;
        this.floor = MathUtils.random(150.0F, 200.0F) * Settings.scale;
    }

    public void update() {
        this.vY -= GRAVITY / this.scale * Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        Vector2 test = new Vector2(this.vX, this.vY);
        this.rotation = test.angle();
        if (this.y < this.floor) {
            this.vY = -this.vY * 0.75F;
            this.y = this.floor + 0.1F;
            this.vX *= 1.1F;
        }
        if (1.0F - this.duration < 0.1F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 10.0F);
        } else {
            this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw((TextureRegion) this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth,
                this.img.packedHeight, this.scale *

                        MathUtils.random(0.8F, 1.2F),
                this.scale *
                        MathUtils.random(0.8F, 1.2F),
                this.rotation);
        sb.draw((TextureRegion) this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth,
                this.img.packedHeight, this.scale *

                        MathUtils.random(0.8F, 1.2F),
                this.scale *
                        MathUtils.random(0.8F, 1.2F),
                this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}