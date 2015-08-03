package io.mopar.game.model.block;

import io.mopar.game.model.BlockEvent;
import io.mopar.game.model.StillGraphic;

/**
 * @author Hadyn Fitzgerald
 */
public class StillGraphicCreatedBlockEvent extends BlockEvent {

    /**
     * The still graphic that was created.
     */
    private StillGraphic stillGraphic;

    /**
     * Constructs a new {@link StillGraphicCreatedBlockEvent};
     *
     * @param stillGraphic the still graphic.
     */
    public StillGraphicCreatedBlockEvent(StillGraphic stillGraphic) {
        this.stillGraphic = stillGraphic;
    }

    /**
     * Gets the still graphic.
     *
     * @return the still graphic.
     */
    public StillGraphic getStillGraphic() {
        return stillGraphic;
    }
}
