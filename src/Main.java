import java.util.Scanner;

 class LUPDecomposition {
    public static double[][] L;
    public static double[][] U;
    public static double[][] P;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть розмірність матриці: ");
        int n = scanner.nextInt();

        double[][] A = new double[n][n];
        System.out.println("Введіть матрицю A:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("A[%d][%d] = ", i, j);
                A[i][j] = scanner.nextDouble();
            }
        }

        // Введення вектора b з клавіатури
        double[] b = new double[n];
        System.out.println("Введіть вектор b:");
        for (int i = 0; i < n; i++) {
            System.out.printf("b[%d] = ", i);
            b[i] = scanner.nextDouble();
        }

        scanner.close();

        LUPDecomposition lup = new LUPDecomposition();
        lup.decompose(A);
        double[] x = lup.solve(b);

        printMatrix(L, "L");
        printMatrix(U, "U");
        printMatrix(P, "P");

        System.out.println("Розв'язок x:");
        for (int i = 0; i < n; i++) {
            System.out.printf("x[%d] = %.4f%n", i, x[i]);
        }


    }

    public void decompose(double[][] A) {
        int n = A.length;
        L = new double[n][n];
        U = new double[n][n];
        P = new double[n][n];

        for (int i = 0; i < n; i++) {
            P[i][i] = 1.0;
        }

        double[][] tempA = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, tempA[i], 0, n);
        }

        for (int k = 0; k < n; k++) {
            int pivotRow = findPivotRow(tempA, k);
            swapRows(tempA, k, pivotRow);
            swapRows(P, k, pivotRow);

            L[k][k] = 1.0;
            for (int i = k + 1; i < n; i++) {
                L[i][k] = tempA[i][k] / tempA[k][k];
                for (int j = k; j < n; j++) {
                    tempA[i][j] -= L[i][k] * tempA[k][j];
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                U[i][j] = tempA[i][j];
            }
        }
    }

    public double[] solve(double[] b) {
        int n = b.length;
        double[] x = new double[n];

        double[] y = forwardSubstitution(L, P, b);
        x = backwardSubstitution(U, y);

        return x;
    }

    private double[] forwardSubstitution(double[][] L, double[][] P, double[] b) {
        int n = L.length;
        double[] y = new double[n];

        double[] Pb = matrixVectorMultiply(P, b);

        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = (Pb[i] - sum) / L[i][i];
        }

        return y;
    }

    private double[] backwardSubstitution(double[][] U, double[] y) {
        int n = U.length;
        double[] x = new double[n];

        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (y[i] - sum) / U[i][i];
        }

        return x;
    }

    private int findPivotRow(double[][] A, int col) {
        int n = A.length;
        int pivotRow = col;

        for (int i = col + 1; i < n; i++) {
            if (Math.abs(A[i][col]) > Math.abs(A[pivotRow][col])) {
                pivotRow = i;
            }
        }

        return pivotRow;
    }

    private void swapRows(double[][] matrix, int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        int m = matrix.length;
        int n = vector.length;
        double[] result = new double[m];

        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }

        return result;
    }

     public static void printMatrix(double[][] matrix, String variant) {
         int n = matrix.length;
         System.out.println("матриця " + variant);
         for (int i = 0; i < n; i++) {
             for (int j = 0; j < n; j++) {
                 System.out.printf("%.4f\t", matrix[i][j]);
             }
             System.out.println();
         }
         System.out.println();
     }
}
