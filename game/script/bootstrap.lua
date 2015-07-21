require('fixin')

local interface = require('interface')

-- Loads the interface configurations and scripts
interface:load('interface_config.json')

-- // The joys of calling Silab a noob // --

local player_helper = require('helper/player_helper')

-- Load the player variables
player_helper:load_variables('plr_var_config.json')
player_helper:load_bit_variables('plr_varbit_config.json')