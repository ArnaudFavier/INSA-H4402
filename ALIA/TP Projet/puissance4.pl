/* ------------------------------ */
/* ---------- TP Prolog --------- */
/* ---------- Puissance4 -------- */
/* ----------   H4402   --------- */
/* ------------------------------ */

:- dynamic board/1. 

%% Reverse elements of a List
inv([], []).
inv([A|B], R):- inv(B, X), append(X, [A], R).

%% Return the size of a list
% Parameters: L list, N lenght of the list
size([], 0).
size([_|L], N):- size(L, N1), N is N1+1.

%% Return the element of a list at the N index 
% Parameters: N index of the element we want to get back, L list, X return element
nthElem(N, L, []):- size(L, N1), N1 < N.
nthElem(N, L, X):- nth1(N, L, X).

%%%% Test is the game is finished
gameover(1):- board(Board), winner(Board, 1), !.  % There exists a winning configuration: We cut
gameover(2):- board(Board), winner(Board, 2), !.  % There exists a winning configuration: We cut
gameover('Draw'):- board(Board), not(isBoardNotFull(Board)). % the Board is fully instanciated (no free variable): Draw

%%%% Test if a pattern P is inside a list L
match(_, [], _, _).
match([H|TL], [H|TP], L, P):- match(TL, TP, L, P).
match([HL|_], [HP|_], [_|T], P):- HL\= HP, match(T, P, T, P).
match(L, P):- match(L, P, L, P), !.

%%%% Test if a Board is a winning configuration for the player Player
winner(Board, Player):- winnerCol(Board, Player).
winner(Board, Player):- winnerHor(Board, Player).
winner(Board, Player):- winnerDiag(Board, Player).

%%% Check victory condition: align 4 tokens in column:
winnerCol(Board, Player):- nth1(_, Board, Val), match(Val, [Player, Player, Player, Player]).

%%% Check victory condition: align 4 tokens in lign:
winnerHor(N, Board, Player):- maplist(nthElem(N), Board, L), 
					 		  match(L, [Player, Player, Player, Player]), !.

winnerHor(N, Board, Player):- N > 0,
					 N1 is N-1,
					 winnerHor(N1, Board, Player).

winnerHor(Board, Player):- winnerHor(7, Board, Player).	

%%% Check victory condition: align 4 tokens in diagonal:
checkEndDiag(_, D, Player, 0):- match(D, [Player, Player, Player, Player]).
checkEndDiag(Board, D, Player, N):- N > 0,
					  maplist(nthElem(N), Board, L),
					  nthElem(N, L, E),
					  N1 is N-1,
					  checkEndDiag(Board, [E|D], Player, N1).

checkEndDiag(Board, Player):- checkEndDiag(Board, [], Player, 6).

checkAnotherDiag(_, D, Player, 0):- match(D, [Player, Player, Player, Player]).
checkAnotherDiag(Board, D, Player, N):- N > 0,
					    maplist(nthElem(N), Board, L),
						N2 is 7-N,
						nthElem(N2, L, E),
					    N1 is N-1,
					    checkAnotherDiag(Board, [E|D], Player, N1).

checkAnotherDiag(Board, Player):- checkAnotherDiag(Board, [], Player, 6).

winnerDiag(_, _, X, Player):- checkEndDiag(X, Player), !.
winnerDiag(_, _, X, Player):- checkAnotherDiag(X, Player), !.
winnerDiag(Board, N, X, Player):- N < 7,
					  maplist(nthElem(N), Board, L),
					  N1 is N+1,
					  winnerDiag(Board, N1, [L|X], Player).

winnerDiag(Board, Player):- winnerDiag(Board, 1, [], Player).


%%%% Recursive predicate that checks if all the elements of the List (a board)
%%%% are instanciated: true e.g. for [x,x,o,o,x,o,x,x,o] false for [x,x,o,o,_G125,o,x,x,o]
% Adapt it
isBoardNotFull(Board):- nth1(_, Board, Val), nth1(_, Val, Elem), Elem = 0, !.

%%%% Artificial intelligence: choose in a Board the index to play for Player (_)
%%%% This AI plays randomly and does not care who is playing: it chooses a free position
%%%% in the Board (an element which is an free variable)
% Random 7 because there are 7 columns
% Adapt it
ia(Board, Index, _):- repeat, Index is random(7), nth0(Index, Board, Elem), var(Elem), !.

