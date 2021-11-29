package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import dictionnaire.Dictionnaire;

/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 *  * @author CHEMAOU Doha
 */
public final class MainRDFHandler extends AbstractRDFHandler {

	@Override
	public void handleStatement(Statement st) {
		// récupération des subjects, objects et predicates 
		String subject = st.getSubject().stringValue().trim();
		Dictionnaire.addSubject(subject);

		String object = st.getObject().stringValue().trim();
		Dictionnaire.addObject(object);

		String predicate = st.getPredicate().stringValue().trim();
		Dictionnaire.addPredicate(predicate);

		
	}
}