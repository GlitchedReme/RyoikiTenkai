package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.common.DemonFormFlashEffect;
import sts.ryoikitenkai.vfx.common.OrbEffect;

public class DemonFormPatch extends AbstractPowerImpl {
    @Override
    public String getID() {
        return DemonFormPower.POWER_ID;
    }

    DemonFormFlashEffect demonFormFlashEffect;

    @Override
    public void onApply(AbstractPower power) {
        CardCrawlGame.sound.play("NECRONOMICON", -0.5F);
        Color color = new Color(1.0F, 0.15F, 0.1F, 0.4F);
        for (int i = 0; i < 12; i++) {
            Utils.addEffect(new OrbEffect(power.owner.hb.cX, power.owner.hb.cY, i, color));
        }
        demonFormFlashEffect = new DemonFormFlashEffect();
        Utils.addEffect(demonFormFlashEffect);
        Utils.addEffect(new InflameEffect(power.owner));
        Utils.addEffect(new BorderFlashEffect(Color.SCARLET, true));
        Utils.addEffect(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
    }

    @Override
    public void onRemove(AbstractPower power) {
        demonFormFlashEffect.isEnd = true;
    }
}
