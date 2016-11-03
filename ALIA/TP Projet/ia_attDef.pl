% AI identifies a winning move and plays it 
% if there is a winning move she plays it, if there is not then she looks for a move that could make the oponent win and plays it
% if there is nothing then she plays a random move
playIA(Board, Move, Player):- ((Player is 1, findIndexWinnigMove(Board, Move, Player)); (Player is 2,findIndexWinnigMove(Board, Move, 2))), Move \= []; %try to win with the move
							((Player is 1, findIndexWinnigMove(Board, Move, 2)); (Player is 2,findIndexWinnigMove(Board, Move, Player))), Move \= []; %else try to avoiding an opponent winning move
							iaRandom(Board, Move, Player). % else chose an available random move