package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Rupture;
import com.megacrit.cardcrawl.powers.RupturePower;
import com.megacrit.cardcrawl.vfx.combat.RipAndTearEffect;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;

public class RupturePatch {
    @SpirePatch(clz = RupturePower.class, method = "wasHPLost")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(RupturePower $this, DamageInfo info, int damageAmount) {
            if (!RyoikiTenkai.isEnable(Rupture.ID)) {
                return;
            }
            Utils.addEffect(new RipAndTearEffect($this.owner.hb.cX, $this.owner.hb.cY, Color.RED, Color.GOLD));
        }
    }
}
