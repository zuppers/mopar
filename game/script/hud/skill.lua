local ActionHelper  = require('action_helper')
local HUD           = lazy('hud')

local SkillDisplay = {}

SkillDisplay.Menu       = interface.SkillMenu
SkillDisplay.HelpWindow = interface.SkillHelpWindow

SkillDisplay.HelpCategory = varbit.SkillHelpCategory

function SkillDisplay:HelpWindowCallback(skill)
    return function(player, child)
        player:SetBit(varbit.SkillHelpType, skill + 1)
        player:SetBit(SkillDisplay.HelpCategory, 0)
        HUD:OpenWindow(player, interface.SkillHelpWindow)
    end
end

-- TODO(sinisoul): Figure out the method to this madness and see if we can clean it up a bit.

action:OnButton(SkillDisplay.Menu:GetComponent('AttackButton'), option.One, SkillDisplay:HelpWindowCallback(0))
action:OnButton(SkillDisplay.Menu:GetComponent('StrengthButton'), option.One, SkillDisplay:HelpWindowCallback(1))
action:OnButton(SkillDisplay.Menu:GetComponent('RangedButton'), option.One, SkillDisplay:HelpWindowCallback(2))
action:OnButton(SkillDisplay.Menu:GetComponent('MagicButton'), option.One, SkillDisplay:HelpWindowCallback(3))
action:OnButton(SkillDisplay.Menu:GetComponent('DefenseButton'), option.One, SkillDisplay:HelpWindowCallback(4))
action:OnButton(SkillDisplay.Menu:GetComponent('HitpointsButton'), option.One, SkillDisplay:HelpWindowCallback(5))
action:OnButton(SkillDisplay.Menu:GetComponent('PrayerButton'), option.One, SkillDisplay:HelpWindowCallback(6))
action:OnButton(SkillDisplay.Menu:GetComponent('AgilityButton'), option.One, SkillDisplay:HelpWindowCallback(7))
action:OnButton(SkillDisplay.Menu:GetComponent('HerbloreButton'), option.One, SkillDisplay:HelpWindowCallback(8))
action:OnButton(SkillDisplay.Menu:GetComponent('ThievingButton'), option.One, SkillDisplay:HelpWindowCallback(9))
action:OnButton(SkillDisplay.Menu:GetComponent('CraftingButton'), option.One, SkillDisplay:HelpWindowCallback(10))
action:OnButton(SkillDisplay.Menu:GetComponent('RunecraftButton'), option.One, SkillDisplay:HelpWindowCallback(11))
action:OnButton(SkillDisplay.Menu:GetComponent('MiningButton'), option.One, SkillDisplay:HelpWindowCallback(12))
action:OnButton(SkillDisplay.Menu:GetComponent('SmithingButton'), option.One, SkillDisplay:HelpWindowCallback(13))
action:OnButton(SkillDisplay.Menu:GetComponent('FishingButton'), option.One, SkillDisplay:HelpWindowCallback(14))
action:OnButton(SkillDisplay.Menu:GetComponent('CookingButton'), option.One, SkillDisplay:HelpWindowCallback(15))
action:OnButton(SkillDisplay.Menu:GetComponent('FiremakingButton'), option.One, SkillDisplay:HelpWindowCallback(16))
action:OnButton(SkillDisplay.Menu:GetComponent('WoodcuttingButton'), option.One, SkillDisplay:HelpWindowCallback(17))
action:OnButton(SkillDisplay.Menu:GetComponent('FletchingButton'), option.One, SkillDisplay:HelpWindowCallback(18))
action:OnButton(SkillDisplay.Menu:GetComponent('SlayerButton'), option.One, SkillDisplay:HelpWindowCallback(19))
action:OnButton(SkillDisplay.Menu:GetComponent('FarmingButton'), option.One, SkillDisplay:HelpWindowCallback(20))
action:OnButton(SkillDisplay.Menu:GetComponent('ConstructionButton'), option.One, SkillDisplay:HelpWindowCallback(21))
action:OnButton(SkillDisplay.Menu:GetComponent('HunterButton'), option.One, SkillDisplay:HelpWindowCallback(22))
action:OnButton(SkillDisplay.Menu:GetComponent('SummoningButton'), option.One, SkillDisplay:HelpWindowCallback(23))

action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category0'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 0))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category1'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 1))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category2'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 2))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category3'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 3))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category4'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 4))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category5'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 5))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category6'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 6))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category7'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 7))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category8'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 8))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category9'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 9))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category10'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 10))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category11'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 11))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category12'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 12))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category13'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 13))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category14'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 14))
action:OnButton(SkillDisplay.HelpWindow:GetComponent('Category15'), option.One, ActionHelper:SetBit(SkillDisplay.HelpCategory, 15))

return SkillDisplay