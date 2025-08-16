package sts.ryoikitenkai.patch.green;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PostUpdateSubscriber;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.FormParticleEffect;
import sts.ryoikitenkai.vfx.green.WraithFormEffect;
import sts.ryoikitenkai.vfx.red.OrbEffect;

public class WraithFormPatch extends AbstractPowerImpl implements PostUpdateSubscriber {
    public WraithFormPatch() {
        BaseMod.subscribe(this);
    }

    @SpirePatch(clz = DemonForm.class, method = "use")
    public static class Use {
        public static void Prefix(DemonForm $this, AbstractPlayer p, AbstractMonster m) {
            CardCrawlGame.sound.play("NECRONOMICON", -0.5F);
            Color color = new Color(0.3F, 0.3F, 0.3F, 0.4F);
            for (int i = 0; i < 30; i++) {
                Utils.addEffect(new OrbEffect(p.hb.cX, p.hb.cY, i, color));
            }
            Utils.addEffect(new BorderFlashEffect(Color.GRAY, true));
            for (int i = 0; i < 10; i++) {
                AbstractDungeon.effectsQueue.add(new FormParticleEffect(p.hb.cX, color));
            }
        }
    }

    @Override
    public String getID() {
        return IntangiblePlayerPower.POWER_ID;
    }

    WraithFormEffect wraithFormEffect;

    @Override
    public void onApply(AbstractPower power) {
        start = true;
        timer = 0.0F;

        wraithFormEffect = new WraithFormEffect(ReflectionHacks.getPrivate(power.owner, AbstractCreature.class, "skeleton"));
        Utils.addEffect(wraithFormEffect);
    }

    @Override
    public void onRemove(AbstractPower power) {
        color = Color.WHITE.cpy();
        start = false;
        timer = 0.0F;
        wraithFormEffect.isDone = true;
    }

    public static boolean start = false;
    public static float timer = -1.0F;
    public static Color color = Color.WHITE.cpy();
    public static float alpha = 0.4F;

    public static final Color TARGET_COLOR = new Color(0.2F, 0.25F, 0.2F, 0.65F);

    @Override
    public void receivePostUpdate() {
        timer += Gdx.graphics.getDeltaTime();
        if (start) {
            if (timer < 1F) {
                float w = MathUtils.clamp(Interpolation.fade.apply(timer), 0F, 1F);
                Utils.lerp(color, Color.WHITE, TARGET_COLOR, w);
            } else {
                alpha = 0.65F + MathUtils.sinDeg(timer * 50F) * 0.1F;
                color.a = alpha;
            }
        } else {
            if (timer < 1F) {
                float w = MathUtils.clamp(Interpolation.fade.apply(timer), 1F, 0F);
                Utils.lerp(color, Color.WHITE, TARGET_COLOR, w);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class PlayerRenderAtlasColorChanger2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer $this, SpriteBatch sb) {
            if ($this.hasPower(IntangiblePlayerPower.POWER_ID) && $this.hasPower(WraithFormPower.POWER_ID))
                sb.setColor(color);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage")
    @SpirePatch(clz = Watcher.class, method = "renderPlayerImage")
    public static class PlayerRenderSkeletonColorChanger {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer $this, SpriteBatch sb, Skeleton ___skeleton) {
            if ($this.hasPower(IntangiblePlayerPower.POWER_ID) && $this.hasPower(WraithFormPower.POWER_ID))
                ___skeleton.setColor(color);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(Skeleton.class, "setFlip");
                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), matcher);
            }
        }
    }
}
