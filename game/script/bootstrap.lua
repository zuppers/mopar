-- -------------------------------------------
-- //   Uniboob silab-is-a-nerd technology  //
-- -------------------------------------------
log:Info('Loading game environment')

require('fixin')
require('asset_loader')

-- TEMPORARY
item:ParseJson(asset:Load('items/all.json'))
object:ParseJson(asset:Load('objects/all.json'))

require('player_helper')

require('command/admin')