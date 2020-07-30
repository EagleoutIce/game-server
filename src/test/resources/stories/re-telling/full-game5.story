# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020--2020-07-03--02-30-06--683970811002595712.story
# Date: Fri Jul 03 02:30:06 CEST 2020
# Server-Version: 1.0 (using Game-Data v1.1)
# =============================================================================
SET story-name server020
SET story-date "Fri Jul 03 02:30:06 CEST 2020"
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
  {"characterId":"c11ba9aa-a77a-4bc6-9559-bb94f14f9161","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"f1484c1b-da9f-41c8-88d0-f86da88c5fcb","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"d4d5456f-146c-4e47-813f-065b307920da","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"7b80947c-5c56-456f-aeb2-847d00240d84","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"01e0907a-fe9c-45c0-a477-fca4c852cee4","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"059a6490-74fe-4601-9bf6-fff67c2482a0","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"39214eca-e0ec-4dd0-bdae-89e752273d8a","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"74055aa5-2600-4eaf-8542-8f47f95fbf62","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"cd7e85c0-0bb0-41d5-9f56-fd722def29ac","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"c7c65e7f-0543-4f7a-9af0-593873670b5f","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"fe5dd130-767c-4931-8d04-168433d9068b","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"52410526-fa27-482a-8121-79ffc6a53e9a","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"6f08a92c-c14c-4f45-9e6e-6f316a4abb32","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"09feb159-1e66-4069-ac34-4327d60e9b70","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"12daf153-b70c-4157-8274-bec7c9f1ed72","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"0e13f651-20fb-4b32-a223-9e24a9b214da","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"e7ecb25d-9bfb-44a8-937f-0fa6d13777a5","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal "Udo Hinterberg" "Mister Y,Misses Y,Zackiger Zacharias,chicken_feed,gas_gloss,fog_tin"
CONFIG_INJECT next-proposal Nicht-Du "The legendary Gustav,James Bond,Nr. 7,pocket_litter,grapple,mothball_pouch"
CONFIG_INJECT next-proposal Nicht-Du "Austauschbarer Agent Dieter 42,Nr. 7,Petterson,pocket_litter,hairdryer,mothball_pouch"
CONFIG_INJECT next-proposal Nicht-Du "Schleim B. Olzen,Meister Yoda,Hans Peter Otto,mothball_pouch,grapple,pocket_litter"
CONFIG_INJECT next-proposal Nicht-Du "Schleim B. Olzen,Meister Yoda,Spröder Senf,poison_pills,rocket_pen,mirror_of_wilderness"
CONFIG_INJECT next-proposal Nicht-Du "Nr. 5,Tante Gertrude,Nr. 7,technicolour_prism,bowler_blade,mothball_pouch"
CONFIG_INJECT next-proposal Nicht-Du "Saphira,Tante Gertrude,Hans Peter Otto,rocket_pen,technicolour_prism,jetpack"
CONFIG_INJECT next-proposal Nicht-Du technicolour_prism,mirror_of_wilderness,jetpack
CONFIG_INJECT next-proposal Nicht-Du laser_compact,hairdryer,nugget
CONFIG_INJECT next-proposal "Udo Hinterberg" "Schleim B. Olzen,Ein Wischmob,James Bond,rocket_pen,nugget,laser_compact"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Petterson,Hans Peter Otto,Ein Wischmob,mirror_of_wilderness,moledie,magnetic_watch"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Mister X,Ein Wischmob,Misses Y,pocket_litter,magnetic_watch,mothball_pouch"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Mister Y,Ein Wischmob,Spröder Senf,mothball_pouch,fog_tin,magnetic_watch"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Spröder Senf,Ein Wischmob,James Bond,moledie,anti_plague_mask,gas_gloss"
CONFIG_INJECT next-proposal "Udo Hinterberg" "James Bond,Zackiger Zacharias,Misses Y,magnetic_watch,rocket_pen,wiretap_with_earplugs"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Spröder Senf,Meister Yoda,Mister Y,rocket_pen,gas_gloss,pocket_litter"
CONFIG_INJECT safe-order value 2,3,1
CONFIG_INJECT npc-pick value "Saphira,BOWLER_BLADE,LASER_COMPACT,JETPACK,Misses Y,GAS_GLOSS,MAGNETIC_WATCH,MOTHBALL_POUCH,ANTI_PLAGUE_MASK,WIRETAP_WITH_EARPLUGS,POCKET_LITTER"
CONFIG_INJECT start-positions value "<cat>,1/4,Austauschbarer Agent Dieter 42,5/3,James Bond,1/11,Misses Y,6/6,Mister X,3/9,Nr. 5,2/4,Saphira,2/9,Tante Gertrude,1/6,The legendary Gustav,6/11"
CONFIG_INJECT next-round-order value "The legendary Gustav,<cat>,Misses Y,Nr. 5,James Bond,Austauschbarer Agent Dieter 42,Saphira,Mister X,Tante Gertrude"
CONFIG_INJECT next-round-order value "The legendary Gustav,Saphira,Austauschbarer Agent Dieter 42,Mister X,Misses Y,Nr. 5,<cat>,Tante Gertrude,James Bond"
CONFIG_INJECT next-round-order value "Mister X,The legendary Gustav,Misses Y,Austauschbarer Agent Dieter 42,Saphira,Nr. 5,Tante Gertrude,<cat>,James Bond"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Nr. 5,James Bond,<cat>,Mister X,The legendary Gustav,Misses Y,Saphira,Tante Gertrude"
CONFIG_INJECT next-round-order value "Nr. 5,Saphira,Austauschbarer Agent Dieter 42,The legendary Gustav,<cat>,Misses Y,James Bond,Mister X,Tante Gertrude"
CONFIG_INJECT next-round-order value "<janitor>,Nr. 5,<cat>,Austauschbarer Agent Dieter 42,Tante Gertrude,James Bond,Saphira,Mister X,The legendary Gustav"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,James Bond,The legendary Gustav,Saphira,Tante Gertrude,Nr. 5,<janitor>,<cat>"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,<janitor>,<cat>,James Bond,Nr. 5,Saphira,The legendary Gustav"
CONFIG_INJECT next-round-order value "The legendary Gustav,Saphira,<cat>,James Bond,<janitor>,Nr. 5"
CONFIG_INJECT next-round-order value "<cat>,The legendary Gustav,Nr. 5,<janitor>,James Bond"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Nr. 5:false"
CONFIG_INJECT random-result GAMBLE_WIN "Nr. 5:true;false"
CONFIG_INJECT random-result OPERATION_SUCCESS Saphira:true;true;true;false;true
CONFIG_INJECT random-result NPC_MOVEMENT Saphira:(1,9);(2,9);(3,10);(4,11);(3,10)
CONFIG_INJECT random-result NPC_WAIT_IN_MS Saphira:0;0;0;0;0;0
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:false;true;true;true;false;true;false;false;true
CONFIG_INJECT random-result OPERATION_SUCCESS "The legendary Gustav:false;false;true"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "The legendary Gustav:true;true;false;true;true;false;false;true;false;true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Misses Y:true;true"
CONFIG_INJECT random-result NPC_MOVEMENT "Misses Y:(5,7);(6,6);(5,7);(4,7);(5,6);(5,7);(5,6);(4,7);(4,6);(3,6)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Misses Y:0;1"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Misses Y:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CLOSEST_FREE_FIELD_FADE global:(4,7);(1,5)
CONFIG_INJECT random-result CAT_WALK_TARGET global:(2,3);(3,2);(4,3);(3,2);(2,3);(3,2);(2,3);(2,4);(2,5);(3,5)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(6,1)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:3
CONFIG_INJECT random-result OPERATION_SUCCESS "Mister X:false;true"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:false;true;true;false;true;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "Austauschbarer Agent Dieter 42:true;false"
CONFIG_INJECT random-result HONEY_TRAP_TRIGGERS "Austauschbarer Agent Dieter 42:false"
# =============================================================================
# This is the main part
# =============================================================================
HELLO "Udo Hinterberg" PLAYER
HELLO Nicht-Du PLAYER
ITEM Nicht-Du "The legendary Gustav"
ITEM Nicht-Du "Austauschbarer Agent Dieter 42"
ITEM Nicht-Du grapple
ITEM Nicht-Du poison_pills
ITEM Nicht-Du "Nr. 5"
ITEM Nicht-Du "Tante Gertrude"
ITEM Nicht-Du technicolour_prism
ITEM Nicht-Du hairdryer
EQUIP Nicht-Du "The legendary Gustav,Nr. 5,TECHNICOLOUR_PRISM,HAIRDRYER,GRAPPLE,Austauschbarer Agent Dieter 42,POISON_PILLS,Tante Gertrude"
ITEM "Udo Hinterberg" chicken_feed
ITEM "Udo Hinterberg" nugget
ITEM "Udo Hinterberg" mirror_of_wilderness
ITEM "Udo Hinterberg" "Mister X"
ITEM "Udo Hinterberg" fog_tin
ITEM "Udo Hinterberg" moledie
ITEM "Udo Hinterberg" "James Bond"
ITEM "Udo Hinterberg" rocket_pen
EQUIP "Udo Hinterberg" "Mister X,MIRROR_OF_WILDERNESS,ROCKET_PEN,CHICKEN_FEED,James Bond,FOG_TIN,MOLEDIE,NUGGET"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(6,11), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (6,10)
OPERATION Nicht-Du MOVEMENT (6,9)
OPERATION Nicht-Du MOVEMENT (6,8)
OPERATION Nicht-Du PROPERTY_ACTION (6,6),property:OBSERVATION
# Turn for: Character [characterId=6f08a92c-c14c-4f45-9e6e-6f316a4abb32, name='Misses Y', coordinates=(6,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[GAS_GLOSS(1), MAGNETIC_WATCH, MOTHBALL_POUCH(5), ANTI_PLAGUE_MASK, WIRETAP_WITH_EARPLUGS(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[TECHNICOLOUR_PRISM(1), HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du GADGET_ACTION (3,3),gadget:TECHNICOLOUR_PRISM
OPERATION Nicht-Du MOVEMENT (1,4)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(1,11), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL], gadgets=[FOG_TIN(1), MOLEDIE, NUGGET(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (1,9),gadget:FOG_TIN
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(5)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (4,2)
OPERATION Nicht-Du GADGET_ACTION (3,1),gadget:POISON_PILLS
OPERATION Nicht-Du MOVEMENT (5,3)
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(2,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=fe5dd130-767c-4931-8d04-168433d9068b, name='Mister X', coordinates=(3,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MIRROR_OF_WILDERNESS, ROCKET_PEN(1), CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (2,10),gadget:ROCKET_PEN
OPERATION "Udo Hinterberg" MOVEMENT (3,8)
OPERATION "Udo Hinterberg" MOVEMENT (3,7)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(1,6), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,6)
OPERATION Nicht-Du SPY_ACTION (3,7)
OPERATION Nicht-Du MOVEMENT (2,5)
OPERATION Nicht-Du MOVEMENT (2,4)
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(6,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(2,9), mp=3, ap=1, hp=80/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (6,4)
OPERATION Nicht-Du MOVEMENT (6,5)
OPERATION Nicht-Du SPY_ACTION (6,6)
# Turn for: Character [characterId=fe5dd130-767c-4931-8d04-168433d9068b, name='Mister X', coordinates=(3,7), mp=3, ap=1, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MIRROR_OF_WILDERNESS, CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (4,6)
OPERATION "Udo Hinterberg" MOVEMENT (5,6)
OPERATION "Udo Hinterberg" SPY_ACTION (6,6)
OPERATION "Udo Hinterberg" MOVEMENT (4,5)
# Turn for: Character [characterId=6f08a92c-c14c-4f45-9e6e-6f316a4abb32, name='Misses Y', coordinates=(6,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[GAS_GLOSS(1), MAGNETIC_WATCH, MOTHBALL_POUCH(5), ANTI_PLAGUE_MASK, WIRETAP_WITH_EARPLUGS(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(1,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,5)
OPERATION Nicht-Du MOVEMENT (2,6)
OPERATION Nicht-Du GADGET_ACTION (1,3),gadget:GRAPPLE
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,3)
OPERATION Nicht-Du GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(1,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL], gadgets=[MOLEDIE, NUGGET(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (2,11)
OPERATION "Udo Hinterberg" GADGET_ACTION (3,10),gadget:NUGGET
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=fe5dd130-767c-4931-8d04-168433d9068b, name='Mister X', coordinates=(4,5), mp=3, ap=1, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MIRROR_OF_WILDERNESS, CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(6,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (7,9)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=6f08a92c-c14c-4f45-9e6e-6f316a4abb32, name='Misses Y', coordinates=(4,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[GAS_GLOSS(1), MAGNETIC_WATCH, MOTHBALL_POUCH(5), ANTI_PLAGUE_MASK, WIRETAP_WITH_EARPLUGS(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(6,5), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (5,6)
OPERATION Nicht-Du SPY_ACTION (4,5)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(3,10), mp=3, ap=1, hp=80/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT, JETPACK(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,7),gadget:JETPACK
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,7)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(2,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,6), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(3,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,8)
OPERATION Nicht-Du MOVEMENT (2,9)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(2,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (2,10),gadget:MOLEDIE
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=fe5dd130-767c-4931-8d04-168433d9068b, name='Mister X', coordinates=(4,5), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MIRROR_OF_WILDERNESS, CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (4,6)
OPERATION "Udo Hinterberg" SPY_ACTION (5,7)
OPERATION "Udo Hinterberg" MOVEMENT (3,5)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(7,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du SPY_ACTION (7,10)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=6f08a92c-c14c-4f45-9e6e-6f316a4abb32, name='Misses Y', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[GAS_GLOSS(1), MAGNETIC_WATCH, MOTHBALL_POUCH(5), ANTI_PLAGUE_MASK, WIRETAP_WITH_EARPLUGS(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(6,7), mp=3, ap=1, hp=80/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,8)
OPERATION "Udo Hinterberg" MOVEMENT (7,9)
OPERATION "Udo Hinterberg" SPY_ACTION (7,10)
OPERATION "Udo Hinterberg" MOVEMENT (6,8)
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,2)
OPERATION Nicht-Du GADGET_ACTION (2,2),gadget:COCKTAIL
OPERATION Nicht-Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(6,8), mp=2, ap=2, hp=80/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,7)
OPERATION "Udo Hinterberg" MOVEMENT (5,6)
OPERATION "Udo Hinterberg" SPY_ACTION (4,7)
OPERATION "Udo Hinterberg" SPY_ACTION (4,7)
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(7,8), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (6,7)
OPERATION Nicht-Du MOVEMENT (5,6)
OPERATION Nicht-Du MOVEMENT (4,5)
OPERATION Nicht-Du SPY_ACTION (3,5)
# Turn for: Character [characterId=6f08a92c-c14c-4f45-9e6e-6f316a4abb32, name='Misses Y', coordinates=(4,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[GAS_GLOSS(1), MAGNETIC_WATCH, MOTHBALL_POUCH(5), ANTI_PLAGUE_MASK, WIRETAP_WITH_EARPLUGS(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(2,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=fe5dd130-767c-4931-8d04-168433d9068b, name='Mister X', coordinates=(3,5), mp=3, ap=1, hp=60/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MIRROR_OF_WILDERNESS, CHICKEN_FEED(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (4,4)
OPERATION "Udo Hinterberg" MOVEMENT (4,3)
OPERATION "Udo Hinterberg" MOVEMENT (3,2)
OPERATION "Udo Hinterberg" GADGET_ACTION (3,1),gadget:COCKTAIL
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,2), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du GADGET_ACTION (1,3),gadget:COCKTAIL
OPERATION Nicht-Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,2), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,2)
OPERATION Nicht-Du MOVEMENT (2,2)
OPERATION Nicht-Du SPY_ACTION (1,2)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(2,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (2,10)
OPERATION "Udo Hinterberg" SPY_ACTION (2,9)
OPERATION "Udo Hinterberg" MOVEMENT (3,10)
OPERATION "Udo Hinterberg" SPY_ACTION (2,9)
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(6,7), mp=3, ap=1, hp=80/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[BOWLER_BLADE, LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (5,7),gadget:BOWLER_BLADE
OPERATION "Udo Hinterberg" MOVEMENT (7,8)
OPERATION "Udo Hinterberg" MOVEMENT (7,9)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(4,5), mp=2, ap=2, hp=100/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,6)
OPERATION Nicht-Du MOVEMENT (3,7)
OPERATION Nicht-Du PROPERTY_ACTION (3,10),property:OBSERVATION
OPERATION Nicht-Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(5,7), mp=2, ap=1, hp=60/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4)], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (4,7)
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(3,10), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (2,9)
OPERATION "Udo Hinterberg" MOVEMENT (2,11)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(3,7), mp=2, ap=2, hp=100/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,8)
OPERATION Nicht-Du MOVEMENT (2,9)
OPERATION Nicht-Du SPY_ACTION (1,10)
OPERATION Nicht-Du SPY_ACTION (1,10)
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(7,9), mp=2, ap=2, hp=80/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (1,3),gadget:LASER_COMPACT
OPERATION "Udo Hinterberg" MOVEMENT (6,8)
OPERATION "Udo Hinterberg" MOVEMENT (5,7)
OPERATION "Udo Hinterberg" SPY_ACTION (4,7)
# Turn for: Character [characterId=d4d5456f-146c-4e47-813f-065b307920da, name='Tante Gertrude', coordinates=(2,2), mp=3, ap=1, hp=100/100, ip=3, chips=10, properties=[NIMBLENESS, BABYSITTER, TOUGHNESS], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Nicht-Du GADGET_ACTION (2,2),gadget:COCKTAIL
OPERATION Nicht-Du MOVEMENT (2,3)
OPERATION Nicht-Du MOVEMENT (2,4)
OPERATION Nicht-Du MOVEMENT (2,5)
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(3,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION Nicht-Du GADGET_ACTION (3,7),gadget:MOLEDIE
OPERATION Nicht-Du MOVEMENT (3,7)
OPERATION Nicht-Du MOVEMENT (2,6)
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=09feb159-1e66-4069-ac34-4327d60e9b70, name='Austauschbarer Agent Dieter 42', coordinates=(4,7), mp=2, ap=1, hp=60/100, ip=3, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[POISON_PILLS(4), BOWLER_BLADE, MOLEDIE], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,6)
OPERATION Nicht-Du MOVEMENT (2,5)
OPERATION Nicht-Du GADGET_ACTION (2,6),gadget:BOWLER_BLADE
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(2,11), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (3,10)
OPERATION "Udo Hinterberg" MOVEMENT (3,9)
OPERATION "Udo Hinterberg" SPY_ACTION (2,9)
OPERATION "Udo Hinterberg" SPY_ACTION (2,9)
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,5)
OPERATION Nicht-Du MOVEMENT (2,4)
OPERATION Nicht-Du GAMBLE_ACTION (3,3),stake:3
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(5,7), mp=2, ap=2, hp=80/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (4,7)
OPERATION "Udo Hinterberg" MOVEMENT (3,6)
OPERATION "Udo Hinterberg" GADGET_ACTION (1,3),gadget:LASER_COMPACT
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(2,9), mp=3, ap=1, hp=100/100, ip=6, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (3,8)
OPERATION Nicht-Du MOVEMENT (2,7)
OPERATION Nicht-Du SPY_ACTION (3,6)
OPERATION Nicht-Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(2,7), mp=2, ap=2, hp=100/100, ip=6, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Nicht-Du RETIRE <ignored>
# Turn for: Character [characterId=12daf153-b70c-4157-8274-bec7c9f1ed72, name='Saphira', coordinates=(3,6), mp=3, ap=1, hp=80/100, ip=3, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[LASER_COMPACT], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (2,5)
OPERATION "Udo Hinterberg" SPY_ACTION (2,4)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=c11ba9aa-a77a-4bc6-9559-bb94f14f9161, name='James Bond', coordinates=(3,9), mp=2, ap=2, hp=80/100, ip=0, chips=10, properties=[SPRYNESS, TOUGHNESS, ROBUST_STOMACH, LUCKY_DEVIL, TRADECRAFT], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (3,8)
OPERATION "Udo Hinterberg" MOVEMENT (3,7)
OPERATION "Udo Hinterberg" SPY_ACTION (2,7)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=0e13f651-20fb-4b32-a223-9e24a9b214da, name='Nr. 5', coordinates=(2,4), mp=2, ap=1, hp=100/100, ip=0, chips=7, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[HAIRDRYER, GRAPPLE], exfiltrated=false]
OPERATION Nicht-Du GAMBLE_ACTION (3,3),stake:6
OPERATION Nicht-Du RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=7b80947c-5c56-456f-aeb2-847d00240d84, name='The legendary Gustav', coordinates=(2,7), mp=3, ap=1, hp=100/100, ip=6, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TRADECRAFT, OBSERVATION], gadgets=[DIAMOND_COLLAR], exfiltrated=false]
OPERATION Nicht-Du MOVEMENT (2,6)
OPERATION Nicht-Du MOVEMENT (3,5)
# =============================================================================
# Winner: Nicht-Du for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 366 Player Two: 536
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 30 Player Two: 43
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 60 Player Two: 40
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: true
# ---------------------------------------------------------
# End of File
