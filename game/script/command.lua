local Command = {}

function Command:Register(name, rights, func)
    action:OnCommand(name, self:ForRights(rights, func))
end

function Command:ForRights(rights, func)
    return function(player, name, arguments)
        if player:GetRights() >= rights then
            func(player, name, arguments)
        end
    end
end

return Command