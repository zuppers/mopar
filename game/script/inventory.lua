local items = lazy('items')

local inventory = {}

inventory.configs = {}

--[[
        Creates a new inventory.

            - config    : the inventory configuration.
            - inter     : the interface.
            - return    : the created inventory container.
 ]]
function inventory:create(config, inter)
    local container = {
        config = config
    }

    --[[
            Adds an item to the container. This method does not take into account size restraints.
            If you need to check the container capacity please use <>.
     ]]
    function container:add_item(plr, item, stack)
        stack = stack or items:is_stackable(item:id())
        plr:add_item(config, item, stack)
    end

    --[[
            Adds an item to the container. This method does not take into account size restraints.
     ]]
    function container:give_item(plr, item, amount, stack)
        stack = stack or item.stackable
        plr:give_item(config, item, amount, stack)
    end

    --[[
            Moves an item from one container to another. This does not take into account size restraints.

                - plr       : the player.
                - dest      : the destination container.
                - [stack]   : flag for if the items should stack in the destination container. If not set, uses
                                the items natural stackable configuration.
                - [shift]   : flag for if the items should shift in the source container. Default is false.
     ]]
    function container:move(plr, dest, slot, stack, shift)
        local item = container:get_item(plr, slot)
        stack = stack or items:is_stackable(item:id())
        shift = shift or false
        plr:move_item(config, dest.config, slot, stack, shift)
    end

    --[[
            Swaps items from one container to another. If the items requested to be swapped are the same
             and id and the stack flag is set then the item from the source container is appended to the
             destination container and combined.

                - plr       : the player.
                - dest      : the destination container.
                - dest_slot :
                - [stack]   : flag for if the swapped item should stack if matched. If not set, uses the items
                                natural stackable configuration.
     ]]
    function container:swap(plr, dest, slot, dest_slot, stack, shift)
        local item = container:get_item(plr, slot)
        stack = stack or items:is_stackable(item:id())
        shift = shift or false
        plr:swap_items(config, dest.config, slot, dest_slot, stack, shift)
    end

    --[[
            Gets if the container contains an item.

                - plr       : the player.
                - id        : the item id.
                - [amount]  : the item amount. The default value is 1.
                - returns   : if the inventory contains the specified item and amount.

     ]]
    function container:contains(plr, id, amount)
        amount = amount or 1
        return plr:has_item(config, id, amount)
    end

    --[[
            Gets if a player can accept an item given the inventory size constraints.
     ]]
    function container:accept(plr, item, stack)
        stack = stack or items:is_stackable(item:id())
        return plr:accept_item(config, item, stack)
    end

    --[[
    --      Gets an item.
     ]]
    function container:get_item(plr, slot)
        return plr:get_item(config, slot)
    end


    --[[
            Checks if a slot in the container is not empty.

                - plr   : the player.
                - slot  : the slot.
     ]]
    function container:is_occupied(plr, slot)
        return plr:get_item(config, slot) ~= nil
    end

    --[[
            Checks if a slot in the container is empty.

                - plr   : the player.
                - slot  : the slot.
     ]]
    function container:is_empty(plr, slot)
        return plr:get_item(config, slot) == nil
    end

    --[[
            Registers a callback for when an item menu action is triggered.

                - item      : the item configuration.
                - opt       : the option.
                - callback  : the function callback.
     ]]
    function container:on_item_option(item, opt, callback)
        action:on_item_option(inter, item, opt, callback)
    end

    --[[
            Registers a callback for when an item interface menu action is triggered.

                - opt       : the option.
                - callback  : the function callback.
     ]]
    function container:on_inter_option(opt, callback)
        action:on_inter_item_option(inter, opt, callback)
    end

    --[[
            Registers a callback from when items are switched in the container.

                - callback : the function callback.
     ]]
    function container:on_switch(callback)
        action:on_switch_item(inter, callback)
    end

    --[[
            Binds the default switch function for moving items in the container.
     ]]
    function container:allow_switch()
        container:on_switch(function(plr, first, second, mode)
            plr:switch_items(config, first, second, mode)
        end)
    end

    --[[
            Updates a container. Sends updates for individual slots.

                - plr : the player.
     ]]
    function container:update(plr)
        plr:update_inv(config, inter)
    end

    --[[
            Refreshes the container. Sends the entire contents.
     ]]
    function container:refresh(plr)
        plr:refresh_inv(config, inter)
    end

    --[[
            Clears the container.

                - plr : the player.
     ]]
    function container:clear(plr)
        plr:clear_inv(config, inter)
    end

    return container
end

--[[
        Loads the inventory configurations.
 ]]
function inventory:load_config(file)
    local data = json:decode(asset:load(file))

    for _, inv in pairs(data) do

        -- Store the interface configuration
        self.configs[inv.name] = { id = inv.id, size = inv.size }
    end
end

setmetatable(inventory, {
    __index = function(self, k)
        return self.configs[k]
    end
})

return inventory