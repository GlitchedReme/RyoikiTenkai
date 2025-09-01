package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Buffer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class BufferPatch {
    @SpirePatch(clz = Buffer.class, method = "use")
    public static class Use {
        public static void Prefix(Buffer $this, AbstractPlayer p, AbstractMonster m) {
            if (!RyoikiTenkai.isEnable(Buffer.ID)) {
                return;
            }
            Utils.addEffect(new DefectLogEffect()
                    .addLog("EXECUTE RedirectDamage();", Color.WHITE.cpy())
                    .addLog("Ablative shielding initialized.", Color.WHITE.cpy())
                    .addLog("Ready to negate one instance of incoming damage.", Color.WHITE.cpy()));
        }
    }
}
