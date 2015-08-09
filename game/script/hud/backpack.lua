local Container = require('container')

local Backpack = Container:New(inventory.Backpack, interface:GetComponent('Backpack.Container'))

Backpack.Menu = interface.Backpack

Backpack:AllowSwitch()

return Backpack