# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-Full Game-2020-06-28--12-29-48--1919700421203540730.story
# Date: Sun Jun 28 12:29:48 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Sun Jun 28 12:29:48 CEST 2020"
FORBID_ERRORS
# =============================================================================
# This is default-configs part, which will set the used scenario, ...
# =============================================================================
COLLECT_START "" @server_config
{
  "forbiddenGadgets": [],
  "allowDuplicateCharacters": true,
  "minimumCharacters": 13,
  "storyAuthorSleepThresholdMs": 150,
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
  "magpieToConsole": false,
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
  "bowlerBladeDamage": 4,
  "laserCompactHitChance": 0.125,
  "rocketPenDamage": 2,
  "gasGlossDamage": 6,
  "mothballPouchRange": 2,
  "mothballPouchDamage": 1,
  "fogTinRange": 2,
  "grappleRange": 3,
  "grappleHitChance": 0.35,
  "wiretapWithEarplugsFailChance": 0.64,
  "mirrorSwapChance": 0.35,
  "cocktailDodgeChance": 0.25,
  "cocktailHp": 6,
  "spySuccessChance": 0.65,
  "babysitterSuccessChance": 0.25,
  "honeyTrapSuccessChance": 0.35,
  "observationSuccessChance": 0.12,
  "chipsToIpFactor": 12,
  "secretToIpFactor": 3,
  "minChipsRoulette": 0,
  "maxChipsRoulette": 6,
  "roundLimit": 15,
  "turnPhaseLimit": 60,
  "catIp": 8,
  "strikeMaximum": 4,
  "pauseLimit": 320,
  "reconnectLimit": 200
}
COLLECT_END
CONFIG_INJECT matchconfig RAW-JSON ${@matchconfig}
COLLECT_START "" @characters
[
  {"characterId":"befb70d5-48b6-4cb1-8902-602190dc4665","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"baf7c829-c552-4a01-b8f6-b8624b008cef","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"77666289-bf32-4816-9cd3-e962f6c75a39","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"fd223b4e-6f08-4590-8b54-84492c68c532","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"e107255b-a748-4b9a-af92-b7bf2fc58fe3","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"0e7061c4-2268-4d25-a936-a89d3926f0af","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"3a06b921-2854-4ba7-bd94-25434b68511b","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"0ff34e38-4105-4760-9eaf-08dc36c9786a","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"9814121f-234a-4881-97eb-deaf94b37311","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"f8bc7713-055a-4ec9-b83b-fc5619589a7d","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"10e941b5-0b64-46a8-8332-62de4d3429dc","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"87518be8-73b0-42d1-a237-a458b428d6e5","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"5bd7e4e6-36b5-4179-bee2-7a06151de76a","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"5fadc526-1dd2-4531-9493-754582238fe0","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"9c3b7fff-1441-4b74-9069-3bc1ef2b1b7c","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"6e55a01e-2cff-4eee-a421-a58979af0f86","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"22efc91b-0099-4bb6-acd8-2650812f09dd","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal saphira "Mister X,Spröder Senf,Tante Gertrude,fog_tin,chicken_feed,mirror_of_wilderness"
CONFIG_INJECT next-proposal dieter "Schleim B. Olzen,Ein Wischmob,Saphira,poison_pills,magnetic_watch,bowler_blade"
CONFIG_INJECT next-proposal saphira "Meister Yoda,James Bond,Mister X,moledie,rocket_pen,wiretap_with_earplugs"
CONFIG_INJECT next-proposal dieter "Mister Y,Hans Peter Otto,Nr. 5,grapple,pocket_litter,gas_gloss"
CONFIG_INJECT next-proposal saphira "Saphira,Petterson,James Bond,poison_pills,wiretap_with_earplugs,hairdryer"
CONFIG_INJECT next-proposal dieter "Tante Gertrude,Austauschbarer Agent Dieter 42,Misses Y,pocket_litter,nugget,laser_compact"
CONFIG_INJECT next-proposal saphira "Zackiger Zacharias,Saphira,Petterson,magnetic_watch,rocket_pen,mothball_pouch"
CONFIG_INJECT next-proposal dieter "Spröder Senf,Nr. 5,Austauschbarer Agent Dieter 42,fog_tin,hairdryer,technicolour_prism"
CONFIG_INJECT next-proposal saphira "Mister Y,Mister X,Hans Peter Otto,pocket_litter,nugget,jetpack"
CONFIG_INJECT next-proposal dieter "Ein Wischmob,Tante Gertrude,Petterson,laser_compact,gas_gloss,moledie"
CONFIG_INJECT next-proposal saphira "Mister Y,Zackiger Zacharias,Nr. 7,technicolour_prism,anti_plague_mask,mothball_pouch"
CONFIG_INJECT next-proposal dieter "Spröder Senf,Mister X,Hans Peter Otto,jetpack,wiretap_with_earplugs,magnetic_watch"
CONFIG_INJECT next-proposal saphira "Petterson,James Bond,The legendary Gustav,laser_compact,pocket_litter,rocket_pen"
CONFIG_INJECT next-proposal dieter "Spröder Senf,Tante Gertrude,Nr. 5,wiretap_with_earplugs,magnetic_watch,anti_plague_mask"
CONFIG_INJECT next-proposal saphira mothball_pouch,pocket_litter,laser_compact
CONFIG_INJECT next-proposal dieter "Spröder Senf,Austauschbarer Agent Dieter 42,The legendary Gustav,chicken_feed,magnetic_watch,anti_plague_mask"
CONFIG_INJECT safe-order value 2,3,1
CONFIG_INJECT npc-pick value "Tante Gertrude,TECHNICOLOUR_PRISM,GAS_GLOSS,POCKET_LITTER,Hans Peter Otto,WIRETAP_WITH_EARPLUGS,ROCKET_PEN,ANTI_PLAGUE_MASK,LASER_COMPACT"
CONFIG_INJECT start-positions value "<cat>,6/3,Hans Peter Otto,2/5,Meister Yoda,6/6,Misses Y,1/11,Mister X,4/11,Nr. 5,2/3,Nr. 7,2/9,Petterson,7/8,Saphira,4/2,Tante Gertrude,3/7"
CONFIG_INJECT next-round-order value "Mister X,<cat>,Saphira,Tante Gertrude,Misses Y,Petterson,Nr. 7,Meister Yoda,Hans Peter Otto,Nr. 5"
CONFIG_INJECT next-round-order value "Misses Y,Nr. 5,Nr. 7,Saphira,<cat>,Hans Peter Otto,Tante Gertrude,Meister Yoda,Petterson,Mister X"
CONFIG_INJECT next-round-order value "Petterson,Nr. 5,Saphira,Hans Peter Otto,<cat>,Meister Yoda,Nr. 7,Misses Y,Mister X,Tante Gertrude"
CONFIG_INJECT next-round-order value "Hans Peter Otto,Tante Gertrude,Saphira,Nr. 7,Nr. 5,Petterson,Mister X,Meister Yoda,Misses Y,<cat>"
CONFIG_INJECT next-round-order value "Meister Yoda,Hans Peter Otto,Nr. 7,Misses Y,Saphira,Petterson,Mister X,<cat>,Nr. 5,Tante Gertrude"
CONFIG_INJECT next-round-order value "Mister X,Saphira,Misses Y,Meister Yoda,Hans Peter Otto,<cat>,Petterson,Tante Gertrude,Nr. 5,Nr. 7"
CONFIG_INJECT next-round-order value "<cat>,Saphira,Meister Yoda,Nr. 7,Mister X,Petterson,Tante Gertrude,Nr. 5,Hans Peter Otto,Misses Y"
CONFIG_INJECT next-round-order value "Petterson,Mister X,Tante Gertrude,Nr. 5,Misses Y,<cat>,Saphira,Meister Yoda,Hans Peter Otto,Nr. 7"
CONFIG_INJECT next-round-order value "Hans Peter Otto,Petterson,<cat>,Misses Y,Meister Yoda,Mister X,Nr. 7,Saphira,Nr. 5,Tante Gertrude"
CONFIG_INJECT next-round-order value "<cat>,Mister X,Hans Peter Otto,Saphira,Tante Gertrude,Misses Y,Meister Yoda,Petterson,Nr. 7,Nr. 5"
CONFIG_INJECT next-round-order value "Meister Yoda,Nr. 5,Hans Peter Otto,Mister X,Petterson,Misses Y,Saphira,Tante Gertrude,<cat>,Nr. 7"
CONFIG_INJECT next-round-order value "Saphira,Nr. 7,Mister X,Nr. 5,Petterson,Misses Y,Hans Peter Otto,Tante Gertrude,Meister Yoda,<cat>"
CONFIG_INJECT next-round-order value "Nr. 5,Mister X,Petterson,Meister Yoda,Tante Gertrude,Nr. 7,Misses Y,<cat>,Hans Peter Otto,Saphira"
CONFIG_INJECT next-round-order value "Nr. 7,Mister X,Saphira,Misses Y,<cat>,Hans Peter Otto,Petterson,Nr. 5,Tante Gertrude,Meister Yoda"
CONFIG_INJECT next-round-order value "Meister Yoda,Petterson,<janitor>,Mister X,<cat>,Nr. 5,Saphira,Misses Y,Nr. 7"
CONFIG_INJECT next-round-order value "Misses Y,Nr. 7,Meister Yoda,Petterson,<janitor>,Mister X,<cat>,Nr. 5"
CONFIG_INJECT next-round-order value "<janitor>,Petterson,Mister X,Misses Y,Meister Yoda,<cat>,Nr. 7"
CONFIG_INJECT next-round-order value "<cat>,Misses Y,<janitor>,Nr. 7,Mister X,Petterson"
CONFIG_INJECT next-round-order value "Mister X,<janitor>,Misses Y,<cat>,Nr. 7"
CONFIG_INJECT next-round-order value "<cat>,Misses Y,Mister X,<janitor>"
CONFIG_INJECT next-round-order value "<janitor>,Misses Y,<cat>"
# ---------------------------------------------------------
CONFIG_INJECT random-result NPC_MOVEMENT "Tante Gertrude:(3,8);(2,9);(3,10);(3,11);(4,11);(5,10);(5,11);(6,11);(5,11);(6,11);(6,10);(6,9);(5,10);(6,10);(5,10);(4,11);(4,10);(3,9);(4,10);(3,9);(2,9);(3,9);(3,10);(3,11);(4,10);(3,9);(3,8);(2,9);(3,10);(4,10);(5,11);(4,11);(3,10);(3,11);(3,10);(4,10);(3,9);(3,10);(3,11);(4,11);(3,11);(4,11)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Tante Gertrude:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:false;false;true;false;false;false;false;false;true;false;true;true;false;true;true
CONFIG_INJECT random-result NPC_MOVEMENT "Hans Peter Otto:(2,4);(2,5);(1,6);(1,5);(2,5);(1,5);(2,5);(2,6);(3,6);(2,5);(2,6);(3,5);(2,6);(3,6);(3,7);(2,6);(1,6);(1,7);(1,6);(1,5);(2,4);(2,5);(2,4);(1,5);(2,6);(1,6);(1,5);(1,4)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Hans Peter Otto:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CAT_WALK_TARGET global:(7,4);(6,5);(7,5);(6,6);(7,5);(7,4);(6,5);(5,4);(5,3);(4,2);(5,3);(4,4);(3,5);(2,5);(2,4);(2,5);(1,6);(2,7);(1,7);(1,6)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(6,3)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:6
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:false;true;false;false;true;true;false;false;true;false;true;false;false;false;false;true;false;true;true;false"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Nr. 7:true;false;false;false;false;true;true;false;false;true;true;true;false;false;true;true;false;false;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO saphira PLAYER
HELLO dieter PLAYER
ITEM saphira mirror_of_wilderness
ITEM dieter bowler_blade
ITEM saphira "Meister Yoda"
ITEM dieter grapple
ITEM saphira poison_pills
ITEM dieter "Misses Y"
ITEM saphira Saphira
ITEM dieter fog_tin
ITEM saphira nugget
ITEM dieter moledie
ITEM saphira "Nr. 7"
ITEM dieter "Mister X"
ITEM saphira Petterson
ITEM dieter "Nr. 5"
ITEM saphira mothball_pouch
ITEM dieter magnetic_watch
EQUIP saphira "Nr. 7,POISON_PILLS,Petterson,NUGGET,MOTHBALL_POUCH,Meister Yoda,Saphira,MIRROR_OF_WILDERNESS"
EQUIP dieter "Nr. 5,BOWLER_BLADE,GRAPPLE,FOG_TIN,MOLEDIE,MAGNETIC_WATCH,Mister X,Misses Y"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 13
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 14
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 15
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 16
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 17
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 18
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 19
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 20
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 21
# ---------------------------------------------------------
# =============================================================================
# Winner: saphira for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 480 Player Two: 360
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 0 Player Two: 0
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 0
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
# End of File
