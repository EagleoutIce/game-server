# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-loss-Die siebte Sonate-2020-07-02--05-54-13--13228732796849921287.story
# Date: Thu Jul 02 05:54:13 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Thu Jul 02 05:54:13 CEST 2020"
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
  {"characterId":"40ac05df-05c4-4c96-9003-efd0194bbac8","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"20b1de9e-80e4-4e01-8918-a98f895be5e2","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"366c4693-a2f8-41f0-9151-38e98467c2df","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"52b98f1c-8658-48b1-83ba-b5ad06a01494","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"0ca6e8f1-72f7-4d54-89d2-c8158fe8110a","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"0d0b14e9-b14d-4257-a6c4-25583e85574d","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"6787a538-aadf-40cd-b67b-8b3b717a31c5","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"2e3a0483-4489-458b-9513-92ee3469f649","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"e207e949-76bb-4521-8459-a26a38ad86c0","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"c538032c-7f13-4b1f-b75f-c6257d5e62cb","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"3ea1b176-f824-47eb-ba7b-7ceb1f60051a","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"bcc77b67-f65e-4f77-b2af-8254fad5b7b6","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"41d3b78e-54d7-46fa-b2c9-968eb10bce12","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"b7be98a2-ed4e-4dbd-8617-35f77b8cab15","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"118a7181-d932-46c9-aaeb-0ffd2922971f","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"0e84c0d2-b1b0-40a6-9c38-34310f01c47f","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"ed6304f5-ede8-4a02-a1fc-07e459c34d58","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal "Die siebte Sonate" "Ein Wischmob,Saphira,James Bond,jetpack,rocket_pen,bowler_blade"
CONFIG_INJECT next-proposal "C++ Lover" "Austauschbarer Agent Dieter 42,Meister Yoda,Mister X,mothball_pouch,chicken_feed,poison_pills"
CONFIG_INJECT next-proposal "C++ Lover" "Zackiger Zacharias,Schleim B. Olzen,Mister X,poison_pills,grapple,fog_tin"
CONFIG_INJECT next-proposal "C++ Lover" "Hans Peter Otto,Mister Y,Schleim B. Olzen,moledie,hairdryer,gas_gloss"
CONFIG_INJECT next-proposal "C++ Lover" "Misses Y,Spröder Senf,Nr. 5,grapple,technicolour_prism,hairdryer"
CONFIG_INJECT next-proposal "C++ Lover" "Misses Y,Mister X,Petterson,pocket_litter,gas_gloss,chicken_feed"
CONFIG_INJECT next-proposal "C++ Lover" "Austauschbarer Agent Dieter 42,Hans Peter Otto,Meister Yoda,anti_plague_mask,pocket_litter,chicken_feed"
CONFIG_INJECT next-proposal "C++ Lover" "Petterson,Meister Yoda,Schleim B. Olzen,magnetic_watch,pocket_litter,fog_tin"
CONFIG_INJECT next-proposal "C++ Lover" "Tante Gertrude,Spröder Senf,Misses Y,nugget,wiretap_with_earplugs,anti_plague_mask"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Tante Gertrude,Hans Peter Otto,Meister Yoda,mirror_of_wilderness,poison_pills,moledie"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Schleim B. Olzen,Nr. 5,James Bond,mirror_of_wilderness,jetpack,poison_pills"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Hans Peter Otto,Austauschbarer Agent Dieter 42,Nr. 7,anti_plague_mask,bowler_blade,poison_pills"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Meister Yoda,James Bond,Hans Peter Otto,fog_tin,wiretap_with_earplugs,laser_compact"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Austauschbarer Agent Dieter 42,Tante Gertrude,Spröder Senf,poison_pills,magnetic_watch,mirror_of_wilderness"
CONFIG_INJECT next-proposal "Die siebte Sonate" "Schleim B. Olzen,Hans Peter Otto,Nr. 5,hairdryer,bowler_blade,laser_compact"
CONFIG_INJECT next-proposal "Die siebte Sonate" "James Bond,The legendary Gustav,Spröder Senf,"
CONFIG_INJECT safe-order value 1,2,3
CONFIG_INJECT npc-pick value "Petterson,TECHNICOLOUR_PRISM,MAGNETIC_WATCH,The legendary Gustav,LASER_COMPACT,FOG_TIN,POISON_PILLS"
CONFIG_INJECT start-positions value "<cat>,3/9,James Bond,6/10,Mister Y,5/2,Petterson,2/7,Tante Gertrude,1/4,The legendary Gustav,7/6,Zackiger Zacharias,1/11"
CONFIG_INJECT next-round-order value "Tante Gertrude,James Bond,The legendary Gustav,Zackiger Zacharias,<cat>,Mister Y,Petterson"
CONFIG_INJECT next-round-order value "The legendary Gustav,James Bond,Mister Y,Tante Gertrude,Petterson,Zackiger Zacharias,<cat>"
CONFIG_INJECT next-round-order value "<cat>,Mister Y,Tante Gertrude,Zackiger Zacharias,Petterson,James Bond,The legendary Gustav"
CONFIG_INJECT next-round-order value "<cat>,Petterson,The legendary Gustav,Tante Gertrude,Zackiger Zacharias,James Bond,Mister Y"
CONFIG_INJECT next-round-order value "James Bond,Mister Y,Zackiger Zacharias,Petterson,Tante Gertrude,<cat>,The legendary Gustav"
CONFIG_INJECT next-round-order value "Mister Y,<janitor>,Tante Gertrude,Zackiger Zacharias,<cat>,James Bond"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Tante Gertrude:false;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "James Bond:false;true"
CONFIG_INJECT random-result GAMBLE_WIN "James Bond:false;true"
CONFIG_INJECT random-result HONEY_TRAP_TRIGGERS Petterson:true
CONFIG_INJECT random-result NPC_MOVEMENT Petterson:(3,8);(3,7);(4,5);(4,6);(5,6);(4,7);(5,7);(6,6);(7,7);(6,6)
CONFIG_INJECT random-result NPC_WAIT_IN_MS Petterson:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Zackiger Zacharias:false;false;true;true;true;false"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "The legendary Gustav:true;true"
CONFIG_INJECT random-result NPC_MOVEMENT "The legendary Gustav:(7,7);(6,6);(7,7);(7,6);(6,7);(7,7);(6,7);(7,7);(6,7);(5,6);(6,6);(7,5)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "The legendary Gustav:0;1"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "The legendary Gustav:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:false;false;false;true;true"
CONFIG_INJECT random-result CAT_WALK_TARGET global:(2,9);(4,7);(3,7);(3,8);(2,9)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(5,7)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:1
CONFIG_INJECT random-result OPERATION_SUCCESS "Mister Y:false;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO "Die siebte Sonate" PLAYER
HELLO "C++ Lover" PLAYER
ITEM "C++ Lover" mothball_pouch
ITEM "C++ Lover" "Zackiger Zacharias"
ITEM "C++ Lover" "Mister Y"
ITEM "C++ Lover" grapple
ITEM "C++ Lover" gas_gloss
ITEM "C++ Lover" chicken_feed
ITEM "C++ Lover" pocket_litter
ITEM "C++ Lover" nugget
EQUIP "C++ Lover" "Mister Y,GAS_GLOSS,MOTHBALL_POUCH,NUGGET,POCKET_LITTER,Zackiger Zacharias,GRAPPLE,CHICKEN_FEED"
ITEM "Die siebte Sonate" rocket_pen
ITEM "Die siebte Sonate" moledie
ITEM "Die siebte Sonate" jetpack
ITEM "Die siebte Sonate" anti_plague_mask
ITEM "Die siebte Sonate" wiretap_with_earplugs
ITEM "Die siebte Sonate" "Tante Gertrude"
ITEM "Die siebte Sonate" hairdryer
ITEM "Die siebte Sonate" "James Bond"
EQUIP "Die siebte Sonate" "James Bond,HAIRDRYER,JETPACK,MOLEDIE,ROCKET_PEN,WIRETAP_WITH_EARPLUGS,Tante Gertrude,ANTI_PLAGUE_MASK"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
OPERATION "Die siebte Sonate" GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION "Die siebte Sonate" MOVEMENT (2,4)
OPERATION "Die siebte Sonate" MOVEMENT (3,5)
OPERATION "Die siebte Sonate" MOVEMENT (4,6)
OPERATION "Die siebte Sonate" GADGET_ACTION (6,4),gadget:ROCKET_PEN
OPERATION "Die siebte Sonate" MOVEMENT (6,9)
OPERATION "Die siebte Sonate" MOVEMENT (6,8)
OPERATION "Die siebte Sonate" GADGET_ACTION (7,7),gadget:MOLEDIE
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "C++ Lover" MOVEMENT (5,3)
OPERATION "C++ Lover" MOVEMENT (6,4)
OPERATION "C++ Lover" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
OPERATION "Die siebte Sonate" MOVEMENT (6,7)
OPERATION "Die siebte Sonate" SPY_ACTION (7,6)
OPERATION "Die siebte Sonate" MOVEMENT (5,7)
OPERATION "Die siebte Sonate" GADGET_ACTION (2,2),gadget:JETPACK
OPERATION "C++ Lover" MOVEMENT (6,5)
OPERATION "C++ Lover" SPY_ACTION (7,6)
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "Die siebte Sonate" MOVEMENT (3,7)
OPERATION "Die siebte Sonate" GADGET_ACTION (4,6),gadget:COCKTAIL
OPERATION "Die siebte Sonate" MOVEMENT (3,8)
OPERATION "Die siebte Sonate" MOVEMENT (2,9)
OPERATION "C++ Lover" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
OPERATION "C++ Lover" SPY_ACTION (7,6)
OPERATION "C++ Lover" MOVEMENT (5,4)
OPERATION "C++ Lover" MOVEMENT (4,3)
OPERATION "Die siebte Sonate" RETIRE <ignored>
OPERATION "C++ Lover" GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "Die siebte Sonate" SPY_ACTION (1,2)
OPERATION "Die siebte Sonate" GAMBLE_ACTION (3,3),stake:1
OPERATION "Die siebte Sonate" MOVEMENT (2,3)
OPERATION "Die siebte Sonate" MOVEMENT (2,4)
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
OPERATION "Die siebte Sonate" SPY_ACTION (1,10)
OPERATION "Die siebte Sonate" MOVEMENT (3,10)
OPERATION "Die siebte Sonate" MOVEMENT (4,10)
OPERATION "Die siebte Sonate" MOVEMENT (5,10)
OPERATION "C++ Lover" GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "Die siebte Sonate" GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION "Die siebte Sonate" GAMBLE_ACTION (3,3),stake:2
OPERATION "Die siebte Sonate" MOVEMENT (3,5)
OPERATION "Die siebte Sonate" MOVEMENT (3,6)
OPERATION "C++ Lover" MOVEMENT (3,2)
OPERATION "C++ Lover" MOVEMENT (2,2)
OPERATION "C++ Lover" SPY_ACTION (1,2)
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
OPERATION "Die siebte Sonate" RETIRE <ignored>
OPERATION "C++ Lover" GADGET_ACTION (1,1),gadget:MOTHBALL_POUCH
OPERATION "C++ Lover" MOVEMENT (2,3)
OPERATION "C++ Lover" MOVEMENT (2,4)
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "Die siebte Sonate" MOVEMENT (6,10)
OPERATION "Die siebte Sonate" SPY_ACTION (7,10)
OPERATION "Die siebte Sonate" MOVEMENT (5,10)
OPERATION "Die siebte Sonate" MOVEMENT (4,10)
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
OPERATION "C++ Lover" RETIRE <ignored>
OPERATION "Die siebte Sonate" MOVEMENT (3,9)
OPERATION "Die siebte Sonate" MOVEMENT (2,9)
# =============================================================================
# Winner: Die siebte Sonate for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 272 Player Two: 246
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 22 Player Two: 9
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 70
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: true Player Two: false
# ---------------------------------------------------------
# End of File
