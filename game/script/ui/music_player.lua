local interfaces = require('interface')
local songs      = lazy('songs')

local music_player = {}

local music = interfaces.music

--[[
    Plays a song.

    - plr   : the player.
    - id    : the id or name of the song to play.
 ]]
function music_player:play_song(plr, id)
    local song = songs[id]
    if not song then
        return
    end
    plr:set_interface_text(music.title, song.formatted_name)
    plr:play_song(song.file)
end

action:on_button(music.list, 1, function(plr, widget, comp, child, opt)
    print(child)
    music_player:play_song(plr, child)
end)

return music_player