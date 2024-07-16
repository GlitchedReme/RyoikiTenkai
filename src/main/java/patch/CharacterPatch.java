package patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
import renderer.FirstPersonRenderer;

import java.util.ArrayList;

public class CharacterPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class PlayerRenderPatch {
        public static void Replace(AbstractPlayer $this, SpriteBatch sb) {
            $this.stance.render(sb);
            if (((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT ||
                    AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !$this.isDead) {
                FirstPersonRenderer.renderPlayerHpBar($this, sb);
                if (!$this.orbs.isEmpty())
                    for (AbstractOrb o : $this.orbs)
                        o.render(sb);
            }
            if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
                $this.hb.render(sb);
                $this.healthHb.render(sb);
            } else {
                sb.setColor(Color.WHITE);
                $this.renderShoulderImg(sb);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class HandRenderPatch {
        public static SpireReturn<Void> Prefix(AbstractPlayer $this, SpriteBatch sb, AbstractMonster ___hoveredMonster) {
//            if (Settings.SHOW_CARD_HOTKEYS) {
//                $this.renderCardHotKeyText(sb);
//            }

            if ($this.inspectMode && $this.inspectHb != null) {
                $this.renderReticle(sb, $this.inspectHb);
            }

            if ($this.hoveredCard != null) {
                int aliveMonsters = 0;
                $this.hand.renderHand(sb, $this.hoveredCard);
                $this.hoveredCard.renderHoverShadow(sb);
                if (($this.isDraggingCard || $this.inSingleTargetMode) && $this.isHoveringDropZone) {
                    if ($this.isDraggingCard && !$this.inSingleTargetMode) {
                        AbstractMonster theMonster = null;

                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                            if (!m.isDying && m.currentHealth > 0) {
                                ++aliveMonsters;
                                theMonster = m;
                            }
                        }

                        if (aliveMonsters == 1 && ___hoveredMonster == null) {
                            $this.hoveredCard.calculateCardDamage(theMonster);
                            $this.hoveredCard.render(sb);
                            $this.hoveredCard.applyPowers();
                        } else {
                            $this.hoveredCard.render(sb);
                        }
                    }

                    if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
                        ReflectionHacks.privateMethod(AbstractPlayer.class, "renderHoverReticle", SpriteBatch.class)
                                .invoke($this, sb);
                    }
                }

                if (___hoveredMonster != null) {
                    $this.hoveredCard.calculateCardDamage(___hoveredMonster);
                    $this.hoveredCard.render(sb);
                    $this.hoveredCard.applyPowers();
                } else if (aliveMonsters != 1) {
                    $this.hoveredCard.render(sb);
                }
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
                $this.hand.render(sb);
            } else {
                $this.hand.renderHand(sb, $this.cardInUse);
            }

            if ($this.cardInUse != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT && !PeekButton.isPeeking) {
                $this.cardInUse.render(sb);
                if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                    AbstractDungeon.effectList.add(new CardDisappearEffect($this.cardInUse.makeCopy(), $this.cardInUse.current_x, $this.cardInUse.current_y));
                    $this.cardInUse = null;
                }
            }

            $this.limbo.render(sb);
//            if ($this.inSingleTargetMode && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getCurrRoom().isBattleEnding()) {
//                $this.renderTargetingUi(sb);
//            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "renderHand")
    public static class HandRenderPatch2 {
        public static SpireReturn<Void> Prefix(CardGroup $this, SpriteBatch sb, AbstractCard exceptThis) {
            ArrayList<AbstractCard> queued = ReflectionHacks.getPrivate($this, CardGroup.class, "queued");
            ArrayList<AbstractCard> inHand = ReflectionHacks.getPrivate($this, CardGroup.class, "inHand");
            for (AbstractCard c : $this.group) {
                if (c == exceptThis) continue;
                boolean inQueue = AbstractDungeon.actionManager.cardQueue.stream()
                        .anyMatch(i -> i.card != null && i.card.equals(c));
                if (inQueue) {
                    queued.add(c);
                } else {
                    inHand.add(c);
                }
            }

            FirstPersonRenderer.renderHand(queued, inHand, sb);

            inHand.clear();
            queued.clear();

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = MonsterGroup.class, method = "render")
    public static class MonsterRenderPatch {
        public static void Replace(MonsterGroup $this, SpriteBatch sb) {
            if ($this.hoveredMonster != null && !$this.hoveredMonster.isDead && !$this.hoveredMonster.escaped && AbstractDungeon.player.hoverEnemyWaitTimer < 0.0F && (!AbstractDungeon.isScreenUp || PeekButton.isPeeking)) {
                $this.hoveredMonster.renderTip(sb);
            }

            FirstPersonRenderer.renderEnemies($this, sb);
        }
    }
}
