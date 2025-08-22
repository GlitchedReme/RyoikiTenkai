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

import basemod.Pair;

public class DefectLogEffect extends AbstractGameEffect {

    public ArrayList<DefectLog> logs = new ArrayList<>();
    public float timer = 0.016F;

    public float x;
    public float y;

    public DefectLogEffect() {
        this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-200.0F, 250.0F) * Settings.scale;
        this.y = AbstractDungeon.player.hb.cY + MathUtils.random(-100.0F, 100.0F) * Settings.scale;
        this.renderBehind = this.y > AbstractDungeon.player.hb.cY;
    }

    public DefectLogEffect addLog(String msg, Color color) {
        this.logs.add(new DefectLog(msg, color));
        return this;
    }

    public DefectLogEffect addLog(Pair<String, Color>... fragments) {
        this.logs.add(new DefectLog(fragments));
        return this;
    }

    @Override
    public void update() {
        int size = logs.size();
        for (int i = 0; i < size; i++) {
            DefectLog log = logs.get(i);
            log.y = y + (size - i) * 40.0F;

            if (log.fragments.stream().allMatch(f -> f.index == f.text.length())) {
                log.timer -= Gdx.graphics.getDeltaTime();

                log.alpha = Math.min(1f, log.timer);

                if (log.alpha <= 0.0F) {
                    log.alpha = 0.0F;
                    log.isDone = true;
                }
            } else {
                this.timer += Gdx.graphics.getDeltaTime();

                for (int j = 0; j < log.fragments.size(); j++) {
                    TextFragment fragment = log.fragments.get(j);
                    if (fragment.index < fragment.text.length()) {

                        if (timer >= 0.02F) {
                            timer -= 0.02F;
                            fragment.index++;
                            fragment.visibleText = fragment.text.substring(0, fragment.index);
                        }
                        break;
                    }
                }
            }

            float offset = 0F;
            for (int j = 0; j < log.fragments.size(); j++) {
                TextFragment fragment = log.fragments.get(j);
                fragment.x = x + offset;
                FontHelper.layout.setText(FontHelper.blockInfoFont, fragment.visibleText);
                offset += FontHelper.layout.width;
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
        for (DefectLog log : logs) {
            for (TextFragment f : log.fragments) {
                f.color.a = log.alpha;
                FontHelper.renderFontLeft(sb, FontHelper.blockInfoFont, f.visibleText, f.x, log.y, f.color);
            }
        }
        sb.setBlendFunction(770, 771); // Reset blend function
    }

    @Override
    public void dispose() {

    }

    public static class DefectLog {
        public ArrayList<TextFragment> fragments = new ArrayList<>();
        public float y;
        public float alpha = 1.0F;
        public boolean isDone = false;
        public float timer = MathUtils.random(2.0F, 3.0F);

        public DefectLog(String msg, Color color) {
            this.y = 100.0F;
            this.fragments.add(new TextFragment("[ > ", Color.WHITE.cpy(), true));
            this.fragments.add(new TextFragment(msg, color));
            this.fragments.add(new TextFragment(" ]", Color.WHITE.cpy(), true));
        }

        public DefectLog(Pair<String, Color>... fragments) {
            this.y = 100.0F;
            this.fragments.add(new TextFragment("[ > ", Color.WHITE.cpy(), true));
            for (Pair<String, Color> fragment : fragments) {
                this.fragments.add(new TextFragment(fragment.getKey(), fragment.getValue()));
            }
            this.fragments.add(new TextFragment(" ]", Color.WHITE.cpy(), true));
        }
    }

    public static class TextFragment {
        public String text;
        public Color color;
        public float x;

        public String visibleText = "";
        public int index = 0;

        public TextFragment(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public TextFragment(String text, Color color, boolean isEnd) {
            this.text = text;
            this.color = color;
            this.visibleText = text;
        }
    }
}
