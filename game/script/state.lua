local module = {
    update_state_table = function(name, data)
        local table = rawget(_G, name) or {}
        for _, var in pairs(data) do
            rawset(table, var.name, var.id)
        end
        rawset(_G, name, table)
    end,

    load = function(self)
        self.update_state_table('state', json:decode(asset:load('state_config.json')))
    end
}

module:load()

return module