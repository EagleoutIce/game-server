ASSURE_DEFAULT doDebug true

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

# We want the turing-machine to skip to the end and overwrite
# every charachter with an empty string (which acts as a blank)
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

# Now: how to specify state-transtions?
# we agree on writing t@<state>@<char> to identify any transition
# So whenever we read H in z0 we write H, go one to the right and stay in z0:
LET t@z0@H "SET c${@p} H\n${call:@right}\nSET @z z0"
LET t@z0@E "SET c${@p} E\n${call:@right}\nSET @z z0"
LET t@z0@L "SET c${@p} L\n${call:@right}\nSET @z z0"
LET t@z0@O "SET c${@p} O\n${call:@right}\nSET @z z0"
LET t@z0@W "SET c${@p} W\n${call:@right}\nSET @z z0"
LET t@z0@R "SET c${@p} R\n${call:@right}\nSET @z z0"
LET t@z0@D "SET c${@p} D\n${call:@right}\nSET @z z0"
# The space:
LET "t@z0@ " "SET c${@p} \" \"\n${call:@right}\nSET @z z0"
# The end:
LET t@z0@${blank} "SET c${@p} ${blank}\n${call:@left}\nSET @z ze"
# Please note, that neither calling SET for the field nor moving nor changing the state
# are necessary

# As currently there is no multiline-write support (which doesn't matter anyways)
# We will stick on using '\n' as command-separator

# Just for debug:
LET debug "IF ${doDebug}\nPRINT \"State: ${@z} (Target: ${@e}); Position: ${@p}\"\nSET @out \"\"\nITER 11:\nSET currentField \"c${@i}\"\nSET_EQ ${@i} ${@p} cursorIsHere\nIF ${cursorIsHere}\nSET @out \"${@out}|\"\nFI\nSET @out \"${@out}${eval:currentField}\"\nRETI\nPRINT \"Board: ${@out}\"\nFI"

CALL debug

SET infinite -1
ITER ${infinite}:
    # Get cell for current field
    SET currentField "c${@p}"
    # Determine the transition that would be applicable for the next state
    SET nxtState "t@${@z}@${eval:currentField}"
    # stopp if the next Transition does not exist -- will not expand then
    # we have to put nxtState into quotes as we would gobble the space otherwise
    SET_PRESENT "${nxtState}" isPresent
    BREAK_EQ "${isPresent}" "0"

    # we have to put nxtState into quotes as we would gobble the space otherwise    
    # If it exists: call it
    CALL "${nxtState}"

    CALL debug
RETI

SET_EQ "${@z}" "${@e}" validEnd

# One end is enough as we are able to provide do-nothing transitions
# for any valid end-state that shall lead to this one
IF ${doDebug}
    PRINT "Ended with ${@z} (wanted: ${@e})"
    IF ${validEnd}
        PRINT "Word accepted"
    ELSE 
        PRINT "Word not acceped"
    FI
FI

# WE want to collect the board for testing
SET @out ""
ITER 11:
    # Get cell for current field
    SET currentField "c${@i}"
    # Append
    SET @out "${@out}${eval:currentField}"
RETI
# Just store in a nicer name
SET finalBoard "${@out}"