package de.uulm.team020.server.client.data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.helper.DiggerKeyEnum;
import de.uulm.team020.server.client.helper.IAmDigable;

/**
 * This class holds data received by the Meta Information Messages. It will be
 * searched by Digger if any of the following keys apply (TODO:)
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class MetaInformationBuffer implements IAmDigable {

    private Map<String, Object> informationBuffer;
    private final ServerClientController controller;
    private Date lastUpdate = null;

    /**
     * Build a new Buffer
     * 
     * @param controller The controller to bind this one to
     */
    public MetaInformationBuffer(final ServerClientController controller) {
        informationBuffer = new HashMap<>();
        this.controller = controller;
    }

    private String getInfo(MetaKeyEnum key) {
        return getInfo(key, "Not given");
    }

    private String getInfo(MetaKeyEnum key, String missing) {
        Object value = informationBuffer.get(key.getKey());
        if (key.getExpectedType().isArray()) {
            return Objects.requireNonNullElse(Arrays.toString((Object[]) value), missing);
        } else {
            return Objects.requireNonNullElse(value, missing).toString();
        }
    }

    @Override
    public boolean dig(DiggerKeyEnum key) {
        final ConsoleWriter writer = ConsoleWriter.getWriter();

        switch (key) {
            case SERVER:
                // Server Information
                writer.renderln("@|bold Connected to: '" + controller.getSessionData().getServerTarget()
                        + "'|@ (recovery-date: " + controller.getSessionData().getCreationDate() + ")");
                writer.renderln("Players: " + getInfo(MetaKeyEnum.PLAYER_NAMES) + " ("
                        + (getInfo(MetaKeyEnum.PLAYER_COUNT, "?") + ")"));
                writer.renderln("Spectators: " + getInfo(MetaKeyEnum.SPECTATOR_NAMES) + " ("
                        + (getInfo(MetaKeyEnum.SPECTATOR_COUNT, "?")) + ")");
                break;
            default:
                writer.renderln("@|red Error for digger. Digger asked the MetaInformationBuffer for: " + key
                        + " but the MetaInformationBuffer does not server this key.");
                return false;
        }
        writer.renderln("@|italic This information was last updated:|@ @|italic,bold "
                + Objects.requireNonNullElse(lastUpdate, "<never>") + " |@");
        writer.renderln("@|italic Send meta-messages to perform more updates |@");
        return true;
    }

    /**
     * Update Meta information
     * 
     * @param informationBuffer The buffer to update
     */
    public void updateMetaInformation(Map<String, Object> informationBuffer) {
        this.informationBuffer.putAll(informationBuffer);
        lastUpdate = new Date();
    }

}