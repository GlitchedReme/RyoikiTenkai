package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.FeelNoPain;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.common.FeelNoPainEffect;

public class FeelNoPainPatch {
    @SpirePatch(clz = FeelNoPain.class, method = "use")
    public static class Use {
        public static void Prefix(FeelNoPain $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new FeelNoPainEffect());
        }
    }
}
