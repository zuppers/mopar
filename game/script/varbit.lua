local module = {
    load_config = function(file)
        local data = json:decode(asset:load('varbit/' .. file))
        local t = {}
        for _, var in pairs(data) do
            t[var.name] = { id = var.id,  off = var.off, len = var.len }
        end
        return t
    end
}

return module