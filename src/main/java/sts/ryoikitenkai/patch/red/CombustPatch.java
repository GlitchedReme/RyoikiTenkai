package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CombustPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.common.CombustEffect;
import sts.ryoikitenkai.vfx.forever.BurningEffect;

public class CombustPatch extends AbstractPowerImpl {
    @SpirePatch(clz = CombustPower.class, method = "atEndOfTurn")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(CombustPower $this) {
            Utils.addEffect(new CombustEffect($this.amount / 5));
        }
    }

    BurningEffect burningEffect;

    @Override
    public String getID() {
        return CombustPower.POWER_ID;
    }

    @Override
    public void onApply(AbstractPower power) {
        Utils.addToBot(new VFXAction(power.owner, new InflameEffect(power.owner), 0.1F));

        burningEffect = new BurningEffect(power.owner);
        Utils.addEffect(burningEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        burningEffect.isDone = true;
        burningEffect = null;
    }

}
