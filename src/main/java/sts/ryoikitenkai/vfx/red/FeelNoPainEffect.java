package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

public class FeelNoPainEffect extends AbstractGameEffect {
    private static final TextureAtlas.AtlasRegion img = ImageMaster.vfxAtlas.findRegion("combat/battleStartSword");

    private float swordTimer = 0.5F;
    private float startX;
    private float swordX;
    private float swordY;
    private float swordAngle;
    private boolean swordSound1 = false;
    private Color swordColor = new Color(0.9F, 0.5F, 0.55F, 0.0F);

    public FeelNoPainEffect() {
        this.duration = 3F;
        this.startingDuration = 3F;
        this.scale = Settings.scale * 0.8F;
        this.swordY = AbstractDungeon.player.drawY - img.packedHeight / 2.0F + 150.0F * Settings.scale;

        CardCrawlGame.sound.play("BATTLE_START_BOSS");
        this.startX = -Settings.WIDTH * 0.5F;
    }

    public void update() {
        float dt = Gdx.graphics.getDeltaTime();

        if (Settings.FAST_MODE)
            this.duration -= dt;
        this.duration -= dt;
        if (this.duration < 0.0F)
            this.isDone = true;
        updateSwords();
    }

    private void updateSwords() {
        this.swordTimer -= Gdx.graphics.getDeltaTime();
        float x = AbstractDungeon.player.drawX;
        float y = AbstractDungeon.player.drawY;
        if (this.swordTimer < 0.0F)
            this.swordTimer = 0.0F;

        if (this.duration > 2.5F && this.duration <= 3.0F) {
            this.swordColor.a = Interpolation.fade.apply(0.01F, 1.0F, (2.5F - this.duration) / 0.5F);
        } else if (this.duration < 0.5F) {
            this.swordColor.a = Interpolation.fade.apply(1.0F, 0.01F, (0.5F - this.duration) / 0.5F);
        } else {
            this.swordColor.a = 1.0F;
        }

        if (this.swordTimer < 0.1F && !this.swordSound1) {
            this.swordSound1 = true;
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
            for (int i = 0; i < 40; i++) {
                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.effectsQueue.add(new UpgradeShineParticleEffect(
                            x + MathUtils.random(-150.0F, 150.0F) * Settings.scale,
                            y + MathUtils.random(-10.0F, 50.0F) * Settings.scale));
                } else {
                    AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineParticleEffect(
                            x + MathUtils.random(-150.0F, 150.0F) * Settings.scale,
                            y + MathUtils.random(-10.0F, 50.0F) * Settings.scale));
                }
            }
        }
        this.swordX = Interpolation.pow3Out.apply(0F, startX, this.swordTimer / 0.5F);
        this.swordAngle = Interpolation.pow3Out.apply(-50.0F, 500.0F, this.swordTimer / 0.5F);
    }

    public void render(SpriteBatch sb) {
        renderSwords(sb);
    }

    public void dispose() {
    }

    private void renderSwords(SpriteBatch sb) {
        sb.setColor(this.swordColor);
        float x = AbstractDungeon.player.drawX;
        sb.draw(img, x - this.swordX - img.packedWidth / 2.0F, this.swordY,
                img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, -this.scale, -this.scale,
                -this.swordAngle + 180.0F);
        sb.draw(img, x + this.swordX - img.packedWidth / 2.0F, this.swordY,
                img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, this.scale, this.scale, this.swordAngle);
    }
}
