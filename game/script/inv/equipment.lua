local interface = require('interface')
local inventory = require('inventory')
local items     = require('items')
local backpack  = lazy('inv/backpack')

local equipment = inventory:create(inventory.equipment, interface.equipment['container'])

-- Types
equipment.head      = 0
equipment.cape      = 1
equipment.neck      = 2
equipment.weapon    = 3
equipment.body      = 4
equipment.shield    = 5
equipment.legs      = 7
equipment.hands     = 9
equipment.feet      = 10
equipment.ring      = 12
equipment.ammo      = 13

-- Groups
equipment.dagger            = 0
equipment.sword             = 1
equipment.maul              = 3
equipment.scimitar          = 4
equipment.two_handed_sword  = 5
equipment.claws             = 6
equipment.halberd           = 7
equipment.spear             = 8

-- The two handed weapons
equipment.two_handed_groups = {
    equipment.two_handed_sword,
    equipment.maul,
    equipment.claws,
    equipment.halberd,
    equipment.spear
}

--[[
        Equips an item from the backpack.

            - plr       : the player.
            - slot      : the slot.
            - return    : if the item was successfully equipped.
 ]]
function equipment:equip(plr, slot)
    local item = backpack:get_item(plr, slot)

    -- Check and see if the equipment is two handed, if it is then attempt to unequip the sheild, if that
    -- fails then we move along and just consider it as failed
    if equipment:is_two_handed(item:id()) and not equipment:unequip(plr, equipment.shield) then
        return false
    end

    local type = equipment:type_of(item:id())
    if type == equipment.shield and equipment:is_occupied(plr, equipment.weapon) then
        local weapon = equipment:get_item(plr, equipment.weapon)
        if equipment:is_two_handed(weapon:id()) and not equipment:unequip(plr, equipment.weapon) then
            return false
        end
    end

    -- Swap the requested items from the backpack to the equipment
    backpack:swap(plr, equipment, slot, equipment:type_of(item:id()))
    return true
end


--[[
        Gets the equipment type for an item.

            - id      : the item id
            - returns : the equip type of the item
 ]]
function equipment:type_of(id)
    local config = items[id]
    return config.equipment_data['type']
end

--[[
        Gets the equipment type for an item.

            - id      : the item id
            - return    : the equip type of the item
 ]]
function equipment:group_of(id)
    local config = items[id]
    return config.equipment_data['group']
end


--[[
        Gets if an item is two handed.

            - id        : the item id
            - return    : if the item is two handed
 ]]
function equipment:is_two_handed(id)
    local group = equipment:group_of(id)
    for i = 1, #equipment.two_handed_groups do
        if equipment.two_handed_groups[i] == group then
            return true
        end
    end
    return false
end


--[[
        Unequips an item from the equipment. Items unequipped will naturally stack in the backpack.

            - plr       : the player
            - slot      : the equipment slot
            - return    : if the slot was successfully unequipped
 ]]
function equipment:unequip(plr, slot)
    if equipment:is_empty(plr, slot) then
        return true
    end

    -- Check if the backpack has enough space for the item we will be unequipping
    local item = equipment:get_item(plr, slot)
    if not backpack:accept(plr, item) then
        return false
    end

    -- Add the item to the players inventory
    equipment:move(plr, backpack, slot)
    return true
end

-- Bind all of the equipable items
for _, item in pairs(items.configs) do
    if item.equipable then
        -- Register the handler for equipping items
        backpack:on_item_option(item, 1, function(plr, id, slot)
            if not equipment:equip(plr, slot) then
                plr:print('You do not have enough space in your inventory to successfully complete this action.')
                return
            end

            equipment:update(plr)
            backpack:update(plr)
            plr:update_appearance()
        end)

        -- Register the handler for unequipping items
        equipment:on_inter_option(1, function(plr, id, slot)
            if not equipment:unequip(plr, slot) then
                plr:print('You do not have enough space in your inventory to successfully complete this action.')
                return
            end

            equipment:update(plr)
            backpack:update(plr)
            plr:update_appearance()
        end)
    end
end


return equipment