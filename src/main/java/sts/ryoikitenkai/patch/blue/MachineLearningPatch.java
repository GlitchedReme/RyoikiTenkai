package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.MachineLearning;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class MachineLearningPatch {
    @SpirePatch(clz = MachineLearning.class, method = "use")
    public static class Use {
        public static void Prefix(MachineLearning $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(MachineLearning.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(
                            new Pair<>("INITIATE HeuristicProtocol: ", Color.WHITE.cpy()),
                            new Pair<>("MachineLearning", Color.DARK_GRAY.cpy()))
                    .addLog("Predictive analytics indicate a necessity for increased data throughput.", Color.WHITE.cpy())
                    .addLog("Learning rate has been optimized.", Color.WHITE.cpy()));
        }
    }
}
