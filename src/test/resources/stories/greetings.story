## <- comment
# This is just a simple story file
# We want the client mark to connect with the
# server

HELLO mark PLAYER
# 'mark' is now a player and connected with the server
# currently stories have to be valid, so we cannot
# let 3 or more players try and connect

HELLO "jasmin müller" spectator
# double quotation marks can be used to get multiple
# words into one token
# some keys are case-insensitive (mostly enums)
# the syntax for HELLO is simple:
# HELLO <name> <role>

# This line makes the story wait for 200ms
