# This story was constructed by the StoryAuthor
# ------------------------------------------------------
# Filename: /tmp/Stories/server020--2020-05-22--01-31-49--3400021716688153177.story
# Date: Fri May 22 13:31:49 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# ------------------------------------------------------
SET story-name server020
SET story-date "Fri May 22 13:31:49 CEST 2020"
FORBID_ERRORS
# ------------------------------------------------------
# This is default-configs part, which will set the used scenario, ...
# ------------------------------------------------------
CONFIG_INJECT scenario raw-json {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FIREPLACE","WALL","BAR_TABLE","BAR_SEAT","FREE","FREE","WALL","WALL"],["WALL","SAFE","FREE","FREE","FREE","FREE","WALL","FREE","WALL"],["WALL","BAR_TABLE","FREE","ROULETTE_TABLE","FREE","FREE","FREE","FREE","WALL"],["WALL","BAR_SEAT","FREE","WALL","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","WALL","WALL","FREE","WALL","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","WALL","FREE","FREE","WALL"],["WALL","SAFE","WALL","FREE","FREE","FREE","FREE","SAFE","WALL"],["WALL","FREE","WALL","FREE","FREE","FREE","FREE","WALL","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
CONFIG_INJECT matchconfig raw-json {"moledieRange":1,"bowlerBladeRange":1,"bowlerBladeHitChance":0.25,"bowlerBladeDamage":4,"laserCompactHitChance":0.125,"rocketPenDamage":2,"gasGlossDamage":6,"mothballPouchRange":2,"mothballPouchDamage":1,"fogTinRange":2,"grappleRange":3,"grappleHitChance":0.35,"wiretapWithEarplugsFailChance":0.64,"mirrorSwapChance":0.35,"cocktailDodgeChance":0.25,"cocktailHp":6,"spySuccessChance":0.65,"babysitterSuccessChance":0.25,"honeyTrapSuccessChance":0.35,"observationSuccessChance":0.12,"chipsToIpFactor":12,"secretToIpFactor":3,"minChipsRoulette":0,"maxChipsRoulette":6,"roundLimit":15,"turnPhaseLimit":60,"catIp":8,"strikeMaximum":4,"pauseLimit":320,"reconnectLimit":200}
CONFIG_INJECT characters raw-json [{"characterId":"86733d01-3806-4293-a9bb-150031508213","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]},{"characterId":"48422c74-c64c-45e5-8f70-168e84c394c1","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]},{"characterId":"8043f1f9-89e2-41d1-9d51-8e90b07ee34e","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]},{"characterId":"64f44c84-fdf4-4981-8bee-e8a719620dfa","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]},{"characterId":"cb33731f-1a55-4484-81db-220fdec8db20","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]},{"characterId":"775684fd-4b76-47c0-8677-f992f63ca016","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]},{"characterId":"ae850d11-1b37-4aea-9c1e-1eb7d007d52b","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]},{"characterId":"4bb2c951-b092-426d-b235-f60aa5fff63d","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]},{"characterId":"6753f1f0-3f71-4c2c-945b-0faf3f8ef6e3","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]},{"characterId":"f9e7ae12-9eca-49c6-ad4f-f844c41831c2","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]},{"characterId":"0359c4a0-ec67-47fb-b10d-00a000bf3932","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]},{"characterId":"1f603fb8-27f2-427d-95cd-6262aeb82296","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]},{"characterId":"e682e970-970b-408d-a6b5-8aebb68fc2e3","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},{"characterId":"bf5227a9-2557-48d4-8984-594edb9bfddb","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]},{"characterId":"982f1a30-a281-49c6-abee-04ed4ffc358c","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}]
# ------------------------------------------------------
# Now the server will write config-injects to assure
# deterministic behaviour.
# ------------------------------------------------------
CONFIG_INJECT next-proposal saphira "Tante Gertrude,Mister Y,Zackiger Zacharias,magnetic_watch,pocket_litter,moledie"
CONFIG_INJECT next-proposal dieter "Hans Peter Otto,James Bond,Mister X,fog_tin,hairdryer,rocket_pen"
CONFIG_INJECT next-proposal saphira "Meister Yoda,Misses Y,The legendary Gustav,nugget,chicken_feed,magnetic_watch"
CONFIG_INJECT next-proposal dieter "Austauschbarer Agent Dieter 42,Tante Gertrude,Petterson,mirror_of_wilderness,jetpack,hairdryer"
CONFIG_INJECT next-proposal saphira "Ein Wischmob,Hans Peter Otto,Mister Y,wiretap_with_earplugs,bowler_blade,nugget"
CONFIG_INJECT next-proposal dieter "Austauschbarer Agent Dieter 42,Misses Y,The legendary Gustav,mirror_of_wilderness,technicolour_prism,poison_pills"
CONFIG_INJECT next-proposal saphira "James Bond,Meister Yoda,Schleim B. Olzen,mothball_pouch,grapple,chicken_feed"
CONFIG_INJECT next-proposal dieter "Saphira,Mister Y,Hans Peter Otto,rocket_pen,poison_pills,laser_compact"
CONFIG_INJECT next-proposal saphira "Austauschbarer Agent Dieter 42,Meister Yoda,Tante Gertrude,jetpack,mothball_pouch,wiretap_with_earplugs"
CONFIG_INJECT next-proposal dieter "Schleim B. Olzen,James Bond,Spröder Senf,gas_gloss,anti_plague_mask,bowler_blade"
CONFIG_INJECT next-proposal saphira "Mister Y,Hans Peter Otto,Meister Yoda,fog_tin,pocket_litter,nugget"
CONFIG_INJECT next-proposal dieter mirror_of_wilderness,anti_plague_mask,rocket_pen
CONFIG_INJECT next-proposal saphira "The legendary Gustav,Hans Peter Otto,Saphira,moledie,mothball_pouch,bowler_blade"
CONFIG_INJECT next-proposal dieter chicken_feed,jetpack,nugget
CONFIG_INJECT next-proposal saphira "Spröder Senf,Austauschbarer Agent Dieter 42,Meister Yoda,rocket_pen,moledie,poison_pills"
CONFIG_INJECT next-proposal dieter gas_gloss,anti_plague_mask,nugget
CONFIG_INJECT safe-order value 1,2,3
CONFIG_INJECT npc-pick value "Schleim B. Olzen,Tante Gertrude,CHICKEN_FEED,NUGGET"
CONFIG_INJECT start-positions value "Schleim B. Olzen,6/8,Ein Wischmob,7/4,Mister X,5/1,Hans Peter Otto,4/5,<cat>,2/6,Misses Y,3/10,Spröder Senf,1/11,Petterson,2/4,Tante Gertrude,2/2,Zackiger Zacharias,6/1,James Bond,6/6"
CONFIG_INJECT next-round-order value "Zackiger Zacharias,Mister X,Spröder Senf,Hans Peter Otto,<cat>,Misses Y,Ein Wischmob,Petterson,James Bond,Tante Gertrude,Schleim B. Olzen"
# ------------------------------------------------------
# This is the main part
# ------------------------------------------------------
HELLO saphira PLAYER
HELLO dieter PLAYER
ITEM saphira "Zackiger Zacharias"
ITEM dieter "Mister X"
ITEM saphira magnetic_watch
ITEM dieter Petterson
ITEM saphira "Ein Wischmob"
ITEM dieter "Misses Y"
ITEM saphira grapple
ITEM dieter laser_compact
ITEM saphira wiretap_with_earplugs
ITEM dieter "James Bond"
ITEM saphira fog_tin
ITEM dieter mirror_of_wilderness
ITEM saphira "Hans Peter Otto"
ITEM dieter jetpack
ITEM saphira "Spröder Senf"
ITEM dieter anti_plague_mask
EQUIP saphira "Hans Peter Otto,Ein Wischmob,FOG_TIN,Zackiger Zacharias,MAGNETIC_WATCH,GRAPPLE,Spröder Senf,WIRETAP_WITH_EARPLUGS"
EQUIP dieter "Petterson,Mister X,MIRROR_OF_WILDERNESS,ANTI_PLAGUE_MASK,Misses Y,LASER_COMPACT,JETPACK,James Bond"
# End of File