%%%% Recursive predicate for playing the game.
% The game is over, we use a cut to stop the proof search, and display the winner/board
play(_):- gameover(Winner), !, write('Game Over. Le gagnant est Joueur '), writeln(Winner), write('\n'), board(Board), displayBoard(Board), write('\n'), read(_), halt(0).

% The game is not over, we play the next turn
play(Player):-  write('Nouveau tour de Joueur : '),
				writeln(Player),
				write('\n'),
				board(Board), % instanciate the board from the knowledge base 
				displayBoard(Board), % print it
				(
					(
					Player is 1, % if it is AI turn
					playIA(Board, Move, Player) % ask the AI for a move, that is, an index for the Player
					) 
				;
					(
					write('\nJoueur '),
					write(Player), 
					write(' entrez un numero de colonne : '),
					read(Move) % player turn
					)
				),
				write('\n'),
				playMove(Board, Move, NewBoard, Player), % Play the move and get the result in a new Board
				applyIt(Board, NewBoard), % Remove the old board from the KB and store the new one
				write(''),
				changePlayer(Player, NextPlayer), % Change the player before next turn
				play(NextPlayer). % next turn

% Test if the column is not complete
rowPossible(_, _, Line, Player):- Line =:= 8 , write('Colonne pleine. Merci de jouer une autre colone.\n'), play(Player).
rowPossible(Board, Column, Line, Player) :- nth1(Column, Board, X), nth1(Line, X, 0) ; Line1 is Line+1, rowPossible(Board, Column, Line1, Player).

% Test if a move is valid or not
moveIsValid(Board, Column, Player):- integer(Column), Column > 0, Column < 8, rowPossible(Board, Column, 0, Player) ; Column == end_of_file , halt(0) ; write('Mouvement invalide.\n'), play(Player).

% L1 = first list, L2 = new list, I = index, E = element
replaceAtIndex([], [], 0, _).
replaceAtIndex([H|T1], [H|T2], 0, _):- replaceAtIndex(T1, T2, 0, _).
replaceAtIndex([_|T1], [E|T2], 1, E):- replaceAtIndex(T1, T2, 0, E).
replaceAtIndex([H|T1], [H|T2], I, E):- I=\=0, I=\=1, I2 is I-1, replaceAtIndex(T1, T2, I2, E).

firstOccurence([], -1, _).
firstOccurence([E|_], 1, E).
firstOccurence([H|T], I, E):- H\=E, firstOccurence(T, I2, E), I is I2+1.

% C1 = The column we want to modify,
% C2 = The new column after play
% P = Player (1 or 2)
playColumn(C1, C2, P):- firstOccurence(C1, I, 0), replaceAtIndex(C1, C2, I, P).

%%%% Play a Move, the new Board will be the same, but one value will be instanciated with the Move
playMove(Board, Column, NewBoard, Player):- 	moveIsValid(Board, Column, Player),
												nth1(Column,Board, E),
												playColumn(E, L, Player),
												replaceAtIndex(Board, NewBoard, Column, L).

%%%% Remove old board/save new one in the knowledge base
% Very important
applyIt(Board, NewBoard):- retract(board(Board)), assert(board(NewBoard)).

%%%% Predicate to get the next player
changePlayer(1, 2).
changePlayer(2, 1).

%%%% Print a line of the board at index Line:
printLine(N, Board):- nth1(X, Board, Val), inv(Val, ValInv), nth1(N, ValInv, V), write(V), write('|'), X > 6, writeln('').

%%%% Display the board
displayBoard(Board):-
	write('|'), printLine(1, Board),
	write('|'), printLine(2, Board),
	write('|'), printLine(3, Board),
	write('|'), printLine(4, Board),
	write('|'), printLine(5, Board),
	write('|'), printLine(6, Board).

displayWelcomeMessage:-	write('|--------------------|\n'),
						write('|----- TP Prolog ----|\n'),
						write('|---- Puissance 4 ---|\n'),
						write('|------- H4402 ------|\n'),
						write('|--------------------|\n\n').

%%%% AI
% Uncomment to choose

%% Tools for IA 
% Check if an element belong to a list
member(X, [X|_]).
member(X, [_|Z]):- member(X, Z).

