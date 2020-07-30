# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-loss-UnAufaeLLiG-2020-07-05--05-50-37--17548847002062543535.story
# Date: Sun Jul 05 05:50:37 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Sun Jul 05 05:50:37 CEST 2020"
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
  {"characterId":"6626ae67-a80e-43ee-8a9a-28d06dfa8271","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"be4c89a5-c1ec-4aed-b0ac-7891ac86cc66","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"b77c4610-ca6b-4ebd-8062-6484a4836728","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"0efa1608-d128-4800-804c-ea654cb09760","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"71293eb2-30e8-4c09-86d3-f6e4b25733d8","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"aa2f7c58-4b39-47fa-bdd2-a55154918d6a","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"efcb7cc1-45f9-48f6-8183-76797248754e","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"925042dc-fa48-48ea-a783-a8d2a39a757c","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"1afa40ab-da8a-4c3c-9e8f-1661fb976a7d","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"1e01f510-f83f-46e8-a420-ce6df2c34f17","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"19d03587-e098-4caf-ae97-bf20b6f90597","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"6427aca8-ca58-42d8-987e-0d699b1f7b30","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"39f9d907-484b-45a7-9005-e08ce746f85e","name":"Misses Y","description":"Ist UnAufaeLLiGich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]},
  {"characterId":"fe61de59-9d72-4b0c-a543-9711acc1ce9b","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"968d178e-13cc-40b5-8579-3e29a65f822f","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"a8cfafd0-da71-479e-aeb5-82a456501c2a","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"6b08f6fe-2ec0-4457-9ad1-4e037e6e3591","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal UnAufaeLLiG "Meister Yoda,Mister X,Schleim B. Olzen,pocket_litter,anti_plague_mask,grapple"
CONFIG_INJECT next-proposal Saphira "Hans Peter Otto,James Bond,Tante Gertrude,mothball_pouch,poison_pills,mirror_of_wilderness"
CONFIG_INJECT next-proposal Saphira "Ein Wischmob,Spröder Senf,Austauschbarer Agent Dieter 42,mirror_of_wilderness,wiretap_with_earplugs,bowler_blade"
CONFIG_INJECT next-proposal Saphira "Tante Gertrude,Zackiger Zacharias,Austauschbarer Agent Dieter 42,nugget,poison_pills,chicken_feed"
CONFIG_INJECT next-proposal Saphira "Saphira,Spröder Senf,Hans Peter Otto,moledie,mirror_of_wilderness,magnetic_watch"
CONFIG_INJECT next-proposal Saphira "James Bond,Petterson,Misses Y,fog_tin,moledie,rocket_pen"
CONFIG_INJECT next-proposal Saphira "Nr. 7,Hans Peter Otto,Nr. 5,laser_compact,moledie,jetpack"
CONFIG_INJECT next-proposal Saphira "The legendary Gustav,Tante Gertrude,Misses Y,technicolour_prism,moledie,laser_compact"
CONFIG_INJECT next-proposal Saphira poison_pills,technicolour_prism,moledie
CONFIG_INJECT next-proposal UnAufaeLLiG "Ein Wischmob,Spröder Senf,The legendary Gustav,moledie,technicolour_prism,grapple"
CONFIG_INJECT next-proposal UnAufaeLLiG "The legendary Gustav,Schleim B. Olzen,Mister Y,moledie,bowler_blade,technicolour_prism"
CONFIG_INJECT next-proposal UnAufaeLLiG "Meister Yoda,James Bond,Schleim B. Olzen,laser_compact,magnetic_watch,jetpack"
CONFIG_INJECT next-proposal UnAufaeLLiG "Ein Wischmob,Mister X,Zackiger Zacharias,magnetic_watch,laser_compact,grapple"
CONFIG_INJECT next-proposal UnAufaeLLiG "Schleim B. Olzen,The legendary Gustav,Nr. 7,mirror_of_wilderness,gas_gloss,grapple"
CONFIG_INJECT next-proposal UnAufaeLLiG "The legendary Gustav,Zackiger Zacharias,Mister Y,fog_tin,grapple,anti_plague_mask"
CONFIG_INJECT next-proposal UnAufaeLLiG "Misses Y,Schleim B. Olzen,Nr. 5,jetpack,magnetic_watch,hairdryer"
CONFIG_INJECT safe-order value 3,1,2
CONFIG_INJECT npc-pick value "The legendary Gustav,James Bond,FOG_TIN,MOLEDIE,CHICKEN_FEED,JETPACK,LASER_COMPACT,TECHNICOLOUR_PRISM"
CONFIG_INJECT start-positions value "<cat>,3/2,Ein Wischmob,2/2,Hans Peter Otto,3/7,James Bond,7/8,Meister Yoda,6/11,Petterson,5/2,Saphira,4/10,Spröder Senf,5/6,Tante Gertrude,1/11,The legendary Gustav,1/9"
CONFIG_INJECT next-round-order value "<cat>,Saphira,Hans Peter Otto,Meister Yoda,James Bond,The legendary Gustav,Tante Gertrude,Ein Wischmob,Petterson,Spröder Senf"
CONFIG_INJECT next-round-order value "Tante Gertrude,Saphira,Ein Wischmob,James Bond,Petterson,Hans Peter Otto,Meister Yoda,Spröder Senf,<cat>,The legendary Gustav"
CONFIG_INJECT next-round-order value "Saphira,Tante Gertrude,Meister Yoda,The legendary Gustav,<cat>,Ein Wischmob,James Bond,Petterson,Hans Peter Otto,Spröder Senf"
CONFIG_INJECT next-round-order value "<cat>,Tante Gertrude,Spröder Senf,Petterson,Saphira,Hans Peter Otto,Ein Wischmob,The legendary Gustav,Meister Yoda,James Bond"
CONFIG_INJECT next-round-order value "<cat>,James Bond,Hans Peter Otto,Spröder Senf,Tante Gertrude,Saphira,Petterson,The legendary Gustav,Meister Yoda,Ein Wischmob"
# ---------------------------------------------------------
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "James Bond:true;true"
CONFIG_INJECT random-result NPC_MOVEMENT "James Bond:(7,9);(6,8);(5,7);(4,6);(5,7);(6,6)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "James Bond:(7,7)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "James Bond:0;0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "James Bond:0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS "Ein Wischmob:false"
CONFIG_INJECT random-result GAMBLE_WIN "Ein Wischmob:false"
CONFIG_INJECT random-result OPERATION_SUCCESS "Spröder Senf:true;false;false;false;false;false"
CONFIG_INJECT random-result OPERATION_SUCCESS Petterson:false
CONFIG_INJECT random-result GAMBLE_WIN Petterson:false
CONFIG_INJECT random-result OPERATION_SUCCESS Saphira:true;true
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:true;false;false;false;true
CONFIG_INJECT random-result NPC_MOVEMENT "The legendary Gustav:(2,9);(3,8);(3,10);(4,10);(3,11);(3,10);(3,9);(3,10)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "The legendary Gustav:0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:false;false;false;false;true"
CONFIG_INJECT random-result CLOSEST_FREE_FIELD_FADE global:(6,7)
CONFIG_INJECT random-result CAT_WALK_TARGET global:(4,1);(3,2);(2,2);(2,3);(2,4)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:6
CONFIG_INJECT random-result OPERATION_SUCCESS "Meister Yoda:false"
# =============================================================================
# This is the main part
# =============================================================================
HELLO UnAufaeLLiG PLAYER
HELLO Saphira PLAYER
ITEM Saphira mothball_pouch
ITEM Saphira wiretap_with_earplugs
ITEM Saphira nugget
ITEM Saphira Saphira
ITEM Saphira Petterson
ITEM Saphira "Hans Peter Otto"
ITEM Saphira "Tante Gertrude"
ITEM Saphira poison_pills
EQUIP Saphira "Hans Peter Otto,Saphira,Tante Gertrude,Petterson,MOTHBALL_POUCH,WIRETAP_WITH_EARPLUGS,NUGGET,POISON_PILLS"
ITEM UnAufaeLLiG pocket_litter
ITEM UnAufaeLLiG "Spröder Senf"
ITEM UnAufaeLLiG bowler_blade
ITEM UnAufaeLLiG "Meister Yoda"
ITEM UnAufaeLLiG "Ein Wischmob"
ITEM UnAufaeLLiG mirror_of_wilderness
ITEM UnAufaeLLiG grapple
ITEM UnAufaeLLiG hairdryer
EQUIP UnAufaeLLiG "Spröder Senf,Ein Wischmob,HAIRDRYER,MIRROR_OF_WILDERNESS,Meister Yoda,POCKET_LITTER,BOWLER_BLADE,GRAPPLE"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
OPERATION Saphira MOVEMENT (5,11)
OPERATION Saphira MOVEMENT (4,10)
OPERATION Saphira MOVEMENT (3,9)
OPERATION Saphira RETIRE <ignored>
OPERATION Saphira MOVEMENT (4,7)
OPERATION Saphira SPY_ACTION (5,6)
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (6,10)
OPERATION UnAufaeLLiG MOVEMENT (6,9)
OPERATION UnAufaeLLiG GADGET_ACTION (7,8),gadget:BOWLER_BLADE
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (2,3)
OPERATION UnAufaeLLiG MOVEMENT (2,4)
OPERATION UnAufaeLLiG GAMBLE_ACTION (3,3),stake:5
OPERATION UnAufaeLLiG GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION Saphira MOVEMENT (4,3)
OPERATION Saphira MOVEMENT (4,4)
OPERATION Saphira GAMBLE_ACTION (3,3),stake:1
OPERATION UnAufaeLLiG MOVEMENT (5,7)
OPERATION UnAufaeLLiG SPY_ACTION (6,8)
OPERATION UnAufaeLLiG PROPERTY_ACTION (4,4),property:OBSERVATION
OPERATION UnAufaeLLiG RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
OPERATION Saphira RETIRE <ignored>
OPERATION Saphira MOVEMENT (3,8)
OPERATION Saphira MOVEMENT (3,7)
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (3,5)
OPERATION UnAufaeLLiG MOVEMENT (4,6)
OPERATION UnAufaeLLiG SPY_ACTION (3,7)
OPERATION UnAufaeLLiG SPY_ACTION (4,7)
OPERATION Saphira MOVEMENT (4,5)
OPERATION Saphira MOVEMENT (3,6)
OPERATION Saphira SPY_ACTION (4,6)
OPERATION Saphira MOVEMENT (4,6)
OPERATION Saphira MOVEMENT (5,6)
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (6,8)
OPERATION UnAufaeLLiG MOVEMENT (6,7)
OPERATION UnAufaeLLiG SPY_ACTION (5,6)
OPERATION UnAufaeLLiG RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
OPERATION Saphira SPY_ACTION (4,7)
OPERATION Saphira SPY_ACTION (4,7)
OPERATION Saphira RETIRE <ignored>
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (5,6)
OPERATION UnAufaeLLiG MOVEMENT (4,5)
OPERATION UnAufaeLLiG RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (5,6)
OPERATION UnAufaeLLiG SPY_ACTION (6,7)
OPERATION UnAufaeLLiG RETIRE <ignored>
OPERATION Saphira MOVEMENT (4,6)
OPERATION Saphira MOVEMENT (5,7)
OPERATION Saphira GADGET_ACTION (6,6),gadget:NUGGET
OPERATION Saphira MOVEMENT (5,6)
OPERATION Saphira SPY_ACTION (4,5)
OPERATION Saphira MOVEMENT (4,5)
OPERATION UnAufaeLLiG MOVEMENT (6,10)
OPERATION UnAufaeLLiG MOVEMENT (5,10)
OPERATION UnAufaeLLiG PROPERTY_ACTION (3,10),property:OBSERVATION
OPERATION UnAufaeLLiG PROPERTY_ACTION (3,10),property:OBSERVATION
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
OPERATION Saphira SPY_ACTION (1,10)
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG MOVEMENT (4,10)
OPERATION UnAufaeLLiG MOVEMENT (3,9)
OPERATION UnAufaeLLiG SPY_ACTION (3,10)
OPERATION UnAufaeLLiG PROPERTY_ACTION (3,7),property:OBSERVATION
OPERATION Saphira MOVEMENT (6,8)
OPERATION Saphira MOVEMENT (6,9)
OPERATION Saphira SPY_ACTION (7,10)
OPERATION Saphira MOVEMENT (2,6)
OPERATION Saphira MOVEMENT (2,5)
OPERATION Saphira RETIRE <ignored>
OPERATION Saphira MOVEMENT (3,5)
OPERATION Saphira MOVEMENT (2,4)
OPERATION Saphira RETIRE <ignored>
OPERATION UnAufaeLLiG GADGET_ACTION (6,6),gadget:COCKTAIL
OPERATION UnAufaeLLiG RETIRE <ignored>
OPERATION UnAufaeLLiG RETIRE <ignored>
OPERATION Saphira SPY_ACTION (6,7)
OPERATION Saphira GADGET_ACTION (2,2),gadget:JETPACK
OPERATION Saphira RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
OPERATION Saphira SPY_ACTION (1,2)
OPERATION Saphira MOVEMENT (2,3)
OPERATION Saphira MOVEMENT (2,4)
# =============================================================================
# Winner: Saphira for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 303 Player Two: 608
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 16 Player Two: 24
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 0
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: true
# ---------------------------------------------------------
# Greetings
# Lennart
# End of File