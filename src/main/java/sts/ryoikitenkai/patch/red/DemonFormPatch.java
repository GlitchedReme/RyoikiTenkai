package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.FormParticleEffect;
import sts.ryoikitenkai.vfx.red.DemonFormFlashEffect;
import sts.ryoikitenkai.vfx.red.OrbEffect;

public class DemonFormPatch extends AbstractPowerImpl {
    @SpirePatch(clz = DemonForm.class, method = "use")
    public static class Use {
        public static void Prefix(DemonForm $this, AbstractPlayer p, AbstractMonster m) {
            CardCrawlGame.sound.play("NECRONOMICON", -0.5F);
            Color color = new Color(1.0F, 0.15F, 0.1F, 0.4F);
            for (int i = 0; i < 30; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, color));
            }
            Utils.addEffect(new BorderFlashEffect(Color.SCARLET, true));
            for (int i = 0; i < 10; i++) {
                AbstractDungeon.effectsQueue.add(new FormParticleEffect(p.hb.cX, color));
            }
            Utils.addEffect(new InflameEffect(p));
        }
    }

    @Override
    public String getID() {
        return DemonFormPower.POWER_ID;
    }

    DemonFormFlashEffect demonFormFlashEffect;

    @Override
    public void onApply(AbstractPower power) {
        demonFormFlashEffect = new DemonFormFlashEffect();
        Utils.addEffect(demonFormFlashEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        demonFormFlashEffect.isEnd = true;
    }
}
