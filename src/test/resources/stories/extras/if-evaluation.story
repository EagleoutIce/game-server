# This story should test the if-statements. This story will use 'SET'
# to set some expandables so they may be tested for
# the expected values...
# Because of time, the tests may be extended in the future

# Check the set is working
SET setTest hello

SET checkTrue 0

IF true
    SET checkTrue 1
FI

SET_EQ ${setTest} hello check

IF ${check}
    SET checkTrue2 1
ELSE 
    SET checkTrue2 0
FI

IF_NOT ${check}
    SET checkFalse 1
ELSE 
    SET checkFalse 0
FI

SET true true

SET_LT 50 20 anotherCheck
# shall be false
IF ${anotherCheck}
    SET checkTrue3 0
ELSE 
    IF true
        SET checkTrue3 1
    ELSE
        SET checkTrue3 0
    FI
    IF_NOT ${true}
        SET checkInc -1
    ELSE
        ITER 6:
            SET checkInc ${@i}
        RETI
    FI
FI

# Check Inc should be 5