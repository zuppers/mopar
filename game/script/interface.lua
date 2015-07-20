local module = {

    -- Enumations for interface types
    closable = 0,
    static   = 1,
    screen   = 2,

    -- Scripts to load
    _scripts = { 'display' },

    -- The interface configurations
    _config = {},

    -- Load the interface configurations and subscripts
    load = function(self, file)
        self:load_config(file)
        for _, script in pairs(self._scripts) do
            require('inter/' .. script)
        end
    end,

    -- Load the interface configurations
    load_config = function(self, file)
        local data = json:decode(asset:load(file))

        for _, inter in pairs(data) do

            -- Map all of the interface components to their name
            local comps = {}
            if inter.components ~= nil then
                for _, component in pairs(inter.components) do
                    comps[component.name] = { id = component.id, parent_id = inter.id }
                end
            end

            -- Create the interface configuration from the components, make it so that you
            -- can look up a component from its name from the parent interface
            local conf = { id = inter.id, components = comps }
            setmetatable(conf, { __index = function(t, k) return t.components[k] end})

            -- Store the interface configuration
            self._config[inter.name] = conf
        end
    end,
}

setmetatable(module, {
    __index = function(t, k)
        return t._config[k]
    end
})

return module