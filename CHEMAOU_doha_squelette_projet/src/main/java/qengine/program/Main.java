

package qengine.program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;


import dictionnaire.Dictionnaire;
import indexation.*;
import queries_results.*;

/**
 * Programme simple lisant un fichier de requête et un fichier de données.
 * 
 * <p>
 * Les entrées sont données ici de manière statique, à vous de programmer les
 * entrées par passage d'arguments en ligne de commande comme demandé dans
 * l'énoncé.
 * </p>
 * 
 * <p>
 * Le présent programme se contente de vous montrer la voie pour lire les
 * triples et requêtes depuis les fichiers ; ce sera à vous d'adapter/réécrire
 * le code pour finalement utiliser les requêtes et interroger les données. On
 * ne s'attend pas forcémment à ce que vous gardiez la même structure de code,
 * vous pouvez tout réécrire.
 * </p>
 * 
 * @author Olivier Rodriguez <olivier.rodriguez1@umontpellier.fr>
 * @author CHEMAOU Doha
 */

final class Main {
	static final String baseURI = null;

	
	// Votre répertoire de travail où vont se trouver les fichiers à lire
	
	static final String workingDir = "data/";

	
	 // Fichier contenant les requêtes sparql
	 
	// static final String queryFile = workingDir + "sample_query.queryset";

	static String queryFile = workingDir + "";

	// Fichier contenant des données rdf
	 
	// static final String dataFile = workingDir + "100K.nt";

	static String dataFile = workingDir + "";

	static String outputFile = workingDir + "";

	int cpt = 0;

	public static String getDataFile(){ return dataFile;}

	// ========================================================================

	
	 // Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet obtenu.
	 
	public static void processAQuery(ParsedQuery query,String query_) {
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

		for (int i = 0 ;i < patterns.size();i++){
			// System.out.println("pattern "+i+": " + patterns.get(i))
			String predicate = patterns.get(i).getPredicateVar().getValue().toString();
			String object = patterns.get(i).getObjectVar().getValue().toString();
			// Value subject = patterns.get(i).getSubjectVar().getValue();
			// System.out.println(subject);

			// System.out.println("object of the " +i+ " pattern :"+ predicate);
			// System.out.println("object of the " +i+ " pattern :"+ object);

			QueriesResults.branches.add(predicate);
			QueriesResults.branches.add(object);
		}

		// QueriesResults.results();
		QueriesResults.all(query_);
		QueriesResults.branches = new ArrayList<>();
		// System.out.println("--------------------------------");
		
		// System.out.println("first pattern : " + patterns.get(0));

		// System.out.println("object of the first pattern : " + patterns.get(0).getObjectVar().getValue());

		// System.out.println("variables to project : ");

		// Utilisation d'une classe anonyme
		/*query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {

			public void meet(Projection projection) {
				System.out.println("herer "+projection.getProjectionElemList().getElements());
			}
		});*/
	}

	
	// Entrée du programme
	 
	public static void main(String[] args) throws Exception {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		boolean helpMode = line.hasOption("help"); // args.length == 0
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("RDFEngine", options, true);
			System.exit(0);
		}

		dataFile = line.getOptionValue("data", workingDir + "100K.nt");
		outputFile = line.getOptionValue("output", workingDir + "output.txt");
		queryFile = line.getOptionValue("queries", workingDir + "STAR_ALL_workload.queryset");//"");   sample_query.queryset STAR_ALL_workload
		
		parseData();

		Boolean b = dataFile.toString().equals(workingDir+"sample_data.nt");
		new SPO().spo(b);
		new SOP().sop(b);
		new OPS().ops(b);
		new OSP().osp(b);
		new PSO().pso(b);
		new POS().pos(b);

		System.out.println("-------------------------");

		parseQueries();
		
		// System.out.println(QueriesResults.branches);
		// new QueriesResults().results();
		// System.out.println("subjects "+QueriesResults.subjects);

	}

	// ========================================================================

	
	 // Traite chaque requête lue dans {@link #queryFile} avec {@link #processAQuery(ParsedQuery)}.
	 
	private static void parseQueries() throws FileNotFoundException, IOException {
		
		 //Try-with-resources
		 
		// @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
		 
		
		 // On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
		 //entièrement dans une collection.
		System.out.println("Génération du fichier des résultats en cours ...\n");
		try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();

			StringBuilder queryString = new StringBuilder();

			while (lineIterator.hasNext())
			// On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
			// On considère alors que c'est la fin d'une requête
			 
			{
				String line = lineIterator.next();
				// System.out.println("**************** "+line);
				// System.out.println("****************trim "+line);

				// List l = new ArrayList<>();
				// l.add(line_); 
				// QueriesResults.branches.add(l);

				// System.out.println("*********************linequerystring "+line);
				// QueriesResults.branches.add(line);

				queryString.append(line+"\n");//.trim();
				// System.out.println(".....................querystring "+queryString);
				if (line.trim().endsWith("}")) {
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
					// System.out.println(",,,,,,,,,,,,,,,,,,,query "+query);

					processAQuery(query,queryString.toString()); // Traitement de la requête, à adapter/réécrire pour votre programme
					// System.out.println("after,,,,,,,,,,,,,,,,,,,query "+query);
					// System.out.println("hehe "+query.toString());
					// System.out.println("yogiiiiiiiiiiiii "+queryString);

					queryString.setLength(0); // Reset le buffer de la requête en chaine vide
					// System.out.println("querystring at the end of loop "+queryString);

				}
			}
		}
		System.out.println("---> fichier 'results.md' généré ayant comme contenu les requetes et leur résultat.\n");

	}

	
	//  Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
	 
	private static void parseData() throws FileNotFoundException, IOException {

		try (Reader dataReader = new FileReader(dataFile)) {
			// On va parser des données au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

			// On utilise notre implémentation de handler
			rdfParser.setRDFHandler(new MainRDFHandler());

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);

			Dictionnaire.displaying_dictionary(dataFile);

		}
	}

	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("affiche le menu d'aide").build();

		final Option queriesOption = Option.builder("queries").longOpt("queries").hasArg(true)
				.argName("/chemin/vers/dossier/requetes").desc("Le chemin vers les queries").required(false).build();

		final Option dataOption = Option.builder("data").longOpt("data").hasArg(true)
				.argName("/chemin/vers/fichier/donnees").desc("Le chemin vers les donnees").required(false).build();

		final Option outputOption = Option.builder("output").longOpt("output").hasArg(true)
				.argName("/chemin/vers/dossier/sortie").desc("Le chemin vers le dossier de sortie").required(false)
				.build();

		// crée une liste d'options
		final Options options = new Options();
		options.addOption(queriesOption);
		options.addOption(dataOption);
		options.addOption(outputOption);
		options.addOption(helpFileOption);

		return options;
	}
}
