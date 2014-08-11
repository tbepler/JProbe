
package language.parser.nodes;

import language.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class Not
    extends uopAbstractNode
{

    public final expAbstractNode exp0;

    public Not(_Punctuation8Token _punctuation80, expAbstractNode exp1) {
        this.exp0 = exp1;
    }

    @Override
    public int getLine() {
        return exp0 .getLine();
    }

    @Override
    public int getPos() {
        return exp0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ exp0 .hashCode());
        hash = ((hash* 13)+ getClass().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this.equals(o)) {
            return true;
        }
        if (o.equals(null)) {
            return false;
        }
        if (!(o instanceof Not)) {
            return false;
        }
        Not castResult = ((Not) o);
        if (!this.exp0 .equals(castResult.exp0)) {
            return false;
        }
        return true;
    }

}
