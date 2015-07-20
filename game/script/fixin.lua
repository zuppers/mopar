-- Lazily loads in modules when their values are referenced
function lazy(name)
    local t, lazy_loaded = {}, {}
    setmetatable(t, {
        __index = function(w, k)
            if lazy_loaded[k] == nil then lazy_loaded[name] = require(name) end
            return lazy_loaded[name][k]
        end
    })
    return t
end

-- Wraps a table and function for calling self
function wrap(t, f)
    return function(...)
        f(t, ...)
    end
end