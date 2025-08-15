package sts.ryoikitenkai.patch.green;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.FootworkEffect;

public class FootworkPatch extends AbstractPowerImpl {
    @Override
    public String getID() {
        return DexterityPower.POWER_ID;
    }

    FootworkEffect effect;

    @Override
    public void onApply(AbstractPower power) {
        effect = new FootworkEffect();
        Utils.addEffect(effect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        effect.isDone = true;
    }

    @Override
    public void onStack(AbstractPower power, int stackAmount) {
        int amount = stackAmount + power.amount;

        effect.interval = Math.max(0.1F, 0.04f - (0.001f * amount));
    }
}
