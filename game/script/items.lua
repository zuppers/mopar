local items = {}

items.configs      = {}
items.names        = {}

items.max_stack    = 2147483647

--[[
        Loads the item configuration file.

            - file : the file to parse.
 ]]
function items:load_config(file)
    local data = json:decode(asset:load(file))

    for _, item in pairs(data) do
        local stackable      = item.stackable      or false
        local equipable      = item.equipable      or false
        local equipment_data = item.equipment_data or {}

        -- Store the interface configuration
        local config = {
            id              = item.id,
            name            = item.name,
            stackable       = stackable,
            equipable       = equipable,
            equipment_data  = equipment_data
        }

        self.configs[item.name] = config
        self.names[item.id]     = item.name
    end
end


--[[
        Gets if an item is stackable.

            -- id       : the item identifier, accepts either a string or number.
            -- returns  : if the item is stackable.
 ]]
function items:is_stackable(id)
    local config = items[id]
    return config.stackable
end

--[[
        Gets an item configuration for its id.

            - id : the item id
 ]]
function items:for_id(id)
    local name = self.names[id]
    return name and self.configs[name] or nil
end

-- Allow for items to be grabbed directly from the module, if the item does not exist then use a template
setmetatable(items, {
    __index = function(t, id)
        local config = type(id) == "number" and items:for_id(id) or items.configs[id]
        return config or { id = type(id) == "number" and tonumber(id) or 0, name = "null", stackable = false }
    end
})

return items