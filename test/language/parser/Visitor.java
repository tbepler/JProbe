
package language.parser;

import language.parser.nodes.Add;
import language.parser.nodes.Divide;
import language.parser.nodes.FApply;
import language.parser.nodes.IdListAppend;
import language.parser.nodes.IdListHead;
import language.parser.nodes.Multiply;
import language.parser.nodes.Negate;
import language.parser.nodes.Not;
import language.parser.nodes.Power;
import language.parser.nodes.Subtract;
import language.parser.nodes._Punctuation6exp_Punctuation7exp;
import language.parser.nodes.binopexp;
import language.parser.nodes.errorToken;
import language.parser.nodes.floatToken;
import language.parser.nodes.floatexp;
import language.parser.nodes.idToken;
import language.parser.nodes.idexp;
import language.parser.nodes.idlist_Punctuation5expassign;
import language.parser.nodes.intToken;
import language.parser.nodes.intexp;
import language.parser.nodes.uopexp;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public interface Visitor {


    public void visit(idToken node);

    public void visit(intToken node);

    public void visit(floatToken node);

    public void visit(errorToken node);

    public void visit(idlist_Punctuation5expassign node);

    public void visit(idexp node);

    public void visit(intexp node);

    public void visit(floatexp node);

    public void visit(binopexp node);

    public void visit(uopexp node);

    public void visit(FApply node);

    public void visit(_Punctuation6exp_Punctuation7exp node);

    public void visit(Add node);

    public void visit(Subtract node);

    public void visit(Multiply node);

    public void visit(Divide node);

    public void visit(Power node);

    public void visit(Negate node);

    public void visit(Not node);

    public void visit(IdListHead node);

    public void visit(IdListAppend node);

}
