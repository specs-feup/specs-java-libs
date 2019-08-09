/*
 * Copyright 2011 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import java.util.Collection;
import java.util.List;

/**
 * Utility methods with common mathematical operations.
 *
 * @author Joao Bispo
 */
public class SpecsMath {

    // public static double zeroRatio(List<Number> values) {
    public static double zeroRatio(Collection<Number> values) {
        return zeroRatio(values, 0.0);
    }

    // public static double zeroRatio(List<Number> values, double threshold) {
    public static double zeroRatio(Collection<Number> values, double threshold) {
        double numZeros = 0;

        for (Number value : values) {
            if (!(value.doubleValue() > threshold)) {
                numZeros++;
            }
        }

        return numZeros / values.size();
    }

    // public static double arithmeticMean(List<Number> values) {
    public static double arithmeticMean(Collection<? extends Number> values) {
        if (values.isEmpty()) {
            return 0;
        }

        double result = 0;

        for (Number value : values) {
            result += value.doubleValue();
        }

        result /= values.size();

        return result;
    }

    // public static double arithmeticMeanWithoutZeros(List<Number> values) {
    public static Double arithmeticMeanWithoutZeros(Collection<Number> values) {
        if (values.isEmpty()) {
            return null;
        }

        double result = 0;

        int numZeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                numZeros++;
                continue;
            }

            result += value.doubleValue();
        }

        int numElements = values.size() - numZeros;
        if (numElements == 0) {
            return 0d;
        }

        // result /= values.size();
        result /= numElements;

        return result;
    }

    // public static double geometricMean(List<Number> values) {
    /*
    public static double geometricMean(Collection<Number> values) {
       double result = 1;
    
       //int zeros = 0;
       for(Number value : values) {
          
          if (!(value.doubleValue() > 0.0)) {
             //zeros++;
             //continue;
             //value = 0.000000001;
             value = Double.MIN_VALUE;
          }
          //System.out.println("Value:"+value);
          result *= value.doubleValue();
       }
       //System.out.println("Piatorio:"+result);
    
       int numberOfElements = values.size();
       
       double power = (double)1 / (double)numberOfElements;
       //double power = (double)1 / (double)values.size();
       result = Math.pow(result, power);
       //System.out.println("Final result:"+result);
    
       return result;
    }
    */
    // public static double geometricMeanWithZeroCorrection(List<Number> values) {
    /*
    public static double geometricMeanWithZeroCorrection(Collection<Number> values) {
       double result = 1;
    
       int zeros = 0;
       for(Number value : values) {
          if (!(value.doubleValue() > 0.0)) {
             zeros++;
             continue;
          }
          //System.out.println("Value:"+value);
          result *= value.doubleValue();
       }
       //System.out.println("Piatorio:"+result);
    
       int numberOfElements = values.size() - zeros;
       
       double power = (double)1 / (double)numberOfElements;
       //double power = (double)1 / (double)values.size();
       result = Math.pow(result, power);
       //System.out.println("Final result:"+result);
    
       return result;
    }
    */
    // public static double geometricMeanWithZeroCorrection(List<Number> values) {
    /**
     * 
     * @param values
     * @param withoutZeros
     *            if false, performs geometric mean with zero correction. Otherwise, ignores the zero values.
     * @return
     */
    public static double geometricMean(Collection<Number> values, boolean withoutZeros) {
        double result = 1;

        int zeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                zeros++;
                continue;
            }
            // System.out.println("Value:"+value);
            result *= value.doubleValue();
        }
        // System.out.println("Piatorio:"+result);

        int numberOfElements;
        if (withoutZeros) {
            numberOfElements = values.size() - zeros;
        } else {
            numberOfElements = values.size();
        }
        // int numberOfElements = values.size() - zeros;

        double power = (double) 1 / (double) numberOfElements;
        // double power = (double)1 / (double)values.size();
        result = Math.pow(result, power);
        // System.out.println("Final result:"+result);

        return result;
    }

    // public static double harmonicMean(List<Number> values, boolean useZeroCorrection) {
    public static double harmonicMean(Collection<Number> values, boolean useZeroCorrection) {
        double result = 0;
        int zeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                zeros++;
                continue;
                // value = 0.000000001;
                // value = Double.MIN_VALUE;
            }
            // System.out.println("Value:"+value);
            result += 1 / value.doubleValue();
        }

        int numberOfElements = values.size() - zeros;

        if (numberOfElements == 0) {
            return 0.0;
        }
        // int numberOfElements = values.size();

        // result = (double)values.size() / result;
        result = numberOfElements / result;

        // Zero value correction
        // System.out.println("Number of zeros:"+zeros);
        // System.out.println("BEfore correction:"+result);
        if (useZeroCorrection) {
            result *= (double) numberOfElements / (double) values.size();
        }
        // System.out.println("AFter correction:"+result);
        // result = (double)values.size() / result;
        return result;
    }

    /*
    public static double harmonicMeanWithZeroCorrection(List<Number> values) {
       double result = 0;
       int zeros = 0;
       for(Number value : values) {
          if(!(value.doubleValue() > 0.0)) {
             zeros++;
             continue;
          }
          //System.out.println("Value:"+value);
          result += (double)1 / value.doubleValue();
       }
    
       int numberOfElements = values.size() - zeros;
       //int numberOfElements = values.size();
       
       result = (double)numberOfElements / result;
       //result = (double)values.size() / result;
       return result;
    }
    */

    public static Number max(List<? extends Number> values, boolean ignoreZeros) {
        if (values == null) {
            // return 0;
            return null;
        }

        if (values.isEmpty()) {
            // return 0;
            return null;
        }

        Number max = Double.MAX_VALUE * -1;

        int zeros = 0;
        for (Number number : values) {
            if (!(number.doubleValue() > 0.0) && !(number.doubleValue() < 0.0)) {
                zeros++;
                if (ignoreZeros) {
                    continue;
                }
            }

            max = Math.max(max.doubleValue(), number.doubleValue());
        }

        if (zeros == values.size() && ignoreZeros) {
            return 0;
        }

        return max;
    }

    public static Number min(List<? extends Number> values, boolean ignoreZeros) {
        if (values == null) {
            // return 0;
            return null;
        }

        if (values.isEmpty()) {
            // return 0;
            return null;
        }

        Number min = Double.MAX_VALUE;

        int zeros = 0;
        for (Number number : values) {
            if (!(number.doubleValue() > 0.0) && !(number.doubleValue() < 0.0)) {
                zeros++;
                if (ignoreZeros) {
                    continue;
                }
            }

            min = Math.min(min.doubleValue(), number.doubleValue());
        }

        if (zeros == values.size() && ignoreZeros) {
            return 0;
        }

        return min;
    }

    /*
    public static Number sum(List<Number> graphOperationsPerIt) {
       graphOperationsPerIt.get(0).
    }
     * 
     */

    public static long sum(List<? extends Number> numbers) {
        long acc = 0;
        for (Number number : numbers) {
            acc += number.longValue();
        }

        return acc;
    }

    /**
     * Taken from here: https://stackoverflow.com/a/7879559/1189808
     * 
     * @param number
     * @return
     */
    public static long factorial(int number) {
        long result = 1;

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }
        // System.out.println("RESULT: " + result);
        return result;
    }
}
