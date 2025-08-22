package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AuraParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private static TextureAtlas.AtlasRegion img = ImageMaster.EXHAUST_L;

    public AuraParticleEffect() {
        this.duration = 2.0F;
        this.scale = MathUtils.random(2.8F, 3.5F) * Settings.scale;

        float value = MathUtils.random(0.0F, 0.03F);
        this.color = new Color(value, value, value, 0.0F);

        this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width / 3.5F, AbstractDungeon.player.hb.width / 3.5F);
        this.y = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height / 4.5F, AbstractDungeon.player.hb.height / 4.5F);
        this.x -= (float) img.packedWidth / 2.0F;
        this.y -= (float) img.packedHeight / 2.0F;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (MathUtils.randomBoolean(0.85F)) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }
    }

    @Override
    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.2F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.2F, this.duration);
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
        // sb.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_SRC_ALPHA);
        sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth,
                (float) img.packedHeight, this.scale, this.scale, this.rotation);
        // sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {
    }
}