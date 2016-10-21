:- dynamic board/1. 

% Reverse elements of a List.
inv([],[]) :- !.
inv([A|B],R) :- inv(B,X),append(X,[A],R).

%%%% Test is the game is finished %%%
gameover(Winner) :- board(Board), winner(Board,Winner), !.  % There exists a winning configuration: We cut!
gameover('Draw') :- board(Board), isBoardFull(Board). % the Board is fully instanciated (no free variable): Draw.

%%%% Test if a Board is a winning configuration for the player P.
% TODO ! 
winner(_,_).

%%%% Recursive predicate that checks if all the elements of the List (a board) 
%%%% are instanciated: true e.g. for [x,x,o,o,x,o,x,x,o] false for [x,x,o,o,_G125,o,x,x,o]
% Adapt it ! 
isBoardFull([]).
isBoardFull([H|T]):- nonvar(H), isBoardFull(T).

%%%% Artificial intelligence: choose in a Board the index to play for Player (_)
%%%% This AI plays randomly and does not care who is playing: it chooses a free position
%%%% in the Board (an element which is an free variable).
% Random 7 because there are 7 columns
% % Adapt it ! 
ia(Board, Index,_) :- repeat, Index is random(7), nth0(Index, Board, Elem), var(Elem), !.

%%%% Recursive predicate for playing the game. 
% The game is over, we use a cut to stop the proof search, and display the winner/board. 
%play(_):- gameover(Winner), !, write('Game is Over. Winner: '), writeln(Winner), displayBoard.

% The game is not over, we play the next turn
play(Player):-  write('New turn for:'), writeln(Player),
				board(Board), % instanciate the board from the knowledge base 
				displayBoard(Board), % print it
				%ia(Board, Move,Player), % ask the AI for a move, that is, an index for the Player
				write('Joueur '), write(Player), 
				write(' entrez un numéro de colonne : '),
				read(Move), 
				playMove(Board,Move,NewBoard,Player), % Play the move and get the result in a new Board
				applyIt(Board, NewBoard), % Remove the old board from the KB and store the new one
				write(''),
				changePlayer(Player,NextPlayer), % Change the player before next turn
				play(NextPlayer). % next turn!

%%%% Play a Move, the new Board will be the same, but one value will be instanciated with the Move
/*playMove(Board,Move,NewBoard,Player) :- Move < 8,
    									Board=NewBoard, 
    									nth1(Move, Board, Col),
    									inv(Col, ColInv),*/

										
%%%% Remove old board/save new on in the knowledge base
% Very important !
applyIt(Board,NewBoard) :- retract(board(Board)), assert(board(NewBoard)).

%%%% Predicate to get the next player
% OK ! 
changePlayer(1,2).
changePlayer(2,1).

%%%% Print the value of the board at index N:
% OK !
printLine(N,Board):- nth1(X,Board, Val), nth1(N, Val, V), write(V), write('|'), X > 6, writeln('').

%%%% Display the board
% OK !
displayBoard(Board):-
	write('|'), printLine(1,Board),
	write('|'), printLine(2,Board),
	write('|'), printLine(3,Board),
	write('|'), printLine(4,Board),
	write('|'), printLine(5,Board),
	write('|'), printLine(6,Board).

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
% Adapt it !
init :- Board=[[0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0], 
               [0,0,0,0,0,0]
              ],
    	assert(board(Board)), /*displayBoard*/ play(1).


		
		
		
		
		
		
		
		
		

%%%%% From morpion ! (not used)
test :- length(Board,9), assert(board(Board)),  printtree(Board, 1).


possibleMove(Board, I) :- nth0(I, Board, Val), var(Val).
allPossibleMoves(Board, L) :- findall(I, possibleMove(Board, I), L).   % allPossibleMoves([_,_,_,a,a,_], L).

printtree(Board, _) :- isBoardFull(Board), assert(board(Board)). %, retract(board(_)), assert(board(Board)), displayBoard.

printtree(Board, Player) :- allPossibleMoves(Board, L), member(Move,L), %writeln(Move),
    						NewBoard=Board, nth0(Move,NewBoard,Player),
    						changePlayer(Player, NewPlayer), 
    						%print(NewBoard), 
    						printtree(NewBoard, NewPlayer).