# This story was constructed by the StoryAuthor
# ------------------------------------------------------
# Filename: /tmp/Stories/server020-TEST-2020-04-16--07-58-08--12318012767523882813.story
# Date: Thu Apr 16 19:58:08 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# ------------------------------------------------------
SET story-name server020
SET story-date "Thu Apr 16 19:58:08 CEST 2020"
FORBID_ERRORS
# ------------------------------------------------------
# This is default-configs part, which will set the used scenario, ...
# ------------------------------------------------------
CONFIG_INJECT scenario raw-json {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FIREPLACE","WALL","BAR_TABLE","BAR_SEAT","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","BAR_TABLE","FREE","ROULETTE_TABLE","FREE","FREE","WALL"],["WALL","BAR_SEAT","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","SAFE","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
CONFIG_INJECT matchconfig raw-json {"moledieRange":1,"bowlerBladeRange":1,"bowlerBladeHitChance":0.25,"bowlerBladeDamage":4,"laserCompactHitChance":0.125,"rocketPenDamage":2,"gasGlossDamage":6,"mothballPouchRange":2,"mothballPouchDamage":1,"fogTinRange":2,"grappleRange":3,"grappleHitChance":0.35,"wiretapWithEarplugsFailChance":0.64,"mirrorSwapChance":0.35,"cocktailDodgeChance":0.25,"cocktailHp":6,"spySuccessChance":0.65,"babysitterSuccessChance":0.25,"honeyTrapSuccessChance":0.35,"observationSuccessChance":0.12,"chipsToIpFactor":12,"roundLimit":15,"turnPhaseLimit":6,"catIp":8,"strikeMaximum":4,"pauseLimit":320,"reconnectLimit":200}
CONFIG_INJECT characters raw-json [{"characterId":"20ff071e-e344-449f-a8c5-dd679bc92a68","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]},{"characterId":"65cc7959-aab2-4d81-8877-e5eb74fe2fdf","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]},{"characterId":"220b7aad-429c-4f18-a019-c24a32d4a628","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]},{"characterId":"24fee6e2-a321-4161-8634-833b7a2c472d","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]},{"characterId":"a12dc0b0-bae9-4b8a-8e51-2d51e13ab01b","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]},{"characterId":"4f7664f4-20cc-4a97-a13b-574a63900595","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]},{"characterId":"d9beaf7e-24de-43b1-bf08-7ba6d3fd6dbb","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]},{"characterId":"c131e8ab-5b07-4cd2-95a8-a9a680112cfa","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]},{"characterId":"47c44e32-827d-4ff5-85ca-151bc04b1060","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]},{"characterId":"6cef511a-49c3-4c0f-b6d7-4b8642b0356d","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]},{"characterId":"7c01f4ee-0a94-4c79-8d93-860267fbf97e","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]},{"characterId":"f9c0ca2f-e1a6-4b35-afc1-74652ada2ca5","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]},{"characterId":"c81c17e9-00c3-421f-ac06-0d74f6215dc8","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},{"characterId":"c15c6cea-9384-479c-baec-0b9add3a7747","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]},{"characterId":"f9ed4e03-57be-48ff-aed3-5c78da11016b","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}]
# ------------------------------------------------------
# Now the server will write config-injects to assure
# deterministic behaviour.
# ------------------------------------------------------
CONFIG_INJECT next-proposal walter "The legendary Gustav,Hans Peter Otto,Zackiger Zacharias,mothball_pouch,bowler_blade,fog_tin"
CONFIG_INJECT next-proposal jeff "Saphira,Meister Yoda,Austauschbarer Agent Dieter 42,magnetic_watch,chicken_feed,nugget"
# ------------------------------------------------------
# This is the main part
# ------------------------------------------------------
HELLO Petterson SPECTATOR
HELLO walter AI
HELLO jeff PLAYER
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
META walter "Spectator.Count, Spectator.Names"
# End of File
