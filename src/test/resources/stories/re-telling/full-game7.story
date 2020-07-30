# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /tmp/Stories/server020-loss-Ich-2020-07-08--05-08-40--11336965338280648456.story
# Date: Wed Jul 08 17:08:40 CEST 2020
# Server-Version: 1.1 (using Game-Data v1.2)
# =============================================================================
SET story-name server020
SET story-date "Wed Jul 08 17:08:40 CEST 2020"
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
  {"characterId":"af3786ea-f3c3-4b59-a67d-cb0936045c72","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"7e9d737d-86d5-4317-9703-95d7b0101266","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"fd618e6e-93c5-4868-97f9-745c0f099c3f","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"d0171889-ca7c-463f-8037-5e517a054ed4","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"c8481fb3-e99d-450c-b1ac-a1e72e3e8e3a","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"11c8ee80-d9e0-4969-9349-417058aa974d","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"4f144857-01db-4967-ae14-42ece028765d","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"42794358-895b-44ad-a93b-5b3343eb51cd","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"581914c4-7281-4b86-a6f1-c0e6d441a702","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"cbaff5d4-38d3-4cc3-8aee-0ec818c5b301","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"b45e5f5d-5fed-43cc-9383-6cbfd1950335","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"c0c58b22-5e6e-41b5-9cce-c4deb7b9ee33","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"6936674b-c990-452d-893e-c39bea381249","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"486f45e3-a49c-4354-a7d2-aa38cb5f2f56","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"e4c06fb2-d458-4b50-8611-0bf18436eea3","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"133072bc-f7ee-4955-bc8a-9eea79792d12","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"8a005106-94d4-41c1-8759-948e436da67d","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal Ich "Nr. 5,Ein Wischmob,Meister Yoda,mothball_pouch,mirror_of_wilderness,wiretap_with_earplugs"
CONFIG_INJECT next-proposal Brandstifter "Mister Y,Mister X,The legendary Gustav,rocket_pen,jetpack,nugget"
CONFIG_INJECT next-proposal Brandstifter "Spröder Senf,Zackiger Zacharias,Mister X,technicolour_prism,grapple,chicken_feed"
CONFIG_INJECT next-proposal Brandstifter "Tante Gertrude,Misses Y,Mister X,grapple,magnetic_watch,gas_gloss"
CONFIG_INJECT next-proposal Brandstifter "Petterson,Hans Peter Otto,Austauschbarer Agent Dieter 42,anti_plague_mask,pocket_litter,gas_gloss"
CONFIG_INJECT next-proposal Brandstifter "Misses Y,Petterson,James Bond,bowler_blade,nugget,gas_gloss"
CONFIG_INJECT next-proposal Brandstifter "Zackiger Zacharias,Mister Y,Nr. 7,laser_compact,poison_pills,bowler_blade"
CONFIG_INJECT next-proposal Brandstifter "Mister X,Austauschbarer Agent Dieter 42,Tante Gertrude,technicolour_prism,grapple,fog_tin"
CONFIG_INJECT next-proposal Brandstifter "Tante Gertrude,The legendary Gustav,James Bond,technicolour_prism,nugget,jetpack"
CONFIG_INJECT next-proposal Ich "Meister Yoda,The legendary Gustav,Nr. 7,gas_gloss,bowler_blade,fog_tin"
CONFIG_INJECT next-proposal Ich "Austauschbarer Agent Dieter 42,Mister Y,Tante Gertrude,fog_tin,moledie,poison_pills"
CONFIG_INJECT next-proposal Ich "Nr. 5,Schleim B. Olzen,Austauschbarer Agent Dieter 42,nugget,pocket_litter,hairdryer"
CONFIG_INJECT next-proposal Ich "Tante Gertrude,Saphira,James Bond,jetpack,mothball_pouch,laser_compact"
CONFIG_INJECT next-proposal Ich "Saphira,Mister X,Tante Gertrude,nugget,laser_compact,chicken_feed"
CONFIG_INJECT next-proposal Ich "Schleim B. Olzen,Misses Y,Meister Yoda,laser_compact,hairdryer,poison_pills"
CONFIG_INJECT next-proposal Ich "Mister X,Meister Yoda,Schleim B. Olzen,hairdryer,jetpack,gas_gloss"
CONFIG_INJECT safe-order value 3,2,1
CONFIG_INJECT npc-pick value "Saphira,GAS_GLOSS,HAIRDRYER,MOLEDIE,JETPACK,Austauschbarer Agent Dieter 42,CHICKEN_FEED,POCKET_LITTER"
CONFIG_INJECT start-positions value "<cat>,6/3,Austauschbarer Agent Dieter 42,7/2,Misses Y,7/9,Mister X,2/9,Nr. 5,2/6,Petterson,1/11,Saphira,7/8,Spröder Senf,6/5,Zackiger Zacharias,6/11"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Mister X,Spröder Senf,Saphira,Nr. 5,Misses Y,Petterson,Zackiger Zacharias,<cat>"
CONFIG_INJECT next-round-order value "<cat>,Saphira,Zackiger Zacharias,Mister X,Misses Y,Austauschbarer Agent Dieter 42,Nr. 5,Petterson,Spröder Senf"
CONFIG_INJECT next-round-order value "<cat>,Austauschbarer Agent Dieter 42,Nr. 5,Zackiger Zacharias,Mister X,Saphira,Petterson,Spröder Senf,Misses Y"
CONFIG_INJECT next-round-order value "Spröder Senf,Nr. 5,Saphira,Misses Y,Petterson,Mister X,Austauschbarer Agent Dieter 42,Zackiger Zacharias,<cat>"
CONFIG_INJECT next-round-order value "Nr. 5,Misses Y,Mister X,Petterson,Saphira,<cat>,Spröder Senf,Austauschbarer Agent Dieter 42,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "Zackiger Zacharias,<cat>,Misses Y,Spröder Senf,<janitor>,Petterson,Mister X,Nr. 5"
CONFIG_INJECT next-round-order value "<janitor>,Spröder Senf,Petterson,Mister X,<cat>,Zackiger Zacharias,Misses Y"
CONFIG_INJECT next-round-order value "Spröder Senf,Misses Y,<janitor>,Petterson,Mister X,<cat>"
CONFIG_INJECT next-round-order value "Mister X,Petterson,Misses Y,<cat>,<janitor>"
CONFIG_INJECT next-round-order value "<janitor>,<cat>,Petterson,Mister X"
CONFIG_INJECT next-round-order value <cat>,Petterson,<janitor>
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Spröder Senf:false;false;true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY Saphira:true
CONFIG_INJECT random-result NPC_MOVEMENT Saphira:(6,7);(5,6);(5,7);(6,6);(7,7);(7,6);(7,7);(7,8);(5,8);(4,8);(5,7);(4,7);(4,8);(5,8)
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET Saphira:(6,7)
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS Saphira:1
CONFIG_INJECT random-result NPC_WAIT_IN_MS Saphira:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:true;false;true;true;true
CONFIG_INJECT random-result OPERATION_SUCCESS "Zackiger Zacharias:false;false"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Zackiger Zacharias:true;true;false;true;true;false;true"
CONFIG_INJECT random-result OPERATION_SUCCESS "Misses Y:false;false;false"
CONFIG_INJECT random-result CLOSEST_FREE_FIELD_FADE global:(4,9);(5,8);(3,9);(5,9);(5,10)
CONFIG_INJECT random-result WIRETAP_SHOULD_BREAK global:true
CONFIG_INJECT random-result CAT_WALK_TARGET global:(5,2);(4,3);(5,2);(6,1);(5,1);(4,2);(5,3);(5,4);(6,4);(6,5);(7,6)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(2,3)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:5
CONFIG_INJECT random-result OPERATION_SUCCESS "Mister X:false;false"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:false;true;true;true;true;false;false;false;true;false"
CONFIG_INJECT random-result NPC_MOVEMENT "Austauschbarer Agent Dieter 42:(6,3);(7,2);(6,1);(5,2);(5,2);(4,3);(5,4);(4,5);(5,6);(5,7)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Austauschbarer Agent Dieter 42:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
# =============================================================================
# This is the main part
# =============================================================================
HELLO Ich PLAYER
HELLO Brandstifter PLAYER
ITEM Brandstifter rocket_pen
ITEM Brandstifter "Spröder Senf"
ITEM Brandstifter magnetic_watch
ITEM Brandstifter anti_plague_mask
ITEM Brandstifter Petterson
ITEM Brandstifter "Zackiger Zacharias"
ITEM Brandstifter grapple
ITEM Brandstifter technicolour_prism
EQUIP Brandstifter "Petterson,MAGNETIC_WATCH,Zackiger Zacharias,ANTI_PLAGUE_MASK,GRAPPLE,Spröder Senf,TECHNICOLOUR_PRISM,ROCKET_PEN"
ITEM Ich wiretap_with_earplugs
ITEM Ich bowler_blade
ITEM Ich fog_tin
ITEM Ich "Nr. 5"
ITEM Ich mothball_pouch
ITEM Ich nugget
ITEM Ich "Misses Y"
ITEM Ich "Mister X"
EQUIP Ich "Misses Y,BOWLER_BLADE,FOG_TIN,NUGGET,MOTHBALL_POUCH,Nr. 5,Mister X,WIRETAP_WITH_EARPLUGS"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=486f45e3-a49c-4354-a7d2-aa38cb5f2f56, name='Austauschbarer Agent Dieter 42', coordinates=(7,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[CHICKEN_FEED(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(2,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (3,9)
OPERATION Ich MOVEMENT (3,8)
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(6,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), ROCKET_PEN(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (5,6)
OPERATION Brandstifter MOVEMENT (4,7)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=e4c06fb2-d458-4b50-8611-0bf18436eea3, name='Saphira', coordinates=(7,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[GAS_GLOSS(1), HAIRDRYER, MOLEDIE, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=133072bc-f7ee-4955-bc8a-9eea79792d12, name='Nr. 5', coordinates=(2,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Ich MOVEMENT (3,7)
OPERATION Ich MOVEMENT (3,6)
OPERATION Ich SPY_ACTION (4,7)
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(7,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[BOWLER_BLADE, FOG_TIN(1), NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (6,8)
OPERATION Ich MOVEMENT (6,7)
OPERATION Ich SPY_ACTION (5,7)
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(6,11), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (6,10)
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=e4c06fb2-d458-4b50-8611-0bf18436eea3, name='Saphira', coordinates=(5,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[GAS_GLOSS(1), HAIRDRYER, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(6,10), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (6,9)
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(3,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (4,7)
OPERATION Ich MOVEMENT (5,7)
OPERATION Ich MOVEMENT (6,7)
OPERATION Ich SPY_ACTION (7,7)
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(5,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[BOWLER_BLADE, FOG_TIN(1), NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (6,8)
OPERATION Ich GADGET_ACTION (6,9),gadget:FOG_TIN
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=486f45e3-a49c-4354-a7d2-aa38cb5f2f56, name='Austauschbarer Agent Dieter 42', coordinates=(7,2), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[CHICKEN_FEED(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=133072bc-f7ee-4955-bc8a-9eea79792d12, name='Nr. 5', coordinates=(3,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Ich MOVEMENT (4,7)
OPERATION Ich MOVEMENT (5,7)
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(3,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), ROCKET_PEN(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (3,7)
OPERATION Brandstifter GADGET_ACTION (4,8),gadget:ROCKET_PEN
OPERATION Brandstifter PROPERTY_ACTION (6,8),property:OBSERVATION
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=486f45e3-a49c-4354-a7d2-aa38cb5f2f56, name='Austauschbarer Agent Dieter 42', coordinates=(4,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[CHICKEN_FEED(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=133072bc-f7ee-4955-bc8a-9eea79792d12, name='Nr. 5', coordinates=(5,7), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Ich MOVEMENT (4,7)
OPERATION Ich MOVEMENT (3,7)
OPERATION Ich SPY_ACTION (4,7)
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(6,9), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (6,8)
OPERATION Brandstifter MOVEMENT (6,7)
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(6,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (6,9)
OPERATION Ich MOVEMENT (6,8)
OPERATION Ich MOVEMENT (6,7)
OPERATION Ich GADGET_ACTION (6,8),gadget:WIRETAP_WITH_EARPLUGS
# Turn for: Character [characterId=e4c06fb2-d458-4b50-8611-0bf18436eea3, name='Saphira', coordinates=(7,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[GAS_GLOSS(1), HAIRDRYER, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(4,7), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (5,7)
OPERATION Brandstifter MOVEMENT (6,8)
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(6,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[BOWLER_BLADE, NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (7,8)
OPERATION Ich MOVEMENT (6,8)
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(7,8), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=133072bc-f7ee-4955-bc8a-9eea79792d12, name='Nr. 5', coordinates=(3,7), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Ich MOVEMENT (4,7)
OPERATION Ich MOVEMENT (5,7)
OPERATION Ich SPY_ACTION (4,7)
# Turn for: Character [characterId=e4c06fb2-d458-4b50-8611-0bf18436eea3, name='Saphira', coordinates=(6,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[GAS_GLOSS(1), HAIRDRYER, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(6,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[BOWLER_BLADE, NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (5,8)
OPERATION Ich MOVEMENT (4,8)
OPERATION Ich GADGET_ACTION (5,8),gadget:BOWLER_BLADE
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(6,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (5,8)
OPERATION Ich MOVEMENT (5,9)
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich GADGET_ACTION (4,8),gadget:BOWLER_BLADE
# Turn for: Character [characterId=486f45e3-a49c-4354-a7d2-aa38cb5f2f56, name='Austauschbarer Agent Dieter 42', coordinates=(4,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[CHICKEN_FEED(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(4,7), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (5,8)
OPERATION Brandstifter GADGET_ACTION (4,8),gadget:BOWLER_BLADE
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=133072bc-f7ee-4955-bc8a-9eea79792d12, name='Nr. 5', coordinates=(6,7), mp=2, ap=1, hp=80/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(4,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (3,9)
OPERATION Ich GADGET_ACTION (4,9),gadget:BOWLER_BLADE
OPERATION Ich MOVEMENT (4,9)
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(3,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich MOVEMENT (3,9)
OPERATION Ich MOVEMENT (4,10)
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=e4c06fb2-d458-4b50-8611-0bf18436eea3, name='Saphira', coordinates=(5,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[GAS_GLOSS(1), HAIRDRYER, JETPACK(1)], exfiltrated=false]
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(7,8), mp=2, ap=2, hp=60/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (6,9)
OPERATION Brandstifter MOVEMENT (5,9)
OPERATION Brandstifter GADGET_ACTION (4,9),gadget:BOWLER_BLADE
OPERATION Brandstifter SPY_ACTION (5,8)
# Turn for: Character [characterId=486f45e3-a49c-4354-a7d2-aa38cb5f2f56, name='Austauschbarer Agent Dieter 42', coordinates=(4,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[CHICKEN_FEED(1), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(4,8), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (4,9)
OPERATION Brandstifter GADGET_ACTION (5,10),gadget:GRAPPLE
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=4f144857-01db-4967-ae14-42ece028765d, name='Zackiger Zacharias', coordinates=(4,9), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[ANTI_PLAGUE_MASK, GRAPPLE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(4,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich MOVEMENT (5,9)
OPERATION Ich SPY_ACTION (4,9)
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(4,9), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (5,9)
OPERATION Brandstifter MOVEMENT (4,8)
OPERATION Brandstifter SPY_ACTION (4,9)
OPERATION Brandstifter SPY_ACTION (4,9)
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter GADGET_ACTION (1,10),gadget:MOLEDIE
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(4,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich MOVEMENT (4,8)
OPERATION Ich SPY_ACTION (4,9)
OPERATION Ich SPY_ACTION (5,9)
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(4,9), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1)], exfiltrated=false]
OPERATION Brandstifter MOVEMENT (4,10)
OPERATION Brandstifter MOVEMENT (5,9)
OPERATION Brandstifter SPY_ACTION (4,9)
OPERATION Brandstifter SPY_ACTION (4,9)
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(4,8), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich MOVEMENT (4,10)
OPERATION Ich SPY_ACTION (5,9)
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(4,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[NUGGET(1), MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich GADGET_ACTION (5,9),gadget:NUGGET
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=581914c4-7281-4b86-a6f1-c0e6d441a702, name='Spröder Senf', coordinates=(5,9), mp=2, ap=2, hp=60/100, ip=3, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), NUGGET(1)], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(4,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MOTHBALL_POUCH(5)], exfiltrated=false]
OPERATION Ich SPY_ACTION (5,9)
OPERATION Ich MOVEMENT (4,10)
OPERATION Ich MOVEMENT (5,10)
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(4,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich MOVEMENT (5,9)
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=b45e5f5d-5fed-43cc-9383-6cbfd1950335, name='Mister X', coordinates=(4,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[WIRETAP_WITH_EARPLUGS(1)], exfiltrated=false]
OPERATION Ich RETIRE <ignored>
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# Turn for: Character [characterId=6936674b-c990-452d-893e-c39bea381249, name='Misses Y', coordinates=(5,10), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[OBSERVATION, TOUGHNESS], gadgets=[MOTHBALL_POUCH(5), BOWLER_BLADE], exfiltrated=false]
OPERATION Ich MOVEMENT (4,9)
OPERATION Ich RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# Turn for: Character [characterId=cbaff5d4-38d3-4cc3-8aee-0ec818c5b301, name='Petterson', coordinates=(1,11), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, BABYSITTER], gadgets=[MAGNETIC_WATCH, MOLEDIE], exfiltrated=false]
OPERATION Brandstifter RETIRE <ignored>
# =============================================================================
# Winner: Brandstifter for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 360 Player Two: 363
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 43 Player Two: 17
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 20 Player Two: 40
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
# End of File