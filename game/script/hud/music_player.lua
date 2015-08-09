local MusicPlayer = {}

--log:Info('Loading HUD music player interface configurations')
--interface:ParseJson(asset:Load('interfaces/hud/music_player.json'))

--log:Info('Loading HUD music player varbit configurations')
--varbit:ParseJson(asset:Load('varbits/hud/music_player.json'))

-- TODO(sinisoul): Eventually replace this for area by area
song:ParseJson(asset:Load('songs/all.json'))

MusicPlayer.Menu = interface.MusicMenu

function MusicPlayer:Unlock(player, song)
    player:Print('<col=FF0000>Congratulations you have unlocked the track ' .. song:GetFormattedName() .. '!')
    player:SetBool(self:GetUnlockedVarbit(song), true)
end

function MusicPlayer:IsUnlocked(player, song)
    return player:GetBool(self:GetUnlockedVarbit(song))
end

function MusicPlayer:GetUnlockedVarbit(song)
    return varbit:ForName(song:GetName() .. 'Unlocked')
end

function MusicPlayer:Play(player, song, unlock)
    unlock = unlock and unlock or false
    if not song then return end

    local unlocked = self:IsUnlocked(player, song)
    if unlock and not unlocked then
        self:Unlock(player, song)
        unlocked = true
    end

    if unlocked then
        player:SetInterfaceText(MusicPlayer.Menu:GetComponent('TrackName'), song:GetFormattedName())
        player:PlaySong(song)
    else
        player:Print('You have not yet unlocked this piece of music yet!')
    end
end

function MusicPlayer:PlayAreaMusic(player)
    self:Play(player, song:ForCoordinates(player:GetX(), player:GetY()), true)
end

function MusicPlayer:Reset(player)
    player:SetInterfaceText(MusicPlayer.Menu:GetComponent('TrackName'), '')
end

action:OnButton(MusicPlayer.Menu:GetComponent('List'), option.One, function(player, child)
    MusicPlayer:Play(player, song:ForId(child))
end)

return MusicPlayer