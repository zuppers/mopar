local HUD = lazy('hud')

event:RegisterHandler(event.PlayerDisplayUpdated, function(player)

    -- Update the players HUD if their display mode was updated
    HUD:Update(player)
end)