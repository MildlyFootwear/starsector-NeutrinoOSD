package Shoey.NeutrinoOSD;

import Shoey.NeutrinoOSD.Util.sortDistance;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.CampaignUIRenderingListener;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import org.lazywizard.lazylib.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Shoey.NeutrinoOSD.MainPlugin.*;

public class CampaignUINeutrinoOSD implements CampaignUIRenderingListener {

    List<SectorEntityToken> systemEnts = new ArrayList<>();
    List<SectorEntityToken> entsDisplay = new ArrayList<>();

    void render()
    {
        CampaignFleetAPI player = sector.getPlayerFleet();
        systemEnts = player.getContainingLocation().getAllEntities();
        int seSize = systemEnts.size();
        if (player.isInHyperspace()) {
            iLastCnt = 0;
            return;
        }
        if (!(player.hasAbility("gravitic_scan") && player.getAbility("gravitic_scan").isActive()) && !(player.hasAbility("neutrino_detector_mkII") && player.getAbility("neutrino_detector_mkII").isActive())) {
            iLastCnt = 0;
            return;
        }
        if (seSize != iLastCnt)
        {
            iLastCnt = seSize;
            entsDisplay.clear();
            for (SectorEntityToken e : systemEnts)
            {
                if (e.isVisibleToPlayerFleet() && !showKnown)
                    continue;
                if (e.hasTag("planet") && e.getFaction() == null)
                    continue;
                if (e.hasTag("neutrino") || e.hasTag("neutrino_low") || e.hasTag("station") || e.getName().contains("Gate")) {
                    entsDisplay.add(e);
                    log.info("Added " + e.getName() + " to Neutrino OSD");
                }
            }
            log.info(entsDisplay.size()+" entities are worth rendering out of "+ systemEnts.size());
        }
        pL = player.getLocation();
        Collections.sort(entsDisplay, new sortDistance());

        int i = 0;
        while (i < entsDisplay.size())
        {
            if (i == labels.size())
                break;
            SectorEntityToken e = entsDisplay.get(i);
            if (e.isVisibleToPlayerFleet() && !showKnown) {
                entsDisplay.remove(e);
                continue;
            }
            LabelAPI l = labels.get(i);
            String s = e.getName();
            if (showFaction && e.getFaction() != null && e.getFaction().getEntityNamePrefix() != null && !e.getFaction().getEntityNamePrefix().isEmpty() && !e.getFaction().getEntityNamePrefix().equals("Neutral"))
                s += " ("+e.getFaction().getEntityNamePrefix()+")";
            if (showDistance) {
                float dis = MathUtils.getDistance(player.getLocation(), e.getLocation());
                String idist;
                if (dis > 25000)
                    idist = (Math.round(dis / 2500)*2.5+"k").replace(".0","");
                else if (dis > 10000)
                    idist = Math.round(dis / 1000)+"k";
                else if (dis > 1000)
                    idist = ""+(Math.round(dis / 100)*100);
                else
                    idist = ""+(Math.round(dis / 10)*10);
                s += " (" + idist + ")";
            }
            l.setText(s);
            l.autoSizeToWidth(l.computeTextWidth(s));

            if (e.getFaction() != null)
                l.setColor(e.getFaction().getColor());
            else
                l.setColor(Global.getSettings().getColor("standardTextColor"));

            if (l == labels.get(labels.size()-1) && entsDisplay.size() > labels.size())
            {
                l.setColor(Global.getSettings().getColor("standardTextColor"));
                l.setText(entsDisplay.size()-labels.size()+1+" more...");
                l.autoSizeToWidth(l.computeTextWidth(l.getText()));
            }
            if (i == 0) {
                if (!alignRight)
                    l.getPosition().setLocation(880, 40);
                else
                    l.getPosition().setLocation(877-l.getPosition().getWidth(), 143);
            } else {
                if (!alignRight)
                    l.getPosition().aboveLeft((UIComponentAPI) labels.get(i - 1), 0);
                else
                    l.getPosition().aboveRight((UIComponentAPI) labels.get(i - 1), 0);
            }
            l.render(1f);
            i++;
        }
        if (i == 0) {
            LabelAPI l = labels.get(0);
            String s = "All detections known.";
            l.setText(s);
            l.autoSizeToWidth(l.computeTextWidth(s));
            if (!alignRight)
                l.getPosition().setLocation(880, 40);
            else
                l.getPosition().setLocation(880-l.getPosition().getWidth(), 40);
            l.render(1f);
        }


    }

    @Override
    public void renderInUICoordsBelowUI(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoordsAboveUIBelowTooltips(ViewportAPI viewport) {
        render();
    }

    @Override
    public void renderInUICoordsAboveUIAndTooltips(ViewportAPI viewport) {

    }
}
