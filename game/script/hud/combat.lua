local CombatDisplay = {}

-- TODO(sinisoul):  Implement the rest of the combat menus for what is currently equipped
function CombatDisplay:GetMenu(player)
    local menu = interface.UnarmedAttack
    player:SetInterfaceText(menu:GetComponent('WeaponLabel'), 'Unarmed')
    return menu
end

return CombatDisplay