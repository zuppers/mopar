local MusicPlayer = lazy('hud/music_player')

event:RegisterHandler(event.PlayerRegionUpdated, function(player)
    MusicPlayer:PlayAreaMusic(player)
end)