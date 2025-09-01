package sts.ryoikitenkai.patch.green;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.green.NoxiousFumes;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.green.NoxiousFumesEffect;

public class NoxiousFumesPatch {
    @SpirePatch(clz = NoxiousFumes.class, method = "use")
    public static class Use {
        public static void Prefix(NoxiousFumes $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(NoxiousFumes.ID)) {
                return;
            }
            float y = AbstractDungeon.floorY;
            Utils.addEffect(new NoxiousFumesEffect(0, y - 65f * Settings.scale, Settings.WIDTH * 0.8F, y - 50f * Settings.scale, 1.2F, false));
            Utils.addEffect(
                    new NoxiousFumesEffect(Settings.WIDTH, y + 40f * Settings.scale, Settings.WIDTH * 0.2F, y + 30f * Settings.scale, 1.5F, true));
        }
    }

    @SpirePatch(clz = NoxiousFumesPower.class, method = "atStartOfTurnPostDraw")
    public static class Power {
        @SpireInsertPatch(rloc = 2)
        public static void Insert(NoxiousFumesPower $this) {
            if (!RyoikiTenkai.isEnable(NoxiousFumes.ID)) {
                return;
            }
            float y = AbstractDungeon.floorY + 20f * Settings.scale;
            Utils.addEffect(new NoxiousFumesEffect(Settings.WIDTH * 0.5F, y, Settings.WIDTH, y, 0.5F, false));
        }
    }
}
