package sts.ryoikitenkai.patch.purple;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.purple.DevaForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.DevaPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.FormParticleEffect;
import sts.ryoikitenkai.vfx.purple.DevaEffect;
import sts.ryoikitenkai.vfx.red.OrbEffect;

public class DevaFormPatch extends AbstractPowerImpl {
    @SpirePatch(clz = DevaForm.class, method = "use")
    public static class Use {
        public static void Prefix(DevaForm $this, AbstractPlayer p, AbstractMonster m) {
            for (int i = 0; i < 30; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, Color.PURPLE.cpy()));
            }
            Utils.addEffect(new FormParticleEffect(p.hb.cX, Color.PURPLE.cpy()));
            Utils.addEffect(new BorderFlashEffect(Color.PURPLE, true));
        }
    }

    @Override
    public String getID() {
        return DevaPower.POWER_ID;
    }

    DevaEffect devaEffect;

    @Override
    public void onApply(AbstractPower power) {
        devaEffect = new DevaEffect();
        Utils.addEffect(devaEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        devaEffect.isEnd = true;
    }
}
