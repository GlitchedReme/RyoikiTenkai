package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.FireBreathing;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.common.FireBreathingEffect;
import sts.ryoikitenkai.vfx.forever.BurningEffect;

public class FireBreathingPatch extends AbstractPowerImpl {
    @SpirePatch(clz = FireBreathing.class, method = "use")
    public static class Use {
        public static void Prefix(FireBreathing $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addToBot(new VFXAction(p, new InflameEffect(p), 0.1F));
        }
    }

    @SpirePatch(clz = FireBreathingPower.class, method = "onCardDraw")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(FireBreathingPower $this, AbstractCard card) {
            Utils.addEffect(new FireBreathingEffect(Settings.WIDTH * 0.8F, Settings.HEIGHT * 0.5F, $this.amount / 6));
        }
    }

    BurningEffect burningEffect;

    @Override
    public String getID() {
        return FireBreathingPower.POWER_ID;
    }

    @Override
    public void onApply(AbstractPower power) {

        burningEffect = new BurningEffect(power.owner);
        Utils.addEffect(burningEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        burningEffect.isDone = true;
        burningEffect = null;
    }
}
