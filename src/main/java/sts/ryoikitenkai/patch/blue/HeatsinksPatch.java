package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Heatsinks;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.HeatsinkPower;

import basemod.Pair;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;
import sts.ryoikitenkai.vfx.blue.HeatSinksParticleEffect;

public class HeatsinksPatch {
    @SpirePatch(clz = Heatsinks.class, method = "use")
    public static class Use {
        public static void Prefix(Heatsinks $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Heatsinks.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("INITIATE Protocol: ", Color.WHITE.cpy()),
                            new Pair<>("HEATSINKS", Color.BROWN.cpy()))
                    .addLog("System temperature nominal.", Color.WHITE.cpy())
                    .addLog("Overclocking protocol engaged.", Color.WHITE.cpy()));
        }
    }

    @SpirePatch(clz = HeatsinkPower.class, method = "onUseCard")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(HeatsinkPower $this, AbstractCard card, UseCardAction action) {
            if (!RyoikiTenkai.isEnable(Heatsinks.ID)) {
                return;
            }
            for (int i = 0; i < 15; i++) {
                HeatSinksParticleEffect effect = new HeatSinksParticleEffect($this.owner.hb.cX, $this.owner.hb.cY, 0.6F);
                effect.renderBehind = MathUtils.randomBoolean();
                Utils.addEffect(effect);
            }
        }
    }
}
