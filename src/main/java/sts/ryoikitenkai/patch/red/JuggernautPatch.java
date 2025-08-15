package sts.ryoikitenkai.patch.red;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.red.Juggernaut;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.red.JuggernautEffect;

public class JuggernautPatch {
    @SpirePatch(clz = Juggernaut.class, method = "use")
    public static class Use {
        public static void Prefix(Juggernaut $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new JuggernautEffect(p.hb.cX, p.hb.cY));
        }
    }

    @SpirePatch(clz = JuggernautPower.class, method = "onGainedBlock")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(JuggernautPower $this, float blockAmount) {
            Utils.addEffect(new JuggernautEffect($this.owner.hb.cX, $this.owner.drawY));
        }
    }
}
