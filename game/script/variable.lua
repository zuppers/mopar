local module = {
    update_variable_table = function(name, data)
        local table = rawget(_G, name) or {}
        for _, var in pairs(data) do
            rawset(table, var.name, var.id)
        end
        rawset(_G, name, table)
    end,

    update_bit_variable_table = function(name, data)
        local table = rawget(_G, name) or {}
        for _, var in pairs(data) do
            rawset(table, var.name, { id = var.id, off = var.off, len = var.len })
        end
        rawset(_G, name, table)
    end,

    load = function(self)
        local variable_assets = { plrvar = 'player_variable.json' }
        for k, v in pairs(variable_assets) do
            self.update_variable_table(k, json:decode(asset:load('var/' .. v)))
        end

        local bit_variable_assets = { plrbit = 'player_bit_variable.json' }
        for k, v in pairs(bit_variable_assets) do
            self.update_bit_variable_table(k, json:decode(asset:load('var/' .. v)))
        end
    end
}

module:load()

return module