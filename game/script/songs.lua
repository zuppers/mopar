local songs = {}

songs.names   = {}
songs.configs = {}
songs.regions = {}

--[[ ]]
function songs:for_id(id)
    local id = songs.names[id]
    return id and songs.configs[id] or nil
end

--[[]]
function songs:for_region(rid)
    local id = songs.regions[rid]
    return id and songs.configs[id] or nil
end

--[[ ]]
function songs:load_config(file)
    local data = json:decode(asset:load(file))

    for _, song in pairs(data) do
        local id                = song.id
        local name              = song.name
        local file              = song.file
        local formatted_name    = song.formatted_name
        local regions           = song.regions or {}

        local config = {
            id              = id,
            name            = name,
            file            = file,
            formatted_name  = formatted_name,
            regions         = song.regions
        }

        songs.configs[name] = config
        songs.names[id]     = name

        for i=1, #regions do
            songs.regions[regions[i]] = name
        end
    end
end

setmetatable(songs, {
    __index = function(t, k)
        return type(k) == "number" and songs:for_id(k) or songs.configs[k]
    end
})

return songs