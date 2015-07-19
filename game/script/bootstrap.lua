local interface = require('interface')
local variable  = require('variable')
local state     = require('state')

-- Handle for when a player is registered to the server
world:on_player_state(state.fresh, function(plr, st)

    -- Print out the welcome message
    plr:print('Welcome to Moparscape.')

    -- Open and display the welcome screen
    local game_screen = inter.game_screen
    plr:set_interface(game_screen.overlay, inter.welcome_screen, interface.closable)
    plr:set_root_interface(game_screen)
end)

-- Just have this here so it doesn't freaking constantly bitch about how the state doesnt exist
world:on_player_state(state.idle,  function(plr, st) end)

-- Push all of the interfaces for the display mode when needed
world:on_player_state(state.display_mode_updated, function(plr, st)

    -- Use the game screen for the root interface
    local game_screen = inter.game_screen

    -- Get the screen for the given display mode, fixed for SD/HD small and resizable for HD large and HD fullscreen
    local screen
    if plr:display_mode() < 2 then
        screen = inter.fixed_screen
    end

    -- TODO: Determine the attack strategy from the currently equipped weapon
    plr:set_interface(screen.attack_tab, inter.unarmed_attack, interface.static)
    plr:set_interface(screen.skill_tab, inter.skills, interface.static)
    plr:set_interface(screen.quest_tab, inter.quest, interface.static)
    plr:set_interface(screen.inventory_tab, inter.inventory, interface.static)
    plr:set_interface(screen.equipment_tab, inter.equipment, interface.static)
    plr:set_interface(screen.prayer_tab, inter.prayer_book, interface.static)
    plr:set_interface(screen.spell_tab, inter.modern_spell_book, interface.static)
    plr:set_interface(screen.friend_tab, inter.friends, interface.static)
    plr:set_interface(screen.ignore_tab, inter.ignores, interface.static)
    plr:set_interface(screen.clan_tab, inter.clan, interface.static)
    plr:set_interface(screen.settings_tab, inter.settings, interface.static)
    plr:set_interface(screen.emotes_tab, inter.emotes, interface.static)
    plr:set_interface(screen.music_tab, inter.music, interface.static)
    plr:set_interface(screen.logout_tab, inter.logout, interface.static)

    plr:set_interface(screen.chat_options, inter.chat_options, interface.static)

    local chatbox = inter.chatbox
    plr:set_interface(screen.chatbox, chatbox, interface.static)
    plr:set_interface(chatbox.input, inter.chat_input, interface.static)

    plr:set_interface(screen.orb_0, inter.hitpoints_orb, interface.static)
    plr:set_interface(screen.orb_1, inter.prayer_orb, interface.static)
    plr:set_interface(screen.orb_2, inter.energy_orb, interface.static)
    plr:set_interface(screen.orb_3, inter.summoning_orb, interface.static)

    plr:set_interface(game_screen.window, screen, interface.screen)
end)