package patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import renderer.DecalRenderer;
import renderer.FirstPersonRenderer;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class DungeonPatch {
    public static final Logger logger = LogManager.getLogger(AbstractDungeon.class.getName());

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class DungeonRenderPatch {
        public static void Replace(AbstractDungeon $this, SpriteBatch sb) {
            DecalRenderer.reset();
            renderRoom($this, sb);
            renderUI($this, sb);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class DungeonUpdatePatch {
        public static void Prefix(AbstractDungeon $this) {
            FirstPersonRenderer.update();
        }
    }

    public static void renderRoom(AbstractDungeon $this, SpriteBatch sb) {
        FirstPersonRenderer.renderRoomBg(sb);
        currMapNode.room.render(sb);
        FirstPersonRenderer.renderRoomFg(sb);
    }

    public static void renderUI(AbstractDungeon $this, SpriteBatch sb) {
        AbstractRoom room = getCurrRoom();
        if (room instanceof EventRoom || room instanceof NeowRoom || room instanceof VictoryRoom)
            room.renderEventTexts(sb);
        for (AbstractGameEffect e : effectList) {
            e.render(sb);
        }
        overlayMenu.render(sb);
        overlayMenu.renderBlackScreen(sb);

        FirstPersonRenderer.renderScreen(sb);

        FirstPersonRenderer.render3D(sb);

        AbstractPlayer p = player;
        if (p.inSingleTargetMode && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getCurrRoom().isBattleEnding()) {
            ReflectionHacks.privateMethod(AbstractPlayer.class, "renderTargetingUi", SpriteBatch.class)
                    .invoke(p, sb);
        }

        if (screen != CurrentScreen.UNLOCK) {
            sb.setColor(topGradientColor);
            if (!Settings.hideTopBar)
                sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, Settings.HEIGHT - 128.0F * Settings.scale, Settings.WIDTH, 64.0F * Settings.scale);
            sb.setColor(botGradientColor);
            if (!Settings.hideTopBar)
                sb.draw(ImageMaster.SCROLL_GRADIENT, 0.0F, 64.0F * Settings.scale, Settings.WIDTH, -64.0F * Settings.scale);
        }
        if (screen == CurrentScreen.FTUE)
            ftue.render(sb);
        overlayMenu.cancelButton.render(sb);
        dynamicBanner.render(sb);
        if (screen != CurrentScreen.UNLOCK)
            topPanel.render(sb);
        currMapNode.room.renderAboveTopPanel(sb);
        for (AbstractGameEffect e : topLevelEffects) {
            if (!e.renderBehind)
                e.render(sb);
        }
        sb.setColor(fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
    }

    public static void foldFuckingSwitch(SpriteBatch sb) {
        switch (screen) {
            case NO_INTERACT:
            case FTUE:
                break;
            case NONE:
            case MAP:
                dungeonMapScreen.render(sb);
                break;
            case MASTER_DECK_VIEW:
                deckViewScreen.render(sb);
                break;
            case GAME_DECK_VIEW:
                gameDeckViewScreen.render(sb);
                break;
            case DISCARD_VIEW:
                discardPileViewScreen.render(sb);
                break;
            case EXHAUST_VIEW:
                exhaustPileViewScreen.render(sb);
                break;
            case SETTINGS:
                settingsScreen.render(sb);
                break;
            case INPUT_SETTINGS:
                inputSettingsScreen.render(sb);
                break;
            case GRID:
                gridSelectScreen.render(sb);
                break;
            case CARD_REWARD:
                cardRewardScreen.render(sb);
                break;
            case COMBAT_REWARD:
                combatRewardScreen.render(sb);
                break;
            case BOSS_REWARD:
                bossRelicScreen.render(sb);
                break;
            case HAND_SELECT:
                handCardSelectScreen.render(sb);
                break;
            case SHOP:
                shopScreen.render(sb);
                break;
            case DEATH:
                deathScreen.render(sb);
                break;
            case VICTORY:
                victoryScreen.render(sb);
                break;
            case UNLOCK:
                unlockScreen.render(sb);
                break;
            case NEOW_UNLOCK:
                gUnlockScreen.render(sb);
                break;
            case CREDITS:
                creditsScreen.render(sb);
                break;
            case DOOR_UNLOCK:
                CardCrawlGame.mainMenuScreen.doorUnlockScreen.render(sb);
                break;
            default:
                logger.info("ERROR: UNKNOWN SCREEN TO RENDER: {}", screen.name());
        }
    }
}
