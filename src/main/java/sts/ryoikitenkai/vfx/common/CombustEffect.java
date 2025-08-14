package sts.ryoikitenkai.vfx.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CombustEffect extends AbstractGameEffect {
   private int amount;

   public CombustEffect(int amount) {
      this.amount = amount;
      CardCrawlGame.screenShake.shake(ShakeIntensity.HIGH, ShakeDur.SHORT, true);
   }

   public void update() {
      CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F);
      float px = AbstractDungeon.player.drawX;
      float py = AbstractDungeon.player.drawY + 50.0F * Settings.scale;

      float randomAngle = MathUtils.random(0, 360);

      for (int i = 0; i < 24; i++) {
         float dx = MathUtils.cosDeg(randomAngle + i * 15) * Settings.WIDTH * Settings.scale;
         float dy = MathUtils.sinDeg(randomAngle + i * 15) * Settings.WIDTH * Settings.scale;
         float rx = MathUtils.random(-15.0F, 15.0F) * Settings.scale;
         float ry = MathUtils.random(-15.0F, 15.0F) * Settings.scale;

         AbstractDungeon.effectsQueue.add(new StaticFireballEffect(px + rx, py + ry, px + dx + rx, py + dy + ry, this.amount));
      }

      this.isDone = true;
   }

   public void render(SpriteBatch sb) {
   }

   public void dispose() {
   }
}