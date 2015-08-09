-- Lazily loads in modules when their values are referenced
function lazy(name)
    local t, lazyLoaded = {}, {}
    setmetatable(t, {
        __index = function(w, k)
            if lazyLoaded[k] == nil then lazyLoaded[name] = require(name) end
            return lazyLoaded[name][k]
        end
    })
    return t
end