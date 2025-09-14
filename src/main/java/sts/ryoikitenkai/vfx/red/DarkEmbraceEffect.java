package sts.ryoikitenkai.vfx.red;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DarkEmbraceEffect extends AbstractGameEffect {
    public boolean isEnd;

    private ArrayList<FireFlyEffect> fireFlies = new ArrayList<>();

    public DarkEmbraceEffect() {
        this.renderBehind = true;
        this.color = Color.BLACK.cpy();
        this.color.a = 0f;
    }

    @Override
    public void update() {
        this.color.a += 0.005f;
        if (this.color.a > 0.15f) {
            this.color.a = 0.15f;
        }

        for (int i = this.fireFlies.size() - 1; i >= 0; i--) {
            FireFlyEffect f = this.fireFlies.get(i);
            if (f.isDone) {
                this.fireFlies.remove(i);
            }
        }

        if (this.fireFlies.size() < 15 && MathUtils.randomBoolean(0.6F)) {
            FireFlyEffect ff = new FireFlyEffect(new Color(
                    MathUtils.random(0.5F, 0.7F),
                    MathUtils.random(0.0F, 0.2F),
                    MathUtils.random(0.8F, 1.0F),
                    1.0F), AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY);
            this.fireFlies.add(ff);
            AbstractDungeon.effectsQueue.add(ff);
        }
        if (this.isEnd) {
            this.color.a -= 0.05f;
            if (this.color.a < 0f) {
                this.color.a = 0f;
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }

    @Override
    public void dispose() {
    }
}
