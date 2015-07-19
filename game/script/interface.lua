local module = {
    namespace = 'inter',

    -- Enumations for interface types
    -- TODO: Come up with better name for 'screen'
    screen   = 2,
    closable = 0,
    static   = 1,

    update_interface_table = function(name, data)
        local table = _G[name] or {}
        for _, inter in pairs(data) do

            -- Map all of the interface components to their name
            local components = {}
            if inter.components ~= nil then
                for _, component in pairs(inter.components) do
                    components[component.name] = { id = component.id, parent_id = inter.id }
                end
            end

            local t = { id = inter.id, components = components }
            setmetatable(t, { __index = function(t, k) return t.components[k] end})
            table[inter.name] = t
        end
        rawset(_G, name, table)
    end,

    load = function(self)
        self.update_interface_table(self.namespace, json:decode(asset:load('interface_config.json')))
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