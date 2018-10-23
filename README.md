# jena-transaction-exception

### Sample data

The sample dataset contains a named graph `http://www.test.com/numbers` with 1'000'000 triples:
```
<http://www.test.com/1> rdf:type <http://www.test.com/number>
<http://www.test.com/2> rdf:type <http://www.test.com/number>
...
<http://www.test.com/1000000> rdf:type <http://www.test.com/number>
```
### Run

```
mvn clean compile
mvn exec:java -Dexec.mainClass="TransactionException"
```
