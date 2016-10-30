/* ------------------------------ */
/* ---------- TP Prolog --------- */
/* ---------- Puissance4 -------- */
/* ----------   H4402   --------- */
/* ------------------------------ */

:- dynamic board/1. 

% Reverse elements of a List.
inv([],[]).
inv([A|B], R) :- inv(B, X),append(X, [A], R).

% Return the size of a list
/* Paramètres : L liste, N longueur de la liste */
size([],0).
size([_|L],N):- size(L,N1),
					N is N1+1.

% Return the element of a list at the N index 
/* Paramètres : N index de l'élement qu'on veut récupérer, L liste, X élément retourné */
nthElem(N, L, []):- size(L, N1), N1 < N.
nthElem(N, L, X):- nth1(N, L, X).

%%%% Test is the game is finished %%%
gameover(1) :- board(Board), winner(Board, 1), !.  % There exists a winning configuration: We cut!
gameover(2) :- board(Board), winner(Board, 2), !.  % There exists a winning configuration: We cut!
gameover('Draw') :- board(Board), not(isBoardNotFull(Board)). % the Board is fully instanciated (no free variable): Draw.

%%%% Test if a pattern P is inside a list L.
match(_,[],_,_).
match([H|TL],[H|TP],L,P):- match(TL,TP,L,P).
match([HL|_],[HP|_],[_|T],P):- HL\= HP, match(T,P,T,P).
match(L,P):- match(L,P,L,P), !.

%%%% Test if a Board is a winning configuration for the player Player.
winner(Board,Player):-	winnerCol(Board, Player).
winner(Board,Player):-	winnerHor(Board, Player).
winner(Board, Player):- winnerDiag(Board,Player).

%%% Check victory condition : align 4 tokens in column : 
winnerCol(Board, Player):- nth1(_,Board,Val), match(Val, [Player,Player,Player,Player]).

%%% Check victory condition : align 4 tokens in lign : 
winnerHor(N, Board, Player):- maplist(nthElem(N), Board, L), 
					 		  match(L, [Player,Player,Player,Player]),!.

winnerHor(N, Board, Player):- N > 0,
					 N1 is N-1,
					 winnerHor(N1, Board, Player).

winnerHor(Board,Player):- winnerHor(7, Board, Player).	

%%% Check victory condition : align 4 tokens in diagonal : 
checkEndDiag(_,D,Player,0):- match(D, [Player,Player,Player,Player]).
checkEndDiag(Board,D,Player,N):- N > 0,
					  maplist(nthElem(N), Board, L),
					  nthElem(N,L,E),
					  N1 is N-1,
					  checkEndDiag(Board,[E|D],Player,N1).

checkEndDiag(Board,Player):- checkEndDiag(Board,[],Player,6).

checkAnotherDiag(_,D,Player,0):- match(D, [Player,Player,Player,Player]).
checkAnotherDiag(Board,D,Player,N):- N > 0,
					    maplist(nthElem(N), Board, L),
						N2 is 7-N,
						nthElem(N2,L,E),
					    N1 is N-1,
					    checkAnotherDiag(Board,[E|D],Player,N1).

checkAnotherDiag(Board,Player):- checkAnotherDiag(Board,[],Player,6).

winnerDiag(_,_,X,Player):- checkEndDiag(X,Player),!.
winnerDiag(_,_,X,Player):- checkAnotherDiag(X,Player),!.
winnerDiag(Board,N,X,Player):- N < 7,
					  maplist(nthElem(N), Board, L),
					  N1 is N+1,
					  winnerDiag(Board,N1,[L|X],Player).

winnerDiag(Board,Player):- winnerDiag(Board,1,[],Player).


%%%% Recursive predicate that checks if all the elements of the List (a board) 
%%%% are instanciated: true e.g. for [x,x,o,o,x,o,x,x,o] false for [x,x,o,o,_G125,o,x,x,o]
% Adapt it !
isBoardNotFull(Board):- nth1(_,Board,Val), nth1(_,Val,Elem), Elem = 0, !.

%%%% Artificial intelligence: choose in a Board the index to play for Player (_)
%%%% This AI plays randomly and does not care who is playing: it chooses a free position
%%%% in the Board (an element which is an free variable).
% Random 7 because there are 7 columns
% Adapt it !
ia(Board, Index, _) :- repeat, Index is random(7), nth0(Index, Board, Elem), var(Elem), !.

%%%% Recursive predicate for playing the game. 
% The game is over, we use a cut to stop the proof search, and display the winner/board. 
play(_):- gameover(Winner), !, write('Game Over. Le gagnant est Joueur '), writeln(Winner), write('\n'), board(Board), displayBoard(Board), write('\n'), read(_), halt(0).

