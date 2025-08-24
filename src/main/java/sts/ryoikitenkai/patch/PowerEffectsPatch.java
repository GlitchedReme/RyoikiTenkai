package sts.ryoikitenkai.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.ryoikitenkai.modcore.RyoikiTenkai;

public class PowerEffectsPatch {
    @SpirePatch(clz = AbstractPower.class, method = "onInitialApplication")
    public static class OnInitPatch {
        public static void Prefix(AbstractPower $this) {
            if (!RyoikiTenkai.powerImpls.containsKey($this.ID)) {
                return;
            }

            RyoikiTenkai.powerImpls.get($this.ID).onApply($this);
        }
    }

    @SpirePatch(clz = AbstractPower.class, method = "onRemove")
    public static class OnRemovePatch {
        public static void Postfix(AbstractPower $this) {
            if (!RyoikiTenkai.powerImpls.containsKey($this.ID)) {
                return;
            }

            RyoikiTenkai.powerImpls.get($this.ID).onRemove($this);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    public static class OnVictoryPatch {
        public static void Postfix(AbstractPlayer $this) {
            for (AbstractPower power : $this.powers) {
                if (!RyoikiTenkai.powerImpls.containsKey(power.ID)) {
                    continue;
                }

                RyoikiTenkai.powerImpls.get(power.ID).onRemove(power);
            }
        }
    }

    @SpirePatch(clz = AbstractPower.class, method = "onDeath")
    public static class OnDeathPatch {
        public static void Postfix(AbstractPower $this) {
            if (!RyoikiTenkai.powerImpls.containsKey($this.ID)) {
                return;
            }

            RyoikiTenkai.powerImpls.get($this.ID).onRemove($this);
        }
    }
}
