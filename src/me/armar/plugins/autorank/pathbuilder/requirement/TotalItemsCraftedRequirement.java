package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;

import java.util.UUID;

public class TotalItemsCraftedRequirement extends AbstractRequirement {

    int itemsCrafted = -1;

    @Override
    public String getDescription() {

        String lang = Lang.ITEMS_CRAFTED_REQUIREMENT.getConfigValue(itemsCrafted + "");

        // Check if this requirement is world-specific
        if (this.isWorldSpecific()) {
            lang = lang.concat(" (in world '" + this.getWorld() + "')");
        }

        return lang;
    }

    @Override
    public String getProgressString(UUID uuid) {
        final int progressBar = this.getStatisticsManager().getItemsCrafted(uuid, this.getWorld(), null);

        return progressBar + "/" + itemsCrafted;
    }

    @Override
    protected boolean meetsRequirement(UUID uuid) {
        return this.getStatisticsManager().getItemsCrafted(uuid, this.getWorld(), null) >= itemsCrafted;
    }

    @Override
    public boolean initRequirement(final String[] options) {
        try {
            itemsCrafted = Integer.parseInt(options[0]);
        } catch (NumberFormatException e) {
            this.registerWarningMessage("An invalid number is provided");
            return false;
        }

        if (itemsCrafted < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        }

        return true;
    }

    @Override
    public double getProgressPercentage(UUID uuid) {
        final int progressBar = this.getStatisticsManager().getItemsCrafted(uuid, this.getWorld(), null);

        return progressBar * 1.0d / this.itemsCrafted;
    }
}