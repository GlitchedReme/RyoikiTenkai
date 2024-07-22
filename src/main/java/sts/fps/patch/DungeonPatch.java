package sts.fps.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sts.fps.renderer.DecalRenderer;
import sts.fps.renderer.FirstPersonRenderer;
import sts.fps.utils.Utils;

public class DungeonPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class DungeonRenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    String name = m.getMethodName();

                    if ("renderCombatRoomBg".equals(name)) {
                        m.replace(Utils.wrapFPSInstrument("RenderCombatBg($1)"));
                    }
                }
            };
        }

        static ReflectionHacks.RMethod renderTargetingUi = ReflectionHacks.privateMethod(AbstractPlayer.class, "renderTargetingUi", SpriteBatch.class);

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert1(AbstractDungeon $this, SpriteBatch sb) {
            FirstPersonRenderer.render3D(sb);

            AbstractPlayer p = AbstractDungeon.player;
            if (p.inSingleTargetMode && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getCurrRoom().isBattleEnding()) {
                renderTargetingUi.invoke(p, sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, matcher)[2]};
            }
        }

        @SpireInsertPatch(rloc = 46)
        public static void Insert2(AbstractDungeon $this, SpriteBatch sb) {
            FirstPersonRenderer.beforeRenderScreen(sb);
        }

        @SpireInsertPatch(rloc = 115)
        public static void Insert3(AbstractDungeon $this, SpriteBatch sb) {
            FirstPersonRenderer.afterRenderScreen(sb);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class DungeonUpdatePatch {
        public static void Prefix(AbstractDungeon $this) {
            DecalRenderer.reset();
            FirstPersonRenderer.update();
        }
    }

//    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class OverlayRenderPatch {
        public static void Prefix(OverlayMenu $this, SpriteBatch sb) {
            FirstPersonRenderer.beforeRenderEndTurnButton(sb);
        }

        @SpireInsertPatch(rloc = 1)
        public static void Insert1(OverlayMenu $this, SpriteBatch sb) {
            FirstPersonRenderer.afterRenderEndTurnButton(sb);
        }

        @SpireInsertPatch(rloc = 5)
        public static void Insert2(OverlayMenu $this, SpriteBatch sb) {
//            FirstPersonRenderer.beforeRenderEnergy(sb);
        }

        @SpireInsertPatch(rloc = 6)
        public static void Insert3(OverlayMenu $this, SpriteBatch sb) {
//            FirstPersonRenderer.afterRenderEnergy(sb);
        }
    }
}
