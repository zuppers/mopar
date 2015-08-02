local command   = require('command')

local inventory = lazy('inventory')
local interface = lazy('interface')
local items     = lazy('items')

local backpack  = lazy('inv/backpack')
local skills    = lazy('skills')

--[[
    x y plane type config [orientation]
 ]]
action:on_command('createobj', command:for_admin(function(plr, cmd, args)
    local x             = tonumber(args[1])
    local y             = tonumber(args[2])
    local plane         = tonumber(args[3])
    local type          = tonumber(args[4])
    local config        = tonumber(args[5])
    local orientation   = args[6] and tonumber(args[6]) or 0

    world:create_obj(plane, x, y, type, config, orientation)
end))

--[[
    x y plane group
 ]]
action:on_command('removeobj', command:for_admin(function(plr, cmd, args)
    local x             = tonumber(args[1])
    local y             = tonumber(args[2])
    local plane         = tonumber(args[3])
    local group         = tonumber(args[4])

    world:remove_obj(plane, x, y, group)
end))

--[[
    x y plane config [height [delay] ]
 ]]
action:on_command('creategfx', command:for_admin(function(plr, cmd, args)
    local x             = tonumber(args[1])
    local y             = tonumber(args[2])
    local plane         = tonumber(args[3])
    local config        = tonumber(args[4])
    local height        = args[4] and tonumber(args[5]) or 0
    local delay         = args[5] and tonumber(args[6]) or 0

    world:create_still_gfx(plane, x, y, config, height, delay)
end))


--[[ ]]

action:on_command('tele', command:for_admin(function(plr, cmd, args)
    local x     = tonumber(args[1])
    local y     = tonumber(args[2])
    local plane = args[3] and tonumber(args[3]) or 0
    plr:tele(x, y, plane)
end))

--[[ ]]

action:on_command('givexp', command:for_admin(function(plr, cmd, args)
    local id        = tonumber(args[1])
    local amount    = tonumber(args[2])
    plr:give_exp(id, amount)
end))

--[[]]
action:on_command('master', command:for_admin(function(plr, cmd, args)
    for i=0, skills.count do
        plr:give_exp(i, 200000000)
    end
end))

--[[
    song id

        Plays a song.
 ]]
action:on_command('song', command:for_admin(function(plr, cmd, args)
    local id = tonumber(args[1])
    plr:play_song(id)
end))


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