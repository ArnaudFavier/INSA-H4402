%Include our project file
:- [puissance4].

:- begin_tests(project).

%%%% INV %%%%

% Reverse Empty list give empty list
test(inv1) :- inv([], []).

% Reverse simple int list
test(inv2) :- inv([1,2,3], [3,2,1]).

% Reverse simple list with int and other values
test(inv3) :- inv([1,azerty,3], [3,azerty,1]).

% Test if it returns false with non-correct list
test(inv4) :- not(inv([1,azerty,3], [58])).

%%%% SIZE %%%%

% Test with empty list
test(size1) :- size([], 0).

% Test if bad size returns false
test(size2) :- not(size([1,2,3], 598)).

% Test if good size returns true
test(size3) :- size([1,2,3], 3).

%%%% NTHELEM %%%%

% Basic test, search value at index
test(nthelem1) :- nthElem(6, [1,2,5,8,9,-8,8], -8).

% Search value and test if bad result returns false
test(nthelem2) :- not(nthElem(6, [1,2,5,8,9,-8,8], 9)).

% Test if a too high index make the predicate false
test(nthelem3) :- not(nthElem(897, [1,2,5,8,9,-8,8], -8)).

%%%% REPLACE AT INDEX %%%%

% Test if replace at 0 in empty list works
test(replace1) :- replaceAtIndex([], [], 0, _).

% Test basic remplace
test(replace2) :- replaceAtIndex([1,2,3,4], [1,2,8,4], 3, 8).

% Test replace at too high index
test(replace3) :- not(replaceAtIndex([1,2,3,4], [1,2,8,4], 85, 8)).

%%%% FIRST OCCURENCE %%%%

% Simple check first occurence test success
test(firstOcc1) :- firstOccurence([4,6,7], 2, 6).

% Simple check first occurence test failure
test(firstOcc2) :- not(firstOccurence([4,6,7], -45, 8)).

% Simple check first occurence test success with multiple same values
test(firstOcc3) :- firstOccurence([4,6,6,6,7,6,7], 2, 6).

%%%% PLAY COLUMN %%%%

% Test play in empty column
test(playColumn1) :- playColumn([0,0,0,0,0],[1,0,0,0,0],1).

% Test play in non-empty column
test(playColumn2) :- playColumn([2,1,2,0,0],[2,1,2,1,0],1).

% Test play in full column, doesnt add anything
test(playColumn3) :- playColumn([2,1,2,1,2],[2,1,2,1,2],1).

% Others players values must work too
test(playColumn4) :- playColumn([1,0,0,0,0],[1,baaba,0,0,0],baaba).

%%%% MOVE IS VALID %%%%

% Test play with non-integer column value must be false
test(moveIsValid1) :- not(moveIsValid([],baaba,1)).

% Test play with zero column value must be false
test(moveIsValid2) :- not(moveIsValid([],0,1)).

% Test play with negative column value must be false
test(moveIsValid3) :- not(moveIsValid([],-2,1)).

% Test play with 8 or greater column value must be false
test(moveIsValid4) :- not(moveIsValid([],8,1)).

% Test play with empty char value must be false
test(moveIsValid5) :- not(moveIsValid([],'',1)).

% Otherwise, must succeed
test(moveIsValid6) :- moveIsValid([[0,0,0]],1,1).

%%%% PLAY MOVE %%%%
% PlayMove uses playColumn and moveIsValid, so we can avoid some tests

% Basic play test in first column
test(playMove1) :- playMove([[0,0,0]], 1, [[1,0,0]], 1).

% Basic play test with multiple columns and different player
test(playMove1) :- playMove([[0,0,0],[1,2,0]], 2, [[0,0,0],[1,2,1]], 1).

:- end_tests(project).

?- run_tests.