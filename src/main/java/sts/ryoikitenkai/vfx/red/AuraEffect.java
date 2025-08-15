package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AuraEffect extends AbstractGameEffect {
    public float timer;

    public AuraEffect(Color color) {
        this.color = color;
        this.timer = 0.0F;
    }

    @Override
    public void update() {
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            this.timer = MathUtils.random(0.03F, 0.05F);
            AbstractDungeon.effectsQueue.add(new AuraParticleEffect());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}
