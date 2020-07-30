# This story should test the set-statements 
# This story will use 'SET' to set some expandables 
# so they may be tested for the expected values...
# Because of time, the tests may be extended in the future

# Check the set is working
SET setTest hello

SET one 1

SET_LEQ 1 1 shouldBeTrue
SET_LEQ ${one} 1 shouldBeTrue2
SET_LEQ 1 ${one} shouldBeTrue3
SET_LEQ 1 0 shouldBeFalse 
SET_LEQ ${one} 0 shouldBeFalse2

SET_LT 1 1 shouldBeFalse3
SET_LT ${one} 1 shouldBeFalse4
SET_LT 1 ${one} shouldBeFalse5
SET_LT 1 0 shouldBeFalse6 
SET_LT ${one} 0 shouldBeFalse7
SET_LT ${one} 2 shouldBeTrue4
SET_LT 0 1 shouldBeTrue5

# Others, maybe?

# Well, we can curry:
SET addOne "ADD 1"
# this will call 'ADD 1 41 shouldBe42'
${addOne} 41 shouldBe42


# Well, we can do methods, convention is to name the args
# with '#1' to '#X' and to use UNSET afterwards to clear up
# global mess
SET walter 0
# Defining the method to increment:
LET inc "ADD 1 ${eval:#1} ${#1}"
# The argument for this method shall be the variable in walter
SET #1 walter
# Now do 6 times
ITER 6:
    # call the method 'inc' using the argument walter
    ${call:inc}
    # the variable walter will be one highter now
RETI
# walter should be 6 now
UNSET #1
# Keep the convention assure #1 undefined afterwards