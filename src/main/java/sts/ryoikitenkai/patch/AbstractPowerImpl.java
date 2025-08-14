package sts.ryoikitenkai.patch;

import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractPowerImpl {
    public abstract String getID();

    public abstract void onApply(AbstractPower power);

    public abstract void onRemove(AbstractPower power);

    public void onStack(AbstractPower power, int stackAmount) {
    }

    public void onReduce(AbstractPower power, int reduceAmount) {
    }
}
