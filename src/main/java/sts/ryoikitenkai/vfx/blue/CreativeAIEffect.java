package sts.ryoikitenkai.vfx.blue;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CreativeAIEffect extends AbstractGameEffect {

    public ArrayList<Fragment> logs = new ArrayList<>();
    public float timer = 0.016F;
    public float timer2 = 0F;

    public float x;
    public float y;
    public boolean isEnd;

    public CreativeAIEffect() {
        this.x = AbstractDungeon.player.hb.cX;
        this.y = AbstractDungeon.player.hb.cY;
        this.renderBehind = this.y > AbstractDungeon.player.hb.cY;
    }

    @Override
    public void update() {
        if (!isEnd) {
            this.timer2 -= Gdx.graphics.getDeltaTime();

            if (this.timer2 < 0F) {
                this.timer2 = MathUtils.random(0.3F, 0.7F);

                StringBuilder sb = new StringBuilder();

                int roll = MathUtils.random(100);

                if (roll == 0) {
                    sb.append("<ERROR>");
                } else if (roll == 1) {
                    sb.append("D00T D00T");
                } else if (roll == 2) {
                    sb.append("H3110");
                } else {
                    int length = MathUtils.random(3, 10);
                    for (int i = 0; i < length; i++) {
                        sb.append(MathUtils.randomBoolean() ? "1" : "0");
                    }
                }

                String binaryString = sb.toString();
                Fragment fragment = new Fragment(binaryString,
                        new Color(MathUtils.random(0.5F, 1.0F), MathUtils.random(0.5F, 1.0F), MathUtils.random(0.5F, 1.0F), 0.0F));
                fragment.x = MathUtils.random(-400.0F, 200.0F) * Settings.scale;
                fragment.y = MathUtils.random(-150.0F, 150.0F) * Settings.scale;
                logs.add(fragment);
            }
        }

        int size = logs.size();
        for (int i = 0; i < size; i++) {
            Fragment log = logs.get(i);
            if (log.index == log.text.length()) {
                log.timer -= Gdx.graphics.getDeltaTime();

                log.alpha = Math.min(1f, log.timer);

                if (log.alpha <= 0.0F) {
                    log.alpha = 0.0F;
                    log.isDone = true;
                }
            } else {
                this.timer += Gdx.graphics.getDeltaTime();

                if (log.index < log.text.length()) {

                    if (timer >= 0.02F) {
                        timer -= 0.02F;
                        log.index++;
                        log.visibleText = log.text.substring(0, log.index);
                    }
                    break;
                }
            }
        }

        logs.removeIf(l -> l.isDone);

        if (logs.isEmpty()) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        for (Fragment log : logs) {
            log.color.a = log.alpha;
            FontHelper.renderFontLeft(sb, FontHelper.blockInfoFont, log.visibleText, x + log.x, y + log.y, log.color);
        }
        sb.setBlendFunction(770, 771); // Reset blend function
    }

    @Override
    public void dispose() {

    }

    public static class Fragment {
        public String text;
        public Color color;
        public float x;
        public float y;
        public float alpha = 1.0F;
        public boolean isDone = false;
        public float timer = MathUtils.random(2.0F, 3.0F);

        public String visibleText = "";
        public int index = 0;

        public Fragment(String msg, Color color) {
            this.y = 100.0F;
            this.text = msg;
            this.color = color;
        }
    }
}
