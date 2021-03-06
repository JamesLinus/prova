package ws.prova.reference2.builtins;

import java.util.List;

import ws.prova.agent2.ProvaReagent;
import ws.prova.kernel2.ProvaConstant;
import ws.prova.kernel2.ProvaDerivationNode;
import ws.prova.kernel2.ProvaGoal;
import ws.prova.kernel2.ProvaKnowledgeBase;
import ws.prova.kernel2.ProvaList;
import ws.prova.kernel2.ProvaLiteral;
import ws.prova.kernel2.ProvaObject;
import ws.prova.kernel2.ProvaRule;
import ws.prova.kernel2.ProvaVariable;
import ws.prova.kernel2.ProvaVariablePtr;
import ws.prova.reference2.ProvaConstantImpl;

public class ProvaTypeImpl extends ProvaBuiltinImpl {

	public ProvaTypeImpl(ProvaKnowledgeBase kb) {
		super(kb,"type");
	}

	@Override
	public boolean process(ProvaReagent prova, ProvaDerivationNode node,
			ProvaGoal goal, List<ProvaLiteral> newLiterals, ProvaRule query) {
		ProvaLiteral literal = goal.getGoal();
		List<ProvaVariable> variables = query.getVariables();
		ProvaList terms = literal.getTerms();
		ProvaObject[] data = terms.getFixed();
		if( data.length!=2 )
			return false;
		ProvaObject lt = data[0];
		if( lt instanceof ProvaVariablePtr ) {
			ProvaVariablePtr varPtr = (ProvaVariablePtr) lt;
			lt = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
		}
		String className = null;
		if( lt instanceof ProvaConstant )
			className = ((ProvaConstant) lt).getObject().getClass().getName();
		else if( lt instanceof ProvaVariable )
			className = ProvaVariable.class.getName();
		else
			className = ProvaList.class.getName();
		ProvaObject out = data[1];
		if( out instanceof ProvaVariablePtr ) {
			ProvaVariablePtr varPtr = (ProvaVariablePtr) out;
			out = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
		}
		if( out instanceof ProvaConstant ) {
			return ((ProvaConstant) out).getObject().equals(className);
		} else if( out instanceof ProvaVariable ) {
			((ProvaVariable) out).setAssigned(ProvaConstantImpl.create(className));
		}
		return true;
	}

}
