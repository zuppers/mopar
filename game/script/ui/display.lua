local state     = require('state')
local interface = lazy('interface')

require('ui/settings')
require('ui/orbs')
require('ui/songs')

local module = {

    fixed_sd        = 0,
    fixed_hd        = 1,
    resizable_hd    = 2,
    fullscreen_hd   = 3,

    -- Gets the display screen for the display mode
    screen_for = function(self, mode)
        if mode >= self.resizable_hd then
            return interface.resizable_screen
        else
            return interface.fixed_screen
        end
    end,

    open_screen = function(self, plr, mode)

        -- Get the screen for the given display mode
        local screen = self:screen_for(mode)

        -- TODO: Determine the attack strategy from the currently equipped weapon
        plr:set_interface(screen.attack_tab, interface.unarmed_attack, interface.static)
        plr:set_interface(screen.skill_tab, interface.skills, interface.static)
        plr:set_interface(screen.quest_tab, interface.quest, interface.static)
        plr:set_interface(screen.backpack_tab, interface.backpack, interface.static)
        plr:set_interface(screen.equipment_tab, interface.equipment, interface.static)
        plr:set_interface(screen.prayer_tab, interface.prayer_book, interface.static)
        plr:set_interface(screen.spell_tab, interface.modern_spell_book, interface.static)
        plr:set_interface(screen.friend_tab, interface.friends, interface.static)
        plr:set_interface(screen.ignore_tab, interface.ignores, interface.static)
        plr:set_interface(screen.clan_tab, interface.clan, interface.static)
        plr:set_interface(screen.settings_tab, interface.settings, interface.static)
        plr:set_interface(screen.emotes_tab, interface.emotes, interface.static)
        plr:set_interface(screen.music_tab, interface.music, interface.static)
        plr:set_interface(screen.logout_tab, interface.logout, interface.static)

        plr:set_interface(screen.chat_options, interface.chat_options, interface.static)

        local chatbox = interface.chatbox
        plr:set_interface(screen.chatbox, chatbox, interface.static)
        plr:set_interface(chatbox.input, interface.chat_input, interface.static)

        plr:set_interface(screen.orb_0, interface.hitpoints_orb, interface.static)
        plr:set_interface(screen.orb_1, interface.prayer_orb, interface.static)
        plr:set_interface(screen.orb_2, interface.energy_orb, interface.static)
        plr:set_interface(screen.orb_3, interface.summoning_orb, interface.static)

        plr:set_root_interface(screen)
    end
}

return module