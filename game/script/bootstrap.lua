require('fixin')

local interface = require('interface')
local state     = require('state')

-- Loads the interface configurations and scripts
interface:load('interface_config.json')

-- Load the player variables
local plr_helper = require('helper/player_helper')
plr_helper:load_variables('plr_var_config.json')
plr_helper:load_bit_variables('plr_varbit_config.json')



--[[ TODO: MOVE THIS EVENTUALLY ]]

-- Handle for when a player is registered to the server
world:on_player_state(state.fresh, function(plr)

    -- Print out the welcome message
    plr:print('Welcome to Moparscape.')

    -- Open and display the welcome screen
    local game_screen = interface.game_screen
    plr:set_interface(game_screen.overlay, interface.welcome_screen, interface.closable)
    plr:set_root_interface(game_screen)
end)

-- Idle state, yo
world:on_player_state(state.idle, function(plr) end)