% Convert a list in set
list2ens([], []).
list2ens([X|Y], A):- member(X, Y), list2ens(Y, A), !.
list2ens([X|Y], [X|A]):- list2ens(Y, A).

% give the list of columns which are not filled
nonFilledColumnIds(Board, Res):- findall(I, (length(Board, Long), between(1, Long, I), nth1(I, Board, List), member(0, List)), R), list2ens(R, Res).

% True if the column is not full, false if the column is full
columnAvailable(Board, Column):- nth1(Column, Board, List), member(0, List).

%% Completely Random 
playIA(Board, Move, _):- repeat, M is random(7), Move is M + 1, columnAvailable(Board, Move), !.

%% Winning move
% AI that identifies a winning move for the player (1 or 2)
% winning move : the players wins when he plays this move
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 1), playMove(Board, 1, _, Player), winner(Board, Player), Column=1.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 2), playMove(Board, 2, _, Player), winner(Board, Player), Column=2.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 3), playMove(Board, 3, _, Player), winner(Board, Player), Column=3.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 4), playMove(Board, 4, _, Player), winner(Board, Player), Column=4.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 5), playMove(Board, 5, _, Player), winner(Board, Player), Column=5.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 6), playMove(Board, 6, _, Player), winner(Board, Player), Column=6.
winningMove(ActualBoard, Column, Player):- columnAvailable(ActualBoard, 7), playMove(Board, 7, _, Player), winner(Board, Player), Column=7.

% AI identifies a winning move and plays it 
% if there is a winning move she plays it, if there is not then she looks for a move that could make the oponent win and plays it
% if there is nothing then she plays a random move
%playIA(Board, Move, _):- winningMove(Board, Move, 1).
%playIA(Board, Move, _):- winningMove(Board, Move, 2).
%playIA(Board, Move, Player):- ia(Board, Move, Player).

%%% TEST VHUGUET %%%%%%%%%%%%%%%%%%%
%% Agresive style IA
% get list of available winning moves
%availableWinningMoves(Board, Player, List):- findall(I, (length(Board, Long), between(1, Long, I), winningMove(Board, I, Player)), List).
% if there is at least an avaiable winning move we choose one of them
%playIA(Board, Move, Player):- availableWinningMoves(Board, Player, [HList|TList]), not([HList|TList] is []), Move is HList.
% else ia plays randomly
%playIA(Board, Move, Player):- ia(Board, Move, Player).

%% Defensive style IA (---) same as agresive style with a switching of player
%% Defensive style IA (+++)
%% Ultimate style IA (merge of =/= styles)
%%% END TEST VHUGUET %%%%%%%%%%%%%%%


%%% MinMax Adrien %%%%%%%%%%%%%%%%%%%

playIANegaMax(Board, BestMove, Player):- negaMax(2, Board, Player, Player, _, BestMove).

playMoveIA(Board, Column, NewBoard, Player):- 	nth1(Column,Board, E),
						playColumn(E, L, Player),
						replaceAtIndex(Board, NewBoard, Column, L).
%Voir pour changer l'heuristique
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

%%% END MinMax %%%%%%%%%%%%%%%

%%% TEST MINIMAX ADUSSAUGE  %%%

% Id = The id of the max value in listscore
% Value = The max value in listscore
% ListScore = The list of scores
maxidforvalue(Id,Value,ListScore) :- max_list(ListScore, Value), nth1(Id,ListScore,Value).

% Same with min
minidforvalue(Id,Value,ListScore) :- min_list(ListScore, Value), nth1(Id,ListScore,Value).

% Basic predicate : minimax(ConfActuelle, JoueurActuel, MonNum, Score, Level, ColumnToPlay)
% ConfActuelle = La configuration actuelle du board
% JoueurActuel = L'id du joueur devant jouer sur ConfActuelle (switch à chaque niveau de l'arbre)
% MonNum = L'id du joueur ayant lancé le minimax
% Score = Le score retourné par minimax
% Level = La hauteur de l'arbre minimax
% ColumnToPlay = La colonne à jouer, calculée dans le minimax

