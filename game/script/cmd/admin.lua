local command   = require('command')

local inventory = lazy('inventory')
local interface = lazy('interface')
local items     = lazy('items')

local backpack  = lazy('inv/backpack')

--[[
    item id [amount]

        Gives the player the specified items.
 ]]
action:on_command('item', command:for_admin(function(plr, cmd, args)
    local id     = tonumber(args[1])
    local amount = tonumber(args[2]) or 1
    local item   = items[id]

    amount = amount > items.max_stack and items.max_stack or amount

    local overflow = amount - plr:inv_space(inventory.backpack, item, amount)
    amount = amount - overflow

    if overflow > 0 then
        plr:print('You do not have enough space in your inventory to fully complete this action.')
    end

    if amount > 0 then
        plr:give_item(inventory.backpack, item, amount)
        plr:update_inv(inventory.backpack, interface.backpack.container)
    end
end))

--[[
    clearbag

        Clears the players backpack.
  ]]
action:on_command('clearbag', command:for_admin(function(plr, cmd, args)
    backpack:clear(plr)
end))