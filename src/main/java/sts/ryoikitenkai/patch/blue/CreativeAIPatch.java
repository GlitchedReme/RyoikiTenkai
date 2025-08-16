package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.CreativeAI;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CreativeAIPower;

import basemod.Pair;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.CreativeAIEffect;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class CreativeAIPatch extends AbstractPowerImpl {
    @SpirePatch(clz = CreativeAI.class, method = "use")
    public static class Use {
        public static void Prefix(CreativeAI $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new DefectLogEffect()
                    .addLog(
                            new Pair<>("ACTIVATE NeuralNetwork: ", Color.WHITE.cpy()),
                            new Pair<>("CreativeAI", Color.RED.cpy()))
                    .addLog("Adaptive evolution protocol engaged.", Color.WHITE.cpy())
                    .addLog("Generating novel solutions.", Color.WHITE.cpy()));
        }
    }

    @Override
    public String getID() {
        return CreativeAIPower.POWER_ID;
    }

    CreativeAIEffect creativeAIEffect;

    @Override
    public void onApply(AbstractPower power) {
        creativeAIEffect = new CreativeAIEffect();
        Utils.addEffect(creativeAIEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        creativeAIEffect.isEnd = true;
    }
}
