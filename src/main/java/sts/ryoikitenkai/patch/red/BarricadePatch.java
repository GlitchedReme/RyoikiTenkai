package sts.ryoikitenkai.patch.red;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.red.BarricadeEffect;

public class BarricadePatch extends AbstractPowerImpl {

    @Override
    public String getID() {
        return BarricadePower.POWER_ID;
    }

    ArrayList<BarricadeEffect> barricadeEffects = new ArrayList<>();

    @Override
    public void onApply(AbstractPower power) {
        for (int i = 0; i < 5; i++) {
            BarricadeEffect barricadeEffect = new BarricadeEffect(power.owner.hb.cX, power.owner.hb.cY - 80F * Settings.scale, i * 72F);
            Utils.addEffect(barricadeEffect);
            barricadeEffects.add(barricadeEffect);
        }
    }

    @Override
    public void onRemove(AbstractPower power) {
        barricadeEffects.forEach(b -> b.isEnd = true);
        barricadeEffects.clear();
    }
}
