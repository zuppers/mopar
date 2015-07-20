local interface = require('interface')

local energy_orb = interface.energy_orb

action:on_button(energy_orb.toggle, 1, function(plr, widget, comp, child, opt) plr:toggle_run() end)