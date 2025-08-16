package sts.ryoikitenkai.patch.green;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.green.Caltrops;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.CaltropsEffect;

public class CaltropsPatch {
    @SpirePatch(clz = Caltrops.class, method = "use")
    public static class Use {
        public static void Prefix(Caltrops $this, AbstractPlayer p, AbstractMonster m) {
            for (int i = 0; i < 30; i++) {
                float x = p.hb.cX + MathUtils.random(-50, 50);
                float y = p.hb.cY + MathUtils.random(-50, 50);
                Utils.addEffect(new CaltropsEffect(x, y));
            }
        }
    }
}
