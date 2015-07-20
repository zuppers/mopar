local interface = require('interface')
local callback  = require('ui/callback')

local settings = interface.settings

local module = {}

-- Audio and graphic setting buttons, open their respected windows
action:on_button(settings.graphics, 1, callback:open_window(interface.graphic_settings))
action:on_button(settings.audio,    1, callback:open_window(interface.audio_settings))

-- TODO: Eventually implement run energy in a player helper sort of bit
action:on_button(settings.run, 1, function(plr, id, comp, child, opt) plr:toggle_run() end)

return module