# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-loss-Unknown-2020-07-20--07-54-56--11481955438942185094.story
# Date: Mon Jul 20 19:54:56 CEST 2020
# Server-Version: 1.1 (using Game-Data v1.2)
# =============================================================================
SET story-name server020
SET story-date "Mon Jul 20 19:54:56 CEST 2020"
FORBID_ERRORS
# =============================================================================
# This is default-configs part, which will set the used scenario, ...
# =============================================================================
COLLECT_START "" @server_config
{
  "forbiddenGadgets": [],
  "allowDuplicateCharacters": true,
  "minimumCharacters": 13,
  "storyAuthorSleepThresholdMs": 35,
  "timezone": "UTC+1",
  "timeoutDetectionTime": 9,
  "unexpectedReconnect": false,
  "sendMetaOnConnectionOpen": true,
  "numberOfNpc": 3,
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
  "gameStatusOnTurnStart": true,
  "receiveInPause": true,
  "sendEmptyOperationListOnStart": true,
  "npcHasAtLeastOneKey": true,
  "clearExfiltrationOnNextRound": true,
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
  {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","WALL","WALL","WALL","WALL","WALL","FREE","WALL","WALL","WALL","WALL","FREE","FREE","FREE","WALL","FREE","FREE","FREE","WALL","FREE","FREE","FREE","FREE","ROULETTE_TABLE","WALL","FREE","WALL"],["WALL","FREE","BAR_SEAT","BAR_TABLE","WALL","FREE","FREE","FREE","WALL","BAR_SEAT","FREE","FREE","FREE","FREE","WALL","WALL","WALL","FREE","FREE","WALL","WALL","FREE","BAR_SEAT","FREE","WALL","WALL","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","FREE","WALL","BAR_TABLE","BAR_TABLE","FREE","FREE","WALL","WALL","FREE","WALL","WALL","FREE","WALL","WALL","WALL","BAR_TABLE","FREE","WALL","WALL","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","FREE","FREE","FREE","WALL","WALL","WALL","FREE","FREE","WALL","FREE","FREE","FREE","WALL","FREE","WALL","ROULETTE_TABLE","WALL","WALL","WALL","FREE","WALL","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","BAR_TABLE","FREE","FREE","WALL","ROULETTE_TABLE","FREE","FREE","FREE","WALL","WALL","WALL","WALL","WALL","FREE","WALL","FREE","FREE","WALL","FREE","BAR_SEAT","WALL","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","BAR_TABLE","FREE","FREE","WALL","FIREPLACE","FREE","FREE","FREE","WALL","FIREPLACE","SAFE","FIREPLACE","WALL","FREE","WALL","FREE","FREE","FREE","FREE","ROULETTE_TABLE","WALL","FREE","WALL"],["WALL","ROULETTE_TABLE","FREE","FREE","WALL","FREE","FREE","FREE","WALL","WALL","WALL","WALL","FREE","WALL","FREE","FREE","FREE","WALL","FREE","WALL","FREE","FREE","FREE","FREE","FREE","WALL","FREE","WALL"],["WALL","FIREPLACE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","WALL","FREE","FREE","FREE","FREE","WALL","WALL","WALL","FREE","FREE","FREE","FREE","WALL","WALL","WALL","FREE","FREE","FREE","WALL","WALL","WALL","FREE","FREE","FREE","FREE","WALL","WALL"],["WALL","WALL","WALL","FREE","FREE","WALL","FREE","SAFE","WALL","WALL","FREE","FREE","WALL","WALL","SAFE","WALL","WALL","FREE","WALL","FREE","FREE","WALL","WALL","FREE","FREE","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","WALL","ROULETTE_TABLE","FREE","FREE","WALL","FREE","FREE","WALL","FREE","FREE","FIREPLACE","WALL","FREE","WALL","FREE","FREE","FREE","WALL","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","WALL","ROULETTE_TABLE","FREE","FREE","WALL","FREE","FREE","FREE","FREE","FREE","WALL","WALL","FREE","WALL","FREE","FREE","FREE","WALL","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","WALL","FIREPLACE","BAR_SEAT","FREE","WALL","FREE","FREE","FREE","FREE","WALL","WALL","BAR_SEAT","FREE","WALL","FREE","FREE","FREE","WALL","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","WALL","WALL","BAR_TABLE","FREE","WALL","FREE","FREE","FREE","WALL","WALL","BAR_TABLE","BAR_TABLE","FREE","WALL","WALL","FREE","FREE","WALL","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","FREE","WALL","WALL","WALL","FREE","FREE","FREE","WALL","WALL","WALL","WALL","WALL","FREE","FREE","WALL","WALL","WALL","FREE","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","WALL","WALL","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
COLLECT_END
CONFIG_INJECT scenario RAW-JSON ${@scenario}
COLLECT_START "" @matchconfig
{
  "moledieRange": 6,
  "bowlerBladeRange": 8,
  "bowlerBladeHitChance": 0.85,
  "bowlerBladeDamage": 80,
  "laserCompactHitChance": 0.125,
  "rocketPenDamage": 90,
  "gasGlossDamage": 85,
  "mothballPouchRange": 12,
  "mothballPouchDamage": 65,
  "fogTinRange": 9,
  "grappleRange": 16,
  "grappleHitChance": 0.75,
  "wiretapWithEarplugsFailChance": 0.64,
  "mirrorSwapChance": 0.65,
  "cocktailDodgeChance": 0.15,
  "cocktailHp": 24,
  "spySuccessChance": 0.85,
  "babysitterSuccessChance": 0.25,
  "honeyTrapSuccessChance": 0.385,
  "observationSuccessChance": 0.82,
  "chipsToIpFactor": 8,
  "secretToIpFactor": 24,
  "minChipsRoulette": 2,
  "maxChipsRoulette": 20,
  "roundLimit": 10,
  "turnPhaseLimit": 80,
  "catIp": 56,
  "strikeMaximum": 5,
  "pauseLimit": 320,
  "reconnectLimit": 200
}
COLLECT_END
CONFIG_INJECT matchconfig RAW-JSON ${@matchconfig}
COLLECT_START "" @characters
[
  {"characterId":"f25ed023-f031-4ca0-b8e8-0d78cbdcf907","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"068e74ef-a088-4c97-b3f8-4995af15d60e","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"76d86b49-d53f-4480-b222-b0b9261d1430","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"feb9c5c1-703c-4ab6-b446-6ba28ad747c6","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"421ba664-c739-4784-b961-bdeb08b25dd4","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"1c5d2111-79ee-4a2b-bc25-f5c76bb5cfff","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"85f801fc-6f69-4252-a512-141ba5a8b1f7","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"214e0eaa-d09d-4d61-ae80-ae8c8a072ad2","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"15d4bf54-d5c1-4a64-9add-8155b16dbf2d","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"07eefef8-f0af-4850-9514-117bb9c87c3a","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"b83ea3be-84aa-464e-bf41-10167f93062b","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"ee7240ee-77de-4f77-8394-32ecd9c044d1","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"a777bb0a-db65-4a6d-b728-9ef122e43d15","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"be911c14-3f6c-40d1-bfbf-6f672edc875c","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"77b7fc52-d20c-4ea8-ab93-ffa677a9727b","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"59bbfd7a-fb62-4888-9701-d9a55009636b","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal Du "Meister Yoda,Mister Y,James Bond,mirror_of_wilderness,anti_plague_mask,magnetic_watch"
CONFIG_INJECT next-proposal Florian2 "Ein Wischmob,Tante Gertrude,Schleim B. Olzen,grapple,laser_compact,jetpack"
CONFIG_INJECT next-proposal Florian2 "Mister X,Spröder Senf,Ein Wischmob,poison_pills,laser_compact,chicken_feed"
CONFIG_INJECT next-proposal Florian2 "Saphira,Austauschbarer Agent Dieter 42,Petterson,moledie,pocket_litter,fog_tin"
CONFIG_INJECT next-proposal Florian2 "The legendary Gustav,Ein Wischmob,Misses Y,hairdryer,chicken_feed,jetpack"
CONFIG_INJECT next-proposal Florian2 "Nr. 7,Zackiger Zacharias,Saphira,technicolour_prism,bowler_blade,chicken_feed"
CONFIG_INJECT next-proposal Florian2 "Schleim B. Olzen,Misses Y,Austauschbarer Agent Dieter 42,poison_pills,fog_tin,nugget"
CONFIG_INJECT next-proposal Florian2 "Saphira,Misses Y,Nr. 5,chicken_feed,rocket_pen,pocket_litter"
CONFIG_INJECT next-proposal Florian2 "Zackiger Zacharias,Spröder Senf,Saphira,poison_pills,mothball_pouch,gas_gloss"
CONFIG_INJECT next-proposal Du "Meister Yoda,Misses Y,Saphira,mothball_pouch,hairdryer,fog_tin"
CONFIG_INJECT next-proposal Du "Nr. 7,Mister Y,Tante Gertrude,gas_gloss,rocket_pen,laser_compact"
CONFIG_INJECT next-proposal Du "Austauschbarer Agent Dieter 42,Mister Y,Petterson,anti_plague_mask,gas_gloss,fog_tin"
CONFIG_INJECT next-proposal Du "Saphira,Hans Peter Otto,Austauschbarer Agent Dieter 42,rocket_pen,gas_gloss,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Du "Misses Y,Hans Peter Otto,Nr. 7,bowler_blade,wiretap_with_earplugs,jetpack"
CONFIG_INJECT next-proposal Du "James Bond,Spröder Senf,Misses Y,fog_tin,laser_compact,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Du poison_pills,rocket_pen,mothball_pouch
CONFIG_INJECT safe-order value 1,3,2
CONFIG_INJECT npc-pick value "Saphira,HAIRDRYER,Mister Y,POCKET_LITTER,MIRROR_OF_WILDERNESS,BOWLER_BLADE,WIRETAP_WITH_EARPLUGS,MOTHBALL_POUCH,Hans Peter Otto,LASER_COMPACT,GAS_GLOSS,NUGGET"
CONFIG_INJECT start-positions value "<cat>,23/6,Austauschbarer Agent Dieter 42,23/1,Hans Peter Otto,26/7,James Bond,4/11,Meister Yoda,10/1,Mister X,20/8,Mister Y,23/7,Saphira,13/14,Schleim B. Olzen,8/14,Tante Gertrude,1/7,The legendary Gustav,18/2,Zackiger Zacharias,6/7"
CONFIG_INJECT next-round-order value "Tante Gertrude,Mister X,The legendary Gustav,James Bond,Schleim B. Olzen,Mister Y,Zackiger Zacharias,Saphira,Austauschbarer Agent Dieter 42,Meister Yoda,Hans Peter Otto,<cat>"
CONFIG_INJECT next-round-order value "<cat>,Schleim B. Olzen,Hans Peter Otto,Austauschbarer Agent Dieter 42,Saphira,The legendary Gustav,James Bond,Tante Gertrude,Meister Yoda,Mister X,Mister Y,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "Mister Y,James Bond,Tante Gertrude,Saphira,Mister X,Meister Yoda,Hans Peter Otto,Austauschbarer Agent Dieter 42,Schleim B. Olzen,Zackiger Zacharias,<cat>,The legendary Gustav"
CONFIG_INJECT next-round-order value "Tante Gertrude,Meister Yoda,Zackiger Zacharias,Austauschbarer Agent Dieter 42,The legendary Gustav,Mister X,Schleim B. Olzen,<cat>,James Bond,Saphira,Hans Peter Otto,Mister Y"
CONFIG_INJECT next-round-order value "Meister Yoda,The legendary Gustav,Mister X,Zackiger Zacharias,Mister Y,<cat>,Saphira,Austauschbarer Agent Dieter 42,Schleim B. Olzen,Tante Gertrude,Hans Peter Otto,James Bond"
CONFIG_INJECT next-round-order value "<cat>,Schleim B. Olzen,Mister Y,Tante Gertrude,Meister Yoda,Saphira,Austauschbarer Agent Dieter 42,Mister X,James Bond,The legendary Gustav,Zackiger Zacharias,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Mister X,Mister Y,Saphira,Hans Peter Otto,The legendary Gustav,Schleim B. Olzen,Tante Gertrude,Zackiger Zacharias,Meister Yoda,James Bond,Austauschbarer Agent Dieter 42,<cat>"
CONFIG_INJECT next-round-order value "Saphira,Meister Yoda,Mister Y,Austauschbarer Agent Dieter 42,James Bond,The legendary Gustav,Mister X,Hans Peter Otto,<cat>,Tante Gertrude,Zackiger Zacharias,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Schleim B. Olzen,Saphira,Hans Peter Otto,Tante Gertrude,The legendary Gustav,Zackiger Zacharias,<cat>,Meister Yoda,James Bond,Mister Y,Mister X"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Tante Gertrude,Meister Yoda,Zackiger Zacharias,James Bond,The legendary Gustav,Schleim B. Olzen,Mister X,<cat>,<janitor>"
CONFIG_INJECT next-round-order value "Schleim B. Olzen,<cat>,Mister X,The legendary Gustav,<janitor>,James Bond,Tante Gertrude,Meister Yoda,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "Mister X,Zackiger Zacharias,Schleim B. Olzen,<janitor>,<cat>,The legendary Gustav,Tante Gertrude,James Bond"
CONFIG_INJECT next-round-order value "Mister X,The legendary Gustav,Tante Gertrude,<janitor>,<cat>,Zackiger Zacharias,James Bond"
CONFIG_INJECT next-round-order value "<cat>,James Bond,<janitor>,Mister X,Tante Gertrude,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "James Bond,<janitor>,Zackiger Zacharias,<cat>,Tante Gertrude"
CONFIG_INJECT next-round-order value "<janitor>,<cat>,Tante Gertrude,James Bond"
CONFIG_INJECT next-round-order value "<cat>,Tante Gertrude,<janitor>"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "James Bond:true;false;false"
CONFIG_INJECT random-result GAMBLE_WIN "James Bond:false"
CONFIG_INJECT random-result GAMBLE_WIN "Schleim B. Olzen:false"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY Saphira:true;false
CONFIG_INJECT random-result NPC_MOVEMENT Saphira:(12,15);(11,14);(11,15);(11,16);(10,15);(11,16);(11,15);(11,16);(10,15);(11,14);(10,15);(11,16);(11,15);(10,15);(10,16);(11,16);(12,17);(11,16);(10,17);(10,16);(11,17);(10,16)
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET Saphira:(11,11);(10,12)
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS Saphira:1
CONFIG_INJECT random-result NPC_WAIT_IN_MS Saphira:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:true;false;true;true;false;false;false;true;false
CONFIG_INJECT random-result OPERATION_SUCCESS "Zackiger Zacharias:false;true;true;true"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Zackiger Zacharias:true;false;false;true;true;false;true;false;true;false;false;true;true;false;true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Hans Peter Otto:false"
CONFIG_INJECT random-result NPC_MOVEMENT "Hans Peter Otto:(26,8);(25,9);(26,9);(25,9);(24,9);(24,8);(24,9);(24,10);(23,9);(24,8);(26,9);(26,8);(26,9);(26,8);(26,9);(25,9);(25,10);(24,9)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Hans Peter Otto:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "The legendary Gustav:false;true;false;true;true;true;true"
CONFIG_INJECT random-result GAMBLE_WIN "The legendary Gustav:false"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:false;false;false;false;true;false;false;false;true;false;false;true;false"
CONFIG_INJECT random-result CAT_WALK_TARGET global:(23,7);(24,6);(24,5);(24,6);(23,7);(22,7);(21,8);(21,9);(22,10);(23,11);(24,10);(23,11);(23,10);(23,9);(23,8);(24,8);(24,9)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(17,10)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:12;13;5;19;6;18;12
CONFIG_INJECT random-result OPERATION_SUCCESS "Mister X:true;true;true"
CONFIG_INJECT random-result GAMBLE_WIN "Mister X:true"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:false;false;false;true;false;true;false;false;false;true;true;false;true;false"
CONFIG_INJECT random-result NPC_MOVEMENT "Mister Y:(22,8);(23,8);(23,9);(24,8);(23,7);(24,8);(25,9);(25,10);(24,9);(24,8);(24,9);(23,8);(25,9);(24,9);(23,8);(23,7);(23,6);(22,7)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Mister Y:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "Austauschbarer Agent Dieter 42:true"
CONFIG_INJECT random-result GAMBLE_WIN "Austauschbarer Agent Dieter 42:true;false;true;true;true"
CONFIG_INJECT random-result OPERATION_SUCCESS "Meister Yoda:true;true;true;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO Du PLAYER
HELLO Florian2 PLAYER
ITEM Florian2 grapple
ITEM Florian2 "Mister X"
ITEM Florian2 moledie
ITEM Florian2 "The legendary Gustav"
ITEM Florian2 technicolour_prism
ITEM Florian2 "Schleim B. Olzen"
ITEM Florian2 chicken_feed
ITEM Florian2 "Zackiger Zacharias"
EQUIP Florian2 "Schleim B. Olzen,Zackiger Zacharias,GRAPPLE,Mister X,The legendary Gustav,CHICKEN_FEED,MOLEDIE,TECHNICOLOUR_PRISM"
ITEM Du magnetic_watch
ITEM Du "Meister Yoda"
ITEM Du "Tante Gertrude"
ITEM Du anti_plague_mask
ITEM Du "Austauschbarer Agent Dieter 42"
ITEM Du jetpack
ITEM Du "James Bond"
ITEM Du rocket_pen
EQUIP Du "Meister Yoda,ANTI_PLAGUE_MASK,Tante Gertrude,ROCKET_PEN,Austauschbarer Agent Dieter 42,MAGNETIC_WATCH,James Bond,JETPACK"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(1,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (2,8)
OPERATION Du MOVEMENT (3,9)
OPERATION Du MOVEMENT (4,9)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(20,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (19,9)
OPERATION Florian2 MOVEMENT (18,9)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(18,2), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), MOLEDIE, TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (18,3)
OPERATION Florian2 MOVEMENT (18,4)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(4,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du MOVEMENT (5,10)
OPERATION Du MOVEMENT (6,11)
OPERATION Du GAMBLE_ACTION (6,12),stake:10
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(8,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(23,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,7), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,7),gadget:COCKTAIL
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(13,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(23,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du GAMBLE_ACTION (24,2),stake:10
OPERATION Du MOVEMENT (24,1)
OPERATION Du MOVEMENT (25,1)
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(10,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (11,1)
OPERATION Du MOVEMENT (12,2)
OPERATION Du PROPERTY_ACTION (11,15),property:OBSERVATION
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(26,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(7,13), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 GAMBLE_ACTION (6,13),stake:10
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (6,11)
OPERATION Florian2 MOVEMENT (7,12)
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(25,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(25,1), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du GAMBLE_ACTION (24,2),stake:2
OPERATION Du MOVEMENT (26,2)
OPERATION Du MOVEMENT (26,3)
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(11,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(18,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), MOLEDIE, TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (18,9),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (18,5)
OPERATION Florian2 MOVEMENT (18,6)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(6,11), mp=2, ap=2, hp=100/100, ip=0, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du MOVEMENT (7,12)
OPERATION Du MOVEMENT (8,13)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(4,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (5,9)
OPERATION Du MOVEMENT (6,9)
OPERATION Du MOVEMENT (7,9)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(12,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (13,2)
OPERATION Du MOVEMENT (14,1)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(18,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (18,6),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (17,9)
OPERATION Florian2 MOVEMENT (16,8)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(23,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,7), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(24,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(8,13), mp=2, ap=2, hp=100/100, ip=0, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du MOVEMENT (8,14)
OPERATION Du MOVEMENT (8,15)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(7,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (8,9)
OPERATION Du MOVEMENT (9,10)
OPERATION Du MOVEMENT (10,11)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(10,15), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(16,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (15,8)
OPERATION Florian2 MOVEMENT (14,8)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(14,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (15,1)
OPERATION Du MOVEMENT (16,2)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(25,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(26,3), mp=2, ap=1, hp=100/100, ip=0, chips=18, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (26,4)
OPERATION Du MOVEMENT (26,5)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(6,11), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (6,11),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (5,10)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,7), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (5,8)
OPERATION Florian2 MOVEMENT (4,9)
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(18,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (18,7)
OPERATION Florian2 MOVEMENT (18,8)
OPERATION Florian2 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(10,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (11,12)
OPERATION Du MOVEMENT (11,13)
OPERATION Du MOVEMENT (11,14)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(16,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (17,3)
OPERATION Du MOVEMENT (18,4)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(4,9), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,10),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (5,8)
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(26,5), mp=2, ap=1, hp=100/100, ip=0, chips=18, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (26,6)
OPERATION Du MOVEMENT (26,7)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(18,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (17,9)
OPERATION Florian2 GADGET_ACTION (14,8),gadget:MOLEDIE
OPERATION Florian2 PROPERTY_ACTION (24,9),property:OBSERVATION
OPERATION Florian2 MOVEMENT (16,8)
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(14,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (16,8),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (13,9)
OPERATION Florian2 MOVEMENT (12,10)
OPERATION Florian2 MOVEMENT (11,11)
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(5,10), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,11)
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (8,13)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(8,15), mp=2, ap=2, hp=100/100, ip=0, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du MOVEMENT (9,16)
OPERATION Du MOVEMENT (10,16)
OPERATION Du SPY_ACTION (11,16)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(11,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(24,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(24,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(18,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (18,5)
OPERATION Du MOVEMENT (18,6)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(16,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (15,9)
OPERATION Florian2 GADGET_ACTION (10,10),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (14,9)
OPERATION Florian2 MOVEMENT (13,9)
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(11,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (11,12)
OPERATION Florian2 GADGET_ACTION (11,14),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (11,13)
OPERATION Florian2 SPY_ACTION (11,14)
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,8), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,7),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (6,7)
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(25,10), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(10,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(26,7), mp=2, ap=1, hp=100/100, ip=0, chips=18, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (26,8)
OPERATION Du MOVEMENT (25,9)
OPERATION Du SPY_ACTION (24,10)
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(8,13), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (6,11)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(11,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1), MOLEDIE], exfiltrated=false]
OPERATION Du MOVEMENT (11,13)
OPERATION Du MOVEMENT (11,12)
OPERATION Du MOVEMENT (11,11)
OPERATION Du GADGET_ACTION (11,15),gadget:MOLEDIE
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(24,10), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(10,16), mp=2, ap=2, hp=100/100, ip=24, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du MOVEMENT (9,16)
OPERATION Du MOVEMENT (8,15)
OPERATION Du GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(6,11), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 MOVEMENT (8,14)
OPERATION Florian2 SPY_ACTION (8,15)
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(23,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(11,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (12,10)
OPERATION Du MOVEMENT (13,9)
OPERATION Du MOVEMENT (14,8)
OPERATION Du SPY_ACTION (15,7)
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(18,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (18,7)
OPERATION Du MOVEMENT (18,8)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(11,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER, MOLEDIE], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(25,9), mp=2, ap=1, hp=100/100, ip=24, chips=18, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (24,8)
OPERATION Du GAMBLE_ACTION (24,7),stake:18
OPERATION Du MOVEMENT (23,8)
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(11,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (10,15)
OPERATION Florian2 SPY_ACTION (10,16)
OPERATION Florian2 MOVEMENT (10,16)
OPERATION Florian2 MOVEMENT (9,16)
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(8,15), mp=2, ap=2, hp=100/100, ip=24, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (7,14)
OPERATION Du MOVEMENT (7,13)
OPERATION Du GADGET_ACTION (8,14),gadget:COCKTAIL
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(12,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (11,11)
OPERATION Florian2 GADGET_ACTION (10,15),gadget:MOLEDIE
OPERATION Florian2 PROPERTY_ACTION (10,15),property:OBSERVATION
OPERATION Florian2 MOVEMENT (11,12)
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,7), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,6)
OPERATION Florian2 MOVEMENT (5,5)
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(25,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(9,16), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 SPY_ACTION (10,15)
OPERATION Florian2 SPY_ACTION (10,15)
OPERATION Florian2 MOVEMENT (8,15)
OPERATION Florian2 MOVEMENT (8,14)
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(24,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(10,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER, MOLEDIE], exfiltrated=false]
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(26,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(11,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (6,7),gadget:MOLEDIE
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(8,15), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (8,14)
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(14,8), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (13,9)
OPERATION Du MOVEMENT (12,10)
OPERATION Du MOVEMENT (11,11)
OPERATION Du SPY_ACTION (11,12)
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,5), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,7),gadget:GRAPPLE
OPERATION Florian2 MOVEMENT (6,6)
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(18,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (17,9)
OPERATION Du PROPERTY_ACTION (24,9),property:OBSERVATION
OPERATION Du MOVEMENT (16,9)
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(8,14), mp=2, ap=2, hp=100/100, ip=24, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Du GADGET_ACTION (4,11),gadget:JETPACK
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(23,8), mp=2, ap=1, hp=100/100, ip=24, chips=36, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
HELLO 1EuroAufTolga SPECTATOR
META 1EuroAufTolga "Configuration.Scenario, Configuration.Matchconfig, Configuration.CharacterInformation, Faction.Player1, Faction.Player2, Faction.Neutral"
OPERATION Du MOVEMENT (24,8)
OPERATION Du GAMBLE_ACTION (24,7),stake:1
OPERATION Du MOVEMENT (23,7)
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(12,17), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(16,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (15,9)
OPERATION Du MOVEMENT (14,9)
OPERATION Du PROPERTY_ACTION (24,9),property:OBSERVATION
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(24,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(23,8), mp=2, ap=1, hp=100/100, ip=24, chips=37, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (22,7)
OPERATION Du MOVEMENT (21,6)
OPERATION Du GAMBLE_ACTION (20,5),stake:13
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(4,11), mp=2, ap=2, hp=100/100, ip=24, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(11,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[CHICKEN_FEED(1), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (11,11),gadget:CHICKEN_FEED
OPERATION Florian2 SPY_ACTION (11,11)
OPERATION Florian2 MOVEMENT (12,13)
OPERATION Florian2 MOVEMENT (11,14)
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(8,15), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Florian2 GADGET_ACTION (8,15),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (8,14)
OPERATION Florian2 MOVEMENT (7,13)
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(26,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(11,11), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (11,12)
OPERATION Du MOVEMENT (12,13)
OPERATION Du MOVEMENT (13,12)
OPERATION Du SPY_ACTION (14,11)
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,6), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,7)
OPERATION Florian2 MOVEMENT (5,8)
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(8,14), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (8,14),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (6,11)
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(21,6), mp=2, ap=1, hp=100/100, ip=24, chips=50, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Du MOVEMENT (20,7)
OPERATION Du MOVEMENT (20,8)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(6,11), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (5,10)
OPERATION Florian2 SPY_ACTION (4,11)
OPERATION Florian2 MOVEMENT (6,9)
OPERATION Florian2 MOVEMENT (6,8)
# Turn for: Character [characterId=be911c14-3f6c-40d1-bfbf-6f672edc875c, name='Saphira', coordinates=(10,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[HAIRDRYER], exfiltrated=false]
# Turn for: Character [characterId=421ba664-c739-4784-b961-bdeb08b25dd4, name='Hans Peter Otto', coordinates=(25,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[LASER_COMPACT, GAS_GLOSS(1), NUGGET(1)], exfiltrated=false]
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(13,12), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[ROCKET_PEN(1)], exfiltrated=false]
OPERATION Du MOVEMENT (12,13)
OPERATION Du MOVEMENT (12,14)
OPERATION Du MOVEMENT (12,15)
OPERATION Du GADGET_ACTION (9,15),gadget:ROCKET_PEN
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(11,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 SPY_ACTION (12,15)
OPERATION Florian2 MOVEMENT (12,15)
OPERATION Florian2 MOVEMENT (11,16)
OPERATION Florian2 MOVEMENT (10,16)
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,8), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,7),gadget:GRAPPLE
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(14,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (13,9)
OPERATION Du MOVEMENT (12,10)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(4,11), mp=2, ap=2, hp=100/100, ip=24, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION Du MOVEMENT (5,10)
OPERATION Du MOVEMENT (6,11)
OPERATION Du SPY_ACTION (7,11)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=9cbd7798-ae7d-4f23-bb20-7ab1a0e677f6, name='Mister Y', coordinates=(23,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL], gadgets=[POCKET_LITTER, MIRROR_OF_WILDERNESS, BOWLER_BLADE, WIRETAP_WITH_EARPLUGS(1), MOTHBALL_POUCH(5)], exfiltrated=false]
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(8,14), mp=2, ap=2, hp=10/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Florian2 GADGET_ACTION (8,14),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=a777bb0a-db65-4a6d-b728-9ef122e43d15, name='Austauschbarer Agent Dieter 42', coordinates=(20,8), mp=2, ap=1, hp=100/100, ip=24, chips=50, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
CRASH 1EuroAufTolga
OPERATION Du MOVEMENT (20,7)
OPERATION Du MOVEMENT (20,6)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(11,14), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Du MOVEMENT (10,15)
OPERATION Du MOVEMENT (9,16)
OPERATION Du MOVEMENT (8,16)
OPERATION Du GADGET_ACTION (7,15),gadget:COCKTAIL
# Turn for: Character [characterId=068e74ef-a088-4c97-b3f8-4995af15d60e, name='Meister Yoda', coordinates=(12,10), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
OPERATION Du MOVEMENT (11,10)
OPERATION Du MOVEMENT (10,9)
OPERATION Du PROPERTY_ACTION (10,16),property:OBSERVATION
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,8), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,8)
OPERATION Florian2 MOVEMENT (6,7)
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(6,11), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (5,10)
OPERATION Du MOVEMENT (6,9)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(10,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 PROPERTY_ACTION (8,16),property:OBSERVATION
OPERATION Florian2 PROPERTY_ACTION (10,9),property:OBSERVATION
OPERATION Florian2 MOVEMENT (9,15)
OPERATION Florian2 MOVEMENT (8,14)
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(5,8), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[], exfiltrated=false]
HELLO 2EuroAufTolga SPECTATOR
META 2EuroAufTolga "Configuration.Scenario, Configuration.Matchconfig, Configuration.CharacterInformation, Faction.Player1, Faction.Player2, Faction.Neutral"
OPERATION Florian2 GADGET_ACTION (5,7),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (6,9)
OPERATION Florian2 MOVEMENT (7,8)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(7,13), mp=3, ap=1, hp=34/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 GAMBLE_ACTION (6,13),stake:10
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 MOVEMENT (6,11)
OPERATION Florian2 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(7,8), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,9)
OPERATION Florian2 MOVEMENT (5,10)
OPERATION Florian2 GADGET_ACTION (5,10),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (6,11)
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(5,10), mp=3, ap=1, hp=34/100, ip=24, chips=20, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(8,14), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (7,15),gadget:COCKTAIL
OPERATION Florian2 PROPERTY_ACTION (8,16),property:OBSERVATION
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(5,8), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (4,9)
OPERATION Du MOVEMENT (3,9)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(8,16), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (8,15)
OPERATION Du MOVEMENT (7,14)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,7), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (5,8)
OPERATION Florian2 MOVEMENT (6,9)
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(5,10), mp=2, ap=2, hp=34/100, ip=24, chips=20, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,9)
OPERATION Florian2 MOVEMENT (7,9)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,10), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,9)
OPERATION Florian2 GADGET_ACTION (5,7),gadget:GRAPPLE
# Turn for: Character [characterId=214e0eaa-d09d-4d61-ae80-ae8c8a072ad2, name='Schleim B. Olzen', coordinates=(6,11), mp=3, ap=1, hp=100/100, ip=0, chips=0, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[], exfiltrated=false]
CRASH 2EuroAufTolga
OPERATION Florian2 MOVEMENT (7,12)
OPERATION Florian2 RETIRE <ignored>
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(8,14), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 SPY_ACTION (7,14)
OPERATION Florian2 MOVEMENT (7,14)
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 MOVEMENT (7,12)
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(8,14), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (9,15)
OPERATION Du MOVEMENT (10,16)
OPERATION Du MOVEMENT (11,17)
HELLO Unknown SPECTATOR
META Unknown "Configuration.Scenario, Configuration.Matchconfig, Configuration.CharacterInformation, Faction.Player1, Faction.Player2, Faction.Neutral"
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(3,9), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (2,8)
OPERATION Du MOVEMENT (1,7)
OPERATION Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 13
# ---------------------------------------------------------
# Turn for: Character [characterId=b83ea3be-84aa-464e-bf41-10167f93062b, name='Mister X', coordinates=(7,9), mp=3, ap=1, hp=34/100, ip=24, chips=20, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Florian2 MOVEMENT (7,8)
OPERATION Florian2 MOVEMENT (6,7)
OPERATION Florian2 GADGET_ACTION (5,7),gadget:COCKTAIL
OPERATION Florian2 MOVEMENT (5,8)
# Turn for: Character [characterId=feb9c5c1-703c-4ab6-b446-6ba28ad747c6, name='The legendary Gustav', coordinates=(7,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, OBSERVATION, TRADECRAFT], gadgets=[TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (7,13)
OPERATION Florian2 GAMBLE_ACTION (6,13),stake:10
OPERATION Florian2 GADGET_ACTION (6,13),gadget:TECHNICOLOUR_PRISM
OPERATION Florian2 MOVEMENT (7,12)
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(11,17), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (12,17)
OPERATION Du MOVEMENT (13,17)
OPERATION Du MOVEMENT (14,17)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(6,9), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1), MOLEDIE], exfiltrated=false]
OPERATION Florian2 GADGET_ACTION (5,8),gadget:MOLEDIE
OPERATION Florian2 MOVEMENT (5,8)
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(1,7), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (2,7)
OPERATION Du MOVEMENT (3,7)
OPERATION Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 14
# ---------------------------------------------------------
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(3,7), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (3,6)
OPERATION Du MOVEMENT (3,5)
OPERATION Du RETIRE <ignored>
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(14,17), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (15,17)
OPERATION Du MOVEMENT (16,17)
OPERATION Du MOVEMENT (17,16)
OPERATION Du GADGET_ACTION (17,16),gadget:COCKTAIL
# Turn for: Character [characterId=85f801fc-6f69-4252-a512-141ba5a8b1f7, name='Zackiger Zacharias', coordinates=(5,8), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION Florian2 MOVEMENT (6,7)
OPERATION Florian2 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 15
# ---------------------------------------------------------
# Turn for: Character [characterId=f25ed023-f031-4ca0-b8e8-0d78cbdcf907, name='James Bond', coordinates=(3,5), mp=2, ap=2, hp=100/100, ip=48, chips=0, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Du MOVEMENT (3,4)
OPERATION Du GADGET_ACTION (3,3),gadget:COCKTAIL
OPERATION Du GADGET_ACTION (3,4),gadget:COCKTAIL
OPERATION Du MOVEMENT (2,3)
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(17,16), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Du GADGET_ACTION (16,15),gadget:COCKTAIL
OPERATION Du MOVEMENT (18,17)
OPERATION Du MOVEMENT (19,17)
OPERATION Du MOVEMENT (20,17)
# ---------------------------------------------------------
# Round Number: 16
# ---------------------------------------------------------
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(20,17), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (21,17)
OPERATION Du MOVEMENT (22,16)
OPERATION Du MOVEMENT (23,15)
OPERATION Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 17
# ---------------------------------------------------------
# Turn for: Character [characterId=76d86b49-d53f-4480-b222-b0b9261d1430, name='Tante Gertrude', coordinates=(23,15), mp=3, ap=1, hp=100/100, ip=48, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Du MOVEMENT (23,14)
OPERATION Du MOVEMENT (23,13)
OPERATION Du MOVEMENT (23,12)
OPERATION Du GADGET_ACTION (23,12),gadget:COCKTAIL
# =============================================================================
# Winner: Du for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 680 Player Two: 264
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 116 Player Two: 95
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 3 Player Two: 5
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 1 Player Two: 1
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 90
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------