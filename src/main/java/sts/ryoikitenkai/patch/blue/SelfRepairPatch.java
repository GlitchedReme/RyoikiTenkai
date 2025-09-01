package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.SelfRepair;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class SelfRepairPatch {
    @SpirePatch(clz = SelfRepair.class, method = "use")
    public static class Use {
        public static void Prefix(SelfRepair $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(SelfRepair.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("EXECUTE: ", Color.WHITE.cpy()),
                            new Pair<>("\"SelfRepair.bat\"", Color.GOLD.cpy()))
                    .addLog("Nanite swarm dispatched for post-combat integrity restoration.", Color.WHITE.cpy()));

        }
    }
}
