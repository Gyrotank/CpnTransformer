# CpnToUnit

CpnToUnit utility transforms general colored Petri nets into colored Petri nets using only unit tokens,
effectively making them simple Petri nets with non-distinguishable tokens. It allows us to apply invariants method to their analysis, which uses Truncated Set of Solutions finding algorithm for Petri net state equations expressed through systems of linear homogeneous Diophantine equations.

In order to run the program, provide two files as arguments: the first containing original colored Petri net
in .cpn format used by CPN Tools (http://cpntools.org/), and the second to hold the transformed colored Petri net (it will be created if it doesn't exist):

\> java CpnToUnit file1 file2 


If you want to generate full TSS analysis report, run the program with -r flag:

\> java CpnToUnit file1 file2 -r 


You can also run program with a single argument (name of the source file):

\> java CpnToUnit sourceFile.cpn

In this case, the output file will be created named sourceFile_CpnToUnit.cpn.

The program uses modified CPN parser originally written by Oleg Matsuk, which turns XML CPN representation into an object form.

###Example:

Input:

\> java CpnToUnit resources\G1\G1.cpn

Output:

2019-11-13 13:34:53 INFO  CpnToUnit:37 - Input CPN:
 
2019-11-13 13:34:53 INFO  CpnToUnit:167 - -- 2 pages;

2019-11-13 13:34:53 INFO  CpnToUnit:168 - -- 8 places;

2019-11-13 13:34:53 INFO  CpnToUnit:169 - -- 8 transitions;

2019-11-13 13:34:53 INFO  CpnToUnit:170 - -- 34 arcs.


2019-11-13 13:34:54 INFO  CpnToUnit:52 - Output CPN:
 
2019-11-13 13:34:54 INFO  CpnToUnit:167 - -- 2 pages;

2019-11-13 13:34:54 INFO  CpnToUnit:168 - -- 13 places;

2019-11-13 13:34:54 INFO  CpnToUnit:169 - -- 22 transitions;

2019-11-13 13:34:54 INFO  CpnToUnit:170 - -- 146 arcs.


2019-11-13 13:34:54 INFO  CpnToUnit:54 - Output CPN created successfully

===

# CpnToBdd (UNFINISHED)

CpnToBdd utility transforms specifically structured colored Petri nets into a set of BDD scenarios written in Given-When-Then terms.

In order to run the program, provide two files as arguments: the first containing original colored Petri net
in .cpn format used by CPN Tools (http://cpntools.org/), and the second to hold the generated BDD scenarios (it will be created if it doesn't exist):

\> java CpnToBdd file1 file2 


Alternatively, you can provide a single argument (name of the source file):

\> java CpnToBdd sourceFile.cpn

Then the output file will be created named sourceFile_CpnToBdd.story.