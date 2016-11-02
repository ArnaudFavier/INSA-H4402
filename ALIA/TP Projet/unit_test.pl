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

:- end_tests(project).

?- run_tests.