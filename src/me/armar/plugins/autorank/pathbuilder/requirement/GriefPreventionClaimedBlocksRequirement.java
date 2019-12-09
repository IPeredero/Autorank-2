package me.armar.plugins.autorank.pathbuilder.requirement;

import me.armar.plugins.autorank.language.Lang;
import me.staartvin.plugins.pluginlibrary.Library;
import me.staartvin.plugins.pluginlibrary.hooks.GriefPreventionHook;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GriefPreventionClaimedBlocksRequirement extends AbstractRequirement {

    int claimedBlocks = -1;
    private GriefPreventionHook handler = null;

    @Override
    public String getDescription() {
        return Lang.GRIEF_PREVENTION_CLAIMED_BLOCKS_REQUIREMENT.getConfigValue(claimedBlocks);
    }

    @Override
    public String getProgress(final Player player) {
        final int level = handler.getNumberOfClaimedBlocks(player.getUniqueId());

        return level + "/" + claimedBlocks;
    }

    @Override
    protected boolean meetsRequirement(UUID uuid) {

        if (!handler.isAvailable())
            return false;

        return handler.getNumberOfClaimedBlocks(uuid) >= claimedBlocks;
    }

    @Override
    public boolean initRequirement(final String[] options) {

        // Add dependency
        addDependency(Library.GRIEFPREVENTION);

        handler = (GriefPreventionHook) this.getDependencyManager()
                .getLibraryHook(Library.GRIEFPREVENTION);

        if (options.length > 0) {
            try {
                claimedBlocks = Integer.parseInt(options[0]);
            } catch (NumberFormatException e) {
                this.registerWarningMessage("An invalid number is provided");
                return false;
            }
        }

        if (claimedBlocks < 0) {
            this.registerWarningMessage("No number is provided or smaller than 0.");
            return false;
        }

        if (handler == null || !handler.isAvailable()) {
            this.registerWarningMessage("GriefPrevention is not available");
            return false;
        }

        return true;
    }
}
