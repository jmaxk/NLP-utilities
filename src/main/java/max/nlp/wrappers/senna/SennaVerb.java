package max.nlp.wrappers.senna;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import max.nlp.dal.types.statistics.TaggedText;

public class SennaVerb {
        
        public String text;
        public HashMap<String, String> argumentToText = new HashMap<String, String>();
        public HashMap<String, List<String>> argumentToNPs = new HashMap<String, List<String>>();
        public HashMap<String, List<TaggedText>> argumentToTagged = new HashMap<String, List<TaggedText>>();

}