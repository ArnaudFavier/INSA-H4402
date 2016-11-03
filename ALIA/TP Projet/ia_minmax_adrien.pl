%%%% AI MinMax Adrien %%%%

:- [tools_ai].

playIA(Board, BestMove, Player):- negaMax(3, Board, Player, Player, _, BestMove).

negaMax(0, Board, _, PlayerMax, Value, _):- value(Board, PlayerMax, Value).
negaMax(_, Board, _, PlayerMax, Value, _):- winner(Board, 1), value(Board, PlayerMax, Value).
negaMax(_, Board, _, PlayerMax, Value, _):- winner(Board, 2), value(Board, PlayerMax, Value).
negaMax(Depth, Board, Player, PlayerMax, BestValue, BestMove):-
	setof(PossibleMove, columnAvailable(Board, PossibleMove), AllPossibleMoves),
	Depth1 is Depth - 1,
	negaMax(AllPossibleMoves, Board, Depth1, Player, PlayerMax, BestValue, BestMove, -10000, nil).

negaMax([], _, _, _, _, BestValue, BestMove, BestValue, BestMove).
negaMax([HMoves|TMoves], Board, Depth, Player, PlayerMax, BestValue, BestMove, BestValueTmp, BestMoveTmp):-
	playMoveIA(Board, HMoves, NewBoard, Player),
	changePlayer(Player, NextPlayer),
	negaMax(Depth, NewBoard, NextPlayer, PlayerMax, NextPlayerBestValue, _),
	Value is -NextPlayerBestValue,
	(	Value > BestValueTmp ->
		negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, Value, HMoves)
	;	(	Value = BestValueTmp ->
			(	R is random(2), R is 1 ->
				negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, Value, HMoves)
				;
				negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, BestValueTmp, BestMoveTmp)
			)
		;
			negaMax(TMoves, Board, Depth, Player, PlayerMax, BestValue, BestMove, BestValueTmp, BestMoveTmp)
		)
	).
	
% Heuristique
value(Board, MaxPlayer, Value):- value4(Board, MaxPlayer, Value4), value3(Board, MaxPlayer, Value3), Value is Value4+Value3.


value4(Board, MaxPlayer, -1000):- changePlayer(MaxPlayer, NextPlayer), winner(Board, NextPlayer).
value4(Board, MaxPlayer, 1000):- winner(Board, MaxPlayer).
value4(_, _, 0).

value3(Board, MaxPlayer, -100):- changePlayer(MaxPlayer, NextPlayer), winner3(Board, NextPlayer).
value3(Board, MaxPlayer, 100):- winner3(Board, MaxPlayer).
value3(_, _, 0).
		

% Utility
playMoveIA(Board, Column, NewBoard, Player):-	nth1(Column,Board, E),
												playColumn(E, L, Player),
												replaceAtIndex(Board, NewBoard, Column, L).
		
%%%% Test if a Board is a winning configuration for the player Player
winner3(Board, Player):- winnerCol3(Board, Player).
winner3(Board, Player):- winnerHor3(Board, Player).
winner3(Board, Player):- winnerDiag3(Board, Player).

%%% Check victory condition: align 3 tokens in column:
winnerCol3(Board, Player):- nth1(_, Board, Val), match(Val, [Player, Player, Player]).

%%% Check victory condition: align 3 tokens in lign:
winnerHor3(N, Board, Player):- maplist(nthElem(N), Board, L), 
					 		   match(L, [Player, Player, Player]), !.

winnerHor3(N, Board, Player):- N > 0,
					 N1 is N-1,
					 winnerHor(N1, Board, Player).

winnerHor3(Board, Player):- winnerHor3(7, Board, Player).	

%%% Check victory condition: align 3 tokens in diagonal:
checkEndDiag3(_, D, Player, 0):- match(D, [Player, Player, Player]).
checkEndDiag3(Board, D, Player, N):- N > 0,
					  maplist(nthElem(N), Board, L),
					  nthElem(N, L, E),
					  N1 is N-1,
					  checkEndDiag3(Board, [E|D], Player, N1).

checkEndDiag3(Board, Player):- checkEndDiag3(Board, [], Player, 6).

checkAnotherDiag3(_, D, Player, 0):- match(D, [Player, Player, Player]).
checkAnotherDiag3(Board, D, Player, N):- N > 0,
					    maplist(nthElem(N), Board, L),
						N2 is 7-N,
						nthElem(N2, L, E),
					    N1 is N-1,
					    checkAnotherDiag3(Board, [E|D], Player, N1).

checkAnotherDiag3(Board, Player):- checkAnotherDiag3(Board, [], Player, 6).

winnerDiag3(_, _, X, Player):- checkEndDiag3(X, Player), !.
winnerDiag3(_, _, X, Player):- checkAnotherDiag3(X, Player), !.
winnerDiag3(Board, N, X, Player):- N < 7,
					  maplist(nthElem(N), Board, L),
					  N1 is N+1,
					  winnerDiag3(Board, N1, [L|X], Player).

winnerDiag3(Board, Player):- winnerDiag3(Board, 1, [], Player).
