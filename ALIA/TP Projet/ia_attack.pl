%%%% AI Attack %%%%

:- [tools_ai].

% Check if the player has a winning move and try do it
playIA(Board, Move, Player):-
	(
		(Player is 1, findIndexWinnigMove(Board, Move, Player))
	;
		(Player is 2, findIndexWinnigMove(Board, Move, 2))
	),
	Move \= [];
	iaRandom(Board, Move, Player).