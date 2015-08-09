local HUD = lazy('hud')

local ActionHelper = {}

function ActionHelper:SetBit(config, value)
    return function(...)
        local player = select(1, ...)
        player:SetBit(config, value)
    end
end

function ActionHelper:Toggle(config)
    return function(...)
        local player = select(1, ...)
        player:Toggle(config)
    end
end

function ActionHelper:OpenWindow(config)
    return function(...)
        local player = select(1, ...)
        HUD:OpenWindow(player, config)
    end
end

return ActionHelper