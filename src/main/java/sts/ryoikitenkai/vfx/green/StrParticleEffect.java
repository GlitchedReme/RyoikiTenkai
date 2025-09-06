package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StrParticleEffect extends AbstractGameEffect {
    private float dur_div2 = this.duration / 2.0F;

    private float vX = MathUtils.random(-300.0F, -50.0F) * Settings.scale;
    private float vY = MathUtils.random(-200.0F, -100.0F) * Settings.scale;

    private float x = AbstractDungeon.player.hb.cX + MathUtils.random(100.0F, 160.0F) * Settings.scale - 32.0F;
    private float y = AbstractDungeon.player.hb.cY + MathUtils.random(-50.0F, 220.0F) * Settings.scale - 32.0F;

    private float dvx = 400.0F * Settings.scale * this.scale;
    private float dvy = 100.0F * Settings.scale;

    public StrParticleEffect(Color color, boolean isNegative) {
        this.duration = MathUtils.random(0.6F, 1.0F);
        this.scale = MathUtils.random(0.6F, 1.2F) * Settings.scale;
        this.dur_div2 = this.duration / 2.0F;
        this.color = color;
        if (isNegative) {
            this.vX = MathUtils.random(-10F, 10F) * Settings.scale;
            this.vY = -MathUtils.random(100.0F, 200.0F) * Settings.scale;
            this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-180.0F, 180.0F) * Settings.scale - 32.0F;
            this.y = AbstractDungeon.player.hb.cY + MathUtils.random(140.0F, 200.0F) * Settings.scale - 32.0F;
            this.dvx = 1.0F * Settings.scale * this.scale;
            this.dvy = -100.0F * Settings.scale;
        } else {
            this.vX = MathUtils.random(-10.0F, 10.0F) * Settings.scale;
            this.vY = MathUtils.random(100.0F, 200.0F) * Settings.scale;
            this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-180.0F, 180.0F) * Settings.scale - 32.0F;
            this.y = AbstractDungeon.player.hb.cY - MathUtils.random(140.0F, 200.0F) * Settings.scale - 32.0F;
            this.dvx = 1.0F * Settings.scale * this.scale;
            this.dvy = 100.0F * Settings.scale;
        }
        this.renderBehind = MathUtils.randomBoolean(0.2F + (this.scale - 0.5F));
    }

    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.vY += Gdx.graphics.getDeltaTime() * this.dvy;
        this.vX -= Gdx.graphics.getDeltaTime() * this.dvx;
        this.rotation = -(57.295776F * MathUtils.atan2(this.vX, this.vY)) - 0.0F;
        if (this.duration > this.dur_div2) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - this.dur_div2) / this.dur_div2);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / this.dur_div2);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.FROST_ACTIVATE_VFX_1, this.x, this.y, 32.0F, 32.0F, 25.0F, 128.0F, this.scale,
                this.scale + (this.dur_div2 * 0.4F - this.duration) * Settings.scale, this.rotation, 0, 0, 64, 64, false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}