package sts.ryoikitenkai.patch.blue;

import java.util.WeakHashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.WindParticleEffect;

public class FocusPatch extends AbstractPowerImpl {
    @Override
    public String getID() {
        return FocusPower.POWER_ID;
    }

    WeakHashMap<AbstractCreature, WindParticleEffect> effects = new WeakHashMap<>();

    @Override
    public void onApply(AbstractPower power) {
        WindParticleEffect effect = new WindParticleEffect(() -> new Color( MathUtils.random(0.1F, 0.2F), 0.1F, MathUtils.random(0.6F, 0.8F), 0.0F));
        effect.isDex = false;
        effect.isNegative = power.amount < 0;
        effect.interval = Math.max(0.01F, 0.04f - (0.002f * Math.abs(power.amount))) * 2;
        effects.put(power.owner, effect);
        Utils.addEffect(effect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        WindParticleEffect effect = effects.remove(power.owner);
        if (effect != null) {
            effect.isDone = true;
        }
    }

    @Override
    public void onStack(AbstractPower power, int stackAmount) {

        WindParticleEffect effect = effects.get(power.owner);
        if (effect != null) {
            effect.isNegative = power.amount < 0;
            effect.interval = Math.max(0.01F, 0.04f - (0.002f * Math.abs(power.amount))) * 2;
        }
    }
}
