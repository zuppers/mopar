local _require = require

local types = {}

local extension = '.json'
local seperator = '/'

types.interface = 'interfaces'
types.varbit    = 'varbits'
types.variable  = 'variables'
types.song      = 'songs'
types.inventory = 'inventories'

function loadassets(name)
    local formattedName = string.gsub(name, '([.])', seperator)

    for moduleName, directory in pairs(types) do
        local module = _G[moduleName]
        if module then
            local path = directory .. seperator .. formattedName .. extension
            local data = asset:TryLoad(path)
            if data then
                log:Info('Loaded asset from ' .. path)
                module:ParseJson(data)
            end
        end
    end
end

require = function(modname)
    if not package.loaded[modname] then
        loadassets(modname)
    end
    return _require(modname)
end