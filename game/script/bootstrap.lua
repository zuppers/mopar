require('variable')
require('state')

world:on_player_state(state.fresh, function(plr, s) plr:print('Welcome to Moparscape.') end)
world:on_player_state(state.idle,  function(plr, s) end)