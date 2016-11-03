%%%% AI MinMax Adrien %%%%

:- [tools_ai].

playIA(Board, BestMove, Player):- negaMax(2, Board, Player, Player, _, BestMove).

playMoveIA(Board, Column, NewBoard, Player):-	nth1(Column,Board, E),
												playColumn(E, L, Player),
												replaceAtIndex(Board, NewBoard, Column, L).
% Voir pour changer l'heuristique
value(Board, MaxPlayer, -999):- changePlayer(MaxPlayer, NextPlayer), winner(Board, NextPlayer).
value(Board, MaxPlayer, 999):- winner(Board, MaxPlayer).
value(_, _, 0).

negaMax(0, Board, _, PlayerMax, Value, _):- value(Board, PlayerMax, Value).
negaMax(_, Board, _, PlayerMax, Value, _):- winner(Board, 1), value(Board, PlayerMax, Value).
negaMax(_, Board, _, PlayerMax, Value, _):- winner(Board, 2), value(Board, PlayerMax, Value).
negaMax(Depth, Board, Player, PlayerMax, BestValue, BestMove):-
	setof(PossibleMove, columnAvailable(Board, PossibleMove), AllPossibleMoves),
	Depth1 is Depth - 1,
	negaMax(AllPossibleMoves, Board, Depth1, Player, PlayerMax, BestValue, BestMove, -1000, nil).

negaMax([], _, _, _, _, BestValue, BestMove, BestValue, BestMove).
negaMax([HMoves|TMoves], Board, Depth, Player, PlayerMax, BestValue, BestMove, BestValueTmp, BestMoveTmp):-
	playMoveIA(Board, HMoves, NewBoard, Player),
	changePlayer(Player, NextPlayer),
	negaMax(Depth, NewBoard, NextPlayer, PlayerMax, NextPlayerBestValue, _), 
	Value is -NextPlayerBestValue,
	(	Value > BestValueTmp ->
		negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, Value, HMoves)
	;
		negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, BestValueTmp, BestMoveTmp)
	).
