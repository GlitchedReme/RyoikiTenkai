package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;

public class FireBreathingEffect extends AbstractGameEffect {
   private float x;
   private float y;
   private int timesUpgraded;

   public FireBreathingEffect(float x, float y, int timesUpgraded) {
      this.x = x;
      this.y = y;
      this.timesUpgraded = timesUpgraded;
      CardCrawlGame.screenShake.shake(ShakeIntensity.HIGH, ShakeDur.SHORT, true);
   }

   public void update() {
      CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F);
      float px = AbstractDungeon.player.drawX;
      float py = AbstractDungeon.player.drawY + 50.0F * Settings.scale;
      float rx = MathUtils.random(-150.0F, 150.0F) * Settings.scale;
      float ry = MathUtils.random(-150.0F, 150.0F) * Settings.scale;
      AbstractDungeon.effectsQueue.add(new RedFireballEffect(px, py, this.x + rx, this.y + ry, this.timesUpgraded));
      this.isDone = true;
   }

   public void render(SpriteBatch sb) {
   }

   public void dispose() {
   }
}