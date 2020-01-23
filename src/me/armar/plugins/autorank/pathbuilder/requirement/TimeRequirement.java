package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.armar.plugins.autorank.storage.TimeType;
import me.armar.plugins.autorank.util.AutorankTools;
import me.armar.plugins.autorank.util.AutorankTools.Time;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * This requirement checks for local play time Date created: 13:49:33 15 jan.
 * 2014
 *
 * @author Staartvin
 */
public class TimeRequirement extends AbstractRequirement {

    int timeNeeded = -1;

    @Override
    public String getDescription() {
        return Lang.TIME_REQUIREMENT.getConfigValue(AutorankTools.timeToString(timeNeeded, Time.MINUTES));
    }

    @Override
    public String getProgressString(UUID uuid) {

        int playtime = 0;

        try {
            playtime = (getAutorank().getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, uuid).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return AutorankTools.timeToString(playtime, Time.MINUTES) + "/" + AutorankTools.timeToString(timeNeeded,
                Time.MINUTES);
    }

    @Override
    protected boolean meetsRequirement(UUID uuid) {
        int playTime = 0;

        try {
            playTime = (getAutorank().getPlayTimeManager().getPlayTime(TimeType.TOTAL_TIME, uuid).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return timeNeeded != -1 && playTime >= timeNeeded;
    }

    @Override
    public boolean initRequirement(final String[] options) {

        if (options.length > 0) {
            timeNeeded = AutorankTools.stringToTime(options[0], Time.MINUTES);
        }

        if (timeNeeded < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        }

        return true;
    }
}
