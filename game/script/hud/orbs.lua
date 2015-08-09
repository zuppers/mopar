local PlayerHelper = lazy('player_helper')

local Orbs = {}

Orbs.Summoning  = interface.SummoningOrb
Orbs.Hitpoints  = interface.HitpointsOrb
Orbs.Prayer     = interface.PrayerOrb
Orbs.Energy     = interface.EnergyOrb

action:OnButton(Orbs.Energy:GetComponent('ToggleButton'), option.One, function(player, child)
    PlayerHelper:ToggleRunning(player)
end)

return Orbs