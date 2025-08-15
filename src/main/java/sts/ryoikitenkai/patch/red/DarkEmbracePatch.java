package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.DarkEmbrace;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DarkEmbracePower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.red.AuraEffect;
import sts.ryoikitenkai.vfx.red.DarkEmbraceEffect;
import sts.ryoikitenkai.vfx.red.OrbEffect;

public class DarkEmbracePatch extends AbstractPowerImpl {

    @SpirePatch(clz = DarkEmbrace.class, method = "use")
    public static class Use {
        public static void Prefix(DarkEmbrace $this, AbstractPlayer p, AbstractMonster m) {
            Color color = new Color(1.0F, 0.15F, 0.8F, 0.4F);
            for (int i = 0; i < 32; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, color));
            }
        }
    }

    @Override
    public String getID() {
        return DarkEmbracePower.POWER_ID;
    }

    AuraEffect auraEffect;
    DarkEmbraceEffect darkEmbraceEffect;

    @Override
    public void onApply(AbstractPower power) {

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
