package sts.fps.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sts.fps.renderer.FirstPersonRenderer;
import sts.fps.utils.Utils;

import java.util.UUID;

public class CharacterPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "updateInput")
    public static class MonsterHbUpdatePatch1 {
        @SpireInsertPatch(rloc = 15)
        public static void Insert1(AbstractPlayer $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }

        @SpireInsertPatch(rloc = 23)
        public static void Insert2(AbstractPlayer $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "updateSingleTargetInput")
    public static class MonsterHbUpdatePatch2 {
        @SpireInsertPatch(rloc = 7)
        public static void Insert1(AbstractPlayer $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }

        @SpireInsertPatch(rloc = 28)
        public static void Insert2(AbstractPlayer $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHoverReticle")
    public static class MonsterHbRenderPatch {
        public static void Prefix(AbstractPlayer $this, SpriteBatch sb) {
            if (AbstractDungeon.getMonsters() == null) return;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }

        public static void Postfix(AbstractPlayer $this, SpriteBatch sb) {
            if (AbstractDungeon.getMonsters() == null) return;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class PlayerRenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean first = true;
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    String name = m.getMethodName();
                    if ("renderHealth".equals(name)) {
                        m.replace(Utils.wrapFPSInstrument("RenderPlayerHealth($1, $0)"));
                    }

                    if ("draw".equals(name) || "renderPlayerImage".equals(name)) {
                        m.replace("{if(false){$_=$proceed($$);}}");
                    }

                    if ("setColor".equals(name) && first) {
                        first = false;
                        m.replace("{if(false){$_=$proceed($$);}}");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class HandRenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    String name = m.getMethodName();
                    if ("renderTargetingUi".equals(name)) {
                        m.replace("{if(false){$_=$proceed($$);}}");
                    }

                    if ("render".equals(name) && AbstractCard.class.getName().equals(m.getClassName())) {
                        m.replace(Utils.wrapFPSInstrument("RenderHandCard($1, $0)"));
                    }
                }
            };
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "renderHand")
    public static class HandRenderPatch2 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean inHand = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("render".equals(m.getMethodName())) {
                        if (inHand) {
                            inHand = false;
                            m.replace(Utils.wrapFPSInstrument("RenderHandCard($1, $0)"));
                        } else {
                            m.replace(Utils.wrapFPSInstrument("RenderHandCard($1, $0)"));
                        }
                    }
                }
            };
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "update")
    public static class MonsterUpdatePatch {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(MonsterGroup $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }

        public static void Postfix(MonsterGroup $this) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                FirstPersonRenderer.updateEnemyHitbox(m);
            }
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "render")
    public static class MonsterRenderPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    String name = m.getMethodName();
                    if ("render".equals(name)) {
                        m.replace(Utils.wrapFPSInstrument("RenderEnemy($1, $0)"));
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class AbstractMonsterFieldPatch {
        public static SpireField<UUID> uuid = new SpireField<>(UUID::randomUUID);

        public static SpireField<Vector2> hbMin = new SpireField<>(Vector2::new);
        public static SpireField<Vector2> hbMax = new SpireField<>(Vector2::new);
    }
}