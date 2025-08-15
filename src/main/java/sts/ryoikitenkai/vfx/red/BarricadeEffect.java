package sts.ryoikitenkai.vfx.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BarricadeEffect extends AbstractGameEffect {
    private float cX;
    private float cY;

    private float a;
    private Vector2 p;
    private float rotation;

    private float scaleX;

    public float timer;
    public boolean isEnd;

    private static final float B = 100.0F;

    private static AtlasRegion IMG = new AtlasRegion(ImageMaster.BLOCK_ICON, 0, 0, 64, 64);

    public BarricadeEffect(float x, float y, float angle) {
        this.p = new Vector2();
        this.cX = x;
        this.cY = y;
        this.a = angle;
        this.scaleX = 1.0F;
        this.color = new Color(0.6F, 0.93F, 0.98F, 0.0F);
    }

    private float getR(float a) {
        return (float) (B / Math.sqrt(1f - 0.75f * Math.pow(MathUtils.cosDeg(a), 2)));
    }

    @Override
    public void update() {
        this.a += Gdx.graphics.getDeltaTime() * 60.0F;
        if (this.a >= 360.0F)
            this.a -= 360.0F;
        float r = getR(this.a);

        this.p.x = r * (float) MathUtils.cosDeg(this.a);
        this.p.y = r * (float) MathUtils.sinDeg(this.a);

        this.rotation = 30.0F * MathUtils.cosDeg(this.a);

        this.renderBehind = this.p.y > 0.0F;
        this.scaleX = this.p.y / -B;
        if (this.scaleX == 0.0F)
            this.scaleX = 0.01F;

        if (isEnd) {
            this.timer -= Gdx.graphics.getDeltaTime();
            if (this.timer <= 0.0F) {
                this.isEnd = false;
                this.color.a = this.timer / 0.5F;
            }
        } else {
            this.timer += Gdx.graphics.getDeltaTime();
            if (this.timer >= 0.5F) {
                this.timer = 0.5F;
            }
            this.color.a = this.timer / 0.5F;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(IMG, this.cX + p.x - IMG.packedWidth / 2.0F, this.cY + p.y - IMG.packedHeight / 2.0F, IMG.packedWidth / 2F, IMG.packedHeight / 2F,
                128, 128, 0.8F * scaleX, 0.8F, rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {
    }
}