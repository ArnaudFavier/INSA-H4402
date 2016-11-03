%%%% AI Random %%%%

playIA(Board, Move, _):- repeat, M is random(7), Move is M + 1, columnAvailable(Board, Move), !.