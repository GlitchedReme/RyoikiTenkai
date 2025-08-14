package sts.fps.modcore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer
public class RyoikiTenkai implements PostInitializeSubscriber {

    public static void initialize() {
        BaseMod.subscribe(new RyoikiTenkai());
    }

    @Override
    public void receivePostInitialize() {
    }
}
