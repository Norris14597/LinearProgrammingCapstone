/* This program will use linear programming to solve a cost flow problem given in Section 2.2.1 of
'Creating a More Efficient Course Schedule at WPI Using Linear Optimization' by
Ryan Wormald and Curtis Guimond
*/
public class MinimumCostFlowProblem {
    public static void main(String[] args) {
	/* Minimize z = 2A + 4B + 9C + 3D + E + 3F + 2G
	Subject to:
		  A  B  C  D  E  F  G
	  1)  1  1  1  0  0  0  0  =  50
	  2) -1  0  0  1  0  0  0  =  40
	  3)  0 -1  0 -1  1  0  0  =  0
	  4)  0  0 -1  0  0  1 -1  = -30
	  5)  0  0  0  0 -1 -1  1  = -60

	CONSTRAINTS:
	A,B,C,D,E,F,G >= 0
	GIVEN: A <= 10
	 2+3 : B = -40 + E - A  ( 0 <= B <= 40)- \
	 4+5 : C = 90 - E 		(10 <= C <= 90)   }- (40 <= E <= 80)
	  2  : D = 40 + A       (40 <= D <= 50)- /
	GIVEN: E <= 80
	  5  : F = G + C - 30
	  4  : G = F + E - 60

	 F-G : F-G = C - 30     (-20 <= F-G <= 60) (0 <= F <= 60) (0 <= G <= 20)
	 G-F : G-F = E - 60		(-20 <= G-F <= 20) (0 <= F <= 20) (0 <= G <= 20) discard -> (0 <= F <= 60)

	 Loops for A && E because they are given. Loops for F && G because they are dependent on each other.
	*/

        //given variables
        int a = 0;
        int e = 0;

        //independent variables (independent of a or e)
        int f = 0;
        int g = 0;

        //dependent variables dependent on a or e)
        int B = 0;
        int C = 0;
        int D = 0;

        int sum = 0;
        int min = 999999;   //arbitrarily large value

        for (int A = 0; A <= 10; A++) {
            for (int E = 40; E <= 80; E++) {
                //assign values to the dependent variables given A and E
                B = -40 + E - A;
                C = 90 - E;
                D = 40 + A;

                //added an if statement for given constraints that weren't directly used
                if (A + B + C == 50 && E - B - D == 0) { //not necessary for this problem but probably necessary for more complex problems
                    for (int F = 0; F <= 20; F++) {
                        for (int G = 0; G <= 20; G++) {

                            //this if statemet for the depedent variable constraints is necessary for a correct solution
                            if (G - E - F == -60 && F - G - C == -30) {
                                //solve for z given the current values of A,B,C,D,E,F,G
                                sum = 2*A + 4*B + 9*C + 3*D + E + 3*F + 2*G;
                                if (sum < min) {
                                    min = sum;
                                    a = A;
                                    e = E;
                                    f = F;
                                    g = G;
                                    System.out.println("The current value for z is: " + min);

                                }
                                //true when min is updated
                                if (sum == min) {
                                    System.out.println("A = " + a + ", B = " + B + ", C = " + C + ", D = " + D + ", E = " + e + ", F = " + f + ", G = " + g);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}