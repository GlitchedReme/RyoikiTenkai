package sts.ryoikitenkai.patch.green;

import java.util.WeakHashMap;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;

import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.EnvenomEffect;
import sts.ryoikitenkai.vfx.green.NoxiousFumesEffect;

public class EnvenomPatch extends AbstractPowerImpl {
    @SpirePatch(clz = Envenom.class, method = "use")
    public static class Use {
        public static void Prefix(Envenom $this, AbstractPlayer p, AbstractMonster m) {
            float x = p.hb.cX;
            float y = AbstractDungeon.floorY;
            Utils.addEffect(new NoxiousFumesEffect(x - 200F * Settings.scale, y - 65f * Settings.scale, x + 100F * Settings.scale, y - 50f * Settings.scale, 0.2F, false));
        }
    }

    @Override
    public String getID() {
        return EnvenomPower.POWER_ID;
    }

    WeakHashMap<AbstractCreature, EnvenomEffect> map = new WeakHashMap<>();

    @Override
    public void onApply(AbstractPower power) {
        EnvenomEffect envenomEffect = new EnvenomEffect();
        map.put(power.owner, envenomEffect);
        Utils.addEffect(envenomEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        if (!map.containsKey(power.owner))
            return;
        EnvenomEffect envenomEffect = map.get(power.owner);
        envenomEffect.isDone = true;
        envenomEffect = null;
    }
}
