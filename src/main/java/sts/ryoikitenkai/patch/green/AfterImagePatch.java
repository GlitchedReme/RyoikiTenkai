package sts.ryoikitenkai.patch.green;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AfterImagePower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.AfterImageEffect;

public class AfterImagePatch extends AbstractPowerImpl {
    @Override
    public String getID() {
        return AfterImagePower.POWER_ID;
    }

    AfterImageEffect afterImageEffect;

    @Override
    public void onApply(AbstractPower power) {
        afterImageEffect = new AfterImageEffect();
        Utils.addEffect(afterImageEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        afterImageEffect.isEnd = true;
    }
}
