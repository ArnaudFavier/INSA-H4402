%%%% AI MinMax Aurélien %%%%

:- [tools_ai].

% Id = The id of the max value in listscore
% Value = The max value in listscore
% ListScore = The list of scores
maxidforvalue(Id, Value, ListScore):- max_list(ListScore, Value), nth1(Id, ListScore, Value).

% Same with min
minidforvalue(Id, Value, ListScore):- min_list(ListScore, Value), nth1(Id, ListScore, Value).

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