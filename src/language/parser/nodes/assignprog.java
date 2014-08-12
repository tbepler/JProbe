
package language.parser.nodes;

import language.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class assignprog
    extends progAbstractNode
{

    public final assignAbstractNode assign0;

    public assignprog(assignAbstractNode assign0) {
        this.assign0 = assign0;
    }

    @Override
    public int getLine() {
        return assign0 .getLine();
    }

    @Override
    public int getPos() {
        return assign0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ assign0 .hashCode());
        hash = ((hash* 13)+ getClass().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (!(o instanceof assignprog)) {
            return false;
        }
        assignprog castResult = ((assignprog) o);
        if (!this.assign0 .equals(castResult.assign0)) {
            return false;
        }
        return true;
    }

}
