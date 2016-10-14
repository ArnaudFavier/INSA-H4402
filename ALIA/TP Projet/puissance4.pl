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


% --- Rim & Arnaud

% Longueur de la liste : 
len([],0).
len([_|L], N):- len(L, N1), N is N1+1.
% On commence à 1 pour le plateau de jeu

% getX : récupérer la ligne encore valable dans la colonne demandée par player
getX(_, 7, _).
getX(Board, X, Y) :- Board(X, Y, V) , V =:= 0 , getX(Board, X+1, Y).

moveIsValid(Board, Y) :- ((Y >= 1 , Y <= 7), getX(Board, X, Y), (X \= 7)) ; write("false").

updateBoard(Board, X, Y, NewBoard, Player) :- NewBoard(X, Y, Player).

% Le joueur effectue un coup
playMove(Board, Y, NewBoard, Player) :- moveIsValid(Board, Y) , updateBoard(Board, Y, NewBoard, Player).