package sts.ryoikitenkai.vfx.blue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EchoParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private static TextureAtlas.AtlasRegion img = ImageMaster.EXHAUST_L;

    public EchoParticleEffect(float x, float y) {
        this.duration = 2.0F;
        this.scale = MathUtils.random(0.6F, 0.8F) * Settings.scale;

        float value = MathUtils.random(0.0F, 0.3F);
        this.color = new Color(value, value, 0.8F, 0.0F);

        this.x = x + MathUtils.random(-1F, 1F);
        this.y = y + MathUtils.random(-1F, 1F);
        this.x -= img.packedWidth / 2.0F - 10F * Settings.scale;
        this.y -= img.packedHeight / 2.0F - 10F * Settings.scale;
        this.rotation = MathUtils.random(360.0F);
        this.vY = MathUtils.random(-40.0F, 40.0F);
    }

    @Override
    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }

        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth,
                (float) img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {
    }
}