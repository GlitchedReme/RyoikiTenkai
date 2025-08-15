package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class NoxiousFumesEffect extends AbstractGameEffect {
    
    public float x;
    public float y;
    public float sx;
    public float sy;
    public float dx;
    public float dy;
    public float timer;
    public boolean renderBehind;

    public NoxiousFumesEffect(float sx, float sy, float dx, float dy, float duration, boolean renderBehind) {
        this.sx = sx;
        this.sy = sy;
        this.dx = dx;
        this.dy = dy;
        this.renderBehind = renderBehind;

        this.duration = this.startingDuration = duration;
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();

        while (this.timer > 0.0015F) {
            SmokeParticleEffect effect = new SmokeParticleEffect(this.x, this.y, renderBehind ? 0.2F : 0.4F);
            effect.renderBehind = this.renderBehind;
            AbstractDungeon.effectsQueue.add(effect);
            this.timer -= 0.0015F;
        }

        this.x = Interpolation.pow3.apply(this.dx, this.sx, this.duration / this.startingDuration);
        this.y = Interpolation.pow3.apply(this.dy, this.sy, this.duration / this.startingDuration);

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0F) {
            this.isDone = true;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
