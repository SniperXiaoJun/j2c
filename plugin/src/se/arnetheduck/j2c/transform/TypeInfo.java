package se.arnetheduck.j2c.transform;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/** Contextual info about a type */
public class TypeInfo {
	private final TypeInfo parent;
	private final ITypeBinding type;

	private InitInfo init = new InitInfo();
	private InitInfo clinit = new InitInfo();

	private final Set<IVariableBinding> closures;
	private boolean natives;
	private boolean main;

	public TypeInfo(TypeInfo parent, ITypeBinding type) {
		this.parent = parent;
		this.type = type;

		closures = type.isLocal() ? new LinkedHashSet<IVariableBinding>()
				: null;
	}

	public TypeInfo parent() {
		return parent;
	}

	public ITypeBinding type() {
		return type;
	}

	public boolean hasNatives() {
		return natives;
	}

	public void setHasNatives() {
		natives = true;
	}

	public boolean hasMain() {
		return main;
	}

	public void setHasMain() {
		main = true;
	}

	public void addInit(VariableDeclarationFragment fragment) {
		getInit(TransformUtil.isStatic(fragment)).add(fragment);
	}

	public void addInit(Initializer initializer) {
		getInit(TransformUtil.isStatic(initializer)).add(initializer);
	}

	public InitInfo getInit(boolean static_) {
		return static_ ? clinit : init;
	}

	public boolean hasInit() {
		// strings are not part of init()
		return !init.nodes.isEmpty();
	}

	public boolean hasClinit() {
		return !clinit.nodes.isEmpty() || !clinit.strings.isEmpty();
	}

	public void addClosure(IVariableBinding closure) {
		closures.add(closure);
	}

	public Set<IVariableBinding> closures() {
		return closures;
	}

	public boolean isClosure(IVariableBinding vb) {
		return closures != null && closures.contains(vb);
	}
}
