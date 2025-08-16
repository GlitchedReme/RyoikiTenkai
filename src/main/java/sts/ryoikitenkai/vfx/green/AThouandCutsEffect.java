package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowShivEffect;

public class AThouandCutsEffect extends AbstractGameEffect {
    private float interval = 0.0F;

    public AThouandCutsEffect() {
        this.duration = 0.5F;
    }

    public void update() {
        this.interval -= Gdx.graphics.getDeltaTime();
        if (this.interval < 0.0F) {
            this.interval = MathUtils.random(0.05F, 0.10F);
            int derp = MathUtils.random(1, 4);
            for (int i = 0; i < derp; i++)
                AbstractDungeon.effectsQueue.add(
                        new ThrowShivEffect(MathUtils.random(0.6F, 1.0F) * Settings.WIDTH,
                                AbstractDungeon.floorY + MathUtils.random(-50.0F, 200.0F) * Settings.scale));
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F)
            this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}