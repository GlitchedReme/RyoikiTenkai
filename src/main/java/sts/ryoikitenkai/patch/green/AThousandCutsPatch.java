package sts.ryoikitenkai.patch.green;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.AThousandCuts;
import com.megacrit.cardcrawl.powers.ThousandCutsPower;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.AThouandCutsEffect;

public class AThousandCutsPatch {
    @SpirePatch(clz = ThousandCutsPower.class, method = "onAfterCardPlayed")
    public static class Power {
        public static void Postfix(ThousandCutsPower $this, AbstractCard card) {
            if (!RyoikiTenkai.isEnable(AThousandCuts.ID)) {
                return;
            }
            Utils.addEffect(new AThouandCutsEffect());
        }
    }
}
