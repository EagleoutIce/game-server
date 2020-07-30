# We want to simulate a turing-machine with stories
# First we agree on cell-access by using 'c${i}'
# to get the element on an infinite-band negative
# Values will result in 'c-1' etc. which is perfectly
# fine from a program-perspective.
# The blank:
SET blank #
# We init the desired band:
SET c0  H
SET c1  E
SET c2  L
SET c3  L
SET c4  O
SET c5  " "
SET c6  W
SET c7  O
SET c8  R
SET c9  L
SET c10 D
SET c11 ${blank}

# We specify the valid ending-state:
SET @e ze
# We specify the current state as z0:
SET @z z0
# We will start (by convention) on field '0':
SET @p 0

# As we might often be left with got to the left or right
# We will just write shortcuts for this:
LET @right "ADD ${@p} 1 @p"
LET @left  "SUB ${@p} 1 @p"

# This is the compact variant :D
LET t@z0@H "${call:@right}"
LET t@z0@E "${call:@right}"
LET t@z0@L "${call:@right}"
LET t@z0@O "${call:@right}"
LET t@z0@W "${call:@right}"
LET t@z0@R "${call:@right}"
LET t@z0@D "${call:@right}"
# The space:
LET "t@z0@ " "${call:@right}"
# The end:
LET t@z0@${blank} "${call:@left}\nSET @z ze"

SET infinite -1
ITER ${infinite}:
    # Determine the transition that would be applicable for the next state
    SET nxtState "t@${@z}@${c${@p}}"
    # stopp if the next Transition does not exist -- will not expand then
    # we have to put nxtState into quotes as we would gobble the space otherwise
    SET_PRESENT "${nxtState}" isPresent
    BREAK_EQ "${isPresent}" "0"

    # we have to put nxtState into quotes as we would gobble the space otherwise    
    # If it exists: call it
    CALL "${nxtState}"

RETI

SET_EQ "${@z}" "${@e}" validEnd

# WE want to collect the board for testing
SET @out ""
ITER 11:
    # Append cell for current field
    SET @out "${@out}${c${@i}}"
RETI
# Just store in a nicer name
SET finalBoard "${@out}"