# This story was constructed by the StoryAuthor
# ------------------------------------------------------
# Filename: /tmp/Stories/server020-EQ-TEST-2020-04-16--09-02-30--5301087402770959529.story
# Date: Thu Apr 16 21:02:30 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# ------------------------------------------------------
SET story-name server020
SET story-date "Thu Apr 16 21:02:30 CEST 2020"
ASSURE_DEFAULT pauseItem false
ASSURE_DEFAULT pauseMid  false
# ------------------------------------------------------
# This is default-configs part, which will set the used scenario, ...
# ------------------------------------------------------
CONFIG_INJECT scenario raw-json {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FIREPLACE","WALL","BAR_TABLE","BAR_SEAT","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","BAR_TABLE","FREE","ROULETTE_TABLE","FREE","FREE","WALL"],["WALL","BAR_SEAT","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","SAFE","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
CONFIG_INJECT matchconfig raw-json {"moledieRange":1,"bowlerBladeRange":1,"bowlerBladeHitChance":0.25,"bowlerBladeDamage":4,"laserCompactHitChance":0.125,"rocketPenDamage":2,"gasGlossDamage":6,"mothballPouchRange":2,"mothballPouchDamage":1,"fogTinRange":2,"grappleRange":3,"grappleHitChance":0.35,"wiretapWithEarplugsFailChance":0.64,"mirrorSwapChance":0.35,"cocktailDodgeChance":0.25,"cocktailHp":6,"spySuccessChance":0.65,"babysitterSuccessChance":0.25,"honeyTrapSuccessChance":0.35,"observationSuccessChance":0.12,"chipsToIpFactor":12,"roundLimit":15,"turnPhaseLimit":6,"catIp":8,"strikeMaximum":4,"pauseLimit":320,"reconnectLimit":200}
CONFIG_INJECT characters raw-json [{"characterId":"008cc018-3ae6-4100-ae6c-62ce36ebf60c","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]},{"characterId":"272d327b-6333-487c-827b-081109c34007","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]},{"characterId":"b69d0a61-996a-43d0-9a0d-558d49874e12","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]},{"characterId":"a3e7d347-8435-468b-8041-e0b392c6fab7","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]},{"characterId":"7b393e7f-8565-45c5-8380-a27753e3f176","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]},{"characterId":"f1404276-b8fb-4a87-9913-fa599875e39d","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]},{"characterId":"dbf78a84-4c08-4317-b9b4-0cff0aed293a","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]},{"characterId":"bfd5972b-0604-4fb6-a939-df029808318a","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]},{"characterId":"a5c4a332-d72f-42f8-b69e-be1d29b8a5e5","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]},{"characterId":"785f2461-ebfd-4ccf-8bee-e38591389321","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]},{"characterId":"7e42a99b-6112-4c65-a164-114e0f85192c","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]},{"characterId":"0c228ae2-974b-482e-ae23-9856385a3076","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]},{"characterId":"1ec4f9a9-a3b5-4e2e-9ff9-d427b8842cdb","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},{"characterId":"afafa139-60a1-48f8-aff5-0778b95db261","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]},{"characterId":"c13b00d5-0b7e-438f-ad24-42ba715f5e54","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}]
# ------------------------------------------------------
# Now the server will write config-injects to assure
# deterministic behaviour.
# ------------------------------------------------------
CONFIG_INJECT next-proposal saphira "Mister Y,Ein Wischmob,Tante Gertrude,fog_tin,grapple,mothball_pouch"
CONFIG_INJECT next-proposal dieter "Saphira,Spröder Senf,Austauschbarer Agent Dieter 42,jetpack,hairdryer,mirror_of_wilderness"
CONFIG_INJECT next-proposal saphira "Misses Y,Ein Wischmob,Mister Y,mothball_pouch,nugget,magnetic_watch"
CONFIG_INJECT next-proposal dieter "Hans Peter Otto,Saphira,The legendary Gustav,bowler_blade,technicolour_prism,chicken_feed"
CONFIG_INJECT next-proposal saphira "Austauschbarer Agent Dieter 42,Tante Gertrude,Schleim B. Olzen,poison_pills,rocket_pen,gas_gloss"
CONFIG_INJECT next-proposal dieter "Misses Y,Mister X,The legendary Gustav,mothball_pouch,moledie,nugget"
CONFIG_INJECT next-proposal saphira "Spröder Senf,Schleim B. Olzen,Tante Gertrude,gas_gloss,diamond_collar,laser_compact"
CONFIG_INJECT next-proposal dieter "The legendary Gustav,James Bond,Saphira,moledie,magnetic_watch,poison_pills"
CONFIG_INJECT next-proposal saphira "Schleim B. Olzen,Mister X,Tante Gertrude,laser_compact,hairdryer,diamond_collar"
CONFIG_INJECT next-proposal dieter "The legendary Gustav,Ein Wischmob,Misses Y,chicken_feed,pocket_litter,technicolour_prism"
CONFIG_INJECT next-proposal saphira "Tante Gertrude,Schleim B. Olzen,Spröder Senf,bowler_blade,laser_compact,wiretap_with_earplugs"
CONFIG_INJECT next-proposal dieter "Petterson,Misses Y,Meister Yoda,moledie,hairdryer,diamond_collar"
CONFIG_INJECT next-proposal saphira "Saphira,Zackiger Zacharias,The legendary Gustav,nugget,poison_pills,wiretap_with_earplugs"
CONFIG_INJECT next-proposal dieter "Ein Wischmob,Tante Gertrude,Misses Y,laser_compact,rocket_pen,bowler_blade"
CONFIG_INJECT next-proposal saphira "Meister Yoda,Spröder Senf,The legendary Gustav,chicken_feed,magnetic_watch,poison_pills"
CONFIG_INJECT next-proposal dieter "Ein Wischmob,Petterson,Misses Y,fog_tin,hairdryer,mirror_of_wilderness"
# ------------------------------------------------------
# This is the main part
# ------------------------------------------------------
HELLO saphira PLAYER
HELLO walter SPECTATOR
HELLO dieter PLAYER
ITEM saphira grapple
ITEM dieter jetpack
ITEM saphira "Mister Y"
ITEM dieter "Hans Peter Otto"
ITEM saphira "Austauschbarer Agent Dieter 42"
IF_NOT ${pauseMid}
    ITEM dieter mothball_pouch
    ITEM saphira gas_gloss
    ITEM dieter "James Bond"
    ITEM saphira "Mister X"
    ITEM dieter pocket_litter
    ITEM saphira "Schleim B. Olzen"
    ITEM dieter diamond_collar
    ITEM saphira wiretap_with_earplugs
    ITEM dieter rocket_pen
    IF_NOT ${pauseItem}:
        ITEM saphira poison_pills
        ITEM dieter mirror_of_wilderness
        EQUIP saphira "Schleim B. Olzen,GRAPPLE,GAS_GLOSS,POISON_PILLS,Mister Y,Mister X,Austauschbarer Agent Dieter 42,WIRETAP_WITH_EARPLUGS"
        EQUIP dieter "Hans Peter Otto,JETPACK,MOTHBALL_POUCH,POCKET_LITTER,DIAMOND_COLLAR,MIRROR_OF_WILDERNESS,James Bond,ROCKET_PEN"
    FI
FI
# End of File