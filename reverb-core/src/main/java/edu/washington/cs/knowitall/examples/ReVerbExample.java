package edu.washington.cs.knowitall.examples;

/* For representing a sentence that is annotated with pos tags and np chunks.*/
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/* String -> ChunkedSentence */
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;

/* The class that is responsible for extraction. */
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;

/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;

/* A class for holding a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.normalization.BinaryExtractionNormalizer;
import edu.washington.cs.knowitall.normalization.NormalizedBinaryExtraction;

public class ReVerbExample {

    public static void main(String[] args) throws Exception {

        String sentStr = "Bananas are an excellent source of potassium.";

        // Looks on the classpath for the default model files.
        OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
        ChunkedSentence sent = chunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        System.out.println(sentStr);
        for (int i = 0; i < sent.getLength(); i++) {
            String token = sent.getToken(i);
            String posTag = sent.getPosTag(i);
            String chunkTag = sent.getChunkTag(i);
            System.out.println(token + " " + posTag + " " + chunkTag);
        }

        // Prints out extractions from the sentence.
        
        
        BinaryExtractionNormalizer norm = new BinaryExtractionNormalizer();
        ReVerbExtractor reverb = new ReVerbExtractor();
        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
            double conf = confFunc.getConf(extr);
            System.out.println("Arg1=" + extr.getArgument1());
            System.out.println("Rel=" + extr.getRelation());
            System.out.println("Arg2=" + extr.getArgument2());
            System.out.println("Conf=" + conf);
            NormalizedBinaryExtraction extrNorm = norm.normalize(extr);
            System.out.println("");
            System.out.println("Arg1=" + extrNorm.getArgument1Norm());
            System.out.println("Rel=" + extrNorm.getRelationNorm());
            System.out.println("Arg2=" + extrNorm.getArgument2Norm());
        }
    }
}
