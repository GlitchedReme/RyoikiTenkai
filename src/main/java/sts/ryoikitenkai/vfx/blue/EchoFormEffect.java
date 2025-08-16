package sts.ryoikitenkai.vfx.blue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EchoFormEffect extends AbstractGameEffect {
    public Skeleton skeleton;
    public ArrayList<Bone> bones = new ArrayList<>();
    private float timer;

    public static final List<String> BONES = Arrays.asList(
            "Arm_R_3",
            "Arm_L_3",
            "hand_left",
            "hand_right",
            "Hand_L",
            "hand");

    public EchoFormEffect(Skeleton skeleton) {
        if (skeleton == null) {
            this.isDone = true;
            return;
        }

        this.skeleton = skeleton;
        for (String name : BONES) {
            Bone chest = skeleton.findBone(name);
            if (chest != null) {
                bones.add(chest);
            }
        }

        if (bones.isEmpty()) {
            this.isDone = true;
        }
    }

    @Override
    public void update() {
        timer += Gdx.graphics.getDeltaTime();
        if (timer > 0.1f) {
            for (Bone bone : bones) {
                float x = bone.getWorldX() + skeleton.getX();
                float y = bone.getWorldY() + skeleton.getY();
                AbstractDungeon.effectsQueue.add(new EchoParticleEffect(x, y));
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
