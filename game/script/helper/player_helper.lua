local variable  = require('variable')
local varbit    = require('varbit')
local display   = lazy('ui/display')

local module = {

    -- The variables
    vars = {},

    -- The bit variables
    varbits = {},

    -- Loads the player variable configuration
    load_variables = function(self, file)
        self.vars = variable.load_config(file)
    end,

    load_bit_variables = function(self, file)
        self.varbits = varbit.load_config(file)
    end,

    -- Toggles the player running effect
    toggle_running = function(plr)
    end
}

setmetatable(module, {
    __index = function(m, k)
        return m.vars[k] or m.varbits[k]
    end
})

event:on(event.player_created, function(plr)
    -- Print the whale-cum
    plr:print('Welcome to Moparscape.')

    -- Open up the screen for the players current display mode
    display:open_screen(plr, plr:display_mode())
end)

event:on(event.player_display_updated, function(plr)
    display:open_screen(plr, plr:display_mode())
end)

-- Idle state, yo
world:on_player_state(state.idle, function(plr) end)

return module