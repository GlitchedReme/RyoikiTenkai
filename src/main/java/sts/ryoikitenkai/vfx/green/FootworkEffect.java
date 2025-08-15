package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FootworkEffect extends AbstractGameEffect {

    public float timer;
    public float interval = 0.04f;

    public FootworkEffect() {
        this.renderBehind = true;
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();
        if (this.timer > this.interval) {
            AbstractDungeon.effectsQueue
                    .add(new WindParticleEffect(new Color(MathUtils.random(0.1F, 0.2F), MathUtils.random(0.6F, 0.8F), 0.1F, 0.0F)));
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
