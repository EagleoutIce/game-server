# This story was constructed by the StoryAuthor
# =============================================================================
# Filename: /var/folders/wr/myzf1qjx077bzddtqwp_6nkr0000gn/T/Stories/server020-loss-Udo Hinterberg-2020-07-16--06-36-13--4820081310442091458.story
# Date: Thu Jul 16 18:36:13 CEST 2020
# Server-Version: 1.1 (using Game-Data v1.2)
# =============================================================================
SET story-name server020
SET story-date "Thu Jul 16 18:36:13 CEST 2020"
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
  {"scenario":[["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FIREPLACE","WALL","WALL","FREE","FREE","FREE","FREE","FREE","FREE","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","WALL","WALL","WALL","FREE","SAFE","FREE","FREE","FREE","FREE","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"],["WALL","FREE","FREE","WALL","WALL","FREE","BAR_SEAT","FREE","FREE","WALL","FREE","FREE","FREE","SAFE","WALL","WALL","FIREPLACE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","FREE","WALL"],["WALL","FREE","WALL","FREE","FREE","FREE","FREE","FREE","WALL","WALL","FREE","FREE","SAFE","FREE","WALL","WALL","FREE","FREE","FREE","BAR_TABLE","FREE","FREE","FREE","FREE","WALL","WALL","WALL"],["WALL","FREE","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","WALL","BAR_TABLE","FREE","FREE","FREE","FREE","BAR_TABLE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","WALL","FREE","FREE","FREE","BAR_TABLE","FREE","FREE","FIREPLACE","FREE","FREE","FIREPLACE","WALL","WALL","WALL","WALL","WALL","WALL","FREE","WALL","WALL","WALL","WALL","FREE","FREE","WALL"],["WALL","WALL","WALL","FREE","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","WALL","WALL","FREE","BAR_SEAT","FREE","FREE","FREE","FREE","FREE","WALL","FREE","FREE","BAR_TABLE","ROULETTE_TABLE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FIREPLACE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL"],["WALL","FREE","FREE","FREE","FREE","WALL","WALL","WALL","WALL","FREE","BAR_SEAT","FREE","FREE","FREE","FREE","FREE","WALL","WALL","WALL","FREE","ROULETTE_TABLE","FREE","FREE","WALL","FREE","FREE","WALL"],["WALL","FREE","SAFE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","SAFE","FREE","FREE","FREE","FREE","WALL","WALL","WALL","WALL","WALL","WALL","FREE","WALL","WALL"],["WALL","WALL","FREE","FREE","FREE","ROULETTE_TABLE","FREE","FREE","WALL","WALL","WALL","WALL","FREE","FREE","BAR_TABLE","FREE","FREE","FREE","WALL","WALL","WALL","WALL","WALL","ROULETTE_TABLE","FREE","BAR_SEAT","WALL"],["WALL","WALL","FREE","FREE","FREE","FREE","FREE","FREE","WALL","BAR_SEAT","BAR_SEAT","WALL","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","FREE","WALL","WALL"],["WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"]]}
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
  {"characterId":"f9821ee4-ce1b-4bd9-b664-627b1a0fce5a","name":"James Bond","description":"Bester Geheimagent aller Zeiten mit 00-Status.","gender":"DIVERSE","features":["SPRYNESS","TOUGHNESS","ROBUST_STOMACH","LUCKY_DEVIL","TRADECRAFT"]}, 
  {"characterId":"7ce5273b-8cf3-4fb6-924c-936de1c9bde7","name":"Meister Yoda","description":"Yoda (* 896 VSY; † 4 NSY auf Dagobah) gehörte einer unbekannten Spezies an, war 66 cm groß und war am Ende seines Lebens 900 Jahre alt. Er hatte in über 800 Jahren als Jedi-(Groß-)Meister zahlreiche Schüler in der Macht ausgebildet, darunter u. a. Luke Skywalker und Count Dooku, und war ein Meister im Umgang mit dem Lichtschwert.","gender":null,"features":["LUCKY_DEVIL","OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"468ddbc6-aa43-4b9f-bfc3-170db84bc1af","name":"Tante Gertrude","description":"Nach wie vor die beste Tante, die man sich wünschen kann.","gender":"FEMALE","features":["NIMBLENESS","BABYSITTER","TOUGHNESS"]}, 
  {"characterId":"5273b13d-ae71-4652-ac2c-b0d697366544","name":"The legendary Gustav","description":"Wer ihn wählt, cheated, so einfach ist das -- der hat einfach alles, dieser Gustav.","gender":null,"features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TRADECRAFT","OBSERVATION"]}, 
  {"characterId":"25419519-a4ea-4307-8453-3b09f7e368ce","name":"Hans Peter Otto","description":"Auch Hans Otto, oder Otto-Normal genannt.","gender":"MALE","features":["ROBUST_STOMACH","FLAPS_AND_SEALS"]}, 
  {"characterId":"8e3c7726-ab1d-45ec-ab75-82d8283d28c8","name":"Ein Wischmob","description":"Wieso sollte der nicht mitspielen dürfen?","gender":null,"features":["JINX","SPRYNESS","HONEY_TRAP"]}, 
  {"characterId":"d352d2eb-9e2a-4ffa-9500-333f291f3568","name":"Zackiger Zacharias","description":"Langsamer, als die Polizei erlaubt.","gender":"DIVERSE","features":["PONDEROUSNESS","ROBUST_STOMACH"]}, 
  {"characterId":"5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc","name":"Schleim B. Olzen","description":null,"gender":"MALE","features":["LUCKY_DEVIL","NIMBLENESS","TRADECRAFT"]}, 
  {"characterId":"e7344a0b-7110-4594-a96a-0ed6e45c626a","name":"Spröder Senf","description":"Alle Macht dem Senf","gender":null,"features":["SPRYNESS","CONSTANT_CLAMMY_CLOTHES","OBSERVATION"]}, 
  {"characterId":"0648578d-b3af-47cd-b870-79ec2ad5dae2","name":"Petterson","description":"Den Findus keiner.","gender":null,"features":["HONEY_TRAP","BABYSITTER","FLAPS_AND_SEALS"]}, 
  {"characterId":"795051e6-02f2-4869-bf83-7f9b9926e2f0","name":"Mister X","description":"Wohin könnte er nur gehen?","gender":"MALE","features":["AGILITY","LUCKY_DEVIL"]}, 
  {"characterId":"c967b5b9-26d8-4560-adfe-79acf15a8b5e","name":"Mister Y","description":"Leider als Einzelkind aufgewachsen. Sowas prägt.","gender":"MALE","features":["LUCKY_DEVIL"]}, 
  {"characterId":"f4295113-1a91-44f0-8fdf-5f802b16db2f","name":"Misses Y","description":"Ist eigentlich nur für die Gleichberechtigung hier.","gender":"FEMALE","features":["OBSERVATION","TOUGHNESS"]}, 
  {"characterId":"20fd5820-030e-4c86-b27c-1f1bdb8da7f2","name":"Austauschbarer Agent Dieter 42","description":"Er war auf diesem Austauschseminar und hat sich seitdem so verändert.","gender":"DIVERSE","features":["HONEY_TRAP","LUCKY_DEVIL"]}, 
  {"characterId":"f4b10e6f-0436-469c-b3ea-a7d18add853b","name":"Saphira","description":"Natürlich ist sie im Pool... Es ist immerhin \"Saphira\", die beste!","gender":"FEMALE","features":["AGILITY","LUCKY_DEVIL","BANG_AND_BURN","TOUGHNESS"]}, 
  {"characterId":"44cf5213-ae5d-4055-99da-5373fd60ef20","name":"Nr. 5","description":"Hat diese Beschreibung vor dir gelesen","gender":null,"features":["HONEY_TRAP","TOUGHNESS"]}, 
  {"characterId":"878cf7df-60f5-4710-972e-8bce3f2e2038","name":"Nr. 7","description":"Closely related to Nr. 5, aber doch nur ein Wesen in der Warteschlange","gender":null,"features":["NIMBLENESS","PONDEROUSNESS"]}
]
COLLECT_END
CONFIG_INJECT characters RAW-JSON ${@characters}
# =============================================================================
# Now the server will write config-injects to assure
# deterministic behaviour.
# =============================================================================
CONFIG_INJECT next-proposal "Udo Hinterberg" "Nr. 7,Meister Yoda,Misses Y,technicolour_prism,grapple,laser_compact"
CONFIG_INJECT next-proposal Frodo "Mister X,Mister Y,Saphira,nugget,rocket_pen,moledie"
CONFIG_INJECT next-proposal Frodo "Austauschbarer Agent Dieter 42,Mister Y,Mister X,poison_pills,pocket_litter,anti_plague_mask"
CONFIG_INJECT next-proposal Frodo "Ein Wischmob,Petterson,The legendary Gustav,pocket_litter,nugget,poison_pills"
CONFIG_INJECT next-proposal Frodo "Saphira,James Bond,Nr. 5,pocket_litter,moledie,bowler_blade"
CONFIG_INJECT next-proposal Frodo "The legendary Gustav,Mister Y,Ein Wischmob,moledie,mirror_of_wilderness,magnetic_watch"
CONFIG_INJECT next-proposal Frodo "Mister Y,Mister X,Ein Wischmob,jetpack,pocket_litter,poison_pills"
CONFIG_INJECT next-proposal Frodo "Ein Wischmob,Nr. 5,Hans Peter Otto,gas_gloss,wiretap_with_earplugs,hairdryer"
CONFIG_INJECT next-proposal Frodo "Ein Wischmob,James Bond,Spröder Senf,anti_plague_mask,magnetic_watch,bowler_blade"
CONFIG_INJECT next-proposal "Udo Hinterberg" "The legendary Gustav,Spröder Senf,Hans Peter Otto,mirror_of_wilderness,laser_compact,fog_tin"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Mister Y,Hans Peter Otto,Tante Gertrude,pocket_litter,poison_pills,wiretap_with_earplugs"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Mister X,Schleim B. Olzen,Hans Peter Otto,pocket_litter,chicken_feed,wiretap_with_earplugs"
CONFIG_INJECT next-proposal "Udo Hinterberg" "The legendary Gustav,James Bond,Hans Peter Otto,anti_plague_mask,wiretap_with_earplugs,mirror_of_wilderness"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Zackiger Zacharias,The legendary Gustav,Mister Y,bowler_blade,gas_gloss,pocket_litter"
CONFIG_INJECT next-proposal "Udo Hinterberg" "James Bond,Tante Gertrude,Schleim B. Olzen,grapple,pocket_litter,mirror_of_wilderness"
CONFIG_INJECT next-proposal "Udo Hinterberg" "Petterson,Schleim B. Olzen,Ein Wischmob,hairdryer,fog_tin,pocket_litter"
CONFIG_INJECT safe-order value 3,2,4,5,1
CONFIG_INJECT npc-pick value "Ein Wischmob,HAIRDRYER,MIRROR_OF_WILDERNESS,GAS_GLOSS,CHICKEN_FEED,LASER_COMPACT,Nr. 7,ANTI_PLAGUE_MASK,Schleim B. Olzen,MOTHBALL_POUCH,POCKET_LITTER"
CONFIG_INJECT start-positions value "<cat>,4/2,Austauschbarer Agent Dieter 42,1/8,Ein Wischmob,7/3,Mister X,5/1,Nr. 5,20/5,Nr. 7,6/9,Saphira,21/13,Schleim B. Olzen,20/9,Spröder Senf,14/10,Zackiger Zacharias,13/10"
CONFIG_INJECT next-round-order value "Spröder Senf,Nr. 5,<cat>,Saphira,Nr. 7,Schleim B. Olzen,Austauschbarer Agent Dieter 42,Ein Wischmob,Zackiger Zacharias,Mister X"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,Ein Wischmob,Saphira,Mister X,Nr. 5,<cat>,Nr. 7,Schleim B. Olzen,Zackiger Zacharias,Spröder Senf"
CONFIG_INJECT next-round-order value "Saphira,Nr. 5,Mister X,Spröder Senf,Zackiger Zacharias,Nr. 7,Schleim B. Olzen,<cat>,Austauschbarer Agent Dieter 42,Ein Wischmob"
CONFIG_INJECT next-round-order value "Spröder Senf,Saphira,<cat>,Austauschbarer Agent Dieter 42,Nr. 7,Nr. 5,Ein Wischmob,Mister X,Schleim B. Olzen,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "Schleim B. Olzen,Ein Wischmob,Zackiger Zacharias,Nr. 5,Saphira,Nr. 7,Austauschbarer Agent Dieter 42,Spröder Senf,Mister X,<cat>"
CONFIG_INJECT next-round-order value "Nr. 7,<cat>,Ein Wischmob,Nr. 5,Zackiger Zacharias,Schleim B. Olzen,Spröder Senf,Mister X,Austauschbarer Agent Dieter 42,Saphira"
CONFIG_INJECT next-round-order value "Spröder Senf,Nr. 5,Ein Wischmob,<cat>,Schleim B. Olzen,Zackiger Zacharias,Saphira,Austauschbarer Agent Dieter 42,Mister X,Nr. 7"
CONFIG_INJECT next-round-order value "Spröder Senf,Saphira,Ein Wischmob,Nr. 5,Zackiger Zacharias,Nr. 7,<cat>,Austauschbarer Agent Dieter 42,Mister X,Schleim B. Olzen"
CONFIG_INJECT next-round-order value "Spröder Senf,<cat>,Zackiger Zacharias,Austauschbarer Agent Dieter 42,Mister X,Saphira,Nr. 7,Schleim B. Olzen,Nr. 5,Ein Wischmob"
CONFIG_INJECT next-round-order value "Nr. 5,Saphira,<cat>,Mister X,Schleim B. Olzen,Spröder Senf,Zackiger Zacharias,Austauschbarer Agent Dieter 42,<janitor>"
CONFIG_INJECT next-round-order value "Zackiger Zacharias,Schleim B. Olzen,Saphira,<janitor>,Mister X,Spröder Senf,<cat>,Austauschbarer Agent Dieter 42"
CONFIG_INJECT next-round-order value "<cat>,Austauschbarer Agent Dieter 42,Spröder Senf,Saphira,Zackiger Zacharias,<janitor>,Mister X"
CONFIG_INJECT next-round-order value "Austauschbarer Agent Dieter 42,<cat>,Mister X,Zackiger Zacharias,Spröder Senf,<janitor>"
CONFIG_INJECT next-round-order value "<janitor>,Zackiger Zacharias,Austauschbarer Agent Dieter 42,<cat>,Spröder Senf"
CONFIG_INJECT next-round-order value "<cat>,<janitor>,Spröder Senf,Zackiger Zacharias"
CONFIG_INJECT next-round-order value "<cat>,<janitor>,Spröder Senf"
# ---------------------------------------------------------
CONFIG_INJECT random-result OPERATION_SUCCESS "Spröder Senf:false;true;false;true;false;true;false;false;false;false;true;true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Ein Wischmob:false;false"
CONFIG_INJECT random-result HONEY_TRAP_TRIGGERS "Ein Wischmob:false;true;false;false;true"
CONFIG_INJECT random-result HONEY_TRAP_NEW_TARGET "Ein Wischmob:Mister X"
CONFIG_INJECT random-result NPC_MOVEMENT "Ein Wischmob:(6,4);(6,3);(7,3);(6,3);(6,5);(5,6);(4,5);(5,5);(6,4);(5,4);(8,5);(8,6);(8,6);(7,6);(7,5);(6,4);(6,3);(6,2)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "Ein Wischmob:(7,6);(7,2);(5,7);(12,12)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Ein Wischmob:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result NPC_MOVEMENT "Schleim B. Olzen:(20,8);(19,9);(20,9);(21,8);(21,7);(20,7);(19,7);(19,6);(19,5)"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Schleim B. Olzen:0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result OPERATION_SUCCESS Saphira:true;false;true;false;true
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN Saphira:true;true;false;false;false;false;false;true;false;true;false;false
CONFIG_INJECT random-result OPERATION_SUCCESS "Zackiger Zacharias:true"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Zackiger Zacharias:true;false;true;true;true;false;true;false;false;false;false;true;false;false;false"
CONFIG_INJECT random-result WIRETAP_SHOULD_BREAK global:true
CONFIG_INJECT random-result CAT_WALK_TARGET global:(4,1);(5,1);(6,1);(7,2);(7,1);(6,1);(6,2);(5,4);(4,5);(4,4);(7,3);(6,4);(5,5);(6,5);(7,4);(8,5)
CONFIG_INJECT random-result JANITOR_SUMMON_TARGET global:(19,1)
CONFIG_INJECT random-result ROULETTE_INITIAL_CHIPS global:8;14;18;19
CONFIG_INJECT random-result OPERATION_SUCCESS "Mister X:true;true;false;true;false"
CONFIG_INJECT random-result CHARACTER_MP_AP_GAIN "Mister X:false;false;true;true;true;true;false;false;false;true;false;true;false"
CONFIG_INJECT random-result OPERATION_SUCCESS "Austauschbarer Agent Dieter 42:true"
CONFIG_INJECT random-result NPC_HAS_RIGHT_KEY "Nr. 7:true;true"
CONFIG_INJECT random-result NPC_MOVEMENT "Nr. 7:(7,9);(7,8);(8,8);(8,9);(7,9);(7,8);(8,7);(8,8);(9,7);(8,7);(7,9);(8,8);(7,9);(8,8);(9,8);(9,7);(8,8);(9,8);(9,9);(8,9);(9,10);(10,9)"
CONFIG_INJECT random-result NPC_MOLEDIE_TARGET "Nr. 7:(13,10)"
CONFIG_INJECT random-result NPC_AMOUNT_OF_SAFE_KEYS "Nr. 7:0;0"
CONFIG_INJECT random-result NPC_WAIT_IN_MS "Nr. 7:0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0"
CONFIG_INJECT random-result CHARACTER_MP_AP_LOSS "Nr. 7:true;false;true;false;false;true;true;false;true"
# =============================================================================
# This is the main part
# =============================================================================
HELLO "Udo Hinterberg" PLAYER
HELLO Frodo PLAYER
ITEM Frodo rocket_pen
ITEM Frodo "Austauschbarer Agent Dieter 42"
ITEM Frodo nugget
ITEM Frodo Saphira
ITEM Frodo moledie
ITEM Frodo jetpack
ITEM Frodo "Nr. 5"
ITEM Frodo magnetic_watch
EQUIP Frodo "Saphira,JETPACK,Austauschbarer Agent Dieter 42,MOLEDIE,Nr. 5,MAGNETIC_WATCH,ROCKET_PEN,NUGGET"
ITEM "Udo Hinterberg" technicolour_prism
ITEM "Udo Hinterberg" "Spröder Senf"
ITEM "Udo Hinterberg" poison_pills
ITEM "Udo Hinterberg" "Mister X"
ITEM "Udo Hinterberg" wiretap_with_earplugs
ITEM "Udo Hinterberg" "Zackiger Zacharias"
ITEM "Udo Hinterberg" grapple
ITEM "Udo Hinterberg" fog_tin
EQUIP "Udo Hinterberg" "Spröder Senf,TECHNICOLOUR_PRISM,WIRETAP_WITH_EARPLUGS,POISON_PILLS,GRAPPLE,Zackiger Zacharias,Mister X,FOG_TIN"
# ---------------------------------------------------------
# Round Number: 1
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(14,10), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (13,9)
OPERATION "Udo Hinterberg" MOVEMENT (13,10)
OPERATION "Udo Hinterberg" GADGET_ACTION (14,12),gadget:GRAPPLE
OPERATION "Udo Hinterberg" GADGET_ACTION (16,8),gadget:GRAPPLE
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(20,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, ROCKET_PEN(1), NUGGET(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (20,4)
OPERATION Frodo MOVEMENT (21,4)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(21,13), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[JETPACK(1)], exfiltrated=false]
OPERATION Frodo MOVEMENT (20,13)
OPERATION Frodo GADGET_ACTION (9,8),gadget:JETPACK
OPERATION Frodo MOVEMENT (10,7)
OPERATION Frodo MOVEMENT (10,8)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(6,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(20,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(1,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (3,10),gadget:MOLEDIE
OPERATION Frodo MOVEMENT (2,8)
OPERATION Frodo MOVEMENT (3,7)
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(7,3), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(13,9), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (12,8)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(5,1), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[FOG_TIN(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (9,2),gadget:FOG_TIN
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
OPERATION "Udo Hinterberg" MOVEMENT (6,3)
OPERATION "Udo Hinterberg" SPY_ACTION (6,2)
# ---------------------------------------------------------
# Round Number: 2
# ---------------------------------------------------------
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(3,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(6,2), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(10,8), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (9,7)
OPERATION Frodo MOVEMENT (8,7)
OPERATION Frodo MOVEMENT (7,8)
OPERATION Frodo SPY_ACTION (8,7)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(7,3), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (6,3)
OPERATION "Udo Hinterberg" SPY_ACTION (6,3)
OPERATION "Udo Hinterberg" MOVEMENT (6,3)
OPERATION "Udo Hinterberg" MOVEMENT (6,4)
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(21,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, ROCKET_PEN(1), NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (24,3),gadget:ROCKET_PEN
OPERATION Frodo MOVEMENT (20,3)
OPERATION Frodo MOVEMENT (19,3)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(8,7), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(20,9), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(12,8), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (12,9)
OPERATION "Udo Hinterberg" MOVEMENT (13,10)
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(12,9), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (13,10),gadget:COCKTAIL
OPERATION "Udo Hinterberg" GADGET_ACTION (14,12),gadget:GRAPPLE
OPERATION "Udo Hinterberg" MOVEMENT (11,8)
OPERATION "Udo Hinterberg" MOVEMENT (10,7)
# ---------------------------------------------------------
# Round Number: 3
# ---------------------------------------------------------
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,8), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(19,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, NUGGET(1), COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,3),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (18,4)
OPERATION Frodo MOVEMENT (19,5)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,4), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (6,3)
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(10,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE, COCKTAIL(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (9,7)
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:GRAPPLE
OPERATION "Udo Hinterberg" GADGET_ACTION (9,7),gadget:COCKTAIL
OPERATION "Udo Hinterberg" MOVEMENT (8,6)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(13,10), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (12,9)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(7,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(20,7), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(5), POCKET_LITTER], exfiltrated=false]
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(3,7), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (6,4),gadget:MOLEDIE
OPERATION Frodo MOVEMENT (4,6)
OPERATION Frodo MOVEMENT (5,5)
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(6,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT, MOLEDIE], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 4
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(8,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,9), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(5,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (6,6),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (5,6)
OPERATION Frodo MOVEMENT (6,5)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(8,7), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(19,6), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, NUGGET(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,5),gadget:NUGGET
OPERATION Frodo MOVEMENT (19,5)
OPERATION Frodo MOVEMENT (18,4)
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(5,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,2), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,2)
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
OPERATION "Udo Hinterberg" MOVEMENT (7,2)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(19,6), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(5), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (16,3),gadget:MOTHBALL_POUCH
OPERATION Frodo MOVEMENT (20,5)
OPERATION Frodo MOVEMENT (20,4)
OPERATION Frodo MOVEMENT (19,3)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(12,9), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (11,8)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 5
# ---------------------------------------------------------
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(19,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(4), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(5,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(11,8), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(18,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (19,3)
OPERATION Frodo MOVEMENT (20,3)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,9), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (7,8)
OPERATION Frodo SPY_ACTION (8,7)
OPERATION Frodo SPY_ACTION (8,7)
OPERATION Frodo MOVEMENT (8,7)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(7,8), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(6,5), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo SPY_ACTION (5,4)
OPERATION Frodo MOVEMENT (5,4)
OPERATION Frodo MOVEMENT (5,3)
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(8,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,5),gadget:MOLEDIE
OPERATION "Udo Hinterberg" PROPERTY_ACTION (6,5),property:OBSERVATION
OPERATION "Udo Hinterberg" MOVEMENT (8,7)
OPERATION "Udo Hinterberg" MOVEMENT (7,7)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(7,2), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,5),gadget:MOLEDIE
OPERATION "Udo Hinterberg" MOVEMENT (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (7,4)
OPERATION "Udo Hinterberg" MOVEMENT (6,5)
# ---------------------------------------------------------
# Round Number: 6
# ---------------------------------------------------------
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(7,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(7,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT, MOLEDIE], exfiltrated=false]
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(20,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (20,3),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (21,4)
OPERATION Frodo MOVEMENT (22,3)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(11,8), mp=2, ap=0, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH, CLAMMY_CLOTHES], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (11,7)
OPERATION "Udo Hinterberg" MOVEMENT (11,6)
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(18,4), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(4), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (19,3)
OPERATION Frodo MOVEMENT (20,3)
OPERATION Frodo MOVEMENT (21,3)
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(7,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE, MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (8,6)
OPERATION "Udo Hinterberg" GADGET_ACTION (8,6),gadget:MOLEDIE
OPERATION "Udo Hinterberg" MOVEMENT (8,6)
OPERATION "Udo Hinterberg" MOVEMENT (8,5)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,5), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,4)
OPERATION "Udo Hinterberg" SPY_ACTION (5,3)
OPERATION "Udo Hinterberg" MOVEMENT (5,3)
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(6,4), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (6,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (6,3)
OPERATION Frodo MOVEMENT (7,3)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(8,6), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (8,5)
OPERATION Frodo SPY_ACTION (8,5)
OPERATION Frodo MOVEMENT (8,5)
OPERATION Frodo MOVEMENT (7,5)
# ---------------------------------------------------------
# Round Number: 7
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(8,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(22,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Frodo MOVEMENT (21,3)
OPERATION Frodo MOVEMENT (20,3)
OPERATION Frodo GADGET_ACTION (19,4),gadget:COCKTAIL
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(7,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT, MOLEDIE], exfiltrated=false]
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(22,3), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(4), POCKET_LITTER, COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (16,3),gadget:MOTHBALL_POUCH
OPERATION Frodo MOVEMENT (23,2)
OPERATION Frodo MOVEMENT (22,1)
OPERATION Frodo MOVEMENT (21,1)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(11,6), mp=1, ap=1, hp=100/100, ip=0, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (10,7)
OPERATION "Udo Hinterberg" SPY_ACTION (9,8)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,5), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (6,6),gadget:COCKTAIL
OPERATION Frodo GADGET_ACTION (7,6),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (6,4)
OPERATION Frodo MOVEMENT (7,3)
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(6,4), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (6,3)
OPERATION Frodo MOVEMENT (6,2)
OPERATION Frodo SPY_ACTION (6,1)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,1), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,1)
OPERATION "Udo Hinterberg" SPY_ACTION (6,2)
OPERATION "Udo Hinterberg" MOVEMENT (7,2)
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(9,8), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK, MOLEDIE], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 8
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(7,7), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(5), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (7,6),gadget:WIRETAP_WITH_EARPLUGS
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:POISON_PILLS
OPERATION "Udo Hinterberg" MOVEMENT (6,7)
OPERATION "Udo Hinterberg" MOVEMENT (5,6)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,3), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (7,2)
OPERATION Frodo MOVEMENT (7,4)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(7,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP, CLAMMY_CLOTHES], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(20,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo MOVEMENT (19,3)
OPERATION Frodo MOVEMENT (18,4)
OPERATION Frodo GADGET_ACTION (18,4),gadget:COCKTAIL
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(10,7), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (9,7)
OPERATION "Udo Hinterberg" MOVEMENT (8,6)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(8,8), mp=3, ap=0, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(6,2), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (7,2)
OPERATION Frodo MOVEMENT (7,2)
OPERATION Frodo MOVEMENT (8,2)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,2), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,1)
OPERATION "Udo Hinterberg" MOVEMENT (7,1)
OPERATION "Udo Hinterberg" SPY_ACTION (8,2)
OPERATION "Udo Hinterberg" SPY_ACTION (8,2)
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(21,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(3), POCKET_LITTER, COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (21,1),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (20,1)
OPERATION Frodo MOVEMENT (19,1)
OPERATION Frodo MOVEMENT (18,1)
# ---------------------------------------------------------
# Round Number: 9
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(5,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(4), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(8,6), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,6)
OPERATION "Udo Hinterberg" MOVEMENT (6,5)
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(8,2), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (7,1)
OPERATION Frodo MOVEMENT (7,3)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(7,1), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,2)
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (7,3)
OPERATION "Udo Hinterberg" SPY_ACTION (6,4)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,4), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (6,4)
OPERATION Frodo SPY_ACTION (7,3)
OPERATION Frodo MOVEMENT (7,3)
OPERATION Frodo MOVEMENT (7,2)
# Turn for: Character [characterId=878cf7df-60f5-4710-972e-8bce3f2e2038, name='Nr. 7', coordinates=(8,9), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[NIMBLENESS, PONDEROUSNESS], gadgets=[ANTI_PLAGUE_MASK], exfiltrated=false]
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(18,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(3), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo MOVEMENT (17,1)
OPERATION Frodo MOVEMENT (16,1)
OPERATION Frodo MOVEMENT (15,1)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(18,4), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH], exfiltrated=false]
OPERATION Frodo GADGET_ACTION (19,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (19,3)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=8e3c7726-ab1d-45ec-ab75-82d8283d28c8, name='Ein Wischmob', coordinates=(6,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[JINX, SPRYNESS, HONEY_TRAP, CLAMMY_CLOTHES], gadgets=[HAIRDRYER, MIRROR_OF_WILDERNESS, GAS_GLOSS(1), CHICKEN_FEED(1), LASER_COMPACT], exfiltrated=false]
# ---------------------------------------------------------
# Round Number: 10
# ---------------------------------------------------------
# Turn for: Character [characterId=44cf5213-ae5d-4055-99da-5373fd60ef20, name='Nr. 5', coordinates=(19,3), mp=2, ap=1, hp=100/100, ip=0, chips=10, properties=[HONEY_TRAP, TOUGHNESS], gadgets=[MAGNETIC_WATCH, COCKTAIL(1)], exfiltrated=false]
OPERATION Frodo MOVEMENT (18,4)
OPERATION Frodo GADGET_ACTION (18,4),gadget:COCKTAIL
OPERATION Frodo MOVEMENT (18,3)
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,2), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (6,1)
OPERATION Frodo MOVEMENT (7,1)
OPERATION Frodo MOVEMENT (8,2)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(7,4), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (8,2)
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (7,3)
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(15,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(3), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo MOVEMENT (14,1)
OPERATION Frodo MOVEMENT (13,1)
OPERATION Frodo MOVEMENT (12,1)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(5,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(4), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:POISON_PILLS
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:GRAPPLE
OPERATION "Udo Hinterberg" MOVEMENT (5,5)
OPERATION "Udo Hinterberg" MOVEMENT (4,4)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(6,5), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,4)
OPERATION "Udo Hinterberg" MOVEMENT (5,5)
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(7,4), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (7,3)
OPERATION Frodo SPY_ACTION (7,4)
OPERATION Frodo MOVEMENT (6,2)
# ---------------------------------------------------------
# Round Number: 11
# ---------------------------------------------------------
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(5,5), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=5955e8c1-6cc6-4df2-95d2-d7a4e9ae94bc, name='Schleim B. Olzen', coordinates=(12,1), mp=3, ap=1, hp=100/100, ip=0, chips=10, properties=[LUCKY_DEVIL, NIMBLENESS, TRADECRAFT], gadgets=[MOTHBALL_POUCH(3), POCKET_LITTER], exfiltrated=false]
OPERATION Frodo MOVEMENT (13,1)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(8,2), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (8,1)
OPERATION Frodo MOVEMENT (7,1)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(7,4), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,4)
OPERATION "Udo Hinterberg" MOVEMENT (6,3)
OPERATION "Udo Hinterberg" SPY_ACTION (6,2)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(4,4), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(3), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:GRAPPLE
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:GRAPPLE
OPERATION "Udo Hinterberg" MOVEMENT (3,5)
OPERATION "Udo Hinterberg" MOVEMENT (4,6)
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(6,2), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (6,3)
OPERATION Frodo MOVEMENT (5,3)
OPERATION Frodo MOVEMENT (5,4)
# ---------------------------------------------------------
# Round Number: 12
# ---------------------------------------------------------
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(5,4), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo SPY_ACTION (5,5)
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(4,6), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(3), GRAPPLE], exfiltrated=false]
OPERATION "Udo Hinterberg" GADGET_ACTION (6,6),gadget:GRAPPLE
OPERATION "Udo Hinterberg" GADGET_ACTION (14,12),gadget:GRAPPLE
OPERATION "Udo Hinterberg" MOVEMENT (3,5)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=f4b10e6f-0436-469c-b3ea-a7d18add853b, name='Saphira', coordinates=(7,1), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL, BANG_AND_BURN, TOUGHNESS], gadgets=[], exfiltrated=false]
OPERATION Frodo RETIRE <ignored>
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(5,5), mp=1, ap=1, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (5,4)
OPERATION "Udo Hinterberg" MOVEMENT (4,4)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,3), mp=3, ap=1, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
OPERATION "Udo Hinterberg" MOVEMENT (7,1)
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 13
# ---------------------------------------------------------
# Turn for: Character [characterId=20fd5820-030e-4c86-b27c-1f1bdb8da7f2, name='Austauschbarer Agent Dieter 42', coordinates=(5,4), mp=2, ap=1, hp=100/100, ip=24, chips=10, properties=[HONEY_TRAP, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION Frodo MOVEMENT (6,4)
OPERATION Frodo MOVEMENT (7,3)
OPERATION Frodo SPY_ACTION (6,2)
# Turn for: Character [characterId=795051e6-02f2-4869-bf83-7f9b9926e2f0, name='Mister X', coordinates=(6,2), mp=2, ap=2, hp=100/100, ip=24, chips=10, properties=[AGILITY, LUCKY_DEVIL], gadgets=[], exfiltrated=false]
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
OPERATION "Udo Hinterberg" MOVEMENT (7,1)
OPERATION "Udo Hinterberg" MOVEMENT (6,2)
OPERATION "Udo Hinterberg" SPY_ACTION (7,3)
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(4,4), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(3,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(3), GRAPPLE, COCKTAIL(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 14
# ---------------------------------------------------------
# Turn for: Character [characterId=d352d2eb-9e2a-4ffa-9500-333f291f3568, name='Zackiger Zacharias', coordinates=(4,4), mp=2, ap=0, hp=100/100, ip=24, chips=10, properties=[PONDEROUSNESS, ROBUST_STOMACH], gadgets=[MOLEDIE], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(3,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(3), GRAPPLE, COCKTAIL(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 15
# ---------------------------------------------------------
# Turn for: Character [characterId=e7344a0b-7110-4594-a96a-0ed6e45c626a, name='Spröder Senf', coordinates=(3,5), mp=2, ap=2, hp=100/100, ip=0, chips=10, properties=[SPRYNESS, CONSTANT_CLAMMY_CLOTHES, OBSERVATION], gadgets=[TECHNICOLOUR_PRISM(1), WIRETAP_WITH_EARPLUGS(1), POISON_PILLS(3), GRAPPLE, COCKTAIL(1), COCKTAIL(1)], exfiltrated=false]
OPERATION "Udo Hinterberg" RETIRE <ignored>
# ---------------------------------------------------------
# Round Number: 16
# ---------------------------------------------------------
# =============================================================================
# Winner: Frodo for reason: VICTORY_BY_IP
# ---------------------------------------------------------
# IP-Points gained (Amount of IP points the players have gained over the whole game-phase.):
#   Player one: 288 Player Two: 368
# Total fields moved on (Total number of fields moved on, this excludes if the character was moved by another one.):
#   Player one: 64 Player Two: 79
# Number of cocktails sipped (The total number of cocktails the player has sipped.):
#   Player one: 0 Player Two: 0
# Number of cocktails casted (The total number of cocktails the player has casted on the other faction.):
#   Player one: 0 Player Two: 0
# Total damage received (Total HP lost by all players in the faction.):
#   Player one: 0 Player Two: 0
# Has gifted the diamond collar (The player, that gifted the diamond collar to the cat.):
#   Player one: false Player Two: false
# ---------------------------------------------------------
CRASH "Udo Hinterberg"
# End of File
