package sts.ryoikitenkai.modcore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.blue.BiasedCognition;
import com.megacrit.cardcrawl.cards.blue.Buffer;
import com.megacrit.cardcrawl.cards.blue.Capacitor;
import com.megacrit.cardcrawl.cards.blue.CreativeAI;
import com.megacrit.cardcrawl.cards.blue.Defragment;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.cards.blue.Electrodynamics;
import com.megacrit.cardcrawl.cards.blue.Heatsinks;
import com.megacrit.cardcrawl.cards.blue.HelloWorld;
import com.megacrit.cardcrawl.cards.blue.Loop;
import com.megacrit.cardcrawl.cards.blue.MachineLearning;
import com.megacrit.cardcrawl.cards.blue.SelfRepair;
import com.megacrit.cardcrawl.cards.blue.StaticDischarge;
import com.megacrit.cardcrawl.cards.blue.Storm;
import com.megacrit.cardcrawl.cards.green.Accuracy;
import com.megacrit.cardcrawl.cards.green.AfterImage;
import com.megacrit.cardcrawl.cards.green.Caltrops;
import com.megacrit.cardcrawl.cards.green.AThousandCuts;
import com.megacrit.cardcrawl.cards.green.Envenom;
import com.megacrit.cardcrawl.cards.green.Footwork;
import com.megacrit.cardcrawl.cards.green.InfiniteBlades;
import com.megacrit.cardcrawl.cards.green.NoxiousFumes;
import com.megacrit.cardcrawl.cards.green.ToolsOfTheTrade;
import com.megacrit.cardcrawl.cards.green.WellLaidPlans;
import com.megacrit.cardcrawl.cards.green.WraithForm;
import com.megacrit.cardcrawl.cards.purple.DevaForm;
import com.megacrit.cardcrawl.cards.purple.Devotion;
import com.megacrit.cardcrawl.cards.red.Barricade;
import com.megacrit.cardcrawl.cards.red.Berserk;
import com.megacrit.cardcrawl.cards.red.Brutality;
import com.megacrit.cardcrawl.cards.red.Combust;
import com.megacrit.cardcrawl.cards.red.Corruption;
import com.megacrit.cardcrawl.cards.red.DarkEmbrace;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import com.megacrit.cardcrawl.cards.red.Evolve;
import com.megacrit.cardcrawl.cards.red.FeelNoPain;
import com.megacrit.cardcrawl.cards.red.FireBreathing;
import com.megacrit.cardcrawl.cards.red.Inflame;
import com.megacrit.cardcrawl.cards.red.Juggernaut;
import com.megacrit.cardcrawl.cards.red.Metallicize;
import com.megacrit.cardcrawl.cards.red.Rupture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.DevaPower;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModMinMaxSlider;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import sts.ryoikitenkai.patch.AbstractPowerImpl;

@SpireInitializer
public class RyoikiTenkai implements PostInitializeSubscriber, PostUpdateSubscriber, EditStringsSubscriber {
    public static final String MOD_ID = "RyoikiTenkai";
    public static final HashMap<String, AbstractPowerImpl> powerImpls = new HashMap<>();
    public static final ArrayList<String> disabledCardIDs = new ArrayList<>();
    public static float TIMER = 0F;

    public static SpireConfig config;

    public static void initialize() {
        BaseMod.subscribe(new RyoikiTenkai());
    }

    public static boolean isPowerEnabled(String id) {
        if (!powerImpls.containsKey(id)) {
            return false;
        }

        String cardID = cardPowerPairs.get(id);
        return cardID == null || isEnable(cardID);
    }

    public static boolean isEnable(String id) {
        return !disabledCardIDs.contains(id);
    }

    @Override
    public void receiveEditStrings() {
        String lang = "eng";

        if (Settings.language == GameLanguage.ZHS || Settings.language == GameLanguage.ZHT) {
            lang = "zh";
        }

        BaseMod.loadCustomStringsFile(UIStrings.class, "RyoikiTenkaiResources/locales/UI_" + lang + ".json");
    }

    @Override
    public void receivePostInitialize() {
        new AutoAdd(MOD_ID)
                .packageFilter("sts.ryoikitenkai.patch")
                .any(AbstractPowerImpl.class, (info, instance) -> {
                    powerImpls.put(((AbstractPowerImpl) instance).getID(), (AbstractPowerImpl) instance);
                });

        ShaderManager.init();

        try {
            config = new SpireConfig("ryoikitenkai", "common");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config != null) {
            for (String id : doneCards) {
                if (config.has(id) && !config.getBool(id)) {
                    disabledCardIDs.add(id);
                }
            }
        }

        String[] text = CardCrawlGame.languagePack.getUIString("RyoikiTenkai:UI").TEXT;

        ModPanel settingsPanel = new ModPanel();

        ModLabel topLabel = new ModLabel(text[0], Settings.WIDTH / 2.0F, Settings.HEIGHT - 400.0F * Settings.scale, settingsPanel, (label) -> {
        });

        SettingsPanel settingPanel = new SettingsPanel(settingsPanel);

        ModMinMaxSlider slider = new ModMinMaxSlider("", Settings.WIDTH / 2.0F - 100f * Settings.scale, 300f * Settings.scale, 0, (settingPanel.col - 1) * 200 * Settings.scale, 0, "", settingsPanel, (value) -> {
            settingPanel.sliderX = value.getValue();
        });

        settingsPanel.addUIElement(topLabel);
        settingsPanel.addUIElement(slider);
        settingsPanel.addUIElement(settingPanel);

        BaseMod.registerModBadge(ImageMaster.WHITE_SQUARE_IMG, "Ryoiki Tenkai", "Reme", "", settingsPanel);
    }

