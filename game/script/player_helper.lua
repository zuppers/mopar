log:Info('Loading player variable configurations')
variable:ParseJson(asset:Load('player/variables.json'))

require('player/events/created')
require('player/events/display_updated')
require('player/events/region_updated')

require('hud')

local PlayerHelper = {}

-- TODO(sinisoul): Add/remove effect for run energy based upon if you're currently running or not
function PlayerHelper:ToggleRunning(player)
    local running = not player:GetRunning()
    player:SetBool(variable.Running, running)
    player:SetRunning(running)
end

return PlayerHelper