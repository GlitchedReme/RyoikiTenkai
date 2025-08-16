package sts.ryoikitenkai.utils;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class Utils {
    public static boolean lerp(Color color, Color a, Color b, float w) {
        float dr = b.r - a.r;
        float dg = b.g - a.g;
        float db = b.b - a.b;
        float da = b.a - a.a;
        color.r = a.r + dr * w;
        color.g = a.g + dg * w;
        color.b = a.b + db * w;
        color.a = a.a + da * w;
        return dr * dr + dg * dg + db * db + da * da < 1e-5;
    }

    public static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static void addEffect(AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }
}
