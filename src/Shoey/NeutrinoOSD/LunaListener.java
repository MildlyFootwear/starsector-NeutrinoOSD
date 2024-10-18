package Shoey.NeutrinoOSD;

import lunalib.lunaSettings.LunaSettingsListener;

import static Shoey.NeutrinoOSD.MainPlugin.setLuna;

public class LunaListener implements LunaSettingsListener {

    @Override
    public void settingsChanged(String s) {
        if (s.equals("ShoeyNeutrinoOSD"))
        {
            setLuna();
        }
    }
}
