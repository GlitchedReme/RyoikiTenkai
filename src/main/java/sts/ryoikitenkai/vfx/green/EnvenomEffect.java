package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import sts.ryoikitenkai.vfx.blue.HeatSinksParticleEffect;

public class EnvenomEffect extends AbstractGameEffect {

    public float timer;
    public float interval = 0.1f;

    public EnvenomEffect() {
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();
        if (this.timer > this.interval) {
            HeatSinksParticleEffect effect = new HeatSinksParticleEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.floorY + 60f * Settings.scale,
                    0.3F);
            effect.renderBehind = true;
            AbstractDungeon.effectsQueue.add(effect);
            this.timer = 0f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {

    }
}