% The game is not over, we play the next turn
play(Player):-  write('Nouveau tour de : '),
				writeln(Player),
				write('\n'),
				board(Board), % instanciate the board from the knowledge base 
				displayBoard(Board), % print it
				%ia(Board, Move, Player), % ask the AI for a move, that is, an index for the Player
				write('\nJoueur '), write(Player), 
				write(' entrez un numero de colonne : '),
				read(Move),
				write('\n'),
				playMove(Board, Move, NewBoard, Player), % Play the move and get the result in a new Board
				applyIt(Board, NewBoard), % Remove the old board from the KB and store the new one
				write(''),
				changePlayer(Player, NextPlayer), % Change the player before next turn
				play(NextPlayer). % next turn!

% Test if the column is not complete
rowPossible(_, _, Line, Player) :- Line =:= 8 , write('Colonne pleine. Merci de jouer une autre colone.\n'), play(Player).
rowPossible(Board, Column, Line, Player) :- nth1(Column, Board, X), nth1(Line, X, 0) ; Line1 is Line+1, rowPossible(Board, Column, Line1, Player).

% Test if a move is valid or not
moveIsValid(Board, Column, Player) :- integer(Column), Column > 0, Column < 8, rowPossible(Board, Column, 0, Player) ; Column == end_of_file , halt(0) ; write('Mouvement invalide.\n'), play(Player).

% L1 = first list, L2 = new list, I = index, E = element
replaceAtIndex([],[],0,_).
replaceAtIndex([H|T1],[H|T2],0,_):-replaceAtIndex(T1,T2,0,_).
replaceAtIndex([_|T1],[E|T2],1,E):-replaceAtIndex(T1,T2,0,E).
replaceAtIndex([H|T1],[H|T2],I,E):-I=\=0, I=\=1, I2 is I-1, replaceAtIndex(T1,T2,I2,E).

firstOccurence([],-1,_).
firstOccurence([E|_],1,E).
firstOccurence([H|T],I,E) :- H\=E, firstOccurence(T,I2,E), I is I2+1.

% C1 = The column we want to modify,
% C2 = The new column after play
% P = Player (1 or 2)
playColumn(C1,C2,P) :- firstOccurence(C1,I,0), replaceAtIndex(C1,C2,I,P).

%%%% Play a Move, the new Board will be the same, but one value will be instanciated with the Move
playMove(Board, Column, NewBoard, Player) :- 	moveIsValid(Board, Column, Player),
												nth1(Column,Board, E),
												playColumn(E, L, Player),
												replaceAtIndex(Board, NewBoard, Column, L).

%%%% Remove old board/save new one in the knowledge base
% Very important !
applyIt(Board, NewBoard) :- retract(board(Board)), assert(board(NewBoard)).

%%%% Predicate to get the next player
changePlayer(1, 2).
changePlayer(2, 1).

%%%% Print a line of the board at index Line:
printLine(N,Board):- nth1(X,Board, Val), inv(Val,ValInv), nth1(N, ValInv, V), write(V), write('|'), X > 6, writeln('').

%%%% Display the board
displayBoard(Board):-
	write('|'), printLine(1,Board),
	write('|'), printLine(2,Board),
	write('|'), printLine(3,Board),
	write('|'), printLine(4,Board),
	write('|'), printLine(5,Board),
	write('|'), printLine(6,Board).

displayWelcomeMessage :-	write('|--------------------|\n'),
							write('|----- TP Prolog ----|\n'),
							write('|---- Puissance 4 ---|\n'),
							write('|------- H4402 ------|\n'),
							write('|--------------------|\n\n').



%IA that identifies a winning move for the player (1 or 2)
% winning move : the players wins when he plays this move

winningMove(Column, Player):- playMove(Board,1, NewBoard, Player), winner(Board, Player), Column=1.
winningMove(Column, Player):- playMove(Board,2, NewBoard, Player), winner(Board, Player), Column=2.
winningMove(Column, Player):- playMove(Board,3, NewBoard, Player), winner(Board, Player), Column=3.
winningMove(Column, Player):- playMove(Board,4, NewBoard, Player), winner(Board, Player), Column=4.
winningMove(Column, Player):- playMove(Board,5, NewBoard, Player), winner(Board, Player), Column=5.
winningMove(Column, Player):- playMove(Board,6, NewBoard, Player), winner(Board, Player), Column=6.
winningMove(Column, Player):- playMove(Board,7, NewBoard, Player), winner(Board, Player), Column=7.

%IA identifies a winning move and plays it 
% if there is a winning move she plays it, if there isn't then she looks for a move that could make the oponent win and plays it
%if there is nothing then she plays a random move
playIA(Move) :- winningMove(Move, 1).
playIA(Move) :- winningMove(Move, 2).
playIA(Move) :- ia(Board, Move, Player).


%%%%% Start the game!
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
init :- Board=[[1,0,0,0,0,0], 
               [0,1,0,0,0,0], 
               [0,0,1,0,0,0], 
               [0,0,0,1,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0]
              ],
    	assert(board(Board)), displayWelcomeMessage, play(1).

