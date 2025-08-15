package sts.ryoikitenkai.patch.red;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CorruptionPower;

import basemod.ReflectionHacks;
import sts.ryoikitenkai.modcore.RyoikiTenkai;
import sts.ryoikitenkai.modcore.ShaderManager;
import sts.ryoikitenkai.patch.AbstractPowerImpl;
import sts.ryoikitenkai.utils.Utils;
import sts.ryoikitenkai.vfx.red.CorruptionEffect;

public class CorruptionPatch extends AbstractPowerImpl {
    @Override
    public String getID() {
        return CorruptionPower.POWER_ID;
    }

    CorruptionEffect corruptionEffect;
    public static boolean isApplied;

    @Override
    public void onApply(AbstractPower power) {
        corruptionEffect = new CorruptionEffect(ReflectionHacks.getPrivate(power.owner, AbstractCreature.class, "skeleton"));
        Utils.addEffect(corruptionEffect);
        isApplied = true;
    }

    @Override
    public void onRemove(AbstractPower power) {
        corruptionEffect.isDone = true;
        corruptionEffect = null;
        isApplied = false;
    }

    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = { SpriteBatch.class })
    public static class EnergyRenderPatch {

        public static boolean corrupt(AbstractCard card) {
            return CardCrawlGame.isInARun() && card.type == CardType.SKILL && AbstractDungeon.player != null
                    && AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID);
        }

        @SpirePrefixPatch
        public static void Prefix(AbstractCard $this, SpriteBatch sb) {
            if (!corrupt($this))
                return;

            ShaderProgram shader = ShaderManager.mist.getShaderProgram();
            sb.setShader(shader);

            shader.setUniformi(ShaderManager.mistNoise, 1);
            shader.setUniformf(ShaderManager.mistTime, RyoikiTenkai.TIMER);
            shader.setUniformf(ShaderManager.mistResolution, Settings.WIDTH, Settings.HEIGHT);
            shader.setUniformf(ShaderManager.mistCenterPosition, $this.current_x, $this.current_y);
            ShaderManager.noiseTexture.bind(1);
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractCard $this, SpriteBatch sb) {
            if (!corrupt($this))
                return;
            sb.setShader(null);
        }
    }
}
