# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-regular-game-over-2020-07-28--12-21-57--11590828779183423951.story
# Date: Tue Jul 28 00:21:57 CEST 2020
# Server-Version: 1.1 (using Game-Data v1.2)
# =============================================================================
SET story-name server020
SET story-date "Tue Jul 28 00:21:57 CEST 2020"
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
  "magpieToConsole": false,
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
  {"characterId":"5ee6eec8-1aed-4999-98e8-9e38562765ec","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"0565476c-08de-4676-b6f7-66725cb2844f","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":"DIVERSE","features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"0eba9fc0-3268-44a8-b42a-c75eb57bb663","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":"DIVERSE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"0d53766f-24d7-4bd6-9b76-fa7968750b07","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":"MALE","features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"fd7ecb51-155c-48dd-b8a5-a129ddb71c06","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"acb676f0-cf4f-4365-9da8-674db4221c9c","name":"Schleim B. Olzen","description":"","gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"9f78ef68-9724-4dc1-a674-23ee26b9d907","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":"DIVERSE","features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"be07f99d-1f75-4aac-9da7-a72931e98864","name":"Petterson","description":"Den Findus keiner.","gender":"DIVERSE","features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"26ec60cc-37b0-46c4-bf03-8c41421ee419","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"47327e15-c80b-41b6-bb0d-0b6d3f2727bc","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"d8e4d4f0-bd8f-4404-9bd3-ee23956112b3","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"2690149b-f826-4e1a-ab22-536e937f1227","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"c3f38c02-1342-4144-a4d6-93a5361e063f","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"f0f97df3-ce7a-422e-94a1-ead446ff275a","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":"DIVERSE","features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"1f82ff0f-5103-4bf7-a19e-b37586d0390b","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":"DIVERSE","features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal ki20 "Ein Wischmob,Misses Y,Mister X,mirror_of_wilderness,technicolour_prism,grapple"
CONFIG_INJECT next-proposal "Der Helge" "Spröder Senf,Nr. 5,Petterson,chicken_feed,jetpack,magnetic_watch"
CONFIG_INJECT next-proposal ki20 "Hans Peter Otto,Tante Gertrude,Ein Wischmob,anti_plague_mask,mothball_pouch,nugget"
CONFIG_INJECT next-proposal ki20 "Mister Y,Ein Wischmob,Saphira,nugget,wiretap_with_earplugs,poison_pills"
CONFIG_INJECT next-proposal "Der Helge" "Meister Yoda,Petterson,Austauschbarer Agent Dieter 42,moledie,hairdryer,technicolour_prism"
CONFIG_INJECT next-proposal ki20 "James Bond,Zackiger Zacharias,Ein Wischmob,anti_plague_mask,gas_gloss,jetpack"
CONFIG_INJECT next-proposal ki20 "Mister X,Mister Y,The legendary Gustav,laser_compact,rocket_pen,jetpack"
CONFIG_INJECT next-proposal ki20 "Ein Wischmob,Nr. 5,Nr. 7,mirror_of_wilderness,pocket_litter,bowler_blade"
CONFIG_INJECT next-proposal ki20 nugget,gas_gloss,pocket_litter
CONFIG_INJECT next-proposal ki20 fog_tin,pocket_litter,nugget
CONFIG_INJECT next-proposal "Der Helge" "Mister X,Nr. 5,Schleim B. Olzen,technicolour_prism,bowler_blade,hairdryer"
CONFIG_INJECT next-proposal "Der Helge" "Zackiger Zacharias,Nr. 7,Tante Gertrude,mirror_of_wilderness,laser_compact,poison_pills"
CONFIG_INJECT next-proposal "Der Helge" "Spröder Senf,Tante Gertrude,Misses Y,anti_plague_mask,moledie,jetpack"
CONFIG_INJECT next-proposal "Der Helge" "Nr. 5,Spröder Senf,Petterson,chicken_feed,technicolour_prism,wiretap_with_earplugs"
CONFIG_INJECT next-proposal "Der Helge" chicken_feed,bowler_blade,technicolour_prism
CONFIG_INJECT next-proposal "Der Helge" pocket_litter,wiretap_with_earplugs,anti_plague_mask
CONFIG_INJECT safe-order value 3,2,1
CONFIG_INJECT npc-pick value "Hans Peter Otto,CHICKEN_FEED,ROCKET_PEN,Mister X,Nr. 5,MOLEDIE"
CONFIG_INJECT start-positions value "<cat>,4/16,Ein Wischmob,12/8,Hans Peter Otto,22/16,James Bond,19/14,Meister Yoda,26/3,Mister X,12/6,Nr. 5,22/1,Saphira,4/17,Schleim B. Olzen,24/5,Spröder Senf,13/13,Tante Gertrude,12/1,The legendary Gustav,20/13"
CONFIG_INJECT next-round-order value "Mister X,Tante Gertrude,<cat>,Nr. 5,James Bond,Hans Peter Otto,Ein Wischmob,Meister Yoda,Saphira,The legendary Gustav,Schleim B. Olzen,Spröder Senf"
CONFIG_INJECT next-round-order value "Tante Gertrude,<cat>,The legendary Gustav,Nr. 5,Meister Yoda,Ein Wischmob,Saphira,Spröder Senf,Schleim B. Olzen,Mister X,James Bond,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Mister X,Nr. 5,James Bond,Saphira,<cat>,Ein Wischmob,The legendary Gustav,Hans Peter Otto,Tante Gertrude,Spröder Senf,Meister Yoda,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Saphira,Mister X,Spröder Senf,Meister Yoda,Nr. 5,The legendary Gustav,Tante Gertrude,Ein Wischmob,<cat>,Schleim B. Olzen,James Bond,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Mister X,Nr. 5,Saphira,James Bond,Tante Gertrude,Hans Peter Otto,Ein Wischmob,Schleim B. Olzen,<cat>,Meister Yoda,The legendary Gustav,Spröder Senf"
CONFIG_INJECT next-round-order value "Meister Yoda,James Bond,Hans Peter Otto,<cat>,Spröder Senf,Schleim B. Olzen,Nr. 5,Mister X,The legendary Gustav,Tante Gertrude,Ein Wischmob,Saphira"
CONFIG_INJECT next-round-order value "Nr. 5,Schleim B. Olzen,Meister Yoda,Saphira,Mister X,The legendary Gustav,James Bond,Tante Gertrude,<cat>,Ein Wischmob,Spröder Senf,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Mister X,<cat>,Tante Gertrude,Hans Peter Otto,Schleim B. Olzen,Saphira,Meister Yoda,The legendary Gustav,Nr. 5,James Bond,Ein Wischmob,Spröder Senf"
CONFIG_INJECT next-round-order value "The legendary Gustav,Hans Peter Otto,<cat>,Mister X,Saphira,Spröder Senf,Meister Yoda,Tante Gertrude,Nr. 5,Ein Wischmob,James Bond,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "<cat>,Meister Yoda,James Bond,Saphira,<janitor>,Spröder Senf,Ein Wischmob,The legendary Gustav,Schleim B. Olzen,Tante Gertrude"
CONFIG_INJECT next-round-order value "Saphira,The legendary Gustav,<cat>,<janitor>,Ein Wischmob,Meister Yoda,Tante Gertrude,Spröder Senf,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Saphira,<cat>,Ein Wischmob,Spröder Senf,Meister Yoda,Tante Gertrude,<janitor>,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Ein Wischmob,Saphira,<janitor>,Tante Gertrude,Meister Yoda,Spröder Senf,<cat>"
CONFIG_INJECT next-round-order value "Ein Wischmob,<cat>,<janitor>,Tante Gertrude,Saphira,Spröder Senf"
CONFIG_INJECT next-round-order value "Spröder Senf,Saphira,<janitor>,Ein Wischmob,<cat>"
CONFIG_INJECT next-round-order value "<cat>,Saphira,<janitor>,Spröder Senf"
CONFIG_INJECT next-round-order value <cat>,Saphira,<janitor>
# ---------------------------------------------------------
CONFIG_INJECT random-result NPC_MOVEMENT "Nr. 5:(23,2);(22,1);(23,1);(24,1);(24,1);(23,2);(23,2);(23,3);(22,2);(23,3);(22,2);(23,2);(23,1);(22,2);(23,1);(22,1);(21,1);(21,2)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "Nr. 5:(16,1)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Nr. 5:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "Tante Gertrude:true;false"
CONFIG_INJECT random-result GAMBLE_WIN "Schleim B. Olzen:true"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:false;true;true;true;true;false;false;false;true;true;true;true;false;false;false;false;false
CONFIG_INJECT random-result NPC_MOVEMENT "Hans Peter Otto:(23,15);(22,16);(23,17);(22,17);(23,16);(23,15);(22,16);(23,16);(22,16);(23,17);(23,16);(22,17);(22,16);(23,17);(22,16);(23,15);(23,14);(23,13)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Hans Peter Otto:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:true;false;true;false;true;true;true;true;false;true;false"
CONFIG_INJECT random-result CAT_WALK_TARGET global:(4,15);(4,14);(4,13);(4,12);(4,13);(4,12);(3,11);(4,10);(4,11);(3,10);(3,9);(3,10);(4,11);(3,10);(4,11);(4,10);(5,10)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(16,9)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:17;6;16;3;9;20;13
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Mister X:true"
CONFIG_INJECT random-result NPC_MOVEMENT "Mister X:(11,6);(12,6);(12,5);(11,4);(11,3);(10,3);(11,4);(11,5);(11,4);(12,5);(11,6);(12,6);(11,7);(12,7);(11,6);(12,6);(11,6);(12,5);(12,4);(12,5)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Mister X:1"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Mister X:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:true;false;false;false;false;false;false;true;false"
CONFIG_INJECT random-result GAMBLE_WIN "Meister Yoda:true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO ki20 AI
SLEEP 55406
HELLO "Der Helge" PLAYER
SLEEP 1260
ITEM ki20 grapple
SLEEP 671
ITEM ki20 mothball_pouch
SLEEP 171
ITEM "Der Helge" magnetic_watch
SLEEP 357
ITEM ki20 Saphira
SLEEP 657
ITEM ki20 "James Bond"
SLEEP 399
ITEM ki20 "The legendary Gustav"
SLEEP 350
ITEM ki20 "Ein Wischmob"
SLEEP 67
ITEM ki20 gas_gloss
ITEM ki20 fog_tin
META ki20 Faction.Player1
EQUIP ki20 "Ein Wischmob,James Bond,GRAPPLE,MOTHBALL_POUCH,GAS_GLOSS,FOG_TIN,Saphira,The legendary Gustav"
SLEEP 1311
ITEM "Der Helge" "Meister Yoda"
SLEEP 1453
ITEM "Der Helge" "Schleim B. Olzen"
SLEEP 1163
ITEM "Der Helge" poison_pills
SLEEP 749
ITEM "Der Helge" "Tante Gertrude"
SLEEP 557
ITEM "Der Helge" "Spröder Senf"
SLEEP 948
ITEM "Der Helge" technicolour_prism
SLEEP 2612
ITEM "Der Helge" pocket_litter
SLEEP 1618
EQUIP "Der Helge" "Spröder Senf,Meister Yoda,POISON_PILLS,TECHNICOLOUR_PRISM,Tante Gertrude,POCKET_LITTER,Schleim B. Olzen,MAGNETIC_WATCH"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(12,6), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 3385
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(22,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MOLEDIE], exfiltrated=false]
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(19,14), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), GAS_GLOSS(1), FOG_TIN(1)], exfiltrated=false]
SLEEP 102
OPERATION ki20 GADGET_ACTION (20,13),gadget:GAS_GLOSS
OPERATION ki20 MOVEMENT (20,13)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(22,16), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(12,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
SLEEP 46
OPERATION ki20 MOVEMENT (12,7)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(26,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(5), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 2532
OPERATION "Der Helge" MOVEMENT (26,2)
SLEEP 529
OPERATION "Der Helge" MOVEMENT (25,1)
SLEEP 4423
OPERATION "Der Helge" GAMBLE_ACTION (24,2),stake:10
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,17), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(19,14), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,13)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(24,5), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 1591
OPERATION "Der Helge" MOVEMENT (23,4)
SLEEP 823
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(13,13), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1885
OPERATION "Der Helge" MOVEMENT (14,12)
SLEEP 2879
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER, MOLEDIE], exfiltrated=false]
SLEEP 1912
OPERATION "Der Helge" MOVEMENT (12,2)
SLEEP 362
OPERATION "Der Helge" MOVEMENT (12,3)
SLEEP 597
OPERATION "Der Helge" MOVEMENT (12,4)
SLEEP 922
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,13), mp=2, ap=2, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(22,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(25,1), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(5), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 1113
OPERATION "Der Helge" MOVEMENT (24,1)
SLEEP 1743
OPERATION "Der Helge" MOVEMENT (23,2)
SLEEP 1242
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(12,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (12,8)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(14,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 28247
OPERATION "Der Helge" MOVEMENT (13,12)
SLEEP 1122
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 4458
OPERATION "Der Helge" MOVEMENT (24,5)
SLEEP 626
OPERATION "Der Helge" MOVEMENT (24,6)
SLEEP 3984
OPERATION "Der Helge" GAMBLE_ACTION (24,7),stake:3
SLEEP 1477
OPERATION "Der Helge" MOVEMENT (23,7)
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(12,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(19,14), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,15)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(22,16), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(11,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(25,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(20,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
SLEEP 63
OPERATION ki20 MOVEMENT (21,14)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(12,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (12,9)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(21,12), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(22,17), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER, MOLEDIE], exfiltrated=false]
SLEEP 1453
OPERATION "Der Helge" MOVEMENT (11,4)
SLEEP 4696
OPERATION "Der Helge" GADGET_ACTION (12,10),gadget:MOLEDIE
SLEEP 1204
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(13,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 9840
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(24,1), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(5), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 2190
OPERATION "Der Helge" MOVEMENT (23,2)
SLEEP 603
OPERATION "Der Helge" MOVEMENT (22,3)
SLEEP 48413
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:POISON_PILLS
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,7), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 1208
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,17), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(12,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(13,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 742
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 1758
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:COCKTAIL
SLEEP 6918
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(24,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,11), mp=2, ap=2, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(11,5), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 1212
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(12,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,9)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,7), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 695
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(21,14), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,13)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(23,15), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(11,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(23,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
SLEEP 64
OPERATION ki20 MOVEMENT (4,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(21,13), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(11,5), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 1655
OPERATION "Der Helge" MOVEMENT (11,6)
SLEEP 1822
OPERATION "Der Helge" SPY_ACTION (11,5)
SLEEP 2220
OPERATION "Der Helge" MOVEMENT (12,7)
SLEEP 1757
OPERATION "Der Helge" MOVEMENT (12,8)
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(23,16), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,10)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,7), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 3277
OPERATION "Der Helge" MOVEMENT (24,6)
SLEEP 2880
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
SLEEP 7720
OPERATION "Der Helge" GADGET_ACTION (22,3),gadget:COCKTAIL
SLEEP 3791
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,12), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(13,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 557
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(4), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 5118
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:POISON_PILLS
SLEEP 2423
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(21,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(23,17), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(13,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1370
OPERATION "Der Helge" MOVEMENT (12,13)
SLEEP 519
OPERATION "Der Helge" MOVEMENT (11,12)
SLEEP 825
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(24,6), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 1308
OPERATION "Der Helge" MOVEMENT (24,5)
SLEEP 434
OPERATION "Der Helge" MOVEMENT (23,4)
SLEEP 1552
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(23,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(11,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(21,12), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 49
OPERATION ki20 MOVEMENT (20,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,8), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 783
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(11,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,9)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,17), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(23,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 2489
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:COCKTAIL
SLEEP 2137
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(3), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 5543
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,15)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(11,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,12), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(20,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,8), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 934
OPERATION "Der Helge" MOVEMENT (12,7)
SLEEP 312
OPERATION "Der Helge" MOVEMENT (12,6)
SLEEP 142
OPERATION "Der Helge" MOVEMENT (12,5)
SLEEP 1099
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,10)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 906
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(22,17), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(11,6), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(11,6), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 1345
OPERATION "Der Helge" MOVEMENT (11,5)
SLEEP 1501
OPERATION "Der Helge" SPY_ACTION (12,5)
SLEEP 1400
OPERATION "Der Helge" MOVEMENT (11,4)
SLEEP 1067
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(23,17), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=100/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH, COCKTAIL(1)], exfiltrated=false]
SLEEP 3553
OPERATION "Der Helge" GADGET_ACTION (23,4),gadget:COCKTAIL
SLEEP 38186
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,15), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(3), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 5341
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:POISON_PILLS
SLEEP 1246
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(21,12), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(22,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(21,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,13)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(11,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (10,9)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1276
OPERATION "Der Helge" MOVEMENT (10,11)
SLEEP 707
OPERATION "Der Helge" MOVEMENT (9,10)
SLEEP 5056
OPERATION "Der Helge" SPY_ACTION (10,9)
SLEEP 1523
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,12), mp=2, ap=2, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,13)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5fc9b397-6f15-4c3d-b5b6-d48e3198bf6c, name='Hans Peter Otto', coordinates=(23,15), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[ROBUST_STOMACH, FLAPS_AND_SEALS], gadgets=[CHICKEN_FEED(1), ROCKET_PEN(1)], exfiltrated=false]
# Turn for: Character [characterId=26ec60cc-37b0-46c4-bf03-8c41421ee419, name='Mister X', coordinates=(12,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (6,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(9,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1684
OPERATION "Der Helge" MOVEMENT (10,9)
SLEEP 1193
OPERATION "Der Helge" MOVEMENT (11,9)
SLEEP 1428
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(2), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 5431
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:POISON_PILLS
SLEEP 3347
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(11,4), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 1190
OPERATION "Der Helge" MOVEMENT (12,5)
SLEEP 884
OPERATION "Der Helge" MOVEMENT (12,6)
SLEEP 461
OPERATION "Der Helge" MOVEMENT (12,7)
SLEEP 6180
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=f0f97df3-ce7a-422e-94a1-ead446ff275a, name='Nr. 5', coordinates=(22,1), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(9,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (10,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(21,13), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=76/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 7101
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:COCKTAIL
SLEEP 1878
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[POISON_PILLS(1), TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 3888
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:POISON_PILLS
SLEEP 1983
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=5ee6eec8-1aed-4999-98e8-9e38562765ec, name='James Bond', coordinates=(20,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[GRAPPLE, MOTHBALL_POUCH(5), FOG_TIN(1)], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(6,17), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1025
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(10,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(20,13), mp=3, ap=1, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (21,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=76/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH, COCKTAIL(1)], exfiltrated=false]
SLEEP 3071
OPERATION "Der Helge" GADGET_ACTION (23,4),gadget:COCKTAIL
SLEEP 2092
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,7), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 784
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,17), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0eba9fc0-3268-44a8-b42a-c75eb57bb663, name='The legendary Gustav', coordinates=(21,12), mp=2, ap=2, hp=15/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (20,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(11,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (10,12)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
SLEEP 2885
OPERATION "Der Helge" GADGET_ACTION (22,4),gadget:COCKTAIL
SLEEP 1413
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,7), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 987
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 877
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=acb676f0-cf4f-4365-9da8-674db4221c9c, name='Schleim B. Olzen', coordinates=(23,4), mp=3, ap=1, hp=52/100, ip=0, chips=13, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
SLEEP 817
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (6,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(10,12), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (10,11)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1494
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=0565476c-08de-4676-b6f7-66725cb2844f, name='Meister Yoda', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=20, properties=[LUCKY_DEVIL, OBSERVATION, TOUGHNESS], gadgets=[TECHNICOLOUR_PRISM(1), COCKTAIL(1)], exfiltrated=false]
SLEEP 3049
OPERATION "Der Helge" GADGET_ACTION (22,3),gadget:COCKTAIL
SLEEP 11316
OPERATION "Der Helge" MOVEMENT (23,2)
SLEEP 1462
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,7), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 709
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 13
# ---------------------------------------------------------
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(10,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (10,10)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(6,17), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=ad4e3c34-47e7-43e8-b7f6-2245f6a5e41d, name='Tante Gertrude', coordinates=(12,7), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[POCKET_LITTER], exfiltrated=false]
SLEEP 823
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 616
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 14
# ---------------------------------------------------------
# Turn for: Character [characterId=0d53766f-24d7-4bd6-9b76-fa7968750b07, name='Ein Wischmob', coordinates=(10,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION ki20 MOVEMENT (11,10)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,17)
OPERATION ki20 RETIRE <ignored>
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 760
OPERATION "Der Helge" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 15
# ---------------------------------------------------------
# Turn for: Character [characterId=9f78ef68-9724-4dc1-a674-23ee26b9d907, name='Spröder Senf', coordinates=(11,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[], exfiltrated=false]
SLEEP 1661
OPERATION "Der Helge" MOVEMENT (10,10)
SLEEP 918
OPERATION "Der Helge" MOVEMENT (10,11)
SLEEP 2193
OPERATION "Der Helge" RETIRE <ignored>
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,17), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (4,16)
OPERATION ki20 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 16
# ---------------------------------------------------------
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(4,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,16)
OPERATION ki20 RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 17
# ---------------------------------------------------------
# Turn for: Character [characterId=c3f38c02-1342-4144-a4d6-93a5361e063f, name='Saphira', coordinates=(5,16), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION ki20 MOVEMENT (5,17)
OPERATION ki20 RETIRE <ignored>
# =============================================================================
# Winner: Der Helge for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 320 Player Two: 448
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 52 Player Two: 39
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 4
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 85 Player Two: 96
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# Movements on the cat (The total number of movements performed on a field the cat was on.):
#   Player one: 0 Player Two: 0
# Number of poisoned cocktails (The total number of poisoned cocktails the player did consume.):
#   Player one: 0 Player Two: 4
# ---------------------------------------------------------
# End of File
