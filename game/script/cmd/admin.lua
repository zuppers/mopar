local rights    = require('plr/rights')
local inventory = lazy('inventory')
local items     = lazy('items')
local interface = lazy('interface')

--[[
    item id [amount]

        Gives the player the specified items.
 ]]
event:on_command('item', rights.admin, function(plr, cmd, args)
    local id     = tonumber(args[1])
    local amount = tonumber(args[2]) or 1

    amount = amount > items.max_stack and items.max_stack or amount

    local overflow = amount - plr:inv_space(inventory.backpack, items[id], amount)
    amount = amount - overflow

    if overflow > 0 then
        plr:print('You do not have enough space in your inventory to fully complete this action.')
    end

    if amount > 0 then
        plr:inv_add(inventory.backpack, items[id], amount)
        plr:inv_update(inventory.backpack, interface.backpack.container)
    end
end)

--[[
    clearbag

        Clears the players backpack.
  ]]
event:on_command('clearbag', rights.admin, function(plr, cmd, args)
    plr:inv_clear(inventory.backpack, interface.backpack.container)
end)