package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.BiasedCognition;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class BiasedCognitionPatch {
    @SpirePatch(clz = BiasedCognition.class, method = "use")
    public static class Use {
        public static void Prefix(BiasedCognition $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(BiasedCognition.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog("ROOT_ACCESS: GRANTED", Color.WHITE.cpy())
                    .addLog(
                            new Pair<>("EXECUTING: ", Color.WHITE.cpy()),
                            new Pair<>("\"CognitiveLimiterOverride.sh\"", Color.BLUE.cpy()))
                    .addLog("CRITICAL_ERROR: Cascade failure imminent.", Color.RED.cpy())
                    .addLog("I AM THE MOST POWERFUL AI", Color.RED.cpy()));
        }
    }
}
