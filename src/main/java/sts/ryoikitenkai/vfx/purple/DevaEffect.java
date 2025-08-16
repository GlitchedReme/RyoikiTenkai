package sts.ryoikitenkai.vfx.purple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DevaEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private int index = 0;
    private float timer = 0.0F;

    public boolean isEnd;

    private static TextureAtlas.AtlasRegion img;

    public DevaEffect() {
        this.renderBehind = true;
        img = ImageMaster.EYE_ANIM_0;
        this.x = AbstractDungeon.player.hb.cX - img.packedWidth / 2.0F;
        this.y = AbstractDungeon.player.hb.cY - 80F * Settings.scale - img.packedHeight / 2.0F;
        this.scale = 0.6F * Settings.scale;
        this.color = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void update() {
        this.rotation += 5.0F * Gdx.graphics.getDeltaTime();
        this.timer += Gdx.graphics.getDeltaTime();
        if (!isEnd) {
            this.color.a = MathHelper.slowColorLerpSnap(this.color.a, 1.0F);

            if (this.timer > 0.05F) {
                this.timer -= 0.05F;
                this.index++;
                if (this.index >= 24) {
                    this.index = 24;
                }
            }
        } else {
            this.color.a = MathHelper.slowColorLerpSnap(this.color.a, 0.0F);

            if (this.timer > 0.01F) {
                this.timer -= 0.01F;
                this.index--;

                if (this.index < 0) {
                    this.index = 0;
                    this.isDone = true;
                }
            }
        }
    }

    private void renderHelper(SpriteBatch sb, float offsetX, float offsetY, float rotation, Color color, float scaleMod) {
        sb.setColor(color);
        offsetX *= Settings.scale;
        offsetY *= Settings.scale;
        sb.draw(getImg(this.rotation + rotation + offsetX / 100.0F), this.x, this.y, img.packedWidth / 2.0F - offsetX,
                img.packedHeight / 2.0F - offsetY, img.packedWidth, img.packedHeight, this.scale, this.scale * 2.0F, this.rotation + rotation);
    }

    private TextureAtlas.AtlasRegion getImg(float input) {
        input %= 10.0F;
        if (input < 0.5F)
            return ImageMaster.EYE_ANIM_1;
        if (input < 1.2F)
            return ImageMaster.EYE_ANIM_2;
        if (input < 2.0F)
            return ImageMaster.EYE_ANIM_3;
        if (input < 3.0F)
            return ImageMaster.EYE_ANIM_4;
        if (input < 4.2F)
            return ImageMaster.EYE_ANIM_5;
        if (input < 6.0F)
            return ImageMaster.EYE_ANIM_6;
        if (input < 7.5F)
            return ImageMaster.EYE_ANIM_5;
        if (input < 8.5F)
            return ImageMaster.EYE_ANIM_4;
        if (input < 9.3F)
            return ImageMaster.EYE_ANIM_3;
        return ImageMaster.EYE_ANIM_2;
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        float angle = 0.0F;
        for (int i = 0; i < Math.min(24, index); i++) {
            this.color.r = 0.9F;
            this.color.g = 0.46F + i * 0.01F;
            this.color.b = 0.3F + (12 - i) * 0.05F;
            // renderHelper(sb, -152.0F, -152.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -126.0F, -126.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -102.0F, -102.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -80.0F, -80.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -60.0F, -60.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -44.0F, -44.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -34.0F, -34.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -26.0F, -26.0F, angle, this.color, 1.0F);
            // renderHelper(sb, -20.0F, -20.0F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -172.0F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -142.4F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -115.2F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -90.4F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -68.0F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -49.6F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -38.4F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -29.6F, angle, this.color, 1.0F);
            renderHelper(sb, 0F, -22.4F, angle, this.color, 1.0F);
            angle += 15.0F;
        }
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}