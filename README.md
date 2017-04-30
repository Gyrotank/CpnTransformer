# CpnUnitTransformer

CpnUnitTransformer transforms general colored Petri nets into colored Petri nets using only unit tokens,
effectively making them simple Petri nets with non-distinguishable tokens. It allows us to apply invariants method to their analysis, which uses Truncated Set of Solutions finding algorithm for Petri net state equations expressed through systems of linear homogenous Diophantine equations.

In order to run the program, you must provide two files as arguments: the first containing original colored Petri net
in .cpn format used by CPN Tools (http://cpntools.org/), and the second to hold the transformed colored Petri net (it will be created if it doesn't exist):

\> java Main <file1> <file2> 

The program uses modified CPN parser originally written by Oleg Matsuk, which turns XML CPN representation into an object form.