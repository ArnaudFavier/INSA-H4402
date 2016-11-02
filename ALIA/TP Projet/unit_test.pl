%Include our project file
:- [puissance4].

:- begin_tests(project).

% Reverse Empty list give empty list
test(inv1) :- inv([], []).

% Reverse simple int list
test(inv2) :- inv([1,2,3], [3,2,1]).

% Reverse simple list with int and other values
test(inv3) :- inv([1,azerty,3], [3,azerty,1]).

% Test if it returns false with non-correct list
test(inv4) :- not(inv([1,azerty,3], [58])).

:- end_tests(project).

?- run_tests.