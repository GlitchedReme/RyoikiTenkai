package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

    private float vX;
    private float vY;

    private float startDur;

    private float targetScale;

    private TextureAtlas.AtlasRegion img;

    public CorruptionParticleEffect(float x, float y) {
        this.color = new Color(0.0F, 0.0F, 0.0F, 1.0F);
        this.color.r = MathUtils.random(0.3F, 0.5F);
        this.color.g = MathUtils.random(0.0F, 0.1F);
        this.color.b = MathUtils.random(0.4F, 0.6F);

        int r = MathUtils.random(0, 2);
        if (r == 0)
            this.img = ImageMaster.FLAME_1;
        else if (r == 1)
            this.img = ImageMaster.FLAME_2;
        else if (r == 2)
            this.img = ImageMaster.FLAME_3;
        this.duration = MathUtils.random(2.0F, 2.5F);
        this.targetScale = MathUtils.random(0.2F, 0.32F);
        this.startDur = this.duration;
        this.x = x + MathUtils.random(-2.0F * Settings.scale, 2.0F * Settings.scale) - this.img.packedWidth / 2.0F;
        this.y = y + MathUtils.random(-2.0F * Settings.scale, 2.0F * Settings.scale) - this.img.packedHeight / 2.0F;
        this.scale = 0.01F;
        this.vX = MathUtils.random(-0.15F * Settings.scale, -0.5F * Settings.scale);
        this.vY = MathUtils.random(0.1F * Settings.scale, 0.3F * Settings.scale);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
        this.x += this.vX;
        this.y += this.vY;
        this.rotation = MathUtils.atan2(this.vY, this.vX) * MathUtils.radiansToDegrees - 90F;
        this.scale = Interpolation.exp10Out.apply(0.01F, this.targetScale, 1.0F - this.duration / this.startDur);
        if (this.duration < 0.33F)
            this.color.a = this.duration * 3.0F;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw((TextureRegion) this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth,
                this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void dispose() {
    }
}