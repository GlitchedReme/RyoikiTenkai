package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.Berserk;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect.ShockWaveType;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;

public class BerserkPatch {
    @SpirePatch(clz = Berserk.class, method = "use")
    public static class Use {
        public static void Prefix(Berserk $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Berserk.ID)) {
                return;
            }
            Utils.addEffect(new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveType.ADDITIVE));
        }
    }
}
