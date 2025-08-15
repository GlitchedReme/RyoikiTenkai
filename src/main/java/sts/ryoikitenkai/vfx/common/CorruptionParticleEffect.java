package sts.ryoikitenkai.vfx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CorruptionParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;

    private static TextureAtlas.AtlasRegion IMG = ImageMaster.ROOM_SHINE_2;

    public CorruptionParticleEffect(float x, float y) {
        this.duration = MathUtils.random(0.5F, 1.0F);
        this.startingDuration = this.duration;
        this.x = x - (IMG.packedWidth / 2);
        this.y = y - (IMG.packedHeight / 2);
        this.scale = Settings.scale * MathUtils.random(0.5F, 0.7F);
        this.rotation = 0.0F;
        this.color = new Color(MathUtils.random(0.8F, 1.0F), MathUtils.random(0.1F, 0.2F), MathUtils.random(0.8F, 1.0F), 0.01F);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
        this.color.a = Interpolation.fade.apply(0.0F, 0.5F, this.duration / this.startingDuration);
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw((TextureRegion) IMG, this.x, this.y, IMG.packedWidth / 2.0F, IMG.packedHeight / 2.0F, IMG.packedWidth,
                IMG.packedHeight, this.scale *

                        MathUtils.random(6.0F, 12.0F),
                this.scale *
                        MathUtils.random(0.7F, 0.8F),
                this.rotation +
                        MathUtils.random(-1.0F, 1.0F));
        sb.draw((TextureRegion) IMG, this.x, this.y, IMG.packedWidth / 2.0F, IMG.packedHeight / 2.0F, IMG.packedWidth,
                IMG.packedHeight, this.scale *

                        MathUtils.random(0.2F, 0.5F),
                this.scale *
                        MathUtils.random(2.0F, 3.0F),
                this.rotation +
                        MathUtils.random(-1.0F, 1.0F));
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}