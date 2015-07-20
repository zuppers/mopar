local module = {
    load_config = function(file)
        local data = json:decode(asset:load('var/' .. file))
        local t = {}
        for _, var in pairs(data) do
            t[var.name] = { id = var.id }
        end
        return t
    end
}

return module