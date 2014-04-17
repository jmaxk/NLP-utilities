This project contains various utilities I wrote to help me in my NLP research. I wrote these for personal use, so there is no gaurantee that they will work. 

The most useful section is the wrappers section, which has Java interfaces to various NLP tools. You will need to get the data yourself for most of them. Once you have gotten the data, you will need to change the configuration in src/config/WrappingConfig.yaml



Here is a list of the wrappers currently implemented:

=========Working Wrappers=========
-Berkeley Aligner (https://code.google.com/p/berkeleyaligner/). This is actually a Java tool, but it is designed to be run from the command line. Inside there are some tools to run it programatically
-DBPedia spotlight (https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki). These wrappers allow you to send requests to a dbpedia spoltight server. If you're not connecting to their public server, you will need to start your own (https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki/Installation)
-JVerbNet (http://projects.csail.mit.edu/jverbnet/). 
-JavaMultiwordExpression Toolkit (http://aclweb.org/anthology/W/W11/W11-0818.pdf)
-Pialign (http://www.phontron.com/pialign/)
-Senna (http://ml.nec-labs.com/senna/)

-Universal Wordnet (https://www.mpi-inf.mpg.de/yago-naga/uwn/)
-Wordnet 3.0


=========Works in progress=========
-Semlink (https://verbs.colorado.edu/semlink/
-StanfordNLP. These wrappers are a little buggy
