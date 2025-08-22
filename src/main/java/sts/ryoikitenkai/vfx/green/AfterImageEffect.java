package sts.ryoikitenkai.vfx.green;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import basemod.ReflectionHacks;

public class AfterImageEffect extends AbstractGameEffect {

    private float timer;
    private float offset;
    public boolean isEnd;

    private static final Color COLOR1 = new Color(0.678f, 0.922f, 0.678f, 0.6f);
    private static final Color COLOR2 = new Color(1.0f, 1.0f, 0.678f, 0.6f);

    public static FrameBuffer shadowBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);

    public AfterImageEffect() {
        this.renderBehind = true;
    }

    @Override
    public void update() {
        if (!isEnd) {
            timer += Gdx.graphics.getDeltaTime();
            if (timer > 0.4F) {
                timer = 0.4F;
            }
        } else {
            timer -= Gdx.graphics.getDeltaTime();
            if (timer < 0) {
                isDone = true;
            }
        }
        float w = Interpolation.pow2.apply(timer / 0.4F);
        offset = w * 80F * Settings.scale;
    }

    @Override
    public void render(SpriteBatch sb) {
        renderPlayerImage(sb, COLOR1, -offset, 6F * Settings.scale);
        renderPlayerImage(sb, COLOR2, offset, 6F * Settings.scale);
    }

    public void renderPlayerImage(SpriteBatch sb, Color color, float x, float y) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            return;
        }
        if (p.img != null) {
            sb.setColor(color);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            sb.draw(p.img, x + p.drawX - p.img.getWidth() * Settings.scale / 2.0F + p.animX, y + p.drawY,
                    p.img.getWidth() * Settings.scale, p.img.getHeight() * Settings.scale, 0, 0, p.img.getWidth(),
                    p.img.getHeight(), p.flipHorizontal, p.flipVertical);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            sb.end();
            shadowBuffer.begin();
            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glColorMask(true, true, true, true);
            // sb.begin();

            Skeleton skeleton = ReflectionHacks.getPrivate(p, AbstractCreature.class, "skeleton");
            p.state.apply(skeleton);
            skeleton.updateWorldTransform();
            skeleton.setPosition(x + p.drawX + p.animX, y + p.drawY + p.animY);
            // skeleton.setColor(color);
            skeleton.setFlip(p.flipHorizontal, p.flipVertical);
            // sb.end();
            CardCrawlGame.psb.begin();
            CardCrawlGame.psb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            AbstractCreature.sr.draw(CardCrawlGame.psb, skeleton);
            CardCrawlGame.psb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            CardCrawlGame.psb.end();
            // sb.begin();

            // sb.end();
            shadowBuffer.end();
            sb.begin();

            sb.setColor(color);
            sb.draw(shadowBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);
        }
    }

    @Override
    public void dispose() {

    }
}
