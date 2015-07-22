local module = {
    _config = {},

    max_stack = 2147483647,

    load_config = function(self, file)
        local data = json:decode(asset:load(file))

        for _, item in pairs(data) do
            local stackable = item.stackable or false

            -- Store the interface configuration
            local config = {
                id          = item.id,
                name        = item.name,
                stackable   = stackable
            }

            self._config[item.name] = config
            self._config[item.id]   = config
        end
    end
}

setmetatable(module, {
    __index = function(t, k)
        return t._config[k] or { id = type(k) == "number" and tonumber(k) or 0, name = "null", stackable = false }
    end
})

return module