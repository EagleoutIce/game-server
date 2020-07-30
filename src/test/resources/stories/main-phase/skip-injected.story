# This story was constructed by the StoryAuthor
# ------------------------------------------------------
# Filename: /tmp/Stories/server020--2020-04-24--11-52-42--12072417637230512124.story
# Date: Fri Apr 24 11:52:42 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# ------------------------------------------------------

# Addons:
ASSURE_DEFAULT p1Role AI

SET story-name server020
SET story-date "Fri Apr 24 11:52:42 CEST 2020"
FORBID_ERRORS
# ------------------------------------------------------
# This is default-configs part, which will set the used scenario, ...
# ------------------------------------------------------
CONFIG_INJECT scenario raw-json {"scenario":[["FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE","FREE","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE","FREE","FREE"],["WALL","WALL","WALL","WALL","SAFE","WALL","WALL","WALL","FREE","FREE"],["WALL","FREE","FREE","SAFE","FREE","SAFE","FREE","WALL","FREE","FREE","FREE","FREE"],["WALL","WALL","WALL","WALL","SAFE","WALL","WALL","WALL","FREE","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE","FREE","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE","FREE","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","ROULETTE_TABLE","WALL","FREE"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","WALL","FREE"],["WALL","WALL","WALL","WALL","SAFE","WALL","WALL","WALL","FREE"],["FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE"]]}
CONFIG_INJECT matchconfig raw-json {"moledieRange":1,"bowlerBladeRange":1,"bowlerBladeHitChance":0.25,"bowlerBladeDamage":4,"laserCompactHitChance":0.125,"rocketPenDamage":2,"gasGlossDamage":6,"mothballPouchRange":2,"mothballPouchDamage":1,"fogTinRange":2,"grappleRange":3,"grappleHitChance":0.35,"wiretapWithEarplugsFailChance":0.64,"mirrorSwapChance":0.35,"cocktailDodgeChance":0.25,"cocktailHp":6,"spySuccessChance":0.65,"babysitterSuccessChance":0.25,"honeyTrapSuccessChance":0.35,"observationSuccessChance":0.12,"chipsToIpFactor":12,"roundLimit":6,"turnPhaseLimit":6,"catIp":8,"strikeMaximum":4,"pauseLimit":320,"reconnectLimit":200}
CONFIG_INJECT characters raw-json [{"characterId":"e52d9641-2ac6-41a9-9094-4aafdd71bccf","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]},{"characterId":"b09ab5fa-09fa-4340-82c4-e03c007a7bfe","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]},{"characterId":"013d6505-c663-4585-a5ec-e05cd4438503","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]},{"characterId":"11f1ebc7-dbeb-4d5e-97d3-3890e91afd1c","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]},{"characterId":"5fc9d7c7-46f8-43d0-ae5d-955c69d3c856","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]},{"characterId":"567544fa-d4d4-42b3-87bd-dbf69f451720","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]},{"characterId":"5038b6c8-e077-43fa-ae65-d568cb279370","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]},{"characterId":"5d12ec03-753a-4f6a-a4e5-c1b8bc9e6132","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]},{"characterId":"846ff8d2-70fa-40f0-969e-9d901dca995b","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]},{"characterId":"f35f6fb9-dfe8-43db-85b9-209f07bc5096","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]},{"characterId":"005375e8-1eb8-40d2-9563-a59630f5f103","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]},{"characterId":"09890d88-71e6-46e3-b3d0-0914e1b7c5da","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]},{"characterId":"5f619938-2188-40b8-9951-f7e56cac42fe","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},{"characterId":"0286affd-4565-41a7-9760-6c22b36ea650","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]},{"characterId":"f078af8e-72be-4619-800b-37e4a5fb5072","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}]
# ------------------------------------------------------
# Now the server will write config-injects to assure
# deterministic behaviour.
# ------------------------------------------------------
CONFIG_INJECT next-proposal walter "The legendary Gustav,Hans Peter Otto,Zackiger Zacharias,mothball_pouch,bowler_blade,fog_tin"
CONFIG_INJECT next-proposal saphira "James Bond,Mister X,Austauschbarer Agent Dieter 42,rocket_pen,magnetic_watch,mirror_of_wilderness"
CONFIG_INJECT next-proposal walter "Zackiger Zacharias,Hans Peter Otto,Mister Y,chicken_feed,gas_gloss,pocket_litter"
CONFIG_INJECT next-proposal saphira "Petterson,Ein Wischmob,Schleim B. Olzen,bowler_blade,grapple,hairdryer"
CONFIG_INJECT next-proposal walter "Misses Y,Mister X,Hans Peter Otto,poison_pills,laser_compact,nugget"
CONFIG_INJECT next-proposal saphira "Tante Gertrude,Spröder Senf,James Bond,wiretap_with_earplugs,gas_gloss,pocket_litter"
CONFIG_INJECT next-proposal walter "Meister Yoda,Hans Peter Otto,Schleim B. Olzen,poison_pills,mirror_of_wilderness,moledie"
CONFIG_INJECT next-proposal saphira "Tante Gertrude,Mister X,Zackiger Zacharias,pocket_litter,jetpack,grapple"
CONFIG_INJECT next-proposal walter "Saphira,Petterson,Schleim B. Olzen,bowler_blade,mothball_pouch,gas_gloss"
CONFIG_INJECT next-proposal saphira "James Bond,Misses Y,Spröder Senf,jetpack,poison_pills,technicolour_prism"
CONFIG_INJECT next-proposal walter "Tante Gertrude,Saphira,Zackiger Zacharias,gas_gloss,nugget,mothball_pouch"
CONFIG_INJECT next-proposal saphira "Ein Wischmob,Austauschbarer Agent Dieter 42,James Bond,technicolour_prism,poison_pills,jetpack"
CONFIG_INJECT next-proposal walter "Petterson,Mister X,Spröder Senf,nugget,mothball_pouch,mirror_of_wilderness"
CONFIG_INJECT next-proposal saphira "James Bond,Tante Gertrude,Saphira,poison_pills,pocket_litter,gas_gloss"
CONFIG_INJECT next-proposal walter "The legendary Gustav,Spröder Senf,Austauschbarer Agent Dieter 42,nugget,technicolour_prism,bowler_blade"
CONFIG_INJECT next-proposal saphira "Saphira,Tante Gertrude,Petterson,mothball_pouch,poison_pills,magnetic_watch"
CONFIG_INJECT safe-order value 5,3,2,1,4
CONFIG_INJECT npc-pick value "Meister Yoda,MAGNETIC_WATCH,Austauschbarer Agent Dieter 42,BOWLER_BLADE,POCKET_LITTER,GAS_GLOSS,NUGGET,TECHNICOLOUR_PRISM"
CONFIG_INJECT start-positions value "Schleim B. Olzen,5/4,Mister X,1/8,Zackiger Zacharias,6/8,The legendary Gustav,5/12,Ein Wischmob,3/2,Petterson,2/8,James Bond,4/8,Meister Yoda,8/14,Austauschbarer Agent Dieter 42,3/6,Misses Y,1/14,<cat>,1/2"
CONFIG_INJECT next-round-order value "Schleim B. Olzen,Misses Y,Meister Yoda,Austauschbarer Agent Dieter 42,The legendary Gustav,Zackiger Zacharias,Mister X,Petterson,James Bond,<cat>,Ein Wischmob"

# ------------------------------------------------------
# This is the main part
# ------------------------------------------------------
HELLO Petterson SPECTATOR
HELLO DrGünther SPECTATOR
HELLO walter ${p1Role}
HELLO saphira PLAYER
ITEM walter fog_tin
ITEM saphira rocket_pen
ITEM walter chicken_feed
ITEM saphira hairdryer
ITEM walter laser_compact
ITEM saphira wiretap_with_earplugs
ITEM walter moledie
ITEM saphira grapple
ITEM walter "Schleim B. Olzen"
ITEM saphira "Misses Y"
ITEM walter "Zackiger Zacharias"
ITEM saphira "Ein Wischmob"
ITEM walter "Mister X"
ITEM saphira "James Bond"
ITEM walter "The legendary Gustav"
ITEM saphira Petterson
EQUIP walter "Schleim B. Olzen,Mister X,CHICKEN_FEED,LASER_COMPACT,MOLEDIE,The legendary Gustav,FOG_TIN,Zackiger Zacharias"
EQUIP saphira "Petterson,Ein Wischmob,James Bond,Misses Y,ROCKET_PEN,HAIRDRYER,WIRETAP_WITH_EARPLUGS,GRAPPLE"
# End of File
