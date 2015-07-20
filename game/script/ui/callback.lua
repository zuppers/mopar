local interface = lazy('interface')
local display   = lazy('ui/display')

local module = {
    -- Callback helpers
    open_window = function(self, inter) return self:open(inter, 'window') end,

    -- Callback for when a button is pressed to open an interface
    open = function(self, inter, component)
        return function(plr, id, comp, child, opt)
            local mode = plr:display_mode()
            plr:set_interface(display:screen_for(mode)[component], inter, interface.closable)
        end
    end
}

return module