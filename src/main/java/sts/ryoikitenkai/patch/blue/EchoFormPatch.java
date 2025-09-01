package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EchoPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import basemod.Pair;
import basemod.ReflectionHacks;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.FormParticleEffect;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;
import sts.ryoikitenkai.vfx.blue.EchoFormEffect;
import sts.ryoikitenkai.vfx.red.OrbEffect;

public class EchoFormPatch extends AbstractPowerImpl {
    @SpirePatch(clz = EchoForm.class, method = "use")
    public static class Use {
        public static void Prefix(EchoForm $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(EchoForm.ID)) {
                return;
            }
            for (int i = 0; i < 30; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, Color.CYAN.cpy()));
            }
            Color color = new Color(0.3F, 0.3F, 0.3F, 0.4F);
            for (int i = 0; i < 30; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, color));
            }
            Utils.addEffect(new FormParticleEffect(p.hb.cX, Color.CYAN.cpy()));
            Utils.addEffect(new BorderFlashEffect(Color.CYAN, true));

            Utils.addEffect(new DefectLogEffect()
                    .addLog(
                            new Pair<>(" INITIATE KernelModule: ", Color.WHITE.cpy()),
                            new Pair<>("ECHO", Color.SCARLET.cpy()))
                    .addLog("Volatile subroutine loaded. Flagged for garbage collection at cycle end.", Color.RED.cpy())
                    .addLog("STATUS: UNSTABLE", Color.RED.cpy()));
        }
    }

    @Override
    public String getID() {
        return EchoPower.POWER_ID;
    }

    EchoFormEffect echoFormEffect;

    @Override
    public void onApply(AbstractPower power) {
        echoFormEffect = new EchoFormEffect(ReflectionHacks.getPrivate(power.owner, AbstractCreature.class, "skeleton"));
        Utils.addEffect(echoFormEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        echoFormEffect.isDone = true;
    }
}
