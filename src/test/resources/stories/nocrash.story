# currently we have two chars in the set:

CONFIG_INJECT major timeout-multiplier 1
CONFIG_INJECT major max-strikes 2

HELLO Petterson SPECTATOR
HELLO henry PLAYER
HELLO walter AI

# as henry is player one he will get the proposal
ITER 3:
  ITEM henry random
  ITEM walter random
RETI

ITER 5:
  ITEM henry random
RETI

EQUIP henry random
