/**
 *
 */
package paramwrapper;

import fdtmc.FDTMC;

/**
 * Façade to a PARAM executable.
 *
 * @author Thiago
 *
 */
public class ParamWrapper implements ParametricModelChecker {

	private String paramPath;
	private IModelCollector modelCollector;
	private boolean usePrism = false;

    public ParamWrapper(String paramPath) {
        this(paramPath, new NoopModelCollector());
    }

    public ParamWrapper(String paramPath, IModelCollector modelCollector) {
        this.paramPath = paramPath;
        this.usePrism = paramPath.contains("prism");
        this.modelCollector = modelCollector;
    }

	public String fdtmcToParam(FDTMC fdtmc) {
		ParamModel model = new ParamModel(fdtmc);
		modelCollector.collectModel(model.getParametersNumber(), model.getStatesNumber());
		return model.toString();
	}

	@Override
	public String getReliability(FDTMC fdtmc) {
		
	    ParamModel model = new ParamModel(fdtmc);
        modelCollector.collectModel(model.getParametersNumber(), model.getStatesNumber());
		String modelString = model.toString();

		if (usePrism) {
		    modelString = modelString.replace("param", "const");
		}
		String reliabilityProperty = "P=? [ F \"success\" ]";

		FormulaEvaluator formulaEvaluator = new FormulaEvaluator(modelString, reliabilityProperty, model, usePrism, paramPath);
		
		String reliability = formulaEvaluator.evaluate();

		modelCollector.collectModelCheckingTime(formulaEvaluator.getElapsedTime());

		return reliability;
	}

	
}
