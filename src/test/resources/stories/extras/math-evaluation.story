# This story should test a wide range of math
# Statements and is not useful for testing their 
# indivdual constraints. This story will use 'SET'
# to set some expandables so they may be tested for
# the expected values...

# Check the set is working
SET setTest hello
SET setTest2 "wuhu${setTest}-this is funny: ${setTest}"
# Note, that at the moment you can not create aliasses - because why? 

# Now i want an 'ASSERT' keyword ._.

# check math sets
SET one 1
SET zero 0

# SET var shine
# ${call:inc}
# PRINT ${shine}

## Add
ADD 1 1 testAddTwo
ADD ${one} ${zero} testAddOne
ADD ${one} ${one} testAddTwo2
ADD 1 ${one} testAddTwo3
ADD 0 ${one} testAddOne2
ADD ${zero} 0 testAddZero

## Sub

SUB 1 1 testSubZero
SUB ${one} ${zero} testSubOne
SUB ${one} ${one} testSubZero2
SUB 1 ${one} testSubZero3
SUB 0 ${one} testSubMinusOne
SUB ${zero} 0 testSubZero4

## Mul

MUL 1 1 testMulOne
MUL ${one} ${zero} testMulZero
MUL ${one} ${one} testMulOne2
MUL 2 ${one} testMulTwo
MUL 0 ${one} testMulZero2
MUL 21 2 testMulFortyTwo

## Div

DIV 1 1 testDivOne
DIV ${one} ${one} testDivOne2
DIV 2 ${one} testDivTwo
DIV 0 ${one} testDivZero
DIV 42 2 testDivTwentyOne

## MOD

MOD 1 1 testModZero
MOD ${one} ${one} testModZero2
MOD 2 ${one} testModZero3
MOD 0 ${one} testModZero4
MOD 4 8 testModFour