%%%% AI Defense %%%%

:- [tools_ai].

% Check if the opponent has a winning move and try to avoid it.
playIA(Board, Move, Player):-
	(
		(Player is 1, findIndexWinnigMove(Board, Move, 2))
	;
		(Player is 2,findIndexWinnigMove(Board, Move, Player))
	),
	Move \= [];
	iaRandom(Board, Move, Player).