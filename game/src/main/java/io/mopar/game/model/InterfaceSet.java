package io.mopar.game.model;

import io.mopar.game.config.InterfaceComponent;
import io.mopar.game.config.InterfaceConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class InterfaceSet {

    public static final int CLOSABLE    = 0;
    public static final int STATIC      = 1;

    class OpenedInterface {
        int parentId;
        int componentId;
        int interfaceId;
        int mode;

        OpenedInterface(int parentId, int componentId, int interfaceId, int mode) {
            this.parentId = parentId;
            this.componentId = componentId;
            this.interfaceId = interfaceId;
            this.mode = mode;
        }
    }

    /**
     * The opened interfaces.
     */
    private Map<Integer, OpenedInterface> openedInterfaces = new LinkedHashMap<>();

    /**
     * The currently open screen id.
     */
    private int screenId = -1;

    /**
     * Constructs a new {@link InterfaceSet};
     */
    public InterfaceSet() {}

    /**
     * Opens a screen.
     *
     * @param interfaceId the interface id.
     */
    public void setScreen(int interfaceId) {
        screenId = interfaceId;
    }

    /**
     * Opens an interface.
     *
     * @param parentId the parent id.
     * @param componentId the component id.
     * @param interfaceId the interface id.
     * @param mode the mode to open the interface in.
     */
    public void openInterface(int parentId, int componentId, int interfaceId, int mode) {
        OpenedInterface replaced = openedInterfaces.put(getInterfaceKeyFor(parentId, componentId),
                new OpenedInterface(parentId, componentId, interfaceId, mode));
        if(replaced != null) {
            closeInterface(replaced);
        }
    }

    /**
     * Gets if an interface and component are opened. An interface is considered open if the referenced
     * interface is the currently opened screen, or if the interface is opened and the parent interface is the opened
     * screen.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     *
     * TODO(sinisoul): Add a way to check if an interface is visible.
     */
    public boolean isOpened(int interfaceId, int componentId) {
        if(screenId == -1) {
            return false;
        }

        if(screenId == interfaceId) {
            return true;
        }

        InterfaceConfig config = InterfaceConfig.forId(screenId);
        if(config == null) {
            return false;
        }

        for(InterfaceComponent component : config.getComponents()) {
            OpenedInterface opened = openedInterfaces.get(getInterfaceKeyFor(screenId, component.getId()));
            if(opened != null) {
                if(opened.interfaceId != interfaceId) {
                    continue;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Closes an interface.
     *
     * @param inter the interface that was closed.
     */
    private void closeInterface(OpenedInterface inter) {}

    /**
     * Gets an interface key.
     *
     * @param interfaceId the interface id.
     * @param componentId the component id.
     * @return the hash key for the provided interface and component id.
     */
    private static int getInterfaceKeyFor(int interfaceId, int componentId) {
        return interfaceId << 16 | componentId;
    }
}
