# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-Frau Holle-2020-07-03--02-35-24--10253349933759647401.story
# Date: Fri Jul 03 02:35:24 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Fri Jul 03 02:35:24 CEST 2020"
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
  {"characterId":"660a2a61-d1d3-4afd-ac12-90215ca8f6ca","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"c8707280-2b75-44a5-a43c-c39e0eac1fcb","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"be5134d5-125a-4c11-9d74-afa6f0419ded","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"1b082958-f9c9-4ace-b57a-188bb2e8e2ea","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"99638c39-34c5-4f8a-a478-281ed6e02329","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"dbacd25a-260b-45de-b11a-774bd72c0fba","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"dc3ba102-d696-41eb-ae8a-98fb1161b29f","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"755457e9-4ced-486c-b704-22ee585ef001","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"b3bb69e6-27e9-4c94-a74a-4a3c480413c1","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"a1bb9184-d6c6-4f8e-9b91-09f06f686bad","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"459f17cc-c296-4dbd-a941-604b094e2fc0","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"1ba7246c-45a2-41cc-acd3-118b4a2ce5c0","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"7de23043-4592-4772-9c49-81105ff41c40","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"02434fb5-5b1e-4e63-8a48-7741d66f68d0","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"0f2cbbd7-0411-4ac5-b6b4-aae697cb1fd2","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"e24c5384-0648-45f7-9de0-38a44795c02b","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"af755fc9-1bff-4902-b020-90e340c22840","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal Dieter "Meister Yoda,Austauschbarer Agent Dieter 42,Saphira,magnetic_watch,technicolour_prism,mirror_of_wilderness"
CONFIG_INJECT next-proposal "Frau Holle" "The legendary Gustav,Tante Gertrude,Zackiger Zacharias,rocket_pen,grapple,jetpack"
CONFIG_INJECT next-proposal "Frau Holle" "Schleim B. Olzen,Nr. 7,Mister X,rocket_pen,poison_pills,jetpack"
CONFIG_INJECT next-proposal "Frau Holle" "Nr. 5,Mister Y,Schleim B. Olzen,chicken_feed,anti_plague_mask,wiretap_with_earplugs"
CONFIG_INJECT next-proposal "Frau Holle" "Mister X,Hans Peter Otto,Zackiger Zacharias,jetpack,moledie,grapple"
CONFIG_INJECT next-proposal "Frau Holle" "Ein Wischmob,Petterson,Mister Y,moledie,nugget,mothball_pouch"
CONFIG_INJECT next-proposal "Frau Holle" "Hans Peter Otto,Mister X,Nr. 7,anti_plague_mask,nugget,chicken_feed"
CONFIG_INJECT next-proposal "Frau Holle" "Tante Gertrude,Spröder Senf,Schleim B. Olzen,bowler_blade,fog_tin,chicken_feed"
CONFIG_INJECT next-proposal "Frau Holle" "Schleim B. Olzen,Spröder Senf,Mister Y,gas_gloss,pocket_litter,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Dieter "Hans Peter Otto,James Bond,Meister Yoda,mirror_of_wilderness,wiretap_with_earplugs,anti_plague_mask"
CONFIG_INJECT next-proposal Dieter "Misses Y,Petterson,Nr. 7,hairdryer,technicolour_prism,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Dieter "Mister Y,James Bond,Petterson,technicolour_prism,moledie,fog_tin"
CONFIG_INJECT next-proposal Dieter "Spröder Senf,Misses Y,Saphira,mirror_of_wilderness,gas_gloss,laser_compact"
CONFIG_INJECT next-proposal Dieter "Zackiger Zacharias,Tante Gertrude,Petterson,wiretap_with_earplugs,moledie,fog_tin"
CONFIG_INJECT next-proposal Dieter "Petterson,Hans Peter Otto,Meister Yoda,fog_tin,mothball_pouch,poison_pills"
CONFIG_INJECT next-proposal Dieter "Mister Y,Mister X,Hans Peter Otto,chicken_feed,poison_pills,hairdryer"
CONFIG_INJECT safe-order value 2,1,3
CONFIG_INJECT npc-pick value "Zackiger Zacharias,FOG_TIN,Mister X,MOLEDIE,POISON_PILLS,POCKET_LITTER"
CONFIG_INJECT start-positions value "<cat>,4/5,Ein Wischmob,1/11,Mister X,5/3,Nr. 5,5/11,Nr. 7,6/7,Petterson,5/4,Schleim B. Olzen,7/6,Tante Gertrude,1/7,The legendary Gustav,1/9,Zackiger Zacharias,4/1"
CONFIG_INJECT next-round-order value "<cat>,Mister X,Nr. 7,Schleim B. Olzen,Petterson,Tante Gertrude,The legendary Gustav,Nr. 5,Ein Wischmob,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "Mister X,The legendary Gustav,Petterson,Ein Wischmob,Zackiger Zacharias,<cat>,Tante Gertrude,Schleim B. Olzen,Nr. 5,Nr. 7"
CONFIG_INJECT next-round-order value "Nr. 7,The legendary Gustav,Petterson,Nr. 5,Ein Wischmob,Zackiger Zacharias,Tante Gertrude,Schleim B. Olzen,<cat>,Mister X"
CONFIG_INJECT next-round-order value "Nr. 7,Zackiger Zacharias,Ein Wischmob,<cat>,Mister X,Tante Gertrude,Schleim B. Olzen,Nr. 5,Petterson,The legendary Gustav"
CONFIG_INJECT next-round-order value "Zackiger Zacharias,Schleim B. Olzen,Nr. 5,Petterson,Mister X,<cat>,The legendary Gustav,Ein Wischmob,Nr. 7,Tante Gertrude"
CONFIG_INJECT next-round-order value "Schleim B. Olzen,The legendary Gustav,Tante Gertrude,Ein Wischmob,Petterson,Nr. 5,<cat>,<janitor>,Nr. 7"
CONFIG_INJECT next-round-order value "<janitor>,<cat>,Nr. 7,Ein Wischmob,The legendary Gustav,Petterson,Nr. 5,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Nr. 5,<cat>,Schleim B. Olzen,Nr. 7,Petterson,Ein Wischmob,<janitor>"
CONFIG_INJECT next-round-order value "Nr. 7,<cat>,Schleim B. Olzen,Petterson,Ein Wischmob,<janitor>"
CONFIG_INJECT next-round-order value "Petterson,<cat>,<janitor>,Nr. 7,Ein Wischmob"
CONFIG_INJECT next-round-order value "<janitor>,Nr. 7,Ein Wischmob,<cat>"
CONFIG_INJECT next-round-order value "<janitor>,Ein Wischmob,<cat>"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Tante Gertrude:true;true"
CONFIG_INJECT random-result OPERATION_SUCCESS "Ein Wischmob:true"
CONFIG_INJECT random-result HONEY_TRAP_TRIGGERS "Ein Wischmob:false"
CONFIG_INJECT random-result OPERATION_SUCCESS "Schleim B. Olzen:true;true"
CONFIG_INJECT random-result OPERATION_SUCCESS Petterson:false;true
CONFIG_INJECT random-result GAMBLE_WIN Petterson:true
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Zackiger Zacharias:true"
CONFIG_INJECT random-result NPC_MOVEMENT "Zackiger Zacharias:(5,1);(6,1);(7,2);(6,3);(6,4);(6,3);(6,4)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Zackiger Zacharias:1"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Zackiger Zacharias:0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Zackiger Zacharias:true;true;true;false;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "The legendary Gustav:false;false;false;false"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:false;true;true;false;true;false;false"
CONFIG_INJECT random-result CLOSEST_FREE_FIELD_FADE global:(1,9)
CONFIG_INJECT random-result CAT_WALK_TARGET global:(3,6);(2,7);(1,7);(1,6);(2,7);(3,5);(4,4);(3,5);(2,6);(3,8);(3,9)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(6,3)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:2
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Mister X:true;false"
CONFIG_INJECT random-result NPC_MOVEMENT "Mister X:(6,4);(5,3);(6,3);(7,4);(6,4);(5,3);(6,4);(5,4);(4,5);(5,4);(6,3);(7,2);(7,3)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "Mister X:(5,4)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Mister X:0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Mister X:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:true;false;true;true;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "Nr. 7:false"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Nr. 7:false;true;false;false;false;false;true;false;true;false;false"
# =============================================================================
# This is the main part
# =============================================================================
HELLO Dieter PLAYER
HELLO "Frau Holle" PLAYER
ITEM "Frau Holle" "The legendary Gustav"
ITEM "Frau Holle" rocket_pen
ITEM "Frau Holle" "Nr. 5"
ITEM "Frau Holle" jetpack
ITEM "Frau Holle" "Ein Wischmob"
ITEM "Frau Holle" nugget
ITEM "Frau Holle" bowler_blade
ITEM "Frau Holle" "Schleim B. Olzen"
EQUIP "Frau Holle" "Schleim B. Olzen,NUGGET,JETPACK,The legendary Gustav,ROCKET_PEN,Ein Wischmob,BOWLER_BLADE,Nr. 5"
ITEM Dieter magnetic_watch
ITEM Dieter anti_plague_mask
ITEM Dieter "Nr. 7"
ITEM Dieter technicolour_prism
ITEM Dieter gas_gloss
ITEM Dieter "Tante Gertrude"
ITEM Dieter Petterson
ITEM Dieter chicken_feed
EQUIP Dieter "Nr. 7,GAS_GLOSS,Tante Gertrude,CHICKEN_FEED,TECHNICOLOUR_PRISM,Petterson,MAGNETIC_WATCH,ANTI_PLAGUE_MASK"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=459f17cc-c296-4dbd-a941-604b094e2fc0, name='Mister X', coordinates=(5,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MOLEDIE, POISON_PILLS(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(6,7), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (5,6)
OPERATION Dieter MOVEMENT (4,5)
OPERATION Dieter MOVEMENT (3,5)
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(7,6), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), JETPACK(1)], exfiltrated=false]
OPERATION "Frau Holle" GADGET_ACTION (4,2),gadget:JETPACK
OPERATION "Frau Holle" MOVEMENT (4,1)
OPERATION "Frau Holle" MOVEMENT (3,2)
OPERATION "Frau Holle" MOVEMENT (2,2)
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(5,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK, MOLEDIE], exfiltrated=false]
OPERATION Dieter MOVEMENT (4,4)
OPERATION Dieter GADGET_ACTION (5,3),gadget:MOLEDIE
OPERATION Dieter MOVEMENT (3,5)
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(1,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (2,6)
OPERATION Dieter MOVEMENT (3,6)
OPERATION Dieter MOVEMENT (4,5)
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(1,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION "Frau Holle" GADGET_ACTION (0,8),gadget:ROCKET_PEN
OPERATION "Frau Holle" MOVEMENT (1,8)
OPERATION "Frau Holle" MOVEMENT (0,8)
OPERATION "Frau Holle" PROPERTY_ACTION (4,4),property:OBSERVATION
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(5,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (6,11)
OPERATION "Frau Holle" MOVEMENT (6,10)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[BOWLER_BLADE, MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" GADGET_ACTION (1,11),gadget:BOWLER_BLADE
OPERATION "Frau Holle" GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dc3ba102-d696-41eb-ae8a-98fb1161b29f, name='Zackiger Zacharias', coordinates=(4,2), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[FOG_TIN(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=459f17cc-c296-4dbd-a941-604b094e2fc0, name='Mister X', coordinates=(6,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[POISON_PILLS(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(0,8), mp=3, ap=1, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (1,7)
OPERATION "Frau Holle" MOVEMENT (2,6)
OPERATION "Frau Holle" SPY_ACTION (3,5)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(3,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter MOVEMENT (2,4)
OPERATION Dieter MOVEMENT (2,3)
OPERATION Dieter SPY_ACTION (2,2)
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dc3ba102-d696-41eb-ae8a-98fb1161b29f, name='Zackiger Zacharias', coordinates=(5,1), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[FOG_TIN(1)], exfiltrated=false]
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(4,5), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,5)
OPERATION Dieter MOVEMENT (2,4)
OPERATION Dieter GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION Dieter MOVEMENT (3,5)
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(2,2), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1)], exfiltrated=false]
OPERATION "Frau Holle" GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION "Frau Holle" MOVEMENT (3,2)
OPERATION "Frau Holle" MOVEMENT (4,3)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(6,10), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (6,9)
OPERATION "Frau Holle" MOVEMENT (6,8)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(4,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (5,4)
OPERATION Dieter SPY_ACTION (6,4)
OPERATION Dieter MOVEMENT (5,3)
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(5,3), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1)], exfiltrated=false]
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(2,6), mp=3, ap=1, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (3,6)
OPERATION "Frau Holle" MOVEMENT (4,7)
OPERATION "Frau Holle" MOVEMENT (5,7)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(2,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,2)
OPERATION Dieter MOVEMENT (4,2)
OPERATION Dieter GAMBLE_ACTION (3,3),stake:2
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(6,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (7,7)
OPERATION "Frau Holle" MOVEMENT (7,6)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dc3ba102-d696-41eb-ae8a-98fb1161b29f, name='Zackiger Zacharias', coordinates=(6,1), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[FOG_TIN(1)], exfiltrated=false]
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(3,5), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (4,4)
OPERATION Dieter MOVEMENT (5,4)
OPERATION Dieter SPY_ACTION (6,4)
OPERATION Dieter MOVEMENT (6,3)
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(4,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (5,3)
OPERATION "Frau Holle" MOVEMENT (5,4)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=459f17cc-c296-4dbd-a941-604b094e2fc0, name='Mister X', coordinates=(6,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[POISON_PILLS(5), POCKET_LITTER], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(5,3), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (4,4)
OPERATION Dieter MOVEMENT (4,5)
OPERATION Dieter MOVEMENT (3,6)
# Turn for: Character [characterId=dc3ba102-d696-41eb-ae8a-98fb1161b29f, name='Zackiger Zacharias', coordinates=(7,2), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[FOG_TIN(1)], exfiltrated=false]
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=459f17cc-c296-4dbd-a941-604b094e2fc0, name='Mister X', coordinates=(5,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[POISON_PILLS(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(7,2), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Dieter SPY_ACTION (6,3)
OPERATION Dieter MOVEMENT (7,3)
OPERATION Dieter MOVEMENT (7,4)
OPERATION Dieter MOVEMENT (7,5)
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(5,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (6,4)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(7,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(4,2), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(5,7), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (4,7)
OPERATION "Frau Holle" MOVEMENT (3,7)
OPERATION "Frau Holle" SPY_ACTION (3,6)
OPERATION "Frau Holle" SPY_ACTION (3,6)
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=dc3ba102-d696-41eb-ae8a-98fb1161b29f, name='Zackiger Zacharias', coordinates=(6,4), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[FOG_TIN(1)], exfiltrated=false]
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(5,4), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (6,3)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(7,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (7,5)
OPERATION "Frau Holle" MOVEMENT (6,7)
OPERATION "Frau Holle" MOVEMENT (5,7)
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(4,2), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK, COCKTAIL(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (4,3)
OPERATION Dieter MOVEMENT (4,4)
OPERATION Dieter GADGET_ACTION (5,4),gadget:COCKTAIL
# Turn for: Character [characterId=459f17cc-c296-4dbd-a941-604b094e2fc0, name='Mister X', coordinates=(6,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[POISON_PILLS(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(3,7), mp=3, ap=1, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (1,10)
OPERATION "Frau Holle" SPY_ACTION (1,10)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(3,6), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (2,7)
OPERATION Dieter MOVEMENT (1,8)
OPERATION Dieter MOVEMENT (1,9)
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(7,5), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Dieter GADGET_ACTION (6,4),gadget:CHICKEN_FEED
OPERATION Dieter MOVEMENT (6,5)
OPERATION Dieter MOVEMENT (5,6)
OPERATION Dieter MOVEMENT (4,5)
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(5,4), mp=3, ap=1, hp=100/100, ip=6, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (4,3)
OPERATION "Frau Holle" MOVEMENT (3,2)
OPERATION "Frau Holle" MOVEMENT (2,2)
OPERATION "Frau Holle" SPY_ACTION (1,2)
# Turn for: Character [characterId=1b082958-f9c9-4ace-b57a-188bb2e8e2ea, name='The legendary Gustav', coordinates=(3,7), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (4,7)
OPERATION "Frau Holle" MOVEMENT (5,6)
OPERATION "Frau Holle" SPY_ACTION (4,5)
OPERATION "Frau Holle" PROPERTY_ACTION (4,5),property:OBSERVATION
# Turn for: Character [characterId=be5134d5-125a-4c11-9d74-afa6f0419ded, name='Tante Gertrude', coordinates=(4,5), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Dieter SPY_ACTION (5,6)
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" SPY_ACTION (1,10)
OPERATION "Frau Holle" GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(4,4), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,5)
OPERATION Dieter MOVEMENT (2,6)
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (6,8)
OPERATION "Frau Holle" MOVEMENT (7,9)
OPERATION "Frau Holle" SPY_ACTION (7,10)
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(1,9), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1), BOWLER_BLADE], exfiltrated=false]
OPERATION Dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(1,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1), BOWLER_BLADE], exfiltrated=false]
OPERATION Dieter SPY_ACTION (1,10)
OPERATION Dieter MOVEMENT (1,8)
OPERATION Dieter MOVEMENT (2,9)
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(7,9), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (6,8)
OPERATION "Frau Holle" MOVEMENT (5,7)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(2,2), mp=3, ap=1, hp=100/100, ip=9, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (2,3)
OPERATION "Frau Holle" MOVEMENT (2,4)
OPERATION "Frau Holle" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=e24c5384-0648-45f7-9de0-38a44795c02b, name='Nr. 5', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(2,4), mp=3, ap=1, hp=100/100, ip=9, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" MOVEMENT (2,5)
OPERATION "Frau Holle" MOVEMENT (3,6)
OPERATION "Frau Holle" MOVEMENT (3,5)
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(2,9), mp=3, ap=0, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1), BOWLER_BLADE], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,8)
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(3,8), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1), BOWLER_BLADE], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,9)
OPERATION Dieter MOVEMENT (3,10)
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=755457e9-4ced-486c-b704-22ee585ef001, name='Schleim B. Olzen', coordinates=(3,5), mp=3, ap=1, hp=100/100, ip=9, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Frau Holle" GADGET_ACTION (3,6),gadget:NUGGET
OPERATION "Frau Holle" RETIRE <ignored>
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(3,6), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK, NUGGET(1)], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,7)
OPERATION Dieter MOVEMENT (2,6)
OPERATION Dieter GADGET_ACTION (3,5),gadget:NUGGET
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=a1bb9184-d6c6-4f8e-9b91-09f06f686bad, name='Petterson', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=12, properties=[HONEY_TRAP, BABYSITTER, FLAPS_AND_SEALS], gadgets=[MAGNETIC_WATCH, ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Dieter RETIRE <ignored>
# Turn for: Character [characterId=af755fc9-1bff-4902-b020-90e340c22840, name='Nr. 7', coordinates=(3,10), mp=3, ap=0, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[GAS_GLOSS(1), BOWLER_BLADE], exfiltrated=false]
OPERATION Dieter MOVEMENT (3,11)
OPERATION Dieter MOVEMENT (3,10)
OPERATION Dieter MOVEMENT (3,9)
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# Turn for: Character [characterId=dbacd25a-260b-45de-b11a-774bd72c0fba, name='Ein Wischmob', coordinates=(1,11), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Frau Holle" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
# =============================================================================
# Winner: Frau Holle for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 390 Player Two: 495
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 46 Player Two: 37
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 80
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
# End of File