    @Override
    public void receivePostUpdate() {
        TIMER += Gdx.graphics.getDeltaTime();
    }

    static final HashMap<String, String> cardPowerPairs = new HashMap<>();
    static final ArrayList<String> doneCards = new ArrayList<>();

    static {
        // Blue
        cardPowerPairs.put(BufferPower.POWER_ID, Buffer.ID);
        cardPowerPairs.put(CreativeAIPower.POWER_ID, CreativeAI.ID);
        cardPowerPairs.put(EchoPower.POWER_ID, EchoForm.ID);
        cardPowerPairs.put(ElectroPower.POWER_ID, Electrodynamics.ID);
        cardPowerPairs.put(HeatsinkPower.POWER_ID, Heatsinks.ID);
        cardPowerPairs.put(LoopPower.POWER_ID, Loop.ID);
        cardPowerPairs.put(DrawPower.POWER_ID, MachineLearning.ID);
        cardPowerPairs.put(RepairPower.POWER_ID, SelfRepair.ID);
        cardPowerPairs.put(StaticDischargePower.POWER_ID, StaticDischarge.ID);
        cardPowerPairs.put(StormPower.POWER_ID, Storm.ID);
        cardPowerPairs.put(FocusPower.POWER_ID, Defragment.ID);
        // Red
        cardPowerPairs.put(DemonFormPower.POWER_ID, DemonForm.ID);
        cardPowerPairs.put(BarricadePower.POWER_ID, Barricade.ID);
        cardPowerPairs.put(CorruptionPower.POWER_ID, Corruption.ID);
        cardPowerPairs.put(BerserkPower.POWER_ID, Berserk.ID);
        cardPowerPairs.put(BrutalityPower.POWER_ID, Brutality.ID);
        cardPowerPairs.put(CombustPower.POWER_ID, Combust.ID);
        cardPowerPairs.put(DarkEmbracePower.POWER_ID, DarkEmbrace.ID);
        cardPowerPairs.put(EvolvePower.POWER_ID, Evolve.ID);
        cardPowerPairs.put(FeelNoPainPower.POWER_ID, FeelNoPain.ID);
        cardPowerPairs.put(FireBreathingPower.POWER_ID, FireBreathing.ID);
        cardPowerPairs.put(JuggernautPower.POWER_ID, Juggernaut.ID);
        cardPowerPairs.put(MetallicizePower.POWER_ID, Metallicize.ID);
        cardPowerPairs.put(RupturePower.POWER_ID, Rupture.ID);
        cardPowerPairs.put(StrengthPower.POWER_ID, Inflame.ID);
        // Green
        cardPowerPairs.put(WraithFormPower.POWER_ID, WraithForm.ID);
        cardPowerPairs.put(NoxiousFumesPower.POWER_ID, NoxiousFumes.ID);
        cardPowerPairs.put(AccuracyPower.POWER_ID, Accuracy.ID);
        cardPowerPairs.put(AfterImagePower.POWER_ID, AfterImage.ID);
        cardPowerPairs.put(ThousandCutsPower.POWER_ID, AThousandCuts.ID);
        cardPowerPairs.put(EnvenomPower.POWER_ID, Envenom.ID);
        cardPowerPairs.put(InfiniteBladesPower.POWER_ID, InfiniteBlades.ID);
        cardPowerPairs.put(ToolsOfTheTradePower.POWER_ID, ToolsOfTheTrade.ID);
        cardPowerPairs.put(RetainCardPower.POWER_ID, WellLaidPlans.ID);
        cardPowerPairs.put(DexterityPower.POWER_ID, Footwork.ID);
        // Purple
        cardPowerPairs.put(DevaPower.POWER_ID, DevaForm.ID);
        cardPowerPairs.put(MantraPower.POWER_ID, Devotion.ID);

        // Done Cards
        // Blue
        doneCards.add(BiasedCognition.ID);
        doneCards.add(Buffer.ID);
        doneCards.add(Capacitor.ID);
        doneCards.add(CreativeAI.ID);
        doneCards.add(Defragment.ID);
        doneCards.add(EchoForm.ID);
        doneCards.add(Electrodynamics.ID);
        doneCards.add(Heatsinks.ID);
        doneCards.add(HelloWorld.ID);
        doneCards.add(Loop.ID);
        doneCards.add(MachineLearning.ID);
        doneCards.add(SelfRepair.ID);
        doneCards.add(StaticDischarge.ID);
        doneCards.add(Storm.ID);
        // Red
        doneCards.add(DemonForm.ID);
        doneCards.add(Barricade.ID);
        doneCards.add(Corruption.ID);
        doneCards.add(Berserk.ID);
        doneCards.add(Brutality.ID);
        doneCards.add(Combust.ID);
        doneCards.add(DarkEmbrace.ID);
        doneCards.add(FeelNoPain.ID);
        doneCards.add(FireBreathing.ID);
        doneCards.add(Juggernaut.ID);
        doneCards.add(Rupture.ID);
        doneCards.add(Inflame.ID);
        // Green
        doneCards.add(WraithForm.ID);
        doneCards.add(NoxiousFumes.ID);
        doneCards.add(AfterImage.ID);
        doneCards.add(AThousandCuts.ID);
        doneCards.add(Envenom.ID);
        doneCards.add(WellLaidPlans.ID);
        doneCards.add(Caltrops.ID);
        doneCards.add(Footwork.ID);
        // Purple
        doneCards.add(DevaForm.ID);
        doneCards.add(Devotion.ID);
    }
}
