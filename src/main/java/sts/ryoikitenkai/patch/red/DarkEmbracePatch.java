package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DarkEmbracePower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.common.AuraEffect;
import sts.ryoikitenkai.vfx.common.DarkEmbraceEffect;
import sts.ryoikitenkai.vfx.common.OrbEffect;

public class DarkEmbracePatch extends AbstractPowerImpl {

    @Override
    public String getID() {
        return DarkEmbracePower.POWER_ID;
    }

    AuraEffect auraEffect;
    DarkEmbraceEffect darkEmbraceEffect;

    @Override
    public void onApply(AbstractPower power) {
        Color color = new Color(1.0F, 0.15F, 0.8F, 0.4F);
        for (int i = 0; i < 32; i++) {
            Utils.addEffect(new OrbEffect(power.owner.hb.cX, power.owner.hb.cY, i, color));
        }

        auraEffect = new AuraEffect(Color.WHITE.cpy());
        Utils.addEffect(auraEffect);
        darkEmbraceEffect = new DarkEmbraceEffect();
        Utils.addEffect(darkEmbraceEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        auraEffect.isDone = true;
        darkEmbraceEffect.isEnd = true;
    }

}
