package sts.ryoikitenkai.vfx.red;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class CorruptionEffect extends AbstractGameEffect {
    public Skeleton skeleton;
    public ArrayList<Bone> eyes = new ArrayList<>();
    private float timer;

    public static final List<String> EYE_NAMES = Arrays.asList("Eye", "eye_anchor", "Eye_down", "Eye_up");

    public CorruptionEffect(Skeleton skeleton) {
        if (skeleton == null) {
            this.isDone = true;
            return;
        }

        this.color = new Color(0.5f, 0.0f, 0.5f, 1.0f);

        this.skeleton = skeleton;
        for (String eyeName : EYE_NAMES) {
            Bone eye = skeleton.findBone(eyeName);
            if (eye != null) {
                eyes.add(eye);
            }
        }
    }

    @Override
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        if (timer > 0.1f) {
            for (Bone eye : eyes) {
                float x = eye.getWorldX() + skeleton.getX();
                float y = eye.getWorldY() + skeleton.getY();
                AbstractDungeon.effectsQueue.add(new CorruptionParticleEffect(x, y));
            }
            timer = 0.0f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {
        // Clean up resources
    }
}
