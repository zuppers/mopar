local variable  = require('variable')
local bitvar    = require('bitvar')

local module = {

    -- The variables
    vars = {},

    -- The bit variables
    varbits = {},

    -- Loads the player variable configuration
    load_variables = function(self, file)
        self.vars = variable.load_config(file)
    end,

    load_bit_variables = function(self, file)
        self.varbits = bitvar.load_config(file)
    end,

    -- Toggles the player running effect, TODO: Add in effects to make this possible
    toggle_running = function(plr)
    end
}

setmetatable(module, {
    __index = function(m, k)
        return m.vars[k]
    end
})

return module