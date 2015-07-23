local interface = require('interface')
local inventory = require('inventory')

-- Create a new inventory for the player backpack
local backpack = inventory:create(inventory.backpack, interface.backpack['container'])

-- Allow switching
backpack:allow_switch()

return backpack