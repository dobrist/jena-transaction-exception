import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.system.Txn;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.tdb2.loader.DataLoader;
import org.apache.jena.tdb2.loader.LoaderFactory;
import org.apache.jena.tdb2.loader.base.LoaderOps;

public class TransactionException {

    public static void main(String[] arguments) {
        // load sample data from backup file into tdb
        Dataset dataset = TDB2Factory.connectDataset("sample-data");
        if (Txn.calculateRead(dataset, dataset::isEmpty)) {
            DataLoader loader = LoaderFactory.createLoader(dataset.asDatasetGraph(), LoaderOps.outputToLog());
            loader.startBulk();
            loader.load("sample-data/sample-data.nq.gz");
            loader.finishBulk();
        }

        // get all triples
        String sparql = "SELECT * WHERE { ?s ?p ?o . }";
        ResultSet results = query(dataset, sparql);

        // print subject of first solution
        RDFNode subject = results.next().get("s");
        System.out.println(subject);
    }

    private static ResultSet query(Dataset dataset, String sparql) {
        Query query = QueryFactory.create(sparql, Syntax.syntaxARQ);
        Model model = dataset.getNamedModel("urn:x-arq:UnionGraph");
        return Txn.calculateRead(dataset, () -> {
            try(QueryExecution queryExecution = QueryExecutionFactory.create(query, model)) {
                return ResultSetFactory.copyResults(queryExecution.execSelect());
            }
        });
    }

}
