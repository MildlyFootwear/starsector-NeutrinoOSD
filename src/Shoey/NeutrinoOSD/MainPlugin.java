package Shoey.NeutrinoOSD;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.ui.Fonts;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import lunalib.lunaSettings.LunaSettings;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;


public class MainPlugin extends BaseModPlugin {

    public static Logger log = Global.getLogger(MainPlugin.class);
    public static List<LabelAPI> labels = new ArrayList<>();
    public static SectorAPI sector;
    public static Vector2f pL;
    static int labelsToMake = 6;
    public static boolean showKnown, showDistance, showFaction, alignRight;

    public static int iLastCnt = 0;

    public static void setLuna()
    {
        labelsToMake = LunaSettings.getInt("ShoeyNeutrinoOSD", "OSDCount");
        labels.clear();
        for (int c = 0; c < labelsToMake; c++)
        {
            labels.add(Global.getSettings().createLabel("", Fonts.ORBITRON_12));
        }
        showKnown = LunaSettings.getBoolean("ShoeyNeutrinoOSD", "showKnown");
        showDistance = LunaSettings.getBoolean("ShoeyNeutrinoOSD", "showDistance");
        showFaction = LunaSettings.getBoolean("ShoeyNeutrinoOSD", "showFaction");
        alignRight = LunaSettings.getBoolean("ShoeyNeutrinoOSD", "alignRight");
        iLastCnt = 0;
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        log.setLevel(Level.INFO);
        setLuna();
        LunaSettings.addSettingsListener(new LunaListener());
    }

    @Override
    public void onGameLoad(boolean b) {
        super.onGameLoad(b);
        sector = Global.getSector();
        sector.getListenerManager().addListener(new CampaignUINeutrinoOSD(), true);
    }

    @Override
    public void beforeGameSave()
    {
        super.beforeGameSave();
    }

    @Override
    public void afterGameSave()
    {
        super.afterGameSave();
    }
}
