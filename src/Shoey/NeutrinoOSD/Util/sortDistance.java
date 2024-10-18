package Shoey.NeutrinoOSD.Util;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lazywizard.lazylib.MathUtils;

import static Shoey.NeutrinoOSD.MainPlugin.pL;
import static Shoey.NeutrinoOSD.MainPlugin.sector;

public class sortDistance implements java.util.Comparator<SectorEntityToken>{
    @Override
    public int compare(SectorEntityToken o1, SectorEntityToken o2) {
        return (int) (MathUtils.getDistanceSquared(pL, o1.getLocation()) - MathUtils.getDistanceSquared(pL, o2.getLocation()));
    }
}
