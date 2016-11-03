%%%% Tools for AI %%%%
% Some little tools functions for AI

% Check if an element belong to a list
member(X, [X|_]).
member(X, [_|Z]):- member(X, Z).

% Convert a list in set
list2ens([], []).
list2ens([X|Y], A):- member(X, Y), list2ens(Y, A), !.
list2ens([X|Y], [X|A]):- list2ens(Y, A).

% True if the column is not full, false if the column is full
columnAvailable(Board, Column):- nth1(Column, Board, List), member(0, List).

%% Choose an unfilled column and return it in Move
% Random 7 because there are 7 columns
iaRandom(Board, Move, _):- repeat, Move is random(7), nth1(Move, Board, X), nth1(_, X, 0), !.

%% Winning move
% AI that identifies a winning move for the player (1 or 2)
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, Column), playMove(ActualBoard, Column, Board, Player), !, winner(Board, Player).
findIndexWinnigMove(Board, Column, Player):- between(1, 7, Column), winningMove(Board, Column, Player).