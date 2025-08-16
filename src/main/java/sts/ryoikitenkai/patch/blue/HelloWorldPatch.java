package sts.ryoikitenkai.patch.blue;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.HelloWorld;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.Pair;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.blue.DefectLogEffect;

public class HelloWorldPatch {
    @SpirePatch(clz = HelloWorld.class, method = "use")
    public static class Use {
        public static void Prefix(HelloWorld $this, AbstractPlayer p, AbstractMonster m) {
            Utils.addEffect(new DefectLogEffect()
                    .addLog(new Pair<>("RUN: ", Color.WHITE.cpy()),
                            new Pair<>("\"HelloWorld.exe\"", Color.GOLD.cpy()))
                    .addLog("Core functionalities initialized.", Color.WHITE.cpy())
                    .addLog("Interfacing with external data feed.", Color.WHITE.cpy()));
        }
    }
}
