package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Capacitor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class CapacitorPatch {
    @SpirePatch(clz = Capacitor.class, method = "use")
    public static class Use {
        public static void Prefix(Capacitor $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Capacitor.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(String.format("EXECUTE MemoryAllocation(OrbSlots, +%d)", $this.magicNumber), Color.WHITE.cpy())
                    .addLog("Orb matrix capacity augmented. Ready for additional node instantiation.", Color.WHITE.cpy())
                    .addLog("STATUS: SUCCESS", Color.WHITE.cpy()));
        }
    }
}
