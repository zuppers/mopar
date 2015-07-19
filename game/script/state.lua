local module = {
    namespace = "state",

    update_state_table = function(name, data)
        local table = rawget(_G, name) or {}
        for _, var in pairs(data) do
            rawset(table, var.name, var.id)
        end
        rawset(_G, name, table)
    end,

    load = function(self)
        self.update_state_table(self.namespace, json:decode(asset:load('state_config.json')))
    end
}

setmetatable(module, {
    __index = function(t, k)
        local v = rawget(_G, t.namespace) or {}
        return v[k]
    end
})

module:load()

return module