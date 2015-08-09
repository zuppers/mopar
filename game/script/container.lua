local Container = {}

function Container:New(config, component)
    local Instance = {}

    Instance.config    = config
    Instance.component = component

    function Instance:Switch(player, first, second, mode)
        mode = mode and mode or 0                               -- TODO(sinisoul)
        player:SwitchItems(self.config, first, second, mode)
    end

    function Instance:AllowSwitch()
        action:OnSwitchItems(self.component, function(player, first, second, mode)
            Instance:Switch(player, first, second, mode)
        end)
    end

    function Instance:Update(player)
        player:UpdateInventory(self.config, self.component)
    end

    function Instance:Refresh(player)
        player:RefreshInventory(self.config, self.component)
    end

    return Instance
end

return Container