local rights = lazy('plr/rights')

local module = {
    _scripts = { 'admin' },

    load = function(self)
        for _, script in pairs(self._scripts) do
            require('cmd/' .. script)
        end
    end,

    for_admin = function(self, func)
        return self:for_rights(rights.admin, func)
    end,

    for_rights = function(self, perm, func)
        return function(plr, cmd, args)
            if plr:rights() >= perm then
                func(plr, cmd, args)
            end
        end
    end
}

return module