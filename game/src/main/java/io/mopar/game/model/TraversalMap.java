package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class TraversalMap {

    public static final int BLOCKED                         = 0x01;

    public static final int WALL_NORTH                      = 0x02;
    public static final int WALL_EAST                       = 0x04;
    public static final int WALL_SOUTH                      = 0x08;
    public static final int WALL_WEST                       = 0x10;
    public static final int WALL_NORTH_EAST                 = 0x20;
    public static final int WALL_SOUTH_EAST                 = 0x40;
    public static final int WALL_SOUTH_WEST                 = 0x80;
    public static final int WALL_NORTH_WEST                 = 0x100;
    public static final int OCCUPANT                        = 0x200;

    public static final int IMPENETRABLE_WALL_NORTH         = 0x400;
    public static final int IMPENETRABLE_WALL_EAST          = 0x800;
    public static final int IMPENETRABLE_WALL_SOUTH         = 0x1000;
    public static final int IMPENETRABLE_WALL_WEST          = 0x2000;
    public static final int IMPENETRABLE_WALL_NORTH_EAST    = 0x4000;
    public static final int IMPENETRABLE_WALL_SOUTH_EAST    = 0x8000;
    public static final int IMPENETRABLE_WALL_SOUTH_WEST    = 0x10000;
    public static final int IMPENETRABLE_WALL_NORTH_WEST    = 0x20000;
    public static final int IMPENETRABLE_OCCUPANT           = 0x40000;

    public static final int TRAVERSE_WALKING = 0x1;
    public static final int TRAVERSE_SIGHT   = 0x2;

    /**
     *
     */
    private int[][] values;

    /**
     *
     */
    private int width;

    /**
     *
     */
    private int height;

    /**
     *
     * @param width
     * @param height
     */
    public TraversalMap(int width, int height) {
        values = new int[width][height];
        this.width = width;
        this.height = height;
    }

    /**
     *
     * @param x
     * @param y
     * @param flags
     */
    public void set(int x, int y, int flags) {
        if(x >= 0 && y >= 0 && x < width && y < height) {
            values[x][y] |= flags;
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param flags
     */
    public void unset(int x, int y, int flags) {
        values[x][y] &= ~flags;
    }

    /**
     *
     */
    public void reset() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                values[x][y] = 0;
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param type
     * @param rotation
     * @param impenetrable
     */
    public void markWall(int x, int y, int type, int rotation, boolean impenetrable) {

        //
        // Straight walls
        //

        if(type == 0) {

            //     x
            // > | < y
            if (rotation == 0) {
                set(x, y, WALL_WEST);
                set(x - 1, y, WALL_EAST);
            }

            // v
            // -
            // ^ y
            // x
            if (rotation == 1) {
                set(x, y, WALL_NORTH);
                set(x, y + 1, WALL_SOUTH);
            }

            // y > | <
            //   x
            if (rotation == 2) {
                set(x, y, WALL_EAST);
                set(x + 1, y, WALL_WEST);
            }

            // x
            // v y
            // -
            // ^
            if (rotation == 3) {
                set(x, y, WALL_SOUTH);
                set(x, y - 1, WALL_NORTH);
            }
        }

        //
        // Corner walls
        //

        if(type == 2) {

            //     v
            //     -
            // > | * y
            //     x
            if (rotation == 0) {
                set(x, y, WALL_WEST | WALL_NORTH);
                set(x - 1, y, WALL_EAST);
                set(x, y + 1, WALL_SOUTH);
            }

            // v
            // -
            // * | < y
            // x
            if (rotation == 1) {
                set(x, y, WALL_EAST | WALL_NORTH);
                set(x, y + 1, WALL_SOUTH);
                set(x + 1, y, WALL_WEST);
            }

            // y
            // * | < x
            // -
            // ^
            if (rotation == 2) {
                set(x, y, WALL_EAST | WALL_SOUTH);
                set(x + 1, y, WALL_WEST);
                set(x, y - 1, WALL_NORTH);
            }

            // x > | *
            //       -
            //       ^
            //       y
            if (rotation == 3) {
                set(x, y, WALL_WEST | WALL_SOUTH);
                set(x - 1, y, WALL_EAST);
                set(x, y - 1, WALL_NORTH);
            }
        }

        //
        // Diagonal walls
        //

        if(type == 1 || type == 3) {

            // / y Upper left
            // x
            if (rotation == 0) {
                set(x, y, WALL_NORTH_WEST);
                set(x - 1, y + 1, WALL_SOUTH_EAST);
            }


            // \ y Upper right
            // x
            if (rotation == 1) {
                set(x, y, WALL_NORTH_EAST);
                set(x + 1, y + 1, WALL_SOUTH_WEST);
            }

            // / y Lower right
            // x
            if (rotation == 2) {
                set(x, y, WALL_SOUTH_EAST);
                set(x + 1, y - 1, WALL_NORTH_WEST);
            }

            // \ y Lower left
            // x
            if (rotation == 3) {
                set(x, y, WALL_SOUTH_WEST);
                set(x - 1, y - 1, WALL_NORTH_EAST);
            }
        }

        if(impenetrable) {

            //
            // Straight walls
            //

            if(type == 0) {

                //     x
                // > | < y
                if (rotation == 0) {
                    set(x, y, IMPENETRABLE_WALL_WEST);
                    set(x - 1, y, IMPENETRABLE_WALL_EAST);
                }

                // v
                // -
                // ^ y
                // x
                if (rotation == 1) {
                    set(x, y, IMPENETRABLE_WALL_NORTH);
                    set(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                }

                // y > | <
                //   x
                if (rotation == 2) {
                    set(x, y, IMPENETRABLE_WALL_EAST);
                    set(x + 1, y, IMPENETRABLE_WALL_WEST);
                }

                // x
                // v y
                // -
                // ^
                if (rotation == 3) {
                    set(x, y, IMPENETRABLE_WALL_EAST);
                    set(x, y - 1, IMPENETRABLE_WALL_WEST);
                }
            }

            //
            // Corner walls
            //

            if(type == 2) {

                //     v
                //     -
                // > | * y
                //     x
                if (rotation == 0) {
                    set(x, y, IMPENETRABLE_WALL_WEST | IMPENETRABLE_WALL_NORTH);
                    set(x - 1, y, IMPENETRABLE_WALL_EAST);
                    set(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                }

                // v
                // -
                // * | < y
                // x
                if (rotation == 1) {
                    set(x, y, IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_NORTH);
                    set(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                    set(x + 1, y, IMPENETRABLE_WALL_WEST);
                }

                // y
                // * | < x
                // -
                // ^
                if (rotation == 2) {
                    set(x, y, IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_SOUTH);
                    set(x + 1, y, IMPENETRABLE_WALL_WEST);
                    set(x, y - 1, IMPENETRABLE_WALL_NORTH);
                }

                // x > | *
                //       -
                //       ^
                //       y
                if (rotation == 3) {
                    set(x, y, IMPENETRABLE_WALL_WEST | IMPENETRABLE_WALL_SOUTH);
                    set(x - 1, y, IMPENETRABLE_WALL_EAST);
                    set(x, y - 1, IMPENETRABLE_WALL_NORTH);
                }
            }

            //
            // Diagonal walls
            //

            if(type == 1 || type == 3) {

                // / y Upper left
                // x
                if (rotation == 0) {
                    set(x, y, IMPENETRABLE_WALL_NORTH_WEST);
                    set(x - 1, y + 1, IMPENETRABLE_WALL_SOUTH_EAST);
                }


                // \ y Upper right
                // x
                if (rotation == 1) {
                    set(x, y, IMPENETRABLE_WALL_NORTH_EAST);
                    set(x + 1, y + 1, IMPENETRABLE_WALL_SOUTH_WEST);
                }

                // / y Lower right
                // x
                if (rotation == 2) {
                    set(x, y, IMPENETRABLE_WALL_SOUTH_EAST);
                    set(x + 1, y - 1, IMPENETRABLE_WALL_NORTH_WEST);
                }

                // \ y Lower left
                // x
                if (rotation == 3) {
                    set(x, y, IMPENETRABLE_WALL_SOUTH_WEST);
                    set(x - 1, y - 1, IMPENETRABLE_WALL_NORTH_EAST);
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param type
     * @param impenetrable
     */
    public void unmarkWall(int x, int y, int type, int orientation, boolean impenetrable) {

        //
        // Straight walls
        //

        if(type == 0) {

            //     x
            // > | < y
            if (orientation == 0) {
                unset(x, y, WALL_WEST);
                unset(x - 1, y, WALL_EAST);
            }

            // v
            // -
            // ^ y
            // x
            if (orientation == 1) {
                unset(x, y, WALL_NORTH);
                unset(x, y + 1, WALL_SOUTH);
            }

            // y > | <
            //   x
            if (orientation == 2) {
                unset(x, y, WALL_EAST);
                unset(x + 1, y, WALL_WEST);
            }

            // x
            // v y
            // -
            // ^
            if (orientation == 3) {
                unset(x, y, WALL_SOUTH);
                unset(x, y - 1, WALL_NORTH);
            }
        }

        //
        // Corner walls
        //

        if(type == 2) {

            //     v
            //     -
            // > | * y
            //     x
            if (orientation == 0) {
                unset(x, y, WALL_WEST | WALL_NORTH);
                unset(x - 1, y, WALL_EAST);
                unset(x, y + 1, WALL_SOUTH);
            }

            // v
            // -
            // * | < y
            // x
            if (orientation == 1) {
                unset(x, y, WALL_EAST | WALL_NORTH);
                unset(x, y + 1, WALL_SOUTH);
                unset(x + 1, y, WALL_WEST);
            }

            // y
            // * | < x
            // -
            // ^
            if (orientation == 2) {
                unset(x, y, WALL_EAST | WALL_SOUTH);
                unset(x + 1, y, WALL_WEST);
                unset(x, y - 1, WALL_NORTH);
            }

            // x > | *
            //       -
            //       ^
            //       y
            if (orientation == 3) {
                unset(x, y, WALL_WEST | WALL_SOUTH);
                unset(x - 1, y, WALL_EAST);
                unset(x, y - 1, WALL_NORTH);
            }
        }

        //
        // Diagonal walls
        //

        if(type == 1 || type == 3) {

            // / y Upper left
            // x
            if (orientation == 0) {
                unset(x, y, WALL_NORTH_WEST);
                unset(x - 1, y + 1, WALL_SOUTH_EAST);
            }


            // \ y Upper right
            // x
            if (orientation == 1) {
                unset(x, y, WALL_NORTH_EAST);
                unset(x + 1, y + 1, WALL_SOUTH_WEST);
            }

            // / y Lower right
            // x
            if (orientation == 2) {
                unset(x, y, WALL_SOUTH_EAST);
                unset(x + 1, y - 1, WALL_NORTH_WEST);
            }

            // \ y Lower left
            // x
            if (orientation == 3) {
                unset(x, y, WALL_SOUTH_WEST);
                unset(x - 1, y - 1, WALL_NORTH_EAST);
            }
        }

        if(impenetrable) {

            //
            // Straight walls
            //

            if(type == 0) {

                //     x
                // > | < y
                if (orientation == 0) {
                    unset(x, y, IMPENETRABLE_WALL_WEST);
                    unset(x - 1, y, IMPENETRABLE_WALL_EAST);
                }

                // v
                // -
                // ^ y
                // x
                if (orientation == 1) {
                    unset(x, y, IMPENETRABLE_WALL_NORTH);
                    unset(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                }

                // y > | <
                //   x
                if (orientation == 2) {
                    unset(x, y, IMPENETRABLE_WALL_EAST);
                    unset(x + 1, y, IMPENETRABLE_WALL_WEST);
                }

                // x
                // v y
                // -
                // ^
                if (orientation == 3) {
                    unset(x, y, IMPENETRABLE_WALL_EAST);
                    unset(x, y - 1, IMPENETRABLE_WALL_WEST);
                }
            }

            //
            // Corner walls
            //

            if(type == 2) {

                //     v
                //     -
                // > | * y
                //     x
                if (orientation == 0) {
                    unset(x, y, IMPENETRABLE_WALL_WEST | IMPENETRABLE_WALL_NORTH);
                    unset(x - 1, y, IMPENETRABLE_WALL_EAST);
                    unset(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                }

                // v
                // -
                // * | < y
                // x
                if (orientation == 1) {
                    unset(x, y, IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_NORTH);
                    unset(x, y + 1, IMPENETRABLE_WALL_SOUTH);
                    unset(x + 1, y, IMPENETRABLE_WALL_WEST);
                }

                // y
                // * | < x
                // -
                // ^
                if (orientation == 2) {
                    unset(x, y, IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_SOUTH);
                    unset(x + 1, y, IMPENETRABLE_WALL_WEST);
                    unset(x, y - 1, IMPENETRABLE_WALL_NORTH);
                }

                // x > | *
                //       -
                //       ^
                //       y
                if (orientation == 3) {
                    unset(x, y, IMPENETRABLE_WALL_WEST | IMPENETRABLE_WALL_SOUTH);
                    unset(x - 1, y, IMPENETRABLE_WALL_EAST);
                    unset(x, y - 1, IMPENETRABLE_WALL_NORTH);
                }
            }

            //
            // Diagonal walls
            //

            if(type == 1 || type == 3) {

                // / y Upper left
                // x
                if (orientation == 0) {
                    unset(x, y, IMPENETRABLE_WALL_NORTH_WEST);
                    unset(x - 1, y + 1, IMPENETRABLE_WALL_SOUTH_EAST);
                }


                // \ y Upper right
                // x
                if (orientation == 1) {
                    unset(x, y, IMPENETRABLE_WALL_NORTH_EAST);
                    unset(x + 1, y + 1, IMPENETRABLE_WALL_SOUTH_WEST);
                }

                // / y Lower right
                // x
                if (orientation == 2) {
                    unset(x, y, IMPENETRABLE_WALL_SOUTH_EAST);
                    unset(x + 1, y - 1, IMPENETRABLE_WALL_NORTH_WEST);
                }

                // \ y Lower left
                // x
                if (orientation == 3) {
                    unset(x, y, IMPENETRABLE_WALL_SOUTH_WEST);
                    unset(x - 1, y - 1, IMPENETRABLE_WALL_NORTH_EAST);
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param impenetrable
     */
    public void markOccupant(int x, int y, int w, int h, boolean impenetrable) {
        int flags = OCCUPANT;
        if(impenetrable) {
            flags |= IMPENETRABLE_OCCUPANT;
        }

        for(int i = x; i < x + w; i++) {
            if(i >= 0 && i < width) {
                for(int j = y; j < y + h; j++) {
                    if(j >= 0 && j <= height) {
                        set(i, j, flags);
                    }
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param impenetrable
     */
    public void unmarkOccupant(int x, int y, int w, int h, boolean impenetrable) {
        int flags = OCCUPANT;
        if(impenetrable) {
            flags |= IMPENETRABLE_OCCUPANT;
        }

        for(int i = x; i < x + w; i++) {
            if(i >= 0 && i < width) {
                for(int j = y; j < y + h; j++) {
                    if(j >= 0 && j <= height) {
                        unset(i, j, flags);
                    }
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    public void markBlocked(int x, int y) {
        set(x, y, BLOCKED);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int getValue(int x, int y) {
        return values[x][y];
    }

    /**
     *
     * @param x
     * @param y
     * @param flags
     * @return
     */
    private boolean active(int x, int y, int flags) {
        if(x >= 0 && y >= 0 && x < width && y < height) {
            return (values[x][y] & flags) != 0;
        }
        return false;                                           // Not a huge fan of this
    }

    /**
     *
     * @param x
     * @param y
     * @param direction
     * @param tflags
     * @return
     */
    public boolean isTraversable(int x, int y, Direction direction, int tflags) {
        boolean traversable = true;
        switch (direction) {
            case NORTH:
            {
                int flags = 0;
                if ((tflags & TRAVERSE_WALKING)   != 0) flags |= WALL_SOUTH | OCCUPANT | BLOCKED;
                if ((tflags & TRAVERSE_SIGHT)     != 0) flags |= IMPENETRABLE_WALL_SOUTH | IMPENETRABLE_OCCUPANT;
                traversable &= !active(x, y + 1, flags);
            }
            break;

            case SOUTH:
            {
                int flags = 0;
                if ((tflags & TRAVERSE_WALKING)   != 0) flags |= WALL_NORTH | OCCUPANT | BLOCKED;
                if ((tflags & TRAVERSE_SIGHT)     != 0) flags |= IMPENETRABLE_WALL_NORTH | IMPENETRABLE_OCCUPANT;
                traversable &= !active(x, y - 1, flags);
            }
            break;

            case EAST:
            {
                int flags = 0;
                if ((tflags & TRAVERSE_WALKING)   != 0) flags |= WALL_WEST | OCCUPANT | BLOCKED;
                if ((tflags & TRAVERSE_SIGHT)     != 0) flags |= IMPENETRABLE_WALL_WEST | IMPENETRABLE_OCCUPANT;
                traversable &= !active(x + 1, y, flags);
            }
            break;


            case WEST:
            {
                int flags = 0;
                if ((tflags & TRAVERSE_WALKING)   != 0) flags |= WALL_EAST | OCCUPANT | BLOCKED;
                if ((tflags & TRAVERSE_SIGHT)     != 0) flags |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_OCCUPANT;
                traversable &= !active(x - 1, y, flags);
            }
            break;

            case NORTH_WEST:
            {
                int f0 = 0;
                int f1 = 0;
                int f2 = 0;
                if((tflags & TRAVERSE_WALKING)  != 0) f0 |= WALL_EAST | WALL_SOUTH | WALL_SOUTH_EAST | OCCUPANT | BLOCKED;
                if((tflags & TRAVERSE_SIGHT)    != 0) f0 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_SOUTH | IMPENETRABLE_WALL_SOUTH_EAST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f1 |= WALL_EAST | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f1 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f2 |= WALL_SOUTH | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f2 |= IMPENETRABLE_WALL_SOUTH | IMPENETRABLE_OCCUPANT;

                traversable &= !active(x - 1, y + 1, f0) & !active(x - 1, y, f1) & !active(x, y + 1, f2);
            }
            break;

            case NORTH_EAST:
            {
                int f0 = 0;
                int f1 = 0;
                int f2 = 0;
                if((tflags & TRAVERSE_WALKING)  != 0) f0 |= WALL_WEST | WALL_SOUTH | WALL_SOUTH_WEST | OCCUPANT | BLOCKED;
                if((tflags & TRAVERSE_SIGHT)    != 0) f0 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_SOUTH | IMPENETRABLE_WALL_SOUTH_WEST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f1 |= WALL_WEST | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f1 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f2 |= WALL_SOUTH | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f2 |= IMPENETRABLE_WALL_SOUTH | IMPENETRABLE_OCCUPANT;

                traversable &= !active(x + 1, y + 1, f0) & !active(x + 1, y, f1) & !active(x, y + 1, f2);
            }
            break;

            case SOUTH_WEST:
            {
                int f0 = 0;
                int f1 = 0;
                int f2 = 0;
                if((tflags & TRAVERSE_WALKING)  != 0) f0 |= WALL_EAST | WALL_NORTH | WALL_NORTH_EAST | OCCUPANT | BLOCKED;
                if((tflags & TRAVERSE_SIGHT)    != 0) f0 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_WALL_NORTH | IMPENETRABLE_WALL_NORTH_EAST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f1 |= WALL_EAST | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f1 |= IMPENETRABLE_WALL_EAST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f2 |= WALL_NORTH | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f2 |= IMPENETRABLE_WALL_NORTH | IMPENETRABLE_OCCUPANT;

                traversable &= !active(x - 1, y - 1, f0) & !active(x - 1, y, f1) & !active(x, y - 1, f2);
            }
            break;

            case SOUTH_EAST:
            {
                int f0 = 0;
                int f1 = 0;
                int f2 = 0;
                if((tflags & TRAVERSE_WALKING)  != 0) f0 |= WALL_WEST | WALL_NORTH | WALL_NORTH_WEST | OCCUPANT | BLOCKED;
                if((tflags & TRAVERSE_SIGHT)    != 0) f0 |= IMPENETRABLE_WALL_WEST | IMPENETRABLE_WALL_NORTH | IMPENETRABLE_WALL_NORTH_WEST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f1 |= WALL_WEST | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f1 |= IMPENETRABLE_WALL_WEST | IMPENETRABLE_OCCUPANT;

                if((tflags & TRAVERSE_WALKING)  != 0) f2 |= WALL_NORTH | OCCUPANT;
                if((tflags & TRAVERSE_SIGHT)    != 0) f2 |= IMPENETRABLE_WALL_NORTH | IMPENETRABLE_OCCUPANT;

                traversable &= !active(x + 1, y - 1, f0) & !active(x + 1, y, f1) & !active(x, y - 1, f2);
            }
            break;
        }
        return traversable;
    }

}
