package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class StillGraphic extends Immobile {

    /**
     * The graphic.
     */
    private Graphic graphic;

    /**
     * Constructs a new {@link StillGraphic};
     *
     * @param graphic the graphic.
     */
    public StillGraphic(Graphic graphic) {
        this.graphic = graphic;
    }

    /**
     * Gets the graphic.
     *
     * @return the graphic.
     */
    public Graphic getGraphic() {
        return graphic;
    }
}
