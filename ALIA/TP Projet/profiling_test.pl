% Include our project file
:- [puissance4].

:- begin_tests(project).

%%%% Time %%%%

% AI Random

test(time_ai_random):-
	write('\n\n--- TEST: Time of AI Random:\n'),
	Board =	[[0,0,0,0,0,0],
			[0,0,0,0,0,0],
			[0,0,0,0,0,0],
			[0,0,0,0,0,0],
			[0,0,0,0,0,0],
			[0,0,0,0,0,0],
			[0,0,0,0,0,0]
	], time(playIA(Board, _, 1)).

:- end_tests(project).

?- run_tests.
?- write('\n\n').