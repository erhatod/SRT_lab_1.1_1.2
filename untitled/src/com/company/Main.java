package com.company;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.util.Random;

public class Main {
    static final int N = 256;
    static final int n = 10;
    static final int w = 900;
    static final int deltaW = w/n;
    static final Random random = new Random();

    static long startTime;
    static long addTime;

    static double[][] matrixOfSinusoid = new double[n][N];

    public static void main(String[] args) {
        double[] arrayN = new double[N];
        for (int i = 0; i < N; i++) {
            arrayN[i] = i;
        }

        double[] arrayHalfN = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            arrayHalfN[i] = i;
        }

        double[] signal_x = genRandomSignal();

        startTime = System.nanoTime();
        double Mx = getMx(signal_x);
        System.out.println("Tm = " + (System.nanoTime() - startTime));
        System.out.println("Mx = " + Mx);

        startTime = System.nanoTime();
        double Dx = getDx(signal_x, Mx);
        System.out.println("Td = " + (System.nanoTime() - startTime));
        System.out.println("Dx = " + Dx);

        XYChart chart = QuickChart.getChart("Lab1.1", "N", "x", "x(t)", arrayN, signal_x);
        new SwingWrapper(chart).displayChart();

        //АвтоКореляція лічильник
        startTime = System.nanoTime();
        double[] resultAutoCorrelation = getR(signal_x, Mx);
        long time1 = (System.nanoTime() - startTime);
        System.out.println("Tr = " + time1);

        XYChart chart1 = QuickChart.getChart("Lab1.2", "t", "Rxx", "Rxx(t)", arrayHalfN, resultAutoCorrelation);
        new SwingWrapper(chart1).displayChart();

        double[] signal_x2 = genRandomSignal();
        double Mx2 = getMx(signal_x2);

        //Взаємокореляція лічильник
        startTime = System.nanoTime();
        double[] resultCorrelation = getR(signal_x, Mx, signal_x2, Mx2);
        long time2 = (System.nanoTime() - startTime);

        //Add task out start
        System.out.println("Час автокореляції = " + time1);
        System.out.println("Час взаємокореляції = " + time2);

        if (time1 > time2){
            System.out.println("time1 > time2");
        }else if (time1 == time2) {
            System.out.println("time1 == time2");
        }else {
            System.out.println("time1 < time2");
        //Add task out end
        }


        XYChart chart3 = QuickChart.getChart("Lab1.2", "t", "Rxy", "Rxy(t)", arrayHalfN, resultCorrelation);
        new SwingWrapper(chart3).displayChart();
    }

    static double getMx(double[] arr) {
        double Mx = 0.0;
        for (int i = 0; i < N; i++) {
            Mx += arr[i];
        }
        Mx /= N;
        return Mx;
    }

    static double getDx(double[] arr, double Mx) {
        double Dx = 0.0;
        for (int i = 0; i < N; i++) {
            Dx += Math.pow((arr[i] - Mx), 2.0);
        }
        Dx /= N - 1;
        return Dx;
    }

    static double[] getR(double[] arr, double Mx) {
        return getR(arr, Mx, arr, Mx);
    }

    static double[] getR(double[] arr1, double Mx1, double[] arr2, double Mx2) {
        double[] result = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            double curr = 0;
            for (int j = 0; j < N - i; j++) {
                curr += (arr1[j] - Mx1) * (arr2[i+j] - Mx2);
            }
            curr /= (N - 1.0);
            result[i] = curr;
        }
        return result;
    }

    static double[] genRandomSignal() {
        double[] x = new double[N];
        for (int i = 0; i < n; i++) {
            int A = random.nextInt(100);
            int q = random.nextInt(100);
            for (int j = 0; j < N; j++) {
                final double sinusoid = A * Math.sin(deltaW * (i + 1) * j + q);
                x[j] += sinusoid;
                matrixOfSinusoid[i][j] = sinusoid;
            }
        }
        return x;
    }
}