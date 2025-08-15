package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;

public class BurningEffect extends AbstractGameEffect {
    float x;
    float y;
    float timer = 0f;

    public BurningEffect(AbstractCreature creature) {
        this.x = creature.hb.cX;
        this.y = creature.hb.cY;
    }

    @Override
    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();

        if (this.timer < 0.5f)
            return;

        this.timer = 0f;

        int i;
        for (i = 0; i < 5; i++)
            AbstractDungeon.effectsQueue.add(new FlameParticleEffect(this.x, this.y));
        for (i = 0; i < 5; i++)
            AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x, this.y));
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}
