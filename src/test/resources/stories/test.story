# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-Full Game-2020-06-28--03-43-58--3068787708669032311.story
# Date: Sun Jun 28 03:43:58 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Sun Jun 28 03:43:58 CEST 2020"
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
  {"characterId":"457c6566-dbd6-4f94-a73f-576df2607961","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"211a277b-6af7-4d66-9493-e95abcc9eab3","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"b9ef903f-98e7-40bb-a01f-f633e34d81f6","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"f2954d56-8a54-4757-b1fd-53e2acfe6298","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"231a1efc-205d-48a7-aa2e-c3c7dd1f2ed3","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"e4f46bfa-4350-44d4-8469-222fd1e2ea83","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"d9e2e78b-86b2-4066-b0a6-700e79159fc4","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"049e0225-d12c-44f8-a70b-2a2025308bc0","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"bf6fd8b6-ff3f-405c-b343-e462d6abd0fe","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"9a057c6c-4e73-42a3-a509-60aba70f781a","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"3bc01daa-ec95-42fe-b9a6-a5cf047e385c","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"193b5ebf-ea23-44cf-bd5e-621524c06ab8","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"10589132-e307-43c8-a8b0-cac4ef1e8362","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"ba206f6e-b26c-4777-9cf4-e02bad99ba4e","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"b6fc4cec-51a0-439c-af91-11632970c85c","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"17b45d5c-61b7-4953-b07d-16c730514e4e","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"fc468bb1-429f-4fd1-af74-46b706e188ad","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal saphira "Mister Y,Tante Gertrude,Mister X,bowler_blade,anti_plague_mask,moledie"
CONFIG_INJECT next-proposal dieter "Nr. 5,Nr. 7,Petterson,fog_tin,mothball_pouch,technicolour_prism"
CONFIG_INJECT next-proposal saphira "Spröder Senf,Ein Wischmob,Saphira,chicken_feed,poison_pills,wiretap_with_earplugs"
CONFIG_INJECT next-proposal dieter "Nr. 7,Hans Peter Otto,Misses Y,magnetic_watch,nugget,rocket_pen"
CONFIG_INJECT next-proposal saphira "Mister X,James Bond,Meister Yoda,fog_tin,mothball_pouch,pocket_litter"
CONFIG_INJECT next-proposal dieter "Nr. 5,Ein Wischmob,Mister Y,jetpack,rocket_pen,moledie"
CONFIG_INJECT next-proposal saphira "Mister X,Tante Gertrude,Schleim B. Olzen,hairdryer,pocket_litter,mirror_of_wilderness"
CONFIG_INJECT next-proposal dieter "Saphira,Nr. 5,Zackiger Zacharias,laser_compact,fog_tin,rocket_pen"
CONFIG_INJECT next-proposal saphira "Hans Peter Otto,Misses Y,Schleim B. Olzen,mothball_pouch,grapple,jetpack"
CONFIG_INJECT next-proposal dieter "Tante Gertrude,Mister X,Petterson,rocket_pen,wiretap_with_earplugs,pocket_litter"
CONFIG_INJECT next-proposal saphira "The legendary Gustav,Misses Y,Austauschbarer Agent Dieter 42,mothball_pouch,laser_compact,poison_pills"
CONFIG_INJECT next-proposal dieter "Meister Yoda,Zackiger Zacharias,Schleim B. Olzen,moledie,chicken_feed,fog_tin"
CONFIG_INJECT next-proposal saphira "Saphira,Misses Y,The legendary Gustav,nugget,mirror_of_wilderness,magnetic_watch"
CONFIG_INJECT next-proposal dieter "Schleim B. Olzen,Ein Wischmob,Meister Yoda,laser_compact,gas_gloss,chicken_feed"
CONFIG_INJECT next-proposal saphira mirror_of_wilderness,magnetic_watch,wiretap_with_earplugs
CONFIG_INJECT next-proposal dieter moledie,anti_plague_mask,laser_compact
CONFIG_INJECT safe-order value 1,2,3
CONFIG_INJECT npc-pick value "Zackiger Zacharias,Tante Gertrude,ROCKET_PEN,JETPACK,GAS_GLOSS,ANTI_PLAGUE_MASK,MAGNETIC_WATCH"
CONFIG_INJECT start-positions value "<cat>,7/2,Schleim B. Olzen,7/8,Nr. 7,1/11,Zackiger Zacharias,2/6,Hans Peter Otto,3/8,James Bond,3/11,Misses Y,1/7,Nr. 5,6/11,Mister Y,6/8,Tante Gertrude,6/3,Spröder Senf,6/5"
CONFIG_INJECT next-round-order value "Nr. 5,Spröder Senf,Zackiger Zacharias,Misses Y,<cat>,Hans Peter Otto,Nr. 7,James Bond,Tante Gertrude,Mister Y,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Tante Gertrude,Schleim B. Olzen,Nr. 7,Nr. 5,Zackiger Zacharias,Mister Y,Misses Y,Hans Peter Otto,James Bond,Spröder Senf,<cat>"
CONFIG_INJECT next-round-order value "Hans Peter Otto,Spröder Senf,Nr. 5,Zackiger Zacharias,Schleim B. Olzen,<cat>,James Bond,Misses Y,Tante Gertrude,Nr. 7,Mister Y"
CONFIG_INJECT next-round-order value "Spröder Senf,James Bond,Zackiger Zacharias,Tante Gertrude,Nr. 5,Misses Y,<cat>,Nr. 7,Schleim B. Olzen,Mister Y,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Spröder Senf,James Bond,Schleim B. Olzen,Nr. 7,Nr. 5,Mister Y,Misses Y,Tante Gertrude,Zackiger Zacharias,Hans Peter Otto,<cat>"
CONFIG_INJECT next-round-order value "James Bond,Zackiger Zacharias,Nr. 7,Nr. 5,Hans Peter Otto,Schleim B. Olzen,Spröder Senf,Mister Y,Misses Y,<cat>,Tante Gertrude"
CONFIG_INJECT next-round-order value "Hans Peter Otto,Mister Y,Zackiger Zacharias,Spröder Senf,Nr. 5,Misses Y,Schleim B. Olzen,Tante Gertrude,<cat>,Nr. 7,James Bond"
CONFIG_INJECT next-round-order value "James Bond,Mister Y,Nr. 5,Misses Y,<cat>,Hans Peter Otto,Spröder Senf,Tante Gertrude,Schleim B. Olzen,Zackiger Zacharias,Nr. 7"
CONFIG_INJECT next-round-order value "Hans Peter Otto,<cat>,Nr. 7,James Bond,Tante Gertrude,Nr. 5,Zackiger Zacharias,Schleim B. Olzen,Mister Y,Spröder Senf,Misses Y"
CONFIG_INJECT next-round-order value "Hans Peter Otto,Nr. 5,James Bond,<cat>,Mister Y,Zackiger Zacharias,Tante Gertrude,Schleim B. Olzen,Misses Y,Nr. 7,Spröder Senf"
CONFIG_INJECT next-round-order value "Schleim B. Olzen,Nr. 7,Misses Y,Mister Y,Tante Gertrude,James Bond,Nr. 5,Spröder Senf,<cat>,Zackiger Zacharias,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Zackiger Zacharias,Schleim B. Olzen,Hans Peter Otto,Misses Y,Mister Y,Nr. 5,Nr. 7,<cat>,James Bond,Spröder Senf,Tante Gertrude"
CONFIG_INJECT next-round-order value "Misses Y,Hans Peter Otto,Spröder Senf,<cat>,Mister Y,Zackiger Zacharias,Nr. 5,James Bond,Nr. 7,Schleim B. Olzen,Tante Gertrude"
CONFIG_INJECT next-round-order value "Misses Y,Nr. 5,Nr. 7,Mister Y,<cat>,Tante Gertrude,Hans Peter Otto,Zackiger Zacharias,Spröder Senf,Schleim B. Olzen,James Bond"
CONFIG_INJECT next-round-order value "Spröder Senf,Hans Peter Otto,<cat>,Nr. 7,Schleim B. Olzen,Misses Y,Nr. 5,<janitor>,Mister Y,James Bond"
CONFIG_INJECT next-round-order value "<cat>,<janitor>,Nr. 7,Hans Peter Otto,Schleim B. Olzen,Misses Y,James Bond,Nr. 5,Spröder Senf"
CONFIG_INJECT next-round-order value "Spröder Senf,<cat>,<janitor>,Misses Y,Nr. 7,James Bond,Nr. 5,Hans Peter Otto"
CONFIG_INJECT next-round-order value "Nr. 7,Spröder Senf,Hans Peter Otto,<janitor>,<cat>,James Bond,Misses Y"
CONFIG_INJECT next-round-order value "Misses Y,Spröder Senf,James Bond,<cat>,<janitor>,Nr. 7"
CONFIG_INJECT next-round-order value "Nr. 7,James Bond,<cat>,<janitor>,Spröder Senf"
CONFIG_INJECT next-round-order value "James Bond,<janitor>,Spröder Senf,<cat>"
CONFIG_INJECT next-round-order value "Spröder Senf,<janitor>,<cat>"
# ---------------------------------------------------------
CONFIG_INJECT random-result NPC_MOVEMENT "Tante Gertrude:(7,4);(6,5);(6,4);(6,3);(7,4);(6,4);(5,3);(6,3);(5,4);(6,3);(5,3);(4,2);(5,2);(5,1);(4,1);(5,1);(4,1);(3,2);(2,2);(3,2);(4,2);(4,1);(5,2);(5,1);(5,2);(6,3);(5,3);(6,4);(5,3);(4,4);(3,5);(4,4);(5,4);(6,4);(7,4);(6,5);(7,6);(6,5);(7,6);(6,6);(7,7);(6,8)"
CONFIG_INJECT random-result NPC_GADGET_PICK_AMOUNT "Tante Gertrude:5"
CONFIG_INJECT random-result NPC_GADGET_PICK_GADGETS "Tante Gertrude:ROCKET_PEN;JETPACK;GAS_GLOSS;ANTI_PLAGUE_MASK;MAGNETIC_WATCH"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Tante Gertrude:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result NPC_MOVEMENT "Zackiger Zacharias:(2,5);(1,4);(2,4);(1,5);(2,4);(3,5);(2,6);(2,7);(1,6);(1,7);(1,6);(2,5);(3,6);(3,5);(2,6);(3,7);(3,6);(3,5);(4,5);(5,4);(5,2)"
CONFIG_INJECT random-result NPC_GADGET_PICK_AMOUNT "Zackiger Zacharias:0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Zackiger Zacharias:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result WINS_ON_RANDOM global:false
CONFIG_INJECT random-result CAT_WALK_TARGET global:(6,1);(5,2);(4,3);(3,2);(4,2);(4,3);(5,2);(4,1);(4,3);(4,4);(4,2);(5,2);(5,3);(5,4);(5,3);(5,2);(6,1);(7,2);(6,3);(7,2);(6,1)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(7,6)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:2
# =============================================================================
# This is the main part
# =============================================================================
HELLO saphira PLAYER
SLEEP 199
HELLO dieter PLAYER
ITEM saphira bowler_blade
ITEM dieter technicolour_prism
ITEM saphira "Spröder Senf"
ITEM dieter "Nr. 7"
ITEM saphira "James Bond"
ITEM dieter "Mister Y"
ITEM saphira hairdryer
ITEM dieter "Nr. 5"
ITEM saphira "Hans Peter Otto"
ITEM dieter pocket_litter
ITEM saphira mothball_pouch
ITEM dieter fog_tin
ITEM saphira "Misses Y"
ITEM dieter "Schleim B. Olzen"
ITEM saphira wiretap_with_earplugs
ITEM dieter laser_compact
EQUIP saphira "Hans Peter Otto,James Bond,HAIRDRYER,MOTHBALL_POUCH,WIRETAP_WITH_EARPLUGS,Misses Y,BOWLER_BLADE,Spröder Senf"
EQUIP dieter "Nr. 5,TECHNICOLOUR_PRISM,Mister Y,POCKET_LITTER,Schleim B. Olzen,FOG_TIN,Nr. 7,LASER_COMPACT"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
SLEEP 249
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
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
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 13
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 14
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
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
OPERATION saphira RETIRE <ignored>
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
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 17
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 18
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 19
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION dieter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 20
# ---------------------------------------------------------
OPERATION dieter RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 21
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
OPERATION saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 22
# ---------------------------------------------------------
OPERATION saphira RETIRE <ignored>
# =============================================================================
# Winner: dieter for reason: VICTORY_BY_RANDOMNESS
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 480 Player Two: 480
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
