# This story was constructed by the StoryAuthor
# ------------------------------------------------------
# Filename: /tmp/Stories/server020--2020-05-02--08-51-36--13416300042411079628.story
# Date: Sat May 02 20:51:36 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# ------------------------------------------------------
SET story-name server020
SET story-date "Sat May 02 20:51:36 CEST 2020"
ASSURE_DEFAULT hpOttoFeatures "FLAPS_AND_SEALS","LUCKY_DEVIL"
FORBID_ERRORS
# ------------------------------------------------------
# This is default-configs part, which will set the used scenario, ...
# ------------------------------------------------------
COLLECT_START "" scenario
    {"scenario":
        [
            ["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],
            ["WALL","FIREPLACE","WALL","BAR_TABLE","BAR_SEAT","FREE","FREE","WALL","WALL"],
            ["WALL","SAFE","FREE","FREE","FREE","FREE","WALL","FREE","WALL"],
            ["WALL","BAR_TABLE","FREE","ROULETTE_TABLE","FREE","FREE","FREE","FREE","WALL"],
            ["WALL","BAR_SEAT","FREE","WALL","FREE","FREE","FREE","FREE","WALL"],
            ["WALL","FREE","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],
            ["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],
            ["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],
            ["WALL","WALL","WALL","FREE","WALL","WALL","FREE","FREE","WALL"],
            ["WALL","FREE","FREE","FREE","WALL","WALL","FREE","FREE","WALL"],
            ["WALL","SAFE","WALL","FREE","FREE","FREE","FREE","SAFE","WALL"],
            ["WALL","FREE","WALL","FREE","FREE","FREE","FREE","WALL","WALL"],
            ["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"]
        ]
    }
COLLECT_END

CONFIG_INJECT scenario raw-json ${scenario}
CONFIG_INJECT matchconfig raw-json {"moledieRange":1,"bowlerBladeRange":1,"bowlerBladeHitChance":0.25,"bowlerBladeDamage":4,"laserCompactHitChance":0.125,"rocketPenDamage":2,"gasGlossDamage":6,"mothballPouchRange":2,"mothballPouchDamage":1,"fogTinRange":2,"grappleRange":3,"grappleHitChance":0.35,"wiretapWithEarplugsFailChance":0.64,"mirrorSwapChance":0.35,"cocktailDodgeChance":0.25,"cocktailHp":6,"spySuccessChance":0.65,"babysitterSuccessChance":0.25,"honeyTrapSuccessChance":0.35,"observationSuccessChance":0.12,"chipsToIpFactor":12,"secretToIpFactor":3,"minChipsRoulette":0,"maxChipsRoulette":6,"roundLimit":15,"turnPhaseLimit":60,"catIp":8,"strikeMaximum":4,"pauseLimit":320,"reconnectLimit":200}
CONFIG_INJECT characters raw-json [{"characterId":"8c877420-9dc2-4675-b1c1-99f2c90f5a36","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]},{"characterId":"c5152f8d-259b-47c3-9b63-dd70b31eb8f5","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]},{"characterId":"4ada5340-6ca8-4e2e-a006-dc84bb4d0b35","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]},{"characterId":"8fa92363-4a9c-457c-b6e1-78ac8c298676","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]},{"characterId":"f1014bb6-1d77-4e6d-9417-71db9bc38771","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH",${hpOttoFeatures}]},{"characterId":"2fb2ffa3-22e7-417a-a304-a3f64c674380","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]},{"characterId":"05a2d26e-5c9e-4621-ac51-5254905f8ef5","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]},{"characterId":"9da9d476-201e-4876-863a-2c9470f3c7ce","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]},{"characterId":"f2166a6c-3dc5-4f75-93e2-42df81ccb5e4","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]},{"characterId":"f34cf7cd-408f-46e6-9c3e-6e88caa80442","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]},{"characterId":"7a1c4782-f33f-4b91-820b-10d48da8688f","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]},{"characterId":"11fb11a0-2d9f-4430-8d74-8919c0c1a5eb","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]},{"characterId":"3c839ff6-2017-4185-a601-4474debfaa69","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},{"characterId":"73895204-0e47-4d7d-8229-127d1b2c5c9e","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]},{"characterId":"ce160254-2e66-4811-9c22-da93fcbce347","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}]
# ------------------------------------------------------
# Now the server will write config-injects to assure
# deterministic behaviour.
# ------------------------------------------------------
CONFIG_INJECT next-proposal saphira "Ein Wischmob,Saphira,The legendary Gustav,rocket_pen,fog_tin,moledie"
CONFIG_INJECT next-proposal dieter "Misses Y,James Bond,Meister Yoda,poison_pills,bowler_blade,nugget"
CONFIG_INJECT next-proposal saphira "Schleim B. Olzen,Hans Peter Otto,Austauschbarer Agent Dieter 42,magnetic_watch,moledie,grapple"
CONFIG_INJECT next-proposal dieter "Zackiger Zacharias,Mister X,The legendary Gustav,mothball_pouch,gas_gloss,pocket_litter"
CONFIG_INJECT next-proposal saphira "Austauschbarer Agent Dieter 42,Petterson,Hans Peter Otto,grapple,technicolour_prism,laser_compact"
CONFIG_INJECT next-proposal dieter "Schleim B. Olzen,Zackiger Zacharias,The legendary Gustav,hairdryer,wiretap_with_earplugs,gas_gloss"
CONFIG_INJECT next-proposal saphira "Hans Peter Otto,Petterson,Ein Wischmob,jetpack,technicolour_prism,laser_compact"
CONFIG_INJECT next-proposal dieter "Tante Gertrude,Austauschbarer Agent Dieter 42,Spröder Senf,mirror_of_wilderness,fog_tin,hairdryer"
CONFIG_INJECT next-proposal saphira "Meister Yoda,Zackiger Zacharias,Misses Y,poison_pills,magnetic_watch,chicken_feed"
CONFIG_INJECT next-proposal dieter "Spröder Senf,Tante Gertrude,Ein Wischmob,fog_tin,hairdryer,mirror_of_wilderness"
CONFIG_INJECT next-proposal saphira "Petterson,Misses Y,Hans Peter Otto,laser_compact,jetpack,poison_pills"
CONFIG_INJECT next-proposal dieter "Zackiger Zacharias,Spröder Senf,Mister Y,pocket_litter,wiretap_with_earplugs,hairdryer"
CONFIG_INJECT next-proposal saphira "The legendary Gustav,James Bond,Schleim B. Olzen,mothball_pouch,rocket_pen,mirror_of_wilderness"
CONFIG_INJECT next-proposal dieter laser_compact,hairdryer,poison_pills
CONFIG_INJECT next-proposal saphira "Petterson,The legendary Gustav,Meister Yoda,chicken_feed,bowler_blade,fog_tin"
CONFIG_INJECT next-proposal dieter rocket_pen,mothball_pouch,mirror_of_wilderness
CONFIG_INJECT safe-order value 2,3,1
CONFIG_INJECT npc-pick value "Petterson,POISON_PILLS,James Bond,WIRETAP_WITH_EARPLUGS,MOTHBALL_POUCH,JETPACK,POCKET_LITTER,BOWLER_BLADE"
CONFIG_INJECT start-positions value "<cat>,6/6,Hans Peter Otto,6/5,Austauschbarer Agent Dieter 42,2/2,Petterson,4/10,Schleim B. Olzen,3/5,Mister X,1/5,Ein Wischmob,1/11,James Bond,5/11,Spröder Senf,5/3,Saphira,6/3"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Saphira,Hans Peter Otto,<cat>,Petterson,Ein Wischmob,James Bond,Spröder Senf,Schleim B. Olzen,Mister X"
# ------------------------------------------------------
# This is the main part
# ------------------------------------------------------
HELLO saphira PLAYER
HELLO dieter PLAYER
ITEM saphira Saphira
ITEM dieter nugget
ITEM saphira moledie
ITEM dieter "Mister X"
ITEM saphira grapple
ITEM dieter gas_gloss
ITEM saphira technicolour_prism
ITEM dieter "Austauschbarer Agent Dieter 42"
ITEM saphira magnetic_watch
ITEM dieter "Ein Wischmob"
ITEM saphira "Hans Peter Otto"
ITEM dieter "Spröder Senf"
ITEM saphira "Schleim B. Olzen"
ITEM dieter hairdryer
ITEM saphira fog_tin
ITEM dieter mirror_of_wilderness
EQUIP saphira "Hans Peter Otto,MOLEDIE,TECHNICOLOUR_PRISM,MAGNETIC_WATCH,Schleim B. Olzen,GRAPPLE,Saphira,FOG_TIN"
EQUIP dieter "Mister X,Austauschbarer Agent Dieter 42,NUGGET,GAS_GLOSS,MIRROR_OF_WILDERNESS,Ein Wischmob,HAIRDRYER,Spröder Senf"
# End of File
