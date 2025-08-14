package sts.ryoikitenkai.vfx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StaticFireballEffect extends AbstractGameEffect {
   private float x;
   private float y;
   private float startX;
   private float startY;
   private float targetX;
   private float targetY;
   private float vfxTimer = 0.0F;
   private int timesUpgraded;

   public StaticFireballEffect(float startX, float startY, float targetX, float targetY, int timesUpgraded) {
      this.startingDuration = 0.3F;
      this.duration = 0.3F;
      this.startX = startX;
      this.startY = startY;
      this.targetX = targetX;
      this.targetY = targetY;
      this.timesUpgraded = timesUpgraded;
      this.x = startX;
      this.y = startY;
   }

   public void update() {
      this.x = Interpolation.fade.apply(this.targetX, this.startX, this.duration / this.startingDuration);
      this.y = Interpolation.fade.apply(this.targetY, this.startY, this.duration / this.startingDuration);

      this.vfxTimer -= Gdx.graphics.getDeltaTime();
      if (this.vfxTimer < 0.0F) {
         this.vfxTimer += 0.016F;
         AbstractDungeon.effectsQueue.add(new StaticFireBurstParticleEffect(this.x, this.y, this.timesUpgraded));
         AbstractDungeon.effectsQueue.add(new StaticFireBurstParticleEffect(this.x, this.y, this.timesUpgraded));
      }

      this.duration -= Gdx.graphics.getDeltaTime();
      if (this.duration < 0.0F) {
         this.isDone = true;
      }

   }

   public void render(SpriteBatch sb) {
   }

   public void dispose() {
   }
}
