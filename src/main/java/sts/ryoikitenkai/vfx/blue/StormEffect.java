package sts.ryoikitenkai.vfx.blue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StormEffect extends AbstractGameEffect {
    public float timer;
    public float timer2;

    private float x;
    private float y;

    public StormEffect() {
        this.x = AbstractDungeon.player.hb.cX;
        this.y = AbstractDungeon.player.hb.cY + 400F * Settings.scale;
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();

        while (this.timer > 0.032F) {
            AbstractDungeon.effectsQueue.add(new StormParticleEffect(this.x, this.y, 0.8F * Settings.scale));
            this.timer -= 0.032F;
        }

        this.timer2 -= Gdx.graphics.getDeltaTime();
        if (this.timer2 < 0F) {
            for (int i = 0; i < MathUtils.random(1, 3); i++) {
                AbstractDungeon.effectsQueue.add(new LightningEffect(this.x, this.y, 1.2F));
            }
            this.timer2 = MathUtils.random(0.1f, 0.5f);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {

    }
}
