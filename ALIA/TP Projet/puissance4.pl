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

% longueur de la liste : 
len([],0).
len([_|L],N):- len(L,N1), N is N1+1.
faitx(x,y,0).
%on commence a 1
%getX : récupérer la ligne encore valable dans la colonne demandé par player
getX(_,7,_).
getX(Board, X, Y) :- faitxy(X,Y) =:= 0 , getX(Board, X+1, Y).
moveIsValid(Board, Y) :- (Y >= 1 , Y <= 7), getX(X,Y), (X\=7), Board(X,Y,0).
playMove(Board, y, NewBoard, Player) :- moveIsValid(Board, y) , updateBoard(Board, x, y, NewBoard, Player).
%updateBoard(Board, x, y, NewBoard, Player)