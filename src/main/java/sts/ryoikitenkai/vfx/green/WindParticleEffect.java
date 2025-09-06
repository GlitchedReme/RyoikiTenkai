package sts.ryoikitenkai.vfx.green;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WindParticleEffect extends AbstractGameEffect {

    public float timer;
    public float interval = 0.04f;

    public boolean isDex = true;
    public boolean isNegative = false;

    private final Supplier<Color> color;

    public WindParticleEffect(Supplier<Color> color) {
        this.renderBehind = true;
        this.color = color;
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();
        if (this.timer > this.interval) {
            Color color = this.color.get();
            AbstractDungeon.effectsQueue.add(new StrParticleEffect(color, isNegative));

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
