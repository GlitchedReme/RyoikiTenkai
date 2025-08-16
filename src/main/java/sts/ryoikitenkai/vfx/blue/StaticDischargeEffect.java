package sts.ryoikitenkai.vfx.blue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StaticDischargeEffect extends AbstractGameEffect {
    public float timer;
    public float interval = 0.1f;

    public StaticDischargeEffect() {
    }

    @Override
    public void update() {
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0F) {
            for (int i = 0; i < MathUtils.random(1, 4); i++) {
                AbstractDungeon.effectsQueue.add(new LightningEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 1.2F));
            }
            this.timer = MathUtils.random(0.1f, 0.5f);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {

    }
}