% Si on arrive au niveau 0, on peut calculer le score après avoir joué sur une pos
%minimax(ConfActuelle, JoueurActuel, MonNum, 0, 0, ColumnToPlay) :- nombreJetonsAlignes(ConfActuelle, MonNum, 1).
%minimax(ConfActuelle, JoueurActuel, MonNum, 300, 0, ColumnToPlay) :- nombreJetonsAlignes(ConfActuelle, MonNum, 2).
%minimax(ConfActuelle, JoueurActuel, MonNum, 500, 0, ColumnToPlay) :- nombreJetonsAlignes(ConfActuelle, MonNum, 3).
%minimax(ConfActuelle, JoueurActuel, MonNum, 1000, 0, ColumnToPlay) :- nombreJetonsAlignes(ConfActuelle, MonNum, NbJetons), NbJetons>3.

% Sinon, on peut faire décroitre le level et avancer dans l'arbre
% Si on est le joueur ayant lancé le minimax, on choisi le max parmi les coups sur chaque colonne
%minimax(ConfActuelle, JoueurActuel, MonNum, Score, Level, ColumnToPlay) :- MonNum=:=JoueurActuel, Level > 0, maxidforvalue(ColumnToPlay, Score, [S1,S2,S3,S4,S5,S6,S7]), NewPlayer is (JoueurActuel mod 2)+1, InferiorLevel is Level-1
%	playMove(ConfActuelle, 1, NewConf1, JoueurActuel), minimax(NewConf1, NewPlayer, MonNum, S1, InferiorLevel, 1),
%	playMove(ConfActuelle, 2, NewConf2, JoueurActuel), minimax(NewConf2, NewPlayer, MonNum, S2, InferiorLevel, 2),
%	playMove(ConfActuelle, 3, NewConf3, JoueurActuel), minimax(NewConf3, NewPlayer, MonNum, S3, InferiorLevel, 3),
%	playMove(ConfActuelle, 4, NewConf4, JoueurActuel), minimax(NewConf4, NewPlayer, MonNum, S4, InferiorLevel, 4),
%	playMove(ConfActuelle, 5, NewConf5, JoueurActuel), minimax(NewConf5, NewPlayer, MonNum, S5, InferiorLevel, 5),
%	playMove(ConfActuelle, 6, NewConf6, JoueurActuel), minimax(NewConf6, NewPlayer, MonNum, S6, InferiorLevel, 6),
%	playMove(ConfActuelle, 7, NewConf7, JoueurActuel), minimax(NewConf7, NewPlayer, MonNum, S7, InferiorLevel, 7).
	
% Si on n'est pas le joueur ayant lancé le minimax, on choisi le min parmi les coups sur chaque colonne
%minimax(ConfActuelle, JoueurActuel, MonNum, Score, Level, ColumnToPlay) :- MonNum=:=JoueurActuel, Level > 0, minidforvalue(ColumnToPlay, Score, [S1,S2,S3,S4,S5,S6,S7]), NewPlayer is (JoueurActuel mod 2)+1, InferiorLevel is Level-1
%	playMove(ConfActuelle, 1, NewConf1, JoueurActuel), minimax(NewConf1, NewPlayer, MonNum, S1, InferiorLevel, 1),
%	playMove(ConfActuelle, 2, NewConf2, JoueurActuel), minimax(NewConf2, NewPlayer, MonNum, S2, InferiorLevel, 2),
%	playMove(ConfActuelle, 3, NewConf3, JoueurActuel), minimax(NewConf3, NewPlayer, MonNum, S3, InferiorLevel, 3),
%	playMove(ConfActuelle, 4, NewConf4, JoueurActuel), minimax(NewConf4, NewPlayer, MonNum, S4, InferiorLevel, 4),
%	playMove(ConfActuelle, 5, NewConf5, JoueurActuel), minimax(NewConf5, NewPlayer, MonNum, S5, InferiorLevel, 5),
%	playMove(ConfActuelle, 6, NewConf6, JoueurActuel), minimax(NewConf6, NewPlayer, MonNum, S6, InferiorLevel, 6),
%	playMove(ConfActuelle, 7, NewConf7, JoueurActuel), minimax(NewConf7, NewPlayer, MonNum, S7, InferiorLevel, 7).
	
%%%%% Start the game
% The game state will be represented by a list of 7 lists of 6 elements 
% board([[_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_], 
% 		 [_,_,_,_,_,_]
% 		]) 
% at the beginning
init :- Board=[[0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0]
              ],
    	assert(board(Board)), displayWelcomeMessage, play(1).

% Le init a du etre remove pour les tests unitaires
