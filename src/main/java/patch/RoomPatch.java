package patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;

public class RoomPatch {
    @SpirePatch(clz = AbstractRoom.class, method = "render")
    public static class RoomRenderPatch {
        public static void Replace(AbstractRoom $this, SpriteBatch sb) {
            if ($this instanceof EventRoom || $this instanceof VictoryRoom) {
                if ($this.event != null && (!($this.event instanceof AbstractImageEvent) || $this.event.combatTime)) {
                    $this.event.renderRoomEventPanel(sb);
                    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.VICTORY)
                        AbstractDungeon.player.render(sb);
                }
            } else if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.BOSS_REWARD) {
                AbstractDungeon.player.render(sb);
            }
            if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
                if ($this.monsters != null && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH)
                    $this.monsters.render(sb);
                if ($this.phase == AbstractRoom.RoomPhase.COMBAT)
                    AbstractDungeon.player.renderPlayerBattleUi(sb);
                for (AbstractPotion i : $this.potions) {
                    if (!i.isObtained)
                        i.render(sb);
                }
            }
            for (AbstractRelic r : $this.relics)
                r.render(sb);
            $this.renderTips(sb);
        }
    }
}
