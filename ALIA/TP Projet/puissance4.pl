/* ------------------------------ */
/* ---------- TP Prolog --------- */
/* ---------- Puissance4 -------- */
/* ----------   H4402   --------- */
/* ------------------------------ */

:- dynamic board/1. 

% Reverse elements of a List.
inv([],[]).
inv([A|B], R) :- inv(B, X),append(X, [A], R).

%%%% Test is the game is finished %%%
gameover(1) :- board(Board), winner(Board, 1), !.  % There exists a winning configuration: We cut!
gameover(2) :- board(Board), winner(Board, 2), !.  % There exists a winning configuration: We cut!
gameover('Draw') :- board(Board), not(isBoardNotFull(Board)). % the Board is fully instanciated (no free variable): Draw.

%%%% Test if a pattern P is inside a list L.
match(_,[],_,_).
match([H|TL],[H|TP],L,P):- match(TL,TP,L,P).
match([HL|_],[HP|_],[_|T],P):- HL\= HP, match(T,P,T,P).
match(L,P):- match(L,P,L,P), !.

%%%% Test if a Board is a winning configuration for the player P.
% TODO !
winner(Board,Player):-	winnerCol(Board, Player).

winnerCol(Board, Player):- nth1(_,Board,Val), match(Val, [Player,Player,Player,Player]).

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

%%%% Remove old board/save new on in the knowledge base
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
init :- Board=[[0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0]
              ],
    	assert(board(Board)), displayWelcomeMessage, play(1).

% Launch the game!
?-init.