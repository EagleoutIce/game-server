# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-Nicht das X, das Sie suchen-2020-07-03--02-35-25--8484497094883990865.story
# Date: Fri Jul 03 02:35:25 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Fri Jul 03 02:35:25 CEST 2020"
FORBID_ERRORS
# =============================================================================
# This is default-configs part, which will set the used scenario, ...
# =============================================================================
COLLECT_START "" @server_config
{
  "forbiddenGadgets": [],
  "allowDuplicateCharacters": true,
  "minimumCharacters": 13,
  "storyAuthorSleepThresholdMs": 25,
  "timezone": "UTC+1",
  "timeoutDetectionTime": 9,
  "unexpectedReconnect": false,
  "sendMetaOnConnectionOpen": true,
  "numberOfNpc": 2,
  "gadgetNpcMinimum": 0,
  "gadgetNpcMaximum": 6,
  "npcMinDelay": 0,
  "npcMaxDelay": 0,
  "npcHasRightKeyOnSpyChance": 0.65,
  "useIslandsForSpawn": true,
  "islandSpawnerMayMorePlayers": false,
  "escalateOnTooLateOperation": true,
  "swapIfMoveOnJanitor": true,
  "closestFreeFieldFadesThroughWall": true,
  "closestCharacterByWalk": true,
  "catMayJump": false,
  "offerReplay": true,
  "matchconfigDistanceEuclidean": false,
  "flapsAndSealsThroughWall": true,
  "moledieIsDirectAttack": true,
  "bowlerBladeLineOfSightInterruptedByCatOrJanitor": true,
  "laserCompactIsDirectAttack": true,
  "jetpackAllowedOnIsWalkable": false,
  "liveView": false,
  "resumeByBoth": false,
  "allMpApOnBeginOfTurn": true,
  "gameStatusOnTurnStart": false,
  "receiveInPause": true,
  "sendEmptyOperationListOnStart": true,
  "npcHasAtLeastOneKey": true,
  "clearExfiltrationOnNextRound": false,
  "fogHitsWalledFields": false,
  "catAndJanitorHaveUuid": false,
  "catAndJanitorAreActiveCharacter": false,
  "forgetResumesOnCrash": true,
  "npcShouldMove": true,
  "npcSecretMaySkip": 1,
  "aiTurnDelay": 0,
  "gameStatusDelay": 0,
  "performSemantics": true,
  "minimumSplitsForTile": 3,
  "minimumSafeNeighbours": 1,
  "allSafesConnected": false,
  "magpieToConsole": true,
  "magpieShouldUseRainbow": true,
  "storyAuthorVerbosity": 0,
  "runAsClient": false,
  "dropToCommandLine": false
}
COLLECT_END
CONFIG_INJECT server-config RAW-JSON ${@server_config}
COLLECT_START "" @scenario
  {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FIREPLACE","WALL","BAR_TABLE","BAR_SEAT","FREE","FREE","WALL","WALL"],["WALL","SAFE","FREE","FREE","FREE","FREE","WALL","FREE","WALL"],["WALL","BAR_TABLE","FREE","ROULETTE_TABLE","FREE","FREE","FREE","FREE","WALL"],["WALL","BAR_SEAT","FREE","WALL","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","WALL","WALL","FREE","WALL","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","WALL","FREE","FREE","WALL"],["WALL","SAFE","WALL","FREE","FREE","FREE","FREE","SAFE","WALL"],["WALL","FREE","WALL","FREE","FREE","FREE","FREE","WALL","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
COLLECT_END
CONFIG_INJECT scenario RAW-JSON ${@scenario}
COLLECT_START "" @matchconfig
{
  "moledieRange": 1,
  "bowlerBladeRange": 1,
  "bowlerBladeHitChance": 0.25,
  "bowlerBladeDamage": 40,
  "laserCompactHitChance": 0.4,
  "rocketPenDamage": 40,
  "gasGlossDamage": 60,
  "mothballPouchRange": 2,
  "mothballPouchDamage": 70,
  "fogTinRange": 2,
  "grappleRange": 3,
  "grappleHitChance": 0.65,
  "wiretapWithEarplugsFailChance": 0.64,
  "mirrorSwapChance": 0.35,
  "cocktailDodgeChance": 0.25,
  "cocktailHp": 30,
  "spySuccessChance": 0.65,
  "babysitterSuccessChance": 0.25,
  "honeyTrapSuccessChance": 0.35,
  "observationSuccessChance": 0.12,
  "chipsToIpFactor": 12,
  "secretToIpFactor": 3,
  "minChipsRoulette": 0,
  "maxChipsRoulette": 6,
  "roundLimit": 6,
  "turnPhaseLimit": 120,
  "catIp": 8,
  "strikeMaximum": 4,
  "pauseLimit": 320,
  "reconnectLimit": 200
}
COLLECT_END
CONFIG_INJECT matchconfig RAW-JSON ${@matchconfig}
COLLECT_START "" @characters
[
  {"characterId":"e67f3379-fc19-44de-af7d-e9f3961b3eca","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"2ed4e1a3-5ec6-431d-b3d2-352f4451d656","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"6fa4fa21-184a-483f-87ba-30888f335f5b","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"6b1ad71a-5a88-4045-9e51-13f1dc391a7c","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"bb5df41c-ac4b-4f3e-9824-547c48b19619","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"3571ffe8-a252-4c54-89ba-95caa181eed3","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"166661e4-ca96-446f-89a5-4c662ebe8153","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"9cea97b2-8f16-4778-80f2-4041aa575113","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"9aedd31c-4086-450e-8635-66387bbc3c26","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"612632fb-ad14-452a-b066-ee7916cdae8c","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"9f0777e3-cd2b-4607-94e8-7ea806e9fd61","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"4272e7dc-ccfc-4a41-b263-e4e6688b5084","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"06a85e96-030b-4c53-ad39-b59eeb537320","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"22beb5b6-faa7-4842-8688-0576eb441a79","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"b9d6065f-04df-4582-bcf0-229f3b0534eb","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"2991ca0d-3215-4b54-8b94-641fcec612fc","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"37f2313c-e535-4a83-b169-9485e3482765","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Mister Y,Hans Peter Otto,Zackiger Zacharias,wiretap_with_earplugs,fog_tin,poison_pills"
CONFIG_INJECT next-proposal Brandstifter "Nr. 7,Spröder Senf,Schleim B. Olzen,jetpack,nugget,anti_plague_mask"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "The legendary Gustav,Meister Yoda,Zackiger Zacharias,pocket_litter,laser_compact,technicolour_prism"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Petterson,Misses Y,Nr. 5,fog_tin,laser_compact,poison_pills"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Misses Y,Petterson,Mister X,hairdryer,wiretap_with_earplugs,mirror_of_wilderness"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Saphira,Zackiger Zacharias,Tante Gertrude,hairdryer,rocket_pen,fog_tin"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Ein Wischmob,Nr. 5,Tante Gertrude,wiretap_with_earplugs,pocket_litter,laser_compact"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Mister X,Nr. 5,Meister Yoda,laser_compact,gas_gloss,moledie"
CONFIG_INJECT next-proposal "Nicht das X, das Sie suchen" "Mister X,Zackiger Zacharias,The legendary Gustav,moledie,laser_compact,chicken_feed"
CONFIG_INJECT next-proposal Brandstifter "Nr. 5,The legendary Gustav,Tante Gertrude,hairdryer,moledie,grapple"
CONFIG_INJECT next-proposal Brandstifter "Saphira,Nr. 5,Spröder Senf,fog_tin,grapple,hairdryer"
CONFIG_INJECT next-proposal Brandstifter "Hans Peter Otto,Nr. 5,James Bond,wiretap_with_earplugs,mothball_pouch,magnetic_watch"
CONFIG_INJECT next-proposal Brandstifter "Tante Gertrude,Saphira,Nr. 5,magnetic_watch,bowler_blade,moledie"
CONFIG_INJECT next-proposal Brandstifter "Austauschbarer Agent Dieter 42,Mister X,Nr. 5,wiretap_with_earplugs,pocket_litter,nugget"
CONFIG_INJECT next-proposal Brandstifter "Mister X,Nr. 7,Spröder Senf,anti_plague_mask,wiretap_with_earplugs,moledie"
CONFIG_INJECT next-proposal Brandstifter "Nr. 5,Tante Gertrude,Schleim B. Olzen,anti_plague_mask,magnetic_watch,mirror_of_wilderness"
CONFIG_INJECT safe-order value 2,1,3
CONFIG_INJECT npc-pick value "Misses Y,MIRROR_OF_WILDERNESS,LASER_COMPACT,MAGNETIC_WATCH,MOLEDIE,GRAPPLE,POCKET_LITTER,James Bond,HAIRDRYER"
CONFIG_INJECT start-positions value "<cat>,5/3,Ein Wischmob,2/3,James Bond,6/3,Misses Y,2/4,Mister X,7/3,Mister Y,4/4,Petterson,3/7,Tante Gertrude,7/4,The legendary Gustav,1/11"
CONFIG_INJECT next-round-order value "Ein Wischmob,Mister Y,<cat>,The legendary Gustav,Tante Gertrude,James Bond,Misses Y,Petterson,Mister X"
CONFIG_INJECT next-round-order value "Petterson,<cat>,Misses Y,James Bond,Tante Gertrude,Mister Y,Ein Wischmob,The legendary Gustav,Mister X"
CONFIG_INJECT next-round-order value "Petterson,James Bond,Misses Y,Tante Gertrude,Mister X,The legendary Gustav,Mister Y,Ein Wischmob,<cat>"
CONFIG_INJECT next-round-order value "Tante Gertrude,Petterson,Mister Y,James Bond,Misses Y,<cat>,Ein Wischmob,Mister X,The legendary Gustav"
CONFIG_INJECT next-round-order value "The legendary Gustav,Ein Wischmob,Mister Y,Mister X,Petterson,Misses Y,Tante Gertrude,James Bond,<cat>"
CONFIG_INJECT next-round-order value "Petterson,Ein Wischmob,Tante Gertrude,The legendary Gustav,Mister Y,<janitor>,<cat>,Mister X"
CONFIG_INJECT next-round-order value "<janitor>,Mister Y,Mister X,Ein Wischmob,<cat>,Petterson,The legendary Gustav"
CONFIG_INJECT next-round-order value "Mister X,The legendary Gustav,Mister Y,<cat>,Ein Wischmob,<janitor>"
CONFIG_INJECT next-round-order value "<cat>,Ein Wischmob,<janitor>,Mister Y,The legendary Gustav"
CONFIG_INJECT next-round-order value "The legendary Gustav,<janitor>,Mister Y,<cat>"
CONFIG_INJECT next-round-order value "<janitor>,<cat>,The legendary Gustav"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Tante Gertrude:false"
CONFIG_INJECT random-result NPC_MOVEMENT "James Bond:(5,4);(6,5);(7,5);(6,4);(5,4);(4,4);(5,4);(4,4);(4,5);(3,5)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "James Bond:0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "Ein Wischmob:false"
CONFIG_INJECT random-result OPERATION_SUCCESS Petterson:true
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:true;true;false;true;false;false;false;true;true;true;true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Misses Y:false"
CONFIG_INJECT random-result NPC_MOVEMENT "Misses Y:(1,5);(1,6);(2,6);(3,5);(2,6);(2,5);(2,4);(2,5);(3,4);(2,5)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "Misses Y:(3,5)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Misses Y:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CLOSEST_FREE_FIELD_FADE global:(7,2)
CONFIG_INJECT random-result CAT_WALK_TARGET global:(5,2);(4,2);(5,2);(4,3);(4,4);(3,4);(2,5);(1,4);(1,5);(2,5)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(1,6)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:0
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:true;false;false;false;true;true;false;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO "Nicht das X, das Sie suchen" PLAYER
HELLO Brandstifter PLAYER
ITEM "Nicht das X, das Sie suchen" "Mister Y"
ITEM "Nicht das X, das Sie suchen" technicolour_prism
ITEM "Nicht das X, das Sie suchen" poison_pills
ITEM "Nicht das X, das Sie suchen" Petterson
ITEM "Nicht das X, das Sie suchen" rocket_pen
ITEM "Nicht das X, das Sie suchen" "Ein Wischmob"
ITEM "Nicht das X, das Sie suchen" gas_gloss
ITEM "Nicht das X, das Sie suchen" chicken_feed
EQUIP "Nicht das X, das Sie suchen" "Mister Y,GAS_GLOSS,POISON_PILLS,TECHNICOLOUR_PRISM,ROCKET_PEN,Petterson,CHICKEN_FEED,Ein Wischmob"
ITEM Brandstifter jetpack
ITEM Brandstifter "The legendary Gustav"
ITEM Brandstifter fog_tin
ITEM Brandstifter mothball_pouch
ITEM Brandstifter bowler_blade
ITEM Brandstifter nugget
ITEM Brandstifter "Mister X"
ITEM Brandstifter "Tante Gertrude"
EQUIP Brandstifter "Mister X,FOG_TIN,The legendary Gustav,NUGGET,MOTHBALL_POUCH,Tante Gertrude,BOWLER_BLADE,JETPACK"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(2,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[GAS_GLOSS(1), POISON_PILLS(5), TECHNICOLOUR_PRISM(1), ROCKET_PEN(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (3,4),gadget:ROCKET_PEN
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(7,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[BOWLER_BLADE, JETPACK(1)], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (6,3),gadget:BOWLER_BLADE
OPERATION Brandstifter MOVEMENT (6,4)
OPERATION Brandstifter MOVEMENT (5,4)
OPERATION Brandstifter MOVEMENT (4,3)
# Turn for: Character [characterId=e67f3379-fc19-44de-af7d-e9f3961b3eca, name='James Bond', coordinates=(6,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=06a85e96-030b-4c53-ad39-b59eeb537320, name='Misses Y', coordinates=(2,4), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MIRROR_OF_WILDERNESS, LASER_COMPACT, MAGNETIC_WATCH, MOLEDIE, GRAPPLE, POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(3,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (3,6)
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (3,5)
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(7,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[FOG_TIN(1)], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (5,4),gadget:FOG_TIN
OPERATION Brandstifter MOVEMENT (6,3)
OPERATION Brandstifter MOVEMENT (5,3)
OPERATION Brandstifter MOVEMENT (4,3)
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(3,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=06a85e96-030b-4c53-ad39-b59eeb537320, name='Misses Y', coordinates=(1,6), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MIRROR_OF_WILDERNESS, LASER_COMPACT, MAGNETIC_WATCH, GRAPPLE, POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=e67f3379-fc19-44de-af7d-e9f3961b3eca, name='James Bond', coordinates=(6,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(5,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (4,2)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,4), mp=2, ap=1, hp=60/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[GAS_GLOSS(1), POISON_PILLS(5), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (4,3)
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (3,2)
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (3,1),gadget:POISON_PILLS
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(2,3), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(4,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" SPY_ACTION (3,5)
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=e67f3379-fc19-44de-af7d-e9f3961b3eca, name='James Bond', coordinates=(6,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=06a85e96-030b-4c53-ad39-b59eeb537320, name='Misses Y', coordinates=(3,5), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MIRROR_OF_WILDERNESS, LASER_COMPACT, MAGNETIC_WATCH, GRAPPLE, POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(4,2), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION Brandstifter MOVEMENT (4,3)
OPERATION Brandstifter MOVEMENT (3,4)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(5,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (4,3)
OPERATION Brandstifter MOVEMENT (4,2)
OPERATION Brandstifter SPY_ACTION (3,2)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(3,2), mp=2, ap=1, hp=60/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[GAS_GLOSS(1), POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (4,1)
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (4,2),gadget:GAS_GLOSS
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(2,3), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (1,4)
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (1,4),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(3,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[JETPACK(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (2,3)
OPERATION Brandstifter GADGET_ACTION (2,3),gadget:COCKTAIL
OPERATION Brandstifter MOVEMENT (3,4)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(3,5), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (2,4)
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (3,5)
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=e67f3379-fc19-44de-af7d-e9f3961b3eca, name='James Bond', coordinates=(4,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=06a85e96-030b-4c53-ad39-b59eeb537320, name='Misses Y', coordinates=(2,5), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MIRROR_OF_WILDERNESS, LASER_COMPACT, MAGNETIC_WATCH, GRAPPLE, POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(1,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" SPY_ACTION (2,5)
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(4,2), mp=2, ap=2, hp=40/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (5,3)
OPERATION Brandstifter MOVEMENT (6,4)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(1,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (1,4),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (4,1),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(6,4), mp=3, ap=1, hp=40/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(3,5), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=06a85e96-030b-4c53-ad39-b59eeb537320, name='Misses Y', coordinates=(2,5), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MIRROR_OF_WILDERNESS, LASER_COMPACT, MAGNETIC_WATCH, GRAPPLE, POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(3,4), mp=3, ap=1, hp=70/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=e67f3379-fc19-44de-af7d-e9f3961b3eca, name='James Bond', coordinates=(4,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[HAIRDRYER], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=612632fb-ad14-452a-b066-ee7916cdae8c, name='Petterson', coordinates=(4,5), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(1,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=6fa4fa21-184a-483f-87ba-30888f335f5b, name='Tante Gertrude', coordinates=(3,4), mp=3, ap=1, hp=70/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(6,4), mp=3, ap=1, hp=40/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (6,5)
OPERATION Brandstifter MOVEMENT (6,6)
OPERATION Brandstifter MOVEMENT (6,7)
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION "Nicht das X, das Sie suchen" MOVEMENT (4,2)
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(6,7), mp=2, ap=2, hp=40/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(1,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=9f0777e3-cd2b-4607-94e8-7ea806e9fd61, name='Mister X', coordinates=(6,7), mp=3, ap=1, hp=40/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(2,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=3571ffe8-a252-4c54-89ba-95caa181eed3, name='Ein Wischmob', coordinates=(2,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=4272e7dc-ccfc-4a41-b263-e4e6688b5084, name='Mister Y', coordinates=(4,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Nicht das X, das Sie suchen" RETIRE <ignored>
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=6b1ad71a-5a88-4045-9e51-13f1dc391a7c, name='The legendary Gustav', coordinates=(1,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[NUGGET(1), MOTHBALL_POUCH(5), MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# =============================================================================
# Winner: Nicht das X, das Sie suchen for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 363 Player Two: 360
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 9 Player Two: 18
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 80 Player Two: 90
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
# End of File
