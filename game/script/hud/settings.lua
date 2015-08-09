local PlayerHelper  = lazy('player_helper')
local ActionHelper  = require('action_helper')

local SettingsDisplay = {}

SettingsDisplay.Menu = interface.SettingsMenu

action:OnButton(SettingsDisplay.Menu:GetComponent('ChatEffectsToggle'), option.One, ActionHelper:Toggle(variable.ChatEffectsActive))
action:OnButton(SettingsDisplay.Menu:GetComponent('SplitChatToggle'), option.One, ActionHelper:Toggle(variable.SplitChatActive))
action:OnButton(SettingsDisplay.Menu:GetComponent('MouseButtonToggle'), option.One, ActionHelper:Toggle(variable.MouseButtonMode))
action:OnButton(SettingsDisplay.Menu:GetComponent('AidToggle'), option.One, ActionHelper:Toggle(variable.AidActive))

action:OnButton(SettingsDisplay.Menu:GetComponent('RunToggle'), option.One, function(player, child)
    PlayerHelper:ToggleRunning(player)
end)

action:OnButton(SettingsDisplay.Menu:GetComponent('GraphicSettings'), option.One, ActionHelper:OpenWindow(interface.GraphicSettingsWindow))
action:OnButton(SettingsDisplay.Menu:GetComponent('AudioSettings'), option.One, ActionHelper:OpenWindow(interface.AudioSettingsWindow))

return SettingsDisplay