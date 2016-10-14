% --- Prolog Project ---
%  Puissance 4 - H4402
% ----------------------

:- dynamic board/1.

%switch between players
changePlayer(1,2).
changePlayer(2,1).

%test if the player Player has won
win(Board,Player).

play(Player):-  write('New turn for:'), writeln(Player),
    		board(Board), % instanciate the board from the knowledge base 
			win(Board, Player). % test if the player Player has won

%start the game! 
init :- length(Board,42), assert(board(Board)), play(1).