package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.red.Combust;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CombustPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.red.BurningEffect;
import sts.ryoikitenkai.vfx.red.CombustEffect;

public class CombustPatch extends AbstractPowerImpl {
    @SpirePatch(clz = Combust.class, method = "use")
    public static class Use {
        public static void Prefix(Combust $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Combust.ID)) {
                return;
            }
            Utils.addToBot(new VFXAction(p, new InflameEffect(p), 0.1F));
        }
    }

    @SpirePatch(clz = CombustPower.class, method = "atEndOfTurn")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(CombustPower $this) {
            if (!RyoikiTenkai.isEnable(Combust.ID)) {
                return;
            }
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

        burningEffect = new BurningEffect(power.owner);
        Utils.addEffect(burningEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        burningEffect.isDone = true;
        burningEffect = null;
    }

}
