package sts.ryoikitenkai.modcore;

import java.util.HashMap;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import sts.ryoikitenkai.patch.AbstractPowerImpl;

@SpireInitializer
public class RyoikiTenkai implements PostInitializeSubscriber {
    public static final String MOD_ID = "RyoikiTenkai";
    public static final HashMap<String, AbstractPowerImpl> powerImpls = new HashMap<>();

    public static void initialize() {
        BaseMod.subscribe(new RyoikiTenkai());
    }

    @Override
    public void receivePostInitialize() {
        new AutoAdd(MOD_ID)
                .packageFilter("sts.ryoikitenkai.patch")
                .any(AbstractPowerImpl.class, (info, instance) -> {
                    powerImpls.put(((AbstractPowerImpl) instance).getID(), (AbstractPowerImpl) instance);
                });

        ShaderManager.init();
    }
}
