package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Storm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StormPower;

import basemod.Pair;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;
import sts.ryoikitenkai.vfx.blue.StormEffect;

public class StormPatch extends AbstractPowerImpl {
    @SpirePatch(clz = Storm.class, method = "use")
    public static class Use {
        public static void Prefix(Storm $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("Weather control protocol switched to: ", Color.WHITE.cpy()),
                            new Pair<>("[STORM]", Color.SKY.cpy()))
                    .addLog("TRIGGER SET: Any power module deployment.", Color.WHITE.cpy())
                    .addLog("Energy grid is now configured to weaponize atmospheric discharge.", Color.WHITE.cpy()));
        }
    }

    @Override
    public String getID() {
        return StormPower.POWER_ID;
    }

    StormEffect stormEffect;

    @Override
    public void onApply(AbstractPower power) {
        stormEffect = new StormEffect();
        Utils.addEffect(stormEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        stormEffect.isDone = true;
    }
}
