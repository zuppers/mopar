local Command   = require('command')
local Rights    = require('player/rights')

local function PrintPosition(player, name, arguments)
    player:Print("Plane: " .. player:GetPlane() .. ", X: " .. player:GetX() .. ", Y: " .. player:GetY())
end

Command:Register('pos', Rights.Admin, PrintPosition)