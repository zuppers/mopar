local variable  = require('variable')
local varbit    = require('varbit')

local display   = lazy('ui/display')

local backpack  = require('inv/backpack')
local equipment = require('inv/equipment')

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

-- Handle for when a player is created
service:on_event(service.player_created, function(plr)

    -- Print the whale-cum
    plr:print('Welcome to Moparscape.')

    -- Refresh the inventories
    backpack:refresh(plr)
    equipment:refresh(plr)

    -- Open up the screen for the current display mode
    display:open_screen(plr, plr:display_mode())

    -- TODO: Fix dis
    plr:play_song(-1)
end)

-- TODO Figure out what we may eventually do with this
service:on_event(service.player_display_updated, function(plr)
    display:open_screen(plr, plr:display_mode())
end)

-- Idle state, yo
world:on_player_state(state.idle, function(plr) end)

return module