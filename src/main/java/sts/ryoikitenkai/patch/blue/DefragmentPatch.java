package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Defragment;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.Pair;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class DefragmentPatch {
    @SpirePatch(clz = Defragment.class, method = "use")
    public static class Use {
        public static void Prefix(Defragment $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new DefectLogEffect()
                    .addLog(
                            new Pair<>("EXECUTE Defragment(", Color.WHITE.cpy()),
                            new Pair<>("Focus", Color.ORANGE.cpy()),
                            new Pair<>(");", Color.WHITE.cpy()))
                    .addLog("Optimizing focus conduits.", Color.WHITE.cpy())
                    .addLog("All memory pointers re-aligned.", Color.WHITE.cpy()));
        }
    }
}
