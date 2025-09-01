package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.StaticDischarge;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StaticDischargePower;

import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;
import sts.ryoikitenkai.vfx.blue.StaticDischargeEffect;

public class StaticDischargePatch extends AbstractPowerImpl {

    @SpirePatch(clz = StaticDischarge.class, method = "use")
    public static class Use {
        public static void Prefix(StaticDischarge $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(StaticDischarge.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("ACTIVATE Protocol: ", Color.WHITE.cpy()),
                            new Pair<>("STATIC_DISCHARGE", Color.BLUE.cpy()))
                    .addLog("Defensive grid re-routing kinetic overflow into retaliatory discharge.", Color.WHITE.cpy()));
        }
    }

    @Override
    public String getID() {
        return StaticDischargePower.POWER_ID;
    }

    StaticDischargeEffect effect;

    @Override
    public void onApply(AbstractPower power) {
        this.effect = new StaticDischargeEffect();
        Utils.addEffect(effect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        effect.isDone = true;
    }
}
