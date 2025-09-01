package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Loop;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class LoopPatch {
    @SpirePatch(clz = Loop.class, method = "use")
    public static class Use {
        public static void Prefix(Loop $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Loop.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("INITIATE Subroutine: ", Color.WHITE.cpy()),
                            new Pair<>("LOOP", Color.CYAN.cpy()))
                    .addLog("Re-executing the last cached instruction.", Color.WHITE.cpy()));
        }
    }
}
