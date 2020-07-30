# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-loss-Ich-2020-07-01--05-41-20--12339963404509613231.story
# Date: Wed Jul 01 05:41:20 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Wed Jul 01 05:41:20 CEST 2020"
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
  {"characterId":"103bf84c-f951-49ad-b743-a0c07cf54028","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"52b33b5a-0cd4-4806-86ee-646242ed776f","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"6502b6d2-c56a-42cf-ac0d-07130950da9b","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"c8c650bb-75ca-4a34-92e6-0348617d3461","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"48b2c265-a3d3-4ca2-866f-e9b780653dfb","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"b6977c40-8e6b-4fd8-90d3-aa9d00bb1090","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"f04dbee2-32dd-4787-9aa3-6bc0c198b68e","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"aac5ef2e-7d30-451c-ad76-df46931ad6a0","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"abd257d8-ee5e-4b81-a4b3-54a6a18a2437","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"d2b0a55a-75e0-488c-a06f-bc827c5068d4","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"9aab5ee7-86c5-42a7-9339-cc5ca6be7419","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"4d853c89-cd2b-4dae-b08c-be8bc77e57f4","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"53b5aa35-97ff-4569-962f-235f3c3e8b02","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"a4e170d2-51d8-4ace-9862-5338dcd8e8ca","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"77687386-21dc-4f60-b133-17500a33a08d","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"14bf9bce-8aa5-4498-b9bc-063408ddcf51","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"155e70a2-5f75-4f02-8192-5b5bdf7de481","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal "FatalException: I am not You" "Nr. 7,James Bond,Austauschbarer Agent Dieter 42,fog_tin,chicken_feed,anti_plague_mask"
CONFIG_INJECT next-proposal Ich "Misses Y,Saphira,Petterson,poison_pills,mothball_pouch,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Ich "Schleim B. Olzen,Meister Yoda,Nr. 5,mothball_pouch,rocket_pen,grapple"
CONFIG_INJECT next-proposal Ich "Mister Y,Petterson,Meister Yoda,magnetic_watch,jetpack,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Ich "Tante Gertrude,Meister Yoda,Mister X,wiretap_with_earplugs,gas_gloss,hairdryer"
CONFIG_INJECT next-proposal Ich "Spröder Senf,The legendary Gustav,Hans Peter Otto,nugget,moledie,mirror_of_wilderness"
CONFIG_INJECT next-proposal Ich "Zackiger Zacharias,Hans Peter Otto,Tante Gertrude,moledie,mothball_pouch,jetpack"
CONFIG_INJECT next-proposal Ich "Schleim B. Olzen,Meister Yoda,Saphira,moledie,bowler_blade,nugget"
CONFIG_INJECT next-proposal Ich "Petterson,Ein Wischmob,Zackiger Zacharias,mothball_pouch,pocket_litter,technicolour_prism"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Tante Gertrude,James Bond,Ein Wischmob,technicolour_prism,mothball_pouch,anti_plague_mask"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Austauschbarer Agent Dieter 42,Ein Wischmob,Hans Peter Otto,mothball_pouch,bowler_blade,laser_compact"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Saphira,Hans Peter Otto,Zackiger Zacharias,hairdryer,fog_tin,gas_gloss"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Austauschbarer Agent Dieter 42,Tante Gertrude,Saphira,nugget,hairdryer,moledie"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Hans Peter Otto,Mister X,Nr. 5,chicken_feed,grapple,pocket_litter"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Zackiger Zacharias,Misses Y,Schleim B. Olzen,laser_compact,mothball_pouch,bowler_blade"
CONFIG_INJECT next-proposal "FatalException: I am not You" "Tante Gertrude,Mister Y,Zackiger Zacharias,moledie,laser_compact,gas_gloss"
CONFIG_INJECT safe-order value 3,1,2
CONFIG_INJECT npc-pick value "Nr. 5,GAS_GLOSS,NUGGET,Misses Y,LASER_COMPACT,CHICKEN_FEED,GRAPPLE,ANTI_PLAGUE_MASK"
CONFIG_INJECT start-positions value "<cat>,7/2,Ein Wischmob,3/2,James Bond,1/7,Meister Yoda,3/5,Misses Y,2/9,Nr. 5,2/4,Nr. 7,1/11,Petterson,7/8,Spröder Senf,4/1"
CONFIG_INJECT next-round-order value "Spröder Senf,Misses Y,<cat>,Ein Wischmob,Nr. 7,James Bond,Petterson,Meister Yoda,Nr. 5"
CONFIG_INJECT next-round-order value "Petterson,Misses Y,Nr. 7,Meister Yoda,Nr. 5,<cat>,Ein Wischmob,James Bond,Spröder Senf"
CONFIG_INJECT next-round-order value "Ein Wischmob,Meister Yoda,Misses Y,James Bond,Petterson,Spröder Senf,Nr. 7,<cat>,Nr. 5"
CONFIG_INJECT next-round-order value "Spröder Senf,Petterson,Misses Y,James Bond,Nr. 7,Meister Yoda,<cat>,Ein Wischmob,Nr. 5"
CONFIG_INJECT next-round-order value "Petterson,James Bond,Nr. 7,Nr. 5,Spröder Senf,Misses Y,<cat>,Meister Yoda,Ein Wischmob"
CONFIG_INJECT next-round-order value "<cat>,<janitor>,Nr. 7,Ein Wischmob,James Bond,Spröder Senf,Petterson,Meister Yoda"
CONFIG_INJECT next-round-order value "<cat>,Ein Wischmob,Petterson,Meister Yoda,<janitor>,Nr. 7,James Bond"
CONFIG_INJECT next-round-order value "Ein Wischmob,Petterson,<janitor>,Nr. 7,James Bond,<cat>"
CONFIG_INJECT next-round-order value "Nr. 7,Ein Wischmob,<janitor>,Petterson,<cat>"
CONFIG_INJECT next-round-order value "Petterson,Nr. 7,<cat>,<janitor>"
CONFIG_INJECT next-round-order value "Nr. 7,<cat>,<janitor>"
# ---------------------------------------------------------
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Nr. 5:false;true"
CONFIG_INJECT random-result NPC_MOVEMENT "Nr. 5:(1,4);(2,3);(1,4);(2,4);(2,5);(2,4);(2,3);(2,2);(3,2);(2,3)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Nr. 5:0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Nr. 5:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "Ein Wischmob:true;true;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "James Bond:true"
CONFIG_INJECT random-result GAMBLE_WIN "Spröder Senf:true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Misses Y:true;true"
CONFIG_INJECT random-result NPC_MOVEMENT "Misses Y:(3,8);(2,9);(1,9);(2,9);(4,6);(4,5);(4,6);(3,6);(3,5);(4,4)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Misses Y:0;0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Misses Y:0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CAT_WALK_TARGET global:(6,1);(5,1);(6,2);(6,2);(5,1);(5,2);(4,1);(6,1);(6,2);(6,3);(6,2)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(2,3)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:6
CONFIG_INJECT random-result OPERATION_SUCCESS "Meister Yoda:false;true;true"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Nr. 7:false;true;false;false;false;true;true;true;false;true;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO "FatalException: I am not You" PLAYER
HELLO Ich PLAYER
ITEM Ich poison_pills
ITEM Ich rocket_pen
ITEM Ich magnetic_watch
ITEM Ich wiretap_with_earplugs
ITEM Ich "Spröder Senf"
ITEM Ich jetpack
ITEM Ich "Meister Yoda"
ITEM Ich Petterson
EQUIP Ich "Meister Yoda,POISON_PILLS,WIRETAP_WITH_EARPLUGS,MAGNETIC_WATCH,JETPACK,Spröder Senf,ROCKET_PEN,Petterson"
ITEM "FatalException: I am not You" "Nr. 7"
ITEM "FatalException: I am not You" "James Bond"
ITEM "FatalException: I am not You" "Ein Wischmob"
ITEM "FatalException: I am not You" fog_tin
ITEM "FatalException: I am not You" hairdryer
ITEM "FatalException: I am not You" pocket_litter
ITEM "FatalException: I am not You" mothball_pouch
ITEM "FatalException: I am not You" moledie
EQUIP "FatalException: I am not You" "Ein Wischmob,FOG_TIN,HAIRDRYER,POCKET_LITTER,James Bond,MOLEDIE,MOTHBALL_POUCH,Nr. 7"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" GADGET_ACTION (5,4),gadget:FOG_TIN
OPERATION "FatalException: I am not You" MOVEMENT (2,3)
OPERATION "FatalException: I am not You" SPY_ACTION (2,4)
OPERATION "FatalException: I am not You" MOVEMENT (2,4)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" MOVEMENT (2,6)
OPERATION "FatalException: I am not You" SPY_ACTION (3,5)
OPERATION "FatalException: I am not You" SPY_ACTION (3,5)
OPERATION "FatalException: I am not You" MOVEMENT (3,7)
OPERATION Ich MOVEMENT (6,9)
OPERATION Ich MOVEMENT (5,10)
OPERATION Ich RETIRE <ignored>
OPERATION Ich MOVEMENT (2,4)
OPERATION Ich SPY_ACTION (2,3)
OPERATION Ich RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich SPY_ACTION (2,3)
OPERATION Ich MOVEMENT (2,5)
OPERATION Ich MOVEMENT (2,6)
OPERATION "FatalException: I am not You" SPY_ACTION (2,4)
OPERATION "FatalException: I am not You" SPY_ACTION (2,4)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" MOVEMENT (3,8)
OPERATION "FatalException: I am not You" SPY_ACTION (2,9)
OPERATION "FatalException: I am not You" MOVEMENT (2,9)
OPERATION "FatalException: I am not You" SPY_ACTION (1,10)
OPERATION Ich GADGET_ACTION (3,1),gadget:COCKTAIL
OPERATION Ich MOVEMENT (3,2)
OPERATION Ich MOVEMENT (2,3)
OPERATION Ich GAMBLE_ACTION (3,3),stake:5
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" MOVEMENT (4,6)
OPERATION "FatalException: I am not You" MOVEMENT (5,7)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich MOVEMENT (3,7)
OPERATION Ich MOVEMENT (3,8)
OPERATION Ich SPY_ACTION (3,7)
OPERATION "FatalException: I am not You" SPY_ACTION (1,10)
OPERATION "FatalException: I am not You" SPY_ACTION (3,8)
OPERATION "FatalException: I am not You" MOVEMENT (3,9)
OPERATION "FatalException: I am not You" MOVEMENT (4,10)
OPERATION Ich MOVEMENT (6,10)
OPERATION Ich SPY_ACTION (7,10)
OPERATION Ich RETIRE <ignored>
OPERATION Ich SPY_ACTION (1,2)
OPERATION Ich MOVEMENT (3,2)
OPERATION Ich MOVEMENT (4,2)
OPERATION Ich GADGET_ACTION (5,3),gadget:ROCKET_PEN
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
OPERATION Ich RETIRE <ignored>
OPERATION Ich SPY_ACTION (7,10)
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" MOVEMENT (3,9)
OPERATION "FatalException: I am not You" MOVEMENT (3,8)
OPERATION "FatalException: I am not You" SPY_ACTION (3,9)
OPERATION "FatalException: I am not You" GADGET_ACTION (3,9),gadget:MOLEDIE
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich GADGET_ACTION (5,2),gadget:JETPACK
OPERATION Ich MOVEMENT (6,2)
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich RETIRE <ignored>
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich RETIRE <ignored>
OPERATION Ich MOVEMENT (5,2)
OPERATION Ich MOVEMENT (5,1)
OPERATION Ich GADGET_ACTION (6,2),gadget:MOLEDIE
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" MOVEMENT (6,8)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich SPY_ACTION (7,10)
OPERATION Ich MOVEMENT (5,10)
OPERATION Ich RETIRE <ignored>
OPERATION Ich MOVEMENT (4,1)
OPERATION Ich GADGET_ACTION (3,1),gadget:POISON_PILLS
OPERATION Ich MOVEMENT (3,2)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" MOVEMENT (4,7)
OPERATION "FatalException: I am not You" MOVEMENT (4,6)
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich SPY_ACTION (7,10)
OPERATION Ich MOVEMENT (6,9)
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION "FatalException: I am not You" MOVEMENT (5,7)
OPERATION "FatalException: I am not You" MOVEMENT (4,6)
OPERATION "FatalException: I am not You" RETIRE <ignored>
OPERATION Ich MOVEMENT (5,10)
OPERATION Ich MOVEMENT (4,10)
OPERATION Ich RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
OPERATION Ich RETIRE <ignored>
OPERATION "FatalException: I am not You" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
OPERATION "FatalException: I am not You" RETIRE <ignored>
# =============================================================================
# Winner: Ich for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 369 Player Two: 432
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 17 Player Two: 21
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 40
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
# End of File