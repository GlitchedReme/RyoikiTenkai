package sts.ryoikitenkai.modcore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import basemod.IUIElement;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;

public class SettingsPanel implements IUIElement {
    public float sliderX = 0f;
    public int col = 0;

    ArrayList<ModLabeledToggleButton> toggleButtons = new ArrayList<>();
    public HashMap<ModLabeledToggleButton, Float> originalX = new HashMap<>();

    public SettingsPanel(ModPanel settingsPanel) {
        int count = 0;
        int col = 0;
        for (String id : RyoikiTenkai.doneCards) {
            String name = CardCrawlGame.languagePack.getCardStrings(id).NAME;
            ModLabeledToggleButton toggleButton = new ModLabeledToggleButton(name, (col * 300f + 300f) * Settings.scale, (count * 80f + 400f) * Settings.scale,
                    Color.GOLD, FontHelper.cardTitleFont,
                    RyoikiTenkai.isEnable(id), settingsPanel, (l) -> {
                    }, (button) -> {
                        if (button.enabled) {
                            RyoikiTenkai.disabledCardIDs.remove(id);
                        } else {
                            RyoikiTenkai.disabledCardIDs.add(id);
                        }

                        try {
                            RyoikiTenkai.config.setBool(id, button.enabled);
                            RyoikiTenkai.config.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            toggleButtons.add(toggleButton);

            count++;

            if (count == 5) {
                col++;
                count = 0;
            }
        }

        this.col = col;

        for (ModLabeledToggleButton button : toggleButtons) {
            originalX.put(button, button.getX());
        }
    }

    @Override
    public void update() {
        for (ModLabeledToggleButton button : toggleButtons) {
            float x = originalX.get(button);
            button.setX(-sliderX + x);

            button.update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for (ModLabeledToggleButton button : toggleButtons) {
            button.render(sb);
        }
    }

    @Override
    public int updateOrder() {
        return 0;
    }

    @Override
    public int renderLayer() {
        return 1;
    }

}
