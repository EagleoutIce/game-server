COLLECT_START "\n" @fibIter
    SET f1 0
    SET f2 1

    ITER ${#1}:
        ADD ${f1} ${f2} tmp
        SET f1 ${f2}
        SET f2 ${tmp}
    RETI

    SET result ${f1}
    UNSET f1
    UNSET f2
COLLECT_END

SET fibIter ${@fibIter}
UNSET @fibIter


SET #1 12
CALL fibIter

SET result1 ${result}
# 144

SET #1 6
CALL fibIter
SET result2 ${result}
# 8

SET #1 13
CALL fibIter
SET result3 ${result}
# 233