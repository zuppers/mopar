local Orbs              = require('hud/orbs')
local CombatDisplay     = require('hud/combat')
local SkillDisplay      = require('hud/skill')
local SettingsDisplay   = require('hud/settings')
local MusicPlayer       = require('hud/music_player')
local Backpack          = require('hud/backpack')

local HUD = {}

HUD.FixedStandardDefinition = 0
HUD.FixedHighDefinition     = 1
HUD.ResizableHighDefinition = 2
HUD.FullHighDefinition      = 3

function HUD:UpdateCombatDisplay(player)
    self:OpenDisplay(player, 'Combat', CombatDisplay:GetMenu(player))
end

function HUD:OpenDisplay(player, name, menu, screen)
    screen = screen and screen or self:GetScreenFor(player)
    player:OpenInterface(screen:GetComponent(name .. 'Display'), menu, interface.Static)
end

function HUD:OpenWindow(player, window, screen)
    screen = screen and screen or self:GetScreenFor(player)
    player:OpenInterface(screen:GetComponent('Window'), window, interface.Closable)
end

function HUD:GetScreenFor(player)
    return player:GetDisplayMode() > self.FixedHighDefinition and interface.ResizableScreen or interface.FixedScreen
end

function HUD:Update(player)
    local screen = self:GetScreenFor(player)

    self:OpenDisplay(player, 'Combat', CombatDisplay:GetMenu(player), screen)
    self:OpenDisplay(player, 'Skill', SkillDisplay.Menu, screen)
    self:OpenDisplay(player, 'Settings', SettingsDisplay.Menu, screen)
    self:OpenDisplay(player, 'Music', MusicPlayer.Menu, screen)
    self:OpenDisplay(player, 'Backpack', Backpack.Menu, screen)

    -- Need to move this to its own module
    player:OpenInterface(screen:GetComponent('Chatbox'), interface.Chatbox, interface.Static)
    player:OpenInterface(interface:GetComponent('Chatbox.Input'), interface.ChatInput, interface.Static)
    player:OpenInterface(screen:GetComponent('ChatOptions'), interface.ChatOptions, interface.Static)

    player:OpenInterface(screen:GetComponent('Orb0'), Orbs.Hitpoints, interface.Static)
    player:OpenInterface(screen:GetComponent('Orb1'), Orbs.Prayer, interface.Static)
    player:OpenInterface(screen:GetComponent('Orb2'), Orbs.Energy, interface.Static)
    player:OpenInterface(screen:GetComponent('Orb3'), Orbs.Summoning, interface.Static)

    player:OpenScreen(screen)
end

return HUD