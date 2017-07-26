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

package pt.up.fe.specs.util.utilities;

import java.util.Collection;

import pt.up.fe.specs.util.SpecsMath;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Calculates several types of averages.
 *
 * @author Joao Bispo
 */
public enum AverageType {
    ARITHMETIC_MEAN(false),
    ARITHMETIC_MEAN_WITHOUT_ZEROS(true),
    GEOMETRIC_MEAN(false),
    GEOMETRIC_MEAN_WITHOUT_ZEROS(true),
    HARMONIC_MEAN(false);

    private AverageType(boolean ignoresZeros) {
	this.ignoresZeros = ignoresZeros;
    }

    public boolean ignoresZeros() {
	return this.ignoresZeros;
    }

    public double calcAverage(Collection<Number> values) {
	if (values == null) {
	    return 0;
	}

	switch (this) {
	case ARITHMETIC_MEAN:
	    return SpecsMath.arithmeticMean(values);
	case ARITHMETIC_MEAN_WITHOUT_ZEROS:
	    return SpecsMath.arithmeticMeanWithoutZeros(values);
	case GEOMETRIC_MEAN:
	    return SpecsMath.geometricMean(values, false);
	case GEOMETRIC_MEAN_WITHOUT_ZEROS:
	    return SpecsMath.geometricMean(values, true);
	case HARMONIC_MEAN:
	    // return CalcUtils.harmonicMean(values);
	    return SpecsMath.harmonicMean(values, true);
	default:
	    SpecsLogs.getLogger().
		    warning("Case not implemented: '" + this + "'");
	    return 0.0;
	}
    }

    private final boolean ignoresZeros;
}
