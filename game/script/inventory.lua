local module = {
    _config = {},

    load_config = function(self, file)
        local data = json:decode(asset:load(file))

        for _, inv in pairs(data) do

            -- Store the interface configuration
            self._config[inv.name] = { id = inv.id, size = inv.size }
        end
    end
}

setmetatable(module, {
    __index = function(t, k)
        return t._config[k]
    end
})

return module