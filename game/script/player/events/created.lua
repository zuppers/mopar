local HUD           = lazy('hud')
local MusicPlayer   = lazy('hud/music_player')
local Backpack      = lazy('hud/backpack')

event:RegisterHandler(event.PlayerCreated, function(player)

    -- Rebuild the players scene so that they can visually see where they are
    -- its important that this is done first.
    player:RebuildScene()

    -- Refresh all of the player skills/
    player:RefreshSkills()

    -- Print out the greeting.
    player:Print('Welcome to Moparscape.')

    -- Update the players HUD
    HUD:Update(player)

    player:SetRunning(player:GetBool(variable.Running))

    MusicPlayer:Reset(player)

    MusicPlayer:PlayAreaMusic(player)

    Backpack:Refresh(player)
end)