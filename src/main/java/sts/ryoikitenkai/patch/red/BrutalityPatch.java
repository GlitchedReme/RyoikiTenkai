package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.Brutality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;

public class BrutalityPatch {
    @SpirePatch(clz = Brutality.class, method = "use")
    public static class Use {
        public static void Prefix(Brutality $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Brutality.ID)) {
                return;
            }
            Utils.addEffect(new OfferingEffect());
        }
    }
}
