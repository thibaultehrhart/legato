package legato.indexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import legato.LEGATO;

public class AllTerms {
    private Map<String,Integer> allTerms;
    Integer totalNoOfDocumentInIndex;
    IndexReader indexReader;
    
    public AllTerms() throws IOException
    {    
        allTerms = new HashMap<>();
        indexReader = IndexOpener.GetIndexReader();
        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex();
    }
        
    public void initAllTerms() throws IOException
    {
        int pos = 0;
        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
            Terms vector = indexReader.getTermVector(docId, LEGATO.getInstance().FIELD_CONTENT);
            //Verification de construction
            try
            {
	            TermsEnum termsEnum = null;
	            termsEnum = vector.iterator(termsEnum);
	            BytesRef text = null;
	            while ((text = termsEnum.next()) != null) {
	                String term = text.utf8ToString();
	                allTerms.put(term, pos++);
	            }
            }
            catch(NullPointerException e)
            {
            	System.err.println("initAllTerms() - docId="+docId+" : vector=null !");
            }
        }       
        
        //Update postition
        pos = 0;
        for(Entry<String,Integer> s : allTerms.entrySet())
        {        
           //System.out.println(s.getKey());
           s.setValue(pos++);
        }
    }
    
    public Map<String, Integer> getAllTerms() {
        return allTerms;
    }
}
