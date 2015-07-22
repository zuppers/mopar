require('fixin')

local interface = require('interface')
local inventory = require('inventory')
local items      = require('items')

-- Load the interface configurations and scripts
interface:load('interface_config.json')

-- Load the inventory and item configurations
inventory:load_config('inventory_config.json')
items:load_config('item_config.json')

-- // The joys of calling Silab a noob // --

local player_helper = require('plr/player_helper')

-- Load the player variables
player_helper:load_variables('plr_var_config.json')
player_helper:load_bit_variables('plr_varbit_config.json')

-- TODO: Maybe package this into its own module
require('cmd/admin')

-- TODO: Move to its own module, and figure out the container situation -___-
action:on_swap(interface.backpack.container, function(plr, widget, comp, first, second, mode)
    plr:inv_swap(inventory.backpack, first, second, mode)
end)