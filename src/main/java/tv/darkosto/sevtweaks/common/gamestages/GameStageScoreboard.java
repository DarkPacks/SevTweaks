package tv.darkosto.sevtweaks.common.gamestages;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.config.Configuration;

import java.util.Collection;
import java.util.Objects;

// TODO: Switch hardcoded ENUM to use one via a Config.

public class GameStageScoreboard {
    private final static String OBJECTIVE_NAME = "ST_Stage";
    private final static String OBJECTIVE_TITLE = "Players Stage";

    /**
     * Set the Stage Value to the Scoreboard in the World.
     */
    private static void setStageScore(EntityPlayer player, int stageValue) {
        World world = Objects.requireNonNull(player.world.getMinecraftServer()).getWorld(0);
        Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective objective = scoreboard.getObjective(OBJECTIVE_NAME);

        if (objective == null) {
            SevTweaks.logger.debug("World does not have the objective {}", player.world.getProviderName(), OBJECTIVE_NAME);
            return;
        }

        if (stageValue <= 0) {
            scoreboard.removeObjectiveFromEntity(player.getName(), objective);
        } else {
            Score score = scoreboard.getOrCreateScore(player.getName(), objective);
            score.setScorePoints(stageValue);
        }
    }

    /**
     * Get the listing of Stages a player has.
     */
    private static Collection<String> getPlayerStages(EntityPlayer player) {
        IStageData stageData = GameStageHelper.getPlayerData(player);

        return stageData.getStages();
    }

    /**
     * Handle the event from Stager when a player gains or loses a Stage.
     */
    private static void handlePlayerStage(EntityPlayer player, String stageFromEvent) {
        if (!Stages.contains(stageFromEvent)) {
            SevTweaks.logger.debug("Stage {} is not a valid stage for the modpack.", stageFromEvent);
        }

        Collection<String> stages = getPlayerStages(player);
        Stages highestStage = getHighestStage(stages);
        setStageScore(player, highestStage.ordinal() - 1);
    }

    /**
     * Get the highest stage from the Collection of Stages the player currently has.
     */
    private static Stages getHighestStage(Collection<String> playerStages) {
        Stages[] stages = Stages.values();
        ArrayUtils.reverse(stages);
        for (Stages s : stages) {
            if (playerStages.contains(s.name().toLowerCase())) {
                return s;
            }
        }

        return Stages.TUTORIAL;
    }

    /**
     * When a new Stage is added to a player call the method to add the stage to the players scoreboard.
     */
    @SubscribeEvent
    public static void onStageAdded(GameStageEvent.Added stageEvent) {
        handlePlayerStage(stageEvent.getEntityPlayer(), stageEvent.getStageName());
    }

    /**
     * When a Stage is removed from a player call the method to update the players scoreboard.
     */
    @SubscribeEvent
    public static void onStageRemoved(GameStageEvent.Removed stageEvent) {
        handlePlayerStage(stageEvent.getEntityPlayer(), stageEvent.getStageName());
    }

    /**
     * When the Stages are removed from a player call the method to remove them from the Scoreboard.
     */
    @SubscribeEvent
    public static void onStageCleared(GameStageEvent.Cleared clearedEvent) {
        setStageScore(clearedEvent.getEntityPlayer(), 0);
    }

    /**
     * Init the Scoreboard and create our Objective for tracking.
     */
    public boolean init(MinecraftServer server) {
        Scoreboard scoreboard = server.getWorld(0).getScoreboard();
        if (scoreboard.getObjective(OBJECTIVE_NAME) != null) {
            return false;
        }
        ScoreObjective objective = scoreboard.addScoreObjective(OBJECTIVE_NAME, IScoreCriteria.DUMMY);
        objective.setDisplayName(OBJECTIVE_TITLE);
        scoreboard.setObjectiveInDisplaySlot(validatePosition(), objective);

        return true;
    }

    /**
     * Validate the position from the config, if not valid fall back to 2.
     */
    private int validatePosition() {
        int configPosition = Configuration.gamestageScoreboard.scoreboardPosition;
        if (configPosition < 0 || configPosition > 2) {
            return 2;
        }
        return configPosition;
    }

    /**
     * Sync all connected players to the server with their current Stage unlocked.
     */
    public void syncPlayers(MinecraftServer server) {
        World world = server.getWorld(0);
        Scoreboard scoreboard = world.getScoreboard();
        ScoreObjective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        if (objective == null) {
            return;
        }

        PlayerList playerList = server.getPlayerList();
        for (EntityPlayerMP playerMP : playerList.getPlayers()) {
            Collection<String> stages = getPlayerStages(playerMP);
            Stages highestStage = getHighestStage(stages);
            if (highestStage == Stages.TUTORIAL) {
                continue;
            }
            setStageScore(playerMP, highestStage.ordinal() - 1);
        }
    }

    private enum Stages {
        TUTORIAL,
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        CREATIVE;

        public static boolean contains(String stage) {
            for (Stages s : Stages.values()) {
                if (s.name().equals(stage)) {
                    return true;
                }
            }
            return false;
        }
    }
}
