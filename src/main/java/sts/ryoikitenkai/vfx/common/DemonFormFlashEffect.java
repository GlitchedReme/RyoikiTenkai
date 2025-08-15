package sts.ryoikitenkai.vfx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

import sts.ryoikitenkai.modcore.ShaderManager;

public class DemonFormFlashEffect extends AbstractGameEffect {
    private static Texture IMG;
    private static final float TARGET_ALPHA = 0.9F;

    private float x;
    private float y;
    private float timer;
    private float timer2;
    public boolean isEnd;

    public DemonFormFlashEffect() {
        if (IMG == null) {
            TextureAtlas.AtlasRegion region = AbstractPower.atlas.findRegion("128/phantasmal");
            Texture atlasTexture = region.getTexture();
            if (!atlasTexture.getTextureData().isPrepared()) {
                atlasTexture.getTextureData().prepare();
            }
            Pixmap atlasPixmap = atlasTexture.getTextureData().consumePixmap();

            Pixmap regionPixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), atlasPixmap.getFormat());
            regionPixmap.drawPixmap(atlasPixmap, 0, 0, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());

            IMG = new Texture(regionPixmap);
            IMG.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            regionPixmap.dispose();
            atlasPixmap.dispose();
        }

        this.duration = this.startingDuration = 2.5F;
        this.color = new Color(1.0F, 0.02F, 0.02F, 1.0F);
        this.renderBehind = true;
        this.x = AbstractDungeon.player.hb.cX;
        this.y = AbstractDungeon.player.hb.cY + 80.0F * Settings.scale;
    }

    @Override
    public void update() {
        float dt = Gdx.graphics.getDeltaTime();
        this.timer += dt;
        if (this.duration > this.startingDuration - 0.5F) {
            float progress = (this.startingDuration - this.duration) / 0.5F;
            this.color.a = Interpolation.fade.apply(MathUtils.clamp(progress, 0.0F, TARGET_ALPHA));
        } else if (this.isEnd && this.duration < 0.5F) {
            float progress = this.duration / 0.5F;
            this.color.a = Interpolation.fade.apply(MathUtils.clamp(progress, 0.0F, TARGET_ALPHA));
        } else {
            this.color.a = TARGET_ALPHA;
        }

        if (this.isEnd || this.duration > 0.5F) {
            this.duration -= dt;
        }
        
        this.timer2 += dt;
        if (this.timer2 > 0.1F) {
            AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x, this.y));
            this.timer2 = 0.0F;
        }

        if (this.isEnd && this.duration <= 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        // sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        ShaderProgram shader = ShaderManager.heatWave.getShaderProgram();
        sb.setShader(shader);
        shader.setUniformf(ShaderManager.heatWaveTime, this.timer);
        shader.setUniformf(ShaderManager.heatWaveAmplitude, 0.015F);
        shader.setUniformf(ShaderManager.heatWaveFrequency, 35.0F);
        shader.setUniformf(ShaderManager.heatWaveSpeed, 2.0F);
        sb.draw(IMG, this.x - IMG.getWidth() / 2.0F, this.y - IMG.getHeight() / 2.0F, IMG.getWidth() / 2.0F, IMG.getHeight() / 2.0F,
                IMG.getWidth(), IMG.getHeight(), 5.0F * Settings.scale, 5.0F * Settings.scale, 0, 0, 0, IMG.getWidth(), IMG.getHeight(), false,
                false);
        sb.setShader(null);
        // sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {
    }

}
