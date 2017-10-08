This kata is based on [The evolution of trust|http://ncase.me/trust/].
The rules are:
You have one choice. In front of you is a machine: if you put a coin in the machine, 
the other player gets three coins – and vice versa. You both can either choose to 
COOPERATE (put in coin), or CHEAT (don't put in coin).

# Constraints

Use a actor-based framework (like akka).

# How To
## Step 1 : create the Cheaty Player
The cheaty player always cheats

## Step 2 : create the Game Master
Given 2 players for a game,
Every round, The Game Master should
 - Ask to both players to play
 - Wait for their response (play or cheat)

At the end, it should return the result

 - Create two players and give them to the Master
 - Start the game

## Step 3 : The copy cat
He starts with COOPERATE, then he copies what the other player did at the last round.

## Step 4 : The detective
First: he analyzes the other player. 
He starts with : 
 # Cooperate
 # Cheat
 # Cooperate 
 # Cooperate

 - If the other player cheats back, he'll act like Copycat. 
 - If the other player never cheat back, he'll act like Always Cheat, to exploit it. 
