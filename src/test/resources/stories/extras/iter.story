# This story should test the iter statements 
# This story will use 'SET' to set some expandables 
# so they may be tested for the expected values...

# Check the set is working
SET setTest hello

# Test no change
ITER 4:
    SET testSimple 1
RETI

# Test increment
SET iterI 0
ITER 5:
    ADD ${iterI} 1 iterI
RETI
# should be 5

# Internal counter
ITER 3:
    SET shouldBe2 ${@i}
RETI


# Expand to variable target
ITER 3:
    SET i${@i} ${@i}
RETI
#i0 = 0, i1 = 1, i2 = 2

# nested iterations without save inner:
SET iterINested 0
ITER 5:
    ITER 3:
        ADD ${iterINested} 1 iterINested
    RETI
RETI
# should be 15

# Set iter hold outer:
ITER 5:
    SET @ii ${@i}
    ITER 3:
        SET iterNestedHold "${@ii}.${@i}"
    RETI
RETI
# Should be 4.2

# test with break:
ITER 100:
    SET shallBe50 ${@i}
    BREAK_EQ ${@i} 50
RETI

ITER 100:
    # set check to 1 if < 52
    # set check to 0 if >= 52
    SET_LT ${@i} 52 check
    # ;)
    SET shallBe50Two ${@i}
    # Break if >= 50
    BREAK_EQ ${check} 0
RETI
