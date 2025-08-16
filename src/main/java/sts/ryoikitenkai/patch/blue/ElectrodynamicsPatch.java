package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Electrodynamics;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect;

import basemod.Pair;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class ElectrodynamicsPatch {
    @SpirePatch(clz = Electrodynamics.class, method = "use")
    public static class Use {
        public static void Prefix(Electrodynamics $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new DefectLogEffect()
                    .addLog(
                            new Pair<>("LOAD DRIVER: ", Color.WHITE.cpy()),
                            new Pair<>("Electrodynamics.sys", Color.LIME.cpy()))
                    .addLog("Arc protocol engaged.", Color.WHITE.cpy())
                    .addLog("All lightning nodes are now networked.", Color.WHITE.cpy()));
        }
    }

    @SpirePatch(clz = Lightning.class, method = "updateAnimation")
    public static class LightningPatch {
        @SpireInsertPatch(rloc = 4)
        public static void Insert(Lightning $this) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p != null && p.hasPower(ElectroPower.POWER_ID)) {
                for (int i = 0; i < MathUtils.random(0, 3); i++) {
                    AbstractDungeon.effectList.add(new LightningOrbPassiveEffect($this.cX, $this.cY));
                }
            }
        }
    }
}